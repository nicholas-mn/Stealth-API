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
import com.cosmos.stealth.core.model.api.ServiceName
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
import com.cosmos.stealth.services.reddit.data.repository.RedditRepository
import com.cosmos.stealth.services.reddit.util.extension.redditSort
import com.cosmos.stealth.services.reddit.util.joinSubredditList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import java.net.HttpURLConnection

class RedditGateway(
    private val redditRepository: RedditRepository,
    mainImmediateDispatcher: CoroutineDispatcher
) : ServiceGateway {

    private val scope = CoroutineScope(mainImmediateDispatcher + SupervisorJob())

    override val name: ServiceName
        get() = ServiceName.reddit

    override suspend fun getFeed(
        request: Request,
        communities: List<String>,
        sort: Sort,
        afterKey: AfterKey?
    ): Feed {
        return redditRepository.getSubreddit(request, joinSubredditList(communities), sort.redditSort, afterKey.string)
    }

    override suspend fun getCommunity(
        request: Request,
        community: String,
        sort: Sort,
        afterKey: AfterKey?
    ): Resource<Community> {
        val feedAsync = scope.async {
            redditRepository.getSubreddit(request, community, sort.redditSort, afterKey.string)
        }
        val communityInfoAsync = scope.async { redditRepository.getSubredditInfo(request, community) }

        val feed = feedAsync.await()
        val communityInfo = communityInfoAsync.await()

        val feedStatus = feed.status.firstOrNull() ?: Status(request.service, HttpURLConnection.HTTP_INTERNAL_ERROR)

        return when {
            feedStatus.isFailure -> Resource.Error(feedStatus.code, feedStatus.error.orEmpty())
            else -> communityInfo.map { Community(it, feed) }
        }
    }

    override suspend fun getCommunityInfo(request: Request, community: String): Resource<CommunityInfo> {
        return redditRepository.getSubredditInfo(request, community)
    }

    override suspend fun getUser(
        request: Request,
        user: String,
        sort: Sort,
        afterKey: AfterKey?,
        type: FeedableType
    ): Resource<User> {
        val feedAsync = scope.async {
            when (type) {
                FeedableType.post -> redditRepository.getUserPosts(request, user, sort.redditSort, afterKey.string)
                FeedableType.comment -> {
                    redditRepository.getUserComments(request, user, sort.redditSort, afterKey.string)
                }
                else -> {
                    val status = Status(request.service, HttpURLConnection.HTTP_BAD_REQUEST)
                    Feed(listOf(), null, listOf(status))
                }
            }
        }
        val userInfoAsync = scope.async { redditRepository.getUserInfo(request, user) }

        val feed = feedAsync.await()
        val userInfo = userInfoAsync.await()

        val feedStatus = feed.status.firstOrNull() ?: Status(request.service, HttpURLConnection.HTTP_INTERNAL_ERROR)

        return when {
            feedStatus.isFailure -> Resource.Error(feedStatus.code, feedStatus.error.orEmpty())
            else -> userInfo.map { User(it, feed) }
        }
    }

    override suspend fun getUserInfo(request: Request, user: String): Resource<UserInfo> {
        return redditRepository.getUserInfo(request, user)
    }

    override suspend fun getPost(request: Request, post: String, sort: Sort): Resource<Post> {
        return redditRepository.getPost(request, post, null, sort.redditSort.generalSorting)
    }

    override suspend fun getMoreContent(
        request: Request,
        moreContentFeedable: MoreContentFeedable
    ): Resource<List<Feedable>> {
        return redditRepository.getMoreChildren(request, moreContentFeedable.content, moreContentFeedable.parentId)
    }

    override suspend fun getSearchResults(
        request: Request,
        query: String,
        sort: Sort,
        afterKey: AfterKey?,
        type: SearchType
    ): Resource<SearchResults> {
        return when (type) {
            SearchType.feedable -> redditRepository.searchPost(request, query, sort.redditSort, afterKey.string)
            SearchType.community -> redditRepository.searchSubreddit(request, query, sort.redditSort, afterKey.string)
            SearchType.user -> redditRepository.searchUser(request, query, sort.redditSort, afterKey.string)
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
                redditRepository.searchInSubreddit(request, community, query, sort.redditSort, afterKey.string)
            }
            SearchType.community -> Resource.Exception(IllegalStateException())
            SearchType.user -> Resource.Exception(IllegalStateException())
        }
    }
}
