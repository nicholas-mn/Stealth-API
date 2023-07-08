package com.cosmos.stealth.services.teddit.data.repository

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

@Suppress("TooManyFunctions")
class TedditRepository(
    private val tedditApi: TedditApi,
    postMapper: PostMapper,
    communityMapper: CommunityMapper,
    userMapper: UserMapper,
    commentMapper: CommentMapper
) : Repository(postMapper, communityMapper, userMapper, commentMapper) {

    override suspend fun getSubreddit(request: Request, subreddit: String, sorting: Sorting, after: String?): Feed {
        val response = safeApiCall {
            tedditApi.getSubreddit(
                request.getInstance(),
                subreddit,
                sorting.generalSorting,
                sorting.timeSorting,
                after,
                request.info.host
            )
        }

        return getSubreddit(response, request)
    }

    override suspend fun getSubredditInfo(request: Request, subreddit: String): Resource<CommunityInfo> {
        return getSubredditInfo(request) {
            tedditApi.getSubredditInfo(request.getInstance(), subreddit, request.info.host)
        }
    }

    override suspend fun searchInSubreddit(
        request: Request,
        subreddit: String,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        return searchInSubreddit(request) {
            tedditApi.searchInSubreddit(
                request.getInstance(),
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
        val response = safeApiCall {
            tedditApi.getPost(request.getInstance(), permalink, limit, sort, request.info.host)
        }

        return getPost(response, request)
    }

    override suspend fun getMoreChildren(
        request: Request,
        children: List<String>,
        linkId: String
    ): Resource<List<Feedable>> {
        return Resource.Exception(UnsupportedOperationException())
    }

    override suspend fun getUserInfo(request: Request, user: String): Resource<UserInfo> {
        return getUserInfo(request) {
            tedditApi.getUserPosts(request.getInstance(), user, Sort.HOT, null, null, request.info.host).about
        }
    }

    override suspend fun getUserPosts(request: Request, user: String, sorting: Sorting, after: String?): Feed {
        val response = safeApiCall {
            tedditApi.getUserPosts(
                request.getInstance(),
                user,
                sorting.generalSorting,
                sorting.timeSorting,
                after,
                request.info.host
            ).overview
        }

        return getUserPosts(response, request)
    }

    override suspend fun getUserComments(request: Request, user: String, sorting: Sorting, after: String?): Feed {
        val response = safeApiCall {
            tedditApi.getUserComments(
                request.getInstance(),
                user,
                sorting.generalSorting,
                sorting.timeSorting,
                after,
                request.info.host
            ).overview
        }

        return getUserComments(response, request)
    }

    override suspend fun searchPost(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        return Resource.Exception(UnsupportedOperationException())
    }

    override suspend fun searchUser(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        return Resource.Exception(UnsupportedOperationException())
    }

    override suspend fun searchSubreddit(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        return searchSubreddit(request) {
            tedditApi.searchSubreddit(
                request.getInstance(),
                query,
                sorting.generalSorting,
                sorting.timeSorting,
                after,
                request.info.host
            )
        }
    }

    private fun Request.getInstance(): String = service.instance ?: TedditApi.BASE_URL
}
