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
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.CommentChild
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.MoreChildren
import com.cosmos.stealth.services.reddit.data.model.PostChild
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.Sorting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async

@Suppress("TooManyFunctions")
abstract class Repository(
    private val postMapper: PostMapper,
    private val communityMapper: CommunityMapper,
    private val userMapper: UserMapper,
    private val commentMapper: CommentMapper,
    mainImmediateDispatcher: CoroutineDispatcher
) : NetworkRepository() {

    private val scope = CoroutineScope(mainImmediateDispatcher + SupervisorJob())

    abstract suspend fun getSubreddit(request: Request, subreddit: String, sorting: Sorting, after: String?): Feed

    @Suppress("UNCHECKED_CAST")
    protected suspend fun getSubreddit(response: Resource<Listing>, request: Request): Feed {
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

    abstract suspend fun getSubredditInfo(request: Request, subreddit: String): Resource<CommunityInfo>

    protected suspend fun getSubredditInfo(request: Request, apiCall: suspend () -> Child): Resource<CommunityInfo> {
        return safeApiCall(apiCall) { communityMapper.dataToEntity(it as AboutChild, request.service) }
    }

    abstract suspend fun searchInSubreddit(
        request: Request,
        subreddit: String,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults>

    @Suppress("UNCHECKED_CAST")
    protected suspend fun searchInSubreddit(request: Request, apiCall: suspend () -> Listing): Resource<SearchResults> {
        return safeApiCall(apiCall) {
            val results = postMapper.dataToEntities(it.data.children as List<PostChild>, request.service)
            FeedableResults(results)
        }
    }

    abstract suspend fun getPost(request: Request, permalink: String, limit: Int?, sort: Sort): Resource<Post>

    protected suspend fun getPost(response: Resource<List<Listing>>, request: Request): Resource<Post> {
        return response.map {
            val postChild = it[0].data.children[0] as PostChild

            val post = scope.async { postMapper.dataToEntity(postChild, request.service) }
            val replies = scope.async {
                commentMapper.dataToEntities(it[1].data.children, request.service, postChild.data)
            }

            Post(post.await(), replies.await())
        }
    }

    abstract suspend fun getMoreChildren(
        request: Request,
        children: List<String>,
        linkId: String
    ): Resource<List<Feedable>>

    protected suspend fun getMoreChildren(
        request: Request,
        apiCall: suspend () -> MoreChildren
    ): Resource<List<Feedable>> {
        return safeApiCall(apiCall) { commentMapper.dataToEntities(it.json.data.things, request.service, null) }
    }

    abstract suspend fun getUserInfo(request: Request, user: String): Resource<UserInfo>

    protected suspend fun getUserInfo(request: Request, apiCall: suspend () -> Child): Resource<UserInfo> {
        return safeApiCall(apiCall) { userMapper.dataToEntity(it as AboutUserChild, request.service) }
    }

    abstract suspend fun getUserPosts(request: Request, user: String, sorting: Sorting, after: String?): Feed

    protected suspend fun getUserPosts(response: Resource<Listing>, request: Request): Feed {
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

    abstract suspend fun getUserComments(request: Request, user: String, sorting: Sorting, after: String?): Feed

    @Suppress("UNCHECKED_CAST")
    protected suspend fun getUserComments(response: Resource<Listing>, request: Request): Feed {
        val status = response.toStatus(request.service)
            .run { listOf(this) }

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

    abstract suspend fun searchPost(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults>

    @Suppress("UNCHECKED_CAST")
    protected suspend fun searchPost(request: Request, apiCall: suspend () -> Listing): Resource<SearchResults> {
        return safeApiCall(apiCall) {
            val results = postMapper.dataToEntities(it.data.children as List<PostChild>, request.service)
            FeedableResults(results)
        }
    }

    abstract suspend fun searchUser(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults>

    @Suppress("UNCHECKED_CAST")
    protected suspend fun searchUser(request: Request, apiCall: suspend () -> Listing): Resource<SearchResults> {
        return safeApiCall(apiCall) {
            val results = userMapper.dataToEntities(it.data.children as List<AboutUserChild>, request.service)
            UserResults(results)
        }
    }

    abstract suspend fun searchSubreddit(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults>

    @Suppress("UNCHECKED_CAST")
    protected suspend fun searchSubreddit(request: Request, apiCall: suspend () -> Listing): Resource<SearchResults> {
        return safeApiCall(apiCall) {
            val results = communityMapper.dataToEntities(it.data.children as List<AboutChild>, request.service)
            CommunityResults(results)
        }
    }
}
