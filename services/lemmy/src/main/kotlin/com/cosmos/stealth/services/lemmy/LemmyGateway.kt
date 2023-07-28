package com.cosmos.stealth.services.lemmy

import com.cosmos.stealth.core.model.api.Community
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.Feed
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.FeedableType
import com.cosmos.stealth.core.model.api.Post
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.model.api.Status
import com.cosmos.stealth.core.model.api.User
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.api.int
import com.cosmos.stealth.core.model.data.CommunityInfoRequest
import com.cosmos.stealth.core.model.data.CommunityRequest
import com.cosmos.stealth.core.model.data.MoreContentRequest
import com.cosmos.stealth.core.model.data.PostRequest
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.model.data.RequestInfo
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
import com.cosmos.stealth.services.lemmy.data.model.SearchType.Communities
import com.cosmos.stealth.services.lemmy.data.model.SearchType.Posts
import com.cosmos.stealth.services.lemmy.data.model.SearchType.Users
import com.cosmos.stealth.services.lemmy.data.repository.LemmyRepository
import com.cosmos.stealth.services.lemmy.util.extension.sortType
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

class LemmyGateway(private val repository: LemmyRepository) : ServiceGateway {

    override val name: ServiceName
        get() = ServiceName.lemmy

    override suspend fun getFeed(singleFeedRequest: SingleFeedRequest): Feed {
        return with(singleFeedRequest) {
            val request = getRequest(service, info)

            if (request == null) {
                val status = Status(service, HttpStatusCode.BadRequest.value, MISSING_INSTANCE_ERROR)
                return Feed(listOf(), null, listOf(status))
            }

            repository.getPosts(request, communities, sort.sortType, afterKey.int)
        }
    }

    override suspend fun getCommunity(communityRequest: CommunityRequest): Resource<Community> = supervisorScope {
        with (communityRequest) {
            val request = getRequest(service, info)
                ?: return@supervisorScope Resource.Error(HttpStatusCode.BadRequest.value, MISSING_INSTANCE_ERROR)

            val feedAsync = async { repository.getPosts(request, community, sort.sortType, afterKey.int) }
            val communityInfoAsync = async { repository.getCommunity(request, community) }

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
            val request = getRequest(service, info)
                ?: return Resource.Error(HttpStatusCode.BadRequest.value, MISSING_INSTANCE_ERROR)

            repository.getCommunity(request, community)
        }
    }

    override suspend fun getUser(userRequest: UserRequest): Resource<User> {
        return with(userRequest) {
            val request = getRequest(service, info)
                ?: return Resource.Error(HttpStatusCode.BadRequest.value, MISSING_INSTANCE_ERROR)

            when (type) {
                FeedableType.post -> repository.getUserPosts(request, user, sort.sortType, afterKey.int)
                FeedableType.comment -> repository.getUserComments(request, user, sort.sortType, afterKey.int)
                else -> Resource.Exception(UnsupportedOperationException("Cannot get type $type for user"))
            }
        }
    }

    override suspend fun getUserInfo(userInfoRequest: UserInfoRequest): Resource<UserInfo> {
        return with(userInfoRequest) {
            val request = getRequest(service, info)
                ?: return Resource.Error(HttpStatusCode.BadRequest.value, MISSING_INSTANCE_ERROR)

            repository.getUser(request, user)
        }
    }

    override suspend fun getPost(postRequest: PostRequest): Resource<Post> = supervisorScope {
        with(postRequest) {
            val request = getRequest(service, info)
                ?: return@supervisorScope Resource.Error(HttpStatusCode.BadRequest.value, MISSING_INSTANCE_ERROR)

            val id = post.toInt()

            val postAsync = async { repository.getPost(request, id) }
            val commentsAsync = async { repository.getComments(request, id, sort.sortType, null) }

            val post = postAsync.await()
            val comments = commentsAsync.await()

            val commentsStatus = comments.status.firstOrNull().orInternalError(request.service)

            when {
                commentsStatus.isSuccess -> post.map { Post(it, comments) }
                else -> commentsStatus.toError()
            }
        }
    }

    override suspend fun getMoreContent(moreContentRequest: MoreContentRequest): Resource<List<Feedable>> {
        return with (moreContentRequest) {
            val request = Request(appendable.service, info)

            repository.getMoreComments(request, appendable.parentId.toInt())
        }
    }

    override suspend fun getSearchResults(searchRequest: SearchRequest): Resource<SearchResults> {
        return with(searchRequest) {
            val request = getRequest(service, info)
                ?: return Resource.Error(HttpStatusCode.BadRequest.value, MISSING_INSTANCE_ERROR)

            when (type) {
                SearchType.feedable -> repository.search(request, query, Posts, community, sort.sortType, afterKey.int)
                SearchType.community -> {
                    repository.search(request, query, Communities, community, sort.sortType, afterKey.int)
                }
                SearchType.user -> repository.search(request, query, Users, community, sort.sortType, afterKey.int)
            }
        }
    }

    private fun getRequest(service: Service, info: RequestInfo): Request? {
        return service.instance.takeUnless { it.isNullOrBlank() }?.run { Request(service, info) }
    }

    companion object {
        private const val MISSING_INSTANCE_ERROR = "Instance must be provided"
    }
}
