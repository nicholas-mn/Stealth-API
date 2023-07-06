package com.cosmos.stealth.services.reddit.data.repository

import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.CommunityResults
import com.cosmos.stealth.core.model.api.Feed
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.FeedableResults
import com.cosmos.stealth.core.model.api.Post
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.api.UserResults
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.network.data.repository.NetworkRepository
import com.cosmos.stealth.core.network.util.LinkValidator
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.services.base.util.extension.map
import com.cosmos.stealth.services.base.util.extension.toAfter
import com.cosmos.stealth.services.base.util.extension.toStatus
import com.cosmos.stealth.services.reddit.data.mapper.CommentMapper
import com.cosmos.stealth.services.reddit.data.mapper.CommunityMapper
import com.cosmos.stealth.services.reddit.data.mapper.PostMapper
import com.cosmos.stealth.services.reddit.data.mapper.UserMapper
import com.cosmos.stealth.services.reddit.data.model.AboutChild
import com.cosmos.stealth.services.reddit.data.model.AboutUserChild
import com.cosmos.stealth.services.reddit.data.model.CommentChild
import com.cosmos.stealth.services.reddit.data.model.PostChild
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.Sorting
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import okhttp3.HttpUrl.Companion.toHttpUrl

@Suppress("TooManyFunctions", "LongParameterList")
class RedditRepository(
    private val dataRedditApi: RedditApi,
    private val scrapRedditApi: RedditApi,
    private val postMapper: PostMapper,
    private val communityMapper: CommunityMapper,
    private val userMapper: UserMapper,
    private val commentMapper: CommentMapper,
    mainImmediateDispatcher: CoroutineDispatcher
) : NetworkRepository() {

    private val scope = CoroutineScope(mainImmediateDispatcher + SupervisorJob())

    @Suppress("UNCHECKED_CAST")
    suspend fun getSubreddit(request: Request, subreddit: String, sorting: Sorting, after: String?): Feed {
        val response = safeApiCall {
            getSource(request.service.instance)
                .getSubreddit(subreddit, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }

        val status = response.toStatus(request.service)
            .run { listOf(this) }

        return when (response) {
            is Resource.Success -> {
                val items = postMapper.dataToEntities(response.data.data.children as List<PostChild>, request.service)
                val afterData = response.data.data.after
                    ?.toAfter(request.service)
                    ?.run { listOf(this) }

                Feed(items, afterData, status)
            }

            else -> Feed(listOf(), null, status)
        }
    }

    suspend fun getSubredditInfo(request: Request, subreddit: String): Resource<CommunityInfo> {
        suspend fun apiCall() = getSource(request.service.instance).getSubredditInfo(subreddit, request.info.host)

        return safeApiCall(::apiCall) { communityMapper.dataToEntity(it as AboutChild, request.service) }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun searchInSubreddit(
        request: Request,
        subreddit: String,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        suspend fun apiCall() = getSource(request.service.instance)
            .searchInSubreddit(subreddit, query, sorting.generalSorting, sorting.timeSorting, after, request.info.host)

        return safeApiCall(::apiCall) {
            val results = postMapper.dataToEntities(it.data.children as List<PostChild>, request.service)
            FeedableResults(results)
        }
    }

    suspend fun getPost(request: Request, permalink: String, limit: Int?, sort: Sort): Resource<Post> {
        val response = safeApiCall {
            getSource(request.service.instance)
                .getPost(permalink, limit, sort, request.info.host)
        }

        return response.map {
            val postChild = it[0].data.children[0] as PostChild

            val post = scope.async { postMapper.dataToEntity(postChild, request.service) }
            val replies = scope.async {
                commentMapper.dataToEntities(it[1].data.children, request.service, postChild.data)
            }

            Post(post.await(), replies.await())
        }
    }

    suspend fun getMoreChildren(request: Request, children: List<String>, linkId: String): Resource<List<Feedable>> {
        val childrenString = children.take(LOAD_MORE_LIMIT).joinToString(",")

        suspend fun apiCall() = getSource(request.service.instance).getMoreChildren(childrenString, linkId)

        return safeApiCall(::apiCall) { commentMapper.dataToEntities(it.json.data.things, request.service, null) }
    }

    suspend fun getUserInfo(request: Request, user: String): Resource<UserInfo> {
        suspend fun apiCall() = getSource(request.service.instance).getUserInfo(user, request.info.host)

        return safeApiCall(::apiCall) { userMapper.dataToEntity(it as AboutUserChild, request.service) }
    }

    suspend fun getUserPosts(request: Request, user: String, sorting: Sorting, after: String?): Feed {
        val response = safeApiCall {
            getSource(request.service.instance)
                .getUserPosts(user, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }

        val status = response.toStatus(request.service)
            .run { listOf(this) }

        @Suppress("UNCHECKED_CAST")
        return when (response) {
            is Resource.Success -> {
                val items = postMapper.dataToEntities(response.data.data.children as List<PostChild>, request.service)
                val afterData = response.data.data.after
                    ?.toAfter(request.service)
                    ?.run { listOf(this) }

                Feed(items, afterData, status)
            }

            else -> Feed(listOf(), null, status)
        }
    }

    suspend fun getUserComments(request: Request, user: String, sorting: Sorting, after: String?): Feed {
        val response = safeApiCall {
            getSource(request.service.instance)
                .getUserComments(user, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }

        val status = response.toStatus(request.service)
            .run { listOf(this) }

        @Suppress("UNCHECKED_CAST")
        return when (response) {
            is Resource.Success -> {
                val items = commentMapper.dataToEntities(
                    response.data.data.children as List<CommentChild>,
                    request.service
                )
                val afterData = response.data.data.after
                    ?.toAfter(request.service)
                    ?.run { listOf(this) }

                Feed(items, afterData, status)
            }

            else -> Feed(listOf(), null, status)
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun searchPost(request: Request, query: String, sorting: Sorting, after: String?): Resource<SearchResults> {
        suspend fun apiCall() = getSource(request.service.instance)
            .searchPost(query, sorting.generalSorting, sorting.timeSorting, after, request.info.host)

        return safeApiCall(::apiCall) {
            val results = postMapper.dataToEntities(it.data.children as List<PostChild>, request.service)
            FeedableResults(results)
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun searchUser(request: Request, query: String, sorting: Sorting, after: String?): Resource<SearchResults> {
        suspend fun apiCall() = getSource(request.service.instance)
            .searchUser(query, sorting.generalSorting, sorting.timeSorting, after, request.info.host)

        return safeApiCall(::apiCall) {
            val results = userMapper.dataToEntities(it.data.children as List<AboutUserChild>, request.service)
            UserResults(results)
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun searchSubreddit(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        suspend fun apiCall() = getSource(request.service.instance)
            .searchSubreddit(query, sorting.generalSorting, sorting.timeSorting, after, request.info.host)

        return safeApiCall(::apiCall) {
            val results = communityMapper.dataToEntities(it.data.children as List<AboutChild>, request.service)
            CommunityResults(results)
        }
    }

    private fun getSource(instance: String?): RedditApi {
        val instanceUrl = LinkValidator(instance).validUrl

        return when (instanceUrl?.host) {
            SCRAP_URL.host -> scrapRedditApi
            else -> dataRedditApi
        }
    }

    companion object {
        private const val LOAD_MORE_LIMIT = 100

        private val SCRAP_URL = RedditApi.BASE_URL_OLD.toHttpUrl()
    }
}
