package com.cosmos.stealth.services.base.data

import com.cosmos.stealth.core.model.api.Community
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.Feed
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.Post
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.model.api.User
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.data.CommunityInfoRequest
import com.cosmos.stealth.core.model.data.CommunityRequest
import com.cosmos.stealth.core.model.data.MoreContentRequest
import com.cosmos.stealth.core.model.data.PostRequest
import com.cosmos.stealth.core.model.data.SearchRequest
import com.cosmos.stealth.core.model.data.SingleFeedRequest
import com.cosmos.stealth.core.model.data.UserInfoRequest
import com.cosmos.stealth.core.model.data.UserRequest
import com.cosmos.stealth.core.network.util.Resource

interface ServiceGateway {

    val name: ServiceName

    suspend fun getFeed(singleFeedRequest: SingleFeedRequest): Feed

    suspend fun getCommunity(communityRequest: CommunityRequest): Resource<Community>

    suspend fun getCommunityInfo(communityInfoRequest: CommunityInfoRequest): Resource<CommunityInfo>

    suspend fun getUser(userRequest: UserRequest): Resource<User>

    suspend fun getUserInfo(userInfoRequest: UserInfoRequest): Resource<UserInfo>

    suspend fun getPost(postRequest: PostRequest): Resource<Post>

    suspend fun getMoreContent(moreContentRequest: MoreContentRequest): Resource<List<Feedable>>

    suspend fun getSearchResults(searchRequest: SearchRequest): Resource<SearchResults>
}
