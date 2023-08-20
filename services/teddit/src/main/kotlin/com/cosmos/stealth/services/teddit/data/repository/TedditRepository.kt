package com.cosmos.stealth.services.teddit.data.repository

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.model.api.Appendable
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.Feed
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.Post
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.services.reddit.data.mapper.CommentMapper
import com.cosmos.stealth.services.reddit.data.mapper.CommunityMapper
import com.cosmos.stealth.services.reddit.data.mapper.PostMapper
import com.cosmos.stealth.services.reddit.data.mapper.UserMapper
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.Sorting
import com.cosmos.stealth.services.reddit.data.repository.Repository
import com.cosmos.stealth.services.teddit.data.remote.api.TedditApi
import com.cosmos.stealth.services.teddit.di.TedditModule.Qualifier.TEDDIT_QUALIFIER
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Suppress("TooManyFunctions")
@Single
class TedditRepository(
    @Named(TEDDIT_QUALIFIER) private val tedditApi: TedditApi,
    postMapper: PostMapper,
    communityMapper: CommunityMapper,
    userMapper: UserMapper,
    commentMapper: CommentMapper,
    @Named(DEFAULT_DISPATCHER_QUALIFIER) defaultDispatcher: CoroutineDispatcher
) : Repository(postMapper, communityMapper, userMapper, commentMapper, defaultDispatcher) {

    override suspend fun getSubreddit(
        request: Request,
        subreddit: String,
        sorting: Sorting,
        limit: Int,
        after: String?
    ): Feed {
        val newRequest = request.getRequest()

        val response = safeApiCall {
            tedditApi.getSubreddit(
                newRequest.getInstance(),
                subreddit,
                sorting.generalSorting,
                sorting.timeSorting,
                after,
                request.info.host
            )
        }

        return getSubreddit(response, newRequest)
    }

    override suspend fun getSubredditInfo(request: Request, subreddit: String): Resource<CommunityInfo> {
        val newRequest = request.getRequest()

        return getSubredditInfo(newRequest) {
            tedditApi.getSubredditInfo(newRequest.getInstance(), subreddit, request.info.host)
        }
    }

    override suspend fun searchInSubreddit(
        request: Request,
        subreddit: String,
        query: String,
        sorting: Sorting,
        limit: Int,
        after: String?
    ): Resource<SearchResults> {
        val newRequest = request.getRequest()

        return searchInSubreddit(newRequest) {
            tedditApi.searchInSubreddit(
                newRequest.getInstance(),
                subreddit,
                query,
                sorting.generalSorting,
                sorting.timeSorting,
                after,
                request.info.host
            )
        }
    }

    override suspend fun getPost(request: Request, permalink: String, limit: Int?, sort: Sort): Resource<Post> {
        val newRequest = request.getRequest()

        val response = safeApiCall {
            tedditApi.getPost(newRequest.getInstance(), permalink, limit, sort, request.info.host)
        }

        return getPost(response, newRequest)
    }

    override suspend fun getMoreChildren(
        request: Request,
        appendable: Appendable
    ): Resource<List<Feedable>> {
        return Resource.Exception(UnsupportedOperationException())
    }

    override suspend fun getUserInfo(request: Request, user: String): Resource<UserInfo> {
        val newRequest = request.getRequest()

        return getUserInfo(newRequest) {
            tedditApi.getUserPosts(newRequest.getInstance(), user, Sort.HOT, null, null, request.info.host).about
        }
    }

    override suspend fun getUserPosts(
        request: Request,
        user: String,
        sorting: Sorting,
        limit: Int,
        after: String?
    ): Feed {
        val newRequest = request.getRequest()

        val response = safeApiCall {
            tedditApi.getUserPosts(
                newRequest.getInstance(),
                user,
                sorting.generalSorting,
                sorting.timeSorting,
                after,
                request.info.host
            ).overview
        }

        return getUserPosts(response, newRequest)
    }

    override suspend fun getUserComments(
        request: Request,
        user: String,
        sorting: Sorting,
        limit: Int,
        after: String?
    ): Feed {
        val newRequest = request.getRequest()

        val response = safeApiCall {
            tedditApi.getUserComments(
                newRequest.getInstance(),
                user,
                sorting.generalSorting,
                sorting.timeSorting,
                after,
                request.info.host
            ).overview
        }

        return getUserComments(response, newRequest)
    }

    override suspend fun searchPost(
        request: Request,
        query: String,
        sorting: Sorting,
        limit: Int,
        after: String?
    ): Resource<SearchResults> {
        return Resource.Exception(UnsupportedOperationException())
    }

    override suspend fun searchUser(
        request: Request,
        query: String,
        sorting: Sorting,
        limit: Int,
        after: String?
    ): Resource<SearchResults> {
        return Resource.Exception(UnsupportedOperationException())
    }

    override suspend fun searchSubreddit(
        request: Request,
        query: String,
        sorting: Sorting,
        limit: Int,
        after: String?
    ): Resource<SearchResults> {
        val newRequest = request.getRequest()

        return searchSubreddit(newRequest) {
            tedditApi.searchSubreddit(
                newRequest.getInstance(),
                query,
                sorting.generalSorting,
                sorting.timeSorting,
                after,
                request.info.host
            )
        }
    }

    private fun Request.getInstance(): String = service.instance ?: TedditApi.BASE_URL

    private fun Request.getRequest(): Request {
        return this.copy(service = service.copy(instance = service.instance ?: TedditApi.BASE_URL))
    }
}
