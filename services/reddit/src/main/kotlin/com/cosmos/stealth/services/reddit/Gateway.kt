package com.cosmos.stealth.services.reddit

import com.cosmos.stealth.core.model.api.Community
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.Feed
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.FeedableType
import com.cosmos.stealth.core.model.api.Post
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.api.Status
import com.cosmos.stealth.core.model.api.User
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.api.string
import com.cosmos.stealth.core.model.data.CommunityInfoRequest
import com.cosmos.stealth.core.model.data.CommunityRequest
import com.cosmos.stealth.core.model.data.MoreContentRequest
import com.cosmos.stealth.core.model.data.PostRequest
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.model.data.SearchRequest
import com.cosmos.stealth.core.model.data.SingleFeedRequest
import com.cosmos.stealth.core.model.data.UserInfoRequest
import com.cosmos.stealth.core.model.data.UserRequest
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.services.base.data.ServiceGateway
import com.cosmos.stealth.services.base.util.extension.isSuccess
import com.cosmos.stealth.services.base.util.extension.map
import com.cosmos.stealth.services.base.util.extension.orInternalError
import com.cosmos.stealth.services.base.util.extension.toError
import com.cosmos.stealth.services.reddit.data.model.Sort.BEST
import com.cosmos.stealth.services.reddit.data.repository.Repository
import com.cosmos.stealth.services.reddit.util.extension.redditSort
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import java.net.HttpURLConnection

@Suppress("TooManyFunctions")
abstract class Gateway(
    private val repository: Repository
) : ServiceGateway {

    override suspend fun getFeed(singleFeedRequest: SingleFeedRequest): Feed {
        return with(singleFeedRequest) {
            val request = Request(service, info)

            repository.getSubreddit(request, communities, sort.redditSort, afterKey.string)
        }
    }

    override suspend fun getCommunity(communityRequest: CommunityRequest): Resource<Community> = supervisorScope {
        with(communityRequest) {
            val request = Request(service, info)

            val feedAsync = async {
                repository.getSubreddit(request, community, sort.redditSort, afterKey.string)
            }
            val communityInfoAsync = async { repository.getSubredditInfo(request, community) }

            val feed = feedAsync.await()
            val communityInfo = communityInfoAsync.await()

            val feedStatus = feed.status.firstOrNull().orInternalError(request.service)

            when {
                feedStatus.isSuccess -> communityInfo.map { Community(it, feed) }
                else -> feedStatus.toError()
            }
        }
    }

    override suspend fun getCommunityInfo(communityInfoRequest: CommunityInfoRequest): Resource<CommunityInfo> {
        return with(communityInfoRequest) {
            val request = Request(service, info)

            repository.getSubredditInfo(request, community)
        }
    }

    override suspend fun getUser(userRequest: UserRequest): Resource<User> = supervisorScope {
        with(userRequest) {
            val request = Request(service, info)

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

            val feedStatus = feed.status.firstOrNull().orInternalError(request.service)

            when {
                feedStatus.isSuccess -> userInfo.map { User(it, feed) }
                else -> feedStatus.toError()
            }
        }
    }

    override suspend fun getUserInfo(userInfoRequest: UserInfoRequest): Resource<UserInfo> {
        return with(userInfoRequest) {
            val request = Request(service, info)

            repository.getUserInfo(request, user)
        }
    }

    override suspend fun getPost(postRequest: PostRequest): Resource<Post> {
        return with(postRequest) {
            val request = Request(service, info)

            // TODO: Special sort for posts
            val redditSort = if (sort == Sort.best) BEST else sort.redditSort.generalSorting
            repository.getPost(request, post, null, redditSort)
        }
    }

    override suspend fun getMoreContent(moreContentRequest: MoreContentRequest): Resource<List<Feedable>> {
        return with(moreContentRequest) {
            val request = Request(appendable.service, info)

            repository.getMoreChildren(request, appendable)
        }
    }

    override suspend fun getSearchResults(searchRequest: SearchRequest): Resource<SearchResults> {
        return when (searchRequest.type) {
            SearchType.feedable -> getFeedableResults(searchRequest)
            SearchType.community -> getCommunityResults(searchRequest)
            SearchType.user -> getUserResults(searchRequest)
        }
    }

    private suspend fun getFeedableResults(searchRequest: SearchRequest): Resource<SearchResults> {
        return with(searchRequest) {
            val request = Request(service, info)
            val sorting = sort.redditSort
            val after = afterKey.string

            when {
                community != null -> repository.searchInSubreddit(request, community.orEmpty(), query, sorting, after)
                user != null -> Resource.Exception(IllegalStateException("Cannot search for feedables in user"))
                else -> repository.searchPost(request, query, sorting, after)
            }
        }
    }

    private suspend fun getCommunityResults(searchRequest: SearchRequest): Resource<SearchResults> {
        return with(searchRequest) {
            when {
                community != null -> {
                    Resource.Exception(IllegalStateException("Cannot search for communities in community"))
                }
                user != null -> Resource.Exception(IllegalStateException("Cannot search for communities in user"))
                else -> repository.searchSubreddit(Request(service, info), query, sort.redditSort, afterKey.string)
            }
        }
    }

    private suspend fun getUserResults(searchRequest: SearchRequest): Resource<SearchResults> {
        return with(searchRequest) {
            when {
                community != null -> Resource.Exception(IllegalStateException("Cannot search for users in community"))
                user != null -> Resource.Exception(IllegalStateException("Cannot search for users in user"))
                else -> repository.searchUser(Request(service, info), query, sort.redditSort, afterKey.string)
            }
        }
    }
}
