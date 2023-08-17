package com.cosmos.stealth.services.lemmy.data.repository

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.model.api.After
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.CommunityResults
import com.cosmos.stealth.core.model.api.Feed
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.FeedableResults
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Status
import com.cosmos.stealth.core.model.api.User
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.api.UserResults
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.network.data.repository.NetworkRepository
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.core.network.util.extension.map
import com.cosmos.stealth.services.base.util.extension.isSuccess
import com.cosmos.stealth.services.base.util.extension.orInternalError
import com.cosmos.stealth.services.base.util.extension.sortPosts
import com.cosmos.stealth.services.base.util.extension.toAfter
import com.cosmos.stealth.services.base.util.extension.toStatus
import com.cosmos.stealth.services.lemmy.data.mapper.CommentMapper
import com.cosmos.stealth.services.lemmy.data.mapper.CommunityMapper
import com.cosmos.stealth.services.lemmy.data.mapper.PostMapper
import com.cosmos.stealth.services.lemmy.data.mapper.UserMapper
import com.cosmos.stealth.services.lemmy.data.model.SearchType
import com.cosmos.stealth.services.lemmy.data.model.SortType
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi
import com.cosmos.stealth.services.lemmy.di.LemmyModule.Qualifier.LEMMY_QUALIFIER
import com.cosmos.stealth.services.lemmy.util.extension.toSort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.net.HttpURLConnection

@Suppress("LongParameterList", "TooManyFunctions")
@Single
class LemmyRepository(
    @Named(LEMMY_QUALIFIER) private val lemmyApi: LemmyApi,
    private val postMapper: PostMapper,
    private val communityMapper: CommunityMapper,
    private val userMapper: UserMapper,
    private val commentMapper: CommentMapper,
    @Named(DEFAULT_DISPATCHER_QUALIFIER) private val defaultDispatcher: CoroutineDispatcher
) : NetworkRepository() {

    suspend fun getPosts(request: Request, communities: List<String>, sort: SortType, page: Int?): Feed {
        return if (communities.size == 1) {
            getPosts(request, communities[0], sort, page)
        } else {
            getMultiCommunities(request, communities, sort, page)
        }
    }

    suspend fun getPosts(request: Request, communityName: String, sort: SortType, page: Int?): Feed {
        val response = safeApiCall {
            lemmyApi.getPosts(request.service.instance.orEmpty(), communityName, sort, page, request.info.host)
        }

        val status = response.toStatus(request.service).run { listOf(this) }

        return when (response) {
            is Resource.Success -> {
                val items = postMapper.dataToEntities(response.data.posts, request.service)
                val afterData = page.nextKey(request.service, items.size)?.run { listOf(this) }

                Feed(items, afterData, status)
            }

            else -> Feed(listOf(), null, status)
        }
    }

    private suspend fun getMultiCommunities(
        request: Request,
        communities: List<String>,
        sort: SortType,
        page: Int?
    ): Feed = supervisorScope {
        val responses = communities
            .take(MAX_COMMUNITIES)
            .map { async { getPosts(request, it, sort, page) } }
            .awaitAll()

        val feedables = mutableListOf<List<Feedable>>()

        responses.forEach { response ->
            val status = response.status.firstOrNull().orInternalError(request.service)

            when {
                status.isSuccess -> {
                    feedables.add(response.items)
                }

                else -> return@supervisorScope Feed(listOf(), null, listOf(status))
            }
        }

        val data = withContext(defaultDispatcher) { feedables.sortPosts(sort.toSort()) }

        val afterData = page.nextKey(request.service, data.size)?.run { listOf(this) }
        val status = Status(request.service, HttpURLConnection.HTTP_OK)

        Feed(data, afterData, listOf(status))
    }

    suspend fun getCommunity(request: Request, name: String): Resource<CommunityInfo> {
        val response = safeApiCall {
            lemmyApi.getCommunity(request.service.instance.orEmpty(), name, request.info.host)
        }

        return response.map { communityMapper.dataToEntity(it.communityView, request.service) }
    }

    suspend fun getPost(request: Request, id: Int): Resource<Feedable> {
        val response = safeApiCall { lemmyApi.getPost(request.service.instance.orEmpty(), id, request.info.host) }

        return response.map { postMapper.dataToEntity(it.postView, request.service) }
    }

    suspend fun getUser(request: Request, username: String): Resource<UserInfo> {
        val response = safeApiCall {
            lemmyApi.getUser(request.service.instance.orEmpty(), username, null, null, request.info.host)
        }

        return response.map { userMapper.dataToEntity(it.personView) }
    }

    suspend fun getUserPosts(
        request: Request,
        username: String,
        sort: SortType,
        page: Int?
    ): Resource<User> = supervisorScope {
        val response = safeApiCall {
            lemmyApi.getUser(request.service.instance.orEmpty(), username, sort, page, request.info.host)
        }

        val status = response.toStatus(request.service).run { listOf(this) }

        response.map {
            val userInfo = async { userMapper.dataToEntity(it.personView, request.service) }
            val postsAsync = async { postMapper.dataToEntities(it.posts, request.service) }

            val posts = postsAsync.await()

            val afterData = page.nextKey(request.service, posts.size)?.run { listOf(this) }

            User(userInfo.await(), Feed(posts, afterData, status))
        }
    }

    suspend fun getUserComments(
        request: Request,
        username: String,
        sort: SortType,
        page: Int?
    ): Resource<User> = supervisorScope {
        val response = safeApiCall {
            lemmyApi.getUser(request.service.instance.orEmpty(), username, sort, page, request.info.host)
        }

        val status = response.toStatus(request.service).run { listOf(this) }

        response.map {
            val userInfo = async { userMapper.dataToEntity(it.personView, request.service) }
            val commentsAsync = async { commentMapper.dataToEntities(it.comments, request.service) }

            val comments = commentsAsync.await()

            val afterData = page.nextKey(request.service, comments.size)?.run { listOf(this) }

            User(userInfo.await(), Feed(comments, afterData, status))
        }
    }

    suspend fun getComments(request: Request, postId: Int, sort: SortType, page: Int?): Feed {
        val response = safeApiCall {
            lemmyApi.getComments(request.service.instance.orEmpty(), postId, null, sort, page, request.info.host)
        }

        val status = response.toStatus(request.service).run { listOf(this) }

        return when (response) {
            is Resource.Success -> {
                val items = commentMapper.dataToEntities(response.data.comments, request.service)
                val afterData = page.nextKey(request.service, items.size)?.run { listOf(this) }

                Feed(items, afterData, status)
            }

            else -> Feed(listOf(), null, status)
        }
    }

    suspend fun getMoreComments(request: Request, parentId: Int): Resource<List<Feedable>> {
        val response = safeApiCall {
            lemmyApi.getComments(request.service.instance.orEmpty(), null, parentId, null, null)
        }

        return response.map { commentMapper.dataToEntities(it.comments, request.service) }
    }

    suspend fun search(
        request: Request,
        query: String,
        type: SearchType,
        communityName: String?,
        sort: SortType,
        page: Int?
    ): Resource<SearchResults> {
        val response = safeApiCall {
            lemmyApi.search(
                request.service.instance.orEmpty(),
                query,
                type,
                communityName,
                sort,
                page,
                request.info.host
            )
        }

        return response.map {
            when (type) {
                SearchType.All, SearchType.Posts -> {
                    val posts = postMapper.dataToEntities(it.posts, request.service)
                    val afterKey = page.nextKey(request.service, posts.size)?.key
                    FeedableResults(posts, afterKey)
                }
                SearchType.Communities -> {
                    val communities = communityMapper.dataToEntities(it.communities, request.service)
                    val afterKey = page.nextKey(request.service, communities.size)?.key
                    CommunityResults(communities, afterKey)
                }
                SearchType.Users -> {
                    val users = userMapper.dataToEntities(it.users, request.service)
                    val afterKey = page.nextKey(request.service, users.size)?.key
                    UserResults(users, afterKey)
                }
                else -> throw UnsupportedOperationException("Cannot search for type $type")
            }
        }
    }

    private fun Int?.nextKey(service: Service, responseSize: Int): After? {
        if (responseSize == 0) return null

        val next = this?.plus(1) ?: 2
        return next.toAfter(service)
    }

    companion object {
        private const val MAX_COMMUNITIES = 10
    }
}
