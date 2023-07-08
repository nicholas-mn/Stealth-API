package com.cosmos.stealth.services.reddit

import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.Community
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.Feed
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.FeedableType
import com.cosmos.stealth.core.model.api.MoreContentFeedable
import com.cosmos.stealth.core.model.api.Post
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.api.Status
import com.cosmos.stealth.core.model.api.User
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.api.string
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.services.base.data.ServiceGateway
import com.cosmos.stealth.services.base.util.extension.isFailure
import com.cosmos.stealth.services.base.util.extension.map
import com.cosmos.stealth.services.reddit.data.model.Sort.BEST
import com.cosmos.stealth.services.reddit.data.repository.Repository
import com.cosmos.stealth.services.reddit.util.extension.redditSort
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import java.net.HttpURLConnection

abstract class Gateway(
    private val repository: Repository
) : ServiceGateway {

    override suspend fun getFeed(
        request: Request,
        communities: List<String>,
        sort: Sort,
        afterKey: AfterKey?
    ): Feed {
        return repository.getSubreddit(request, communities, sort.redditSort, afterKey.string)
    }

    override suspend fun getCommunity(
        request: Request,
        community: String,
        sort: Sort,
        afterKey: AfterKey?
    ): Resource<Community> = supervisorScope {
        val feedAsync = async {
            repository.getSubreddit(request, community, sort.redditSort, afterKey.string)
        }
        val communityInfoAsync = async { repository.getSubredditInfo(request, community) }

        val feed = feedAsync.await()
        val communityInfo = communityInfoAsync.await()

        val feedStatus = feed.status.firstOrNull() ?: Status(request.service, HttpURLConnection.HTTP_INTERNAL_ERROR)

        when {
            feedStatus.isFailure -> Resource.Error(feedStatus.code, feedStatus.error.orEmpty())
            else -> communityInfo.map { Community(it, feed) }
        }
    }

    override suspend fun getCommunityInfo(request: Request, community: String): Resource<CommunityInfo> {
        return repository.getSubredditInfo(request, community)
    }

    override suspend fun getUser(
        request: Request,
        user: String,
        sort: Sort,
        afterKey: AfterKey?,
        type: FeedableType
    ): Resource<User> = supervisorScope {
        val feedAsync = async {
            when (type) {
                FeedableType.post -> repository.getUserPosts(request, user, sort.redditSort, afterKey.string)
                FeedableType.comment -> {
                    repository.getUserComments(request, user, sort.redditSort, afterKey.string)
                }
                else -> {
                    val status = Status(request.service, HttpURLConnection.HTTP_BAD_REQUEST)
                    Feed(listOf(), null, listOf(status))
                }
            }
        }
        val userInfoAsync = async { repository.getUserInfo(request, user) }

        val feed = feedAsync.await()
        val userInfo = userInfoAsync.await()

        val feedStatus = feed.status.firstOrNull() ?: Status(request.service, HttpURLConnection.HTTP_INTERNAL_ERROR)

        when {
            feedStatus.isFailure -> Resource.Error(feedStatus.code, feedStatus.error.orEmpty())
            else -> userInfo.map { User(it, feed) }
        }
    }

    override suspend fun getUserInfo(request: Request, user: String): Resource<UserInfo> {
        return repository.getUserInfo(request, user)
    }

    override suspend fun getPost(request: Request, post: String, sort: Sort): Resource<Post> {
        // TODO: Special sort for posts
        val redditSort = if (sort == Sort.best) BEST else sort.redditSort.generalSorting
        return repository.getPost(request, post, null, redditSort)
    }

    override suspend fun getMoreContent(
        request: Request,
        moreContentFeedable: MoreContentFeedable
    ): Resource<List<Feedable>> {
        return repository.getMoreChildren(request, moreContentFeedable.content, moreContentFeedable.parentId)
    }

    override suspend fun getSearchResults(
        request: Request,
        query: String,
        sort: Sort,
        afterKey: AfterKey?,
        type: SearchType
    ): Resource<SearchResults> {
        return when (type) {
            SearchType.feedable -> repository.searchPost(request, query, sort.redditSort, afterKey.string)
            SearchType.community -> repository.searchSubreddit(request, query, sort.redditSort, afterKey.string)
            SearchType.user -> repository.searchUser(request, query, sort.redditSort, afterKey.string)
        }
    }

    override suspend fun getCommunitySearchResults(
        request: Request,
        community: String,
        query: String,
        sort: Sort,
        afterKey: AfterKey?,
        type: SearchType
    ): Resource<SearchResults> {
        return when (type) {
            SearchType.feedable -> {
                repository.searchInSubreddit(request, community, query, sort.redditSort, afterKey.string)
            }
            else -> Resource.Exception(IllegalStateException("Cannot search for type $type in community"))
        }
    }
}
