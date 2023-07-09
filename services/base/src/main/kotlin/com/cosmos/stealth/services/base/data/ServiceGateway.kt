package com.cosmos.stealth.services.base.data

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
import com.cosmos.stealth.core.model.api.User
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.model.data.SearchRequest
import com.cosmos.stealth.core.network.util.Resource

interface ServiceGateway {

    val name: ServiceName

    suspend fun getFeed(request: Request, communities: List<String>, sort: Sort, afterKey: AfterKey? = null): Feed

    suspend fun getCommunity(request: Request, community: String, sort: Sort, afterKey: AfterKey?): Resource<Community>

    suspend fun getCommunityInfo(request: Request, community: String): Resource<CommunityInfo>

    suspend fun getUser(
        request: Request,
        user: String,
        sort: Sort,
        afterKey: AfterKey?,
        type: FeedableType
    ): Resource<User>

    suspend fun getUserInfo(request: Request, user: String): Resource<UserInfo>

    suspend fun getPost(request: Request, post: String, sort: Sort): Resource<Post>

    suspend fun getMoreContent(request: Request, moreContentFeedable: MoreContentFeedable): Resource<List<Feedable>>

    suspend fun getSearchResults(searchRequest: SearchRequest): Resource<SearchResults>
}
