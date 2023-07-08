package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.Post
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.model.data.RequestInfo
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.services.reddit.RedditGateway
import com.cosmos.stealth.services.teddit.TedditGateway

class PostService(
    redditGateway: RedditGateway,
    tedditGateway: TedditGateway
) : BaseService(redditGateway, tedditGateway) {

    suspend fun getPost(requestInfo: RequestInfo, post: String, service: Service, sort: Sort): Resource<Post> {
        return getServiceGateway(service).getPost(Request(service, requestInfo), post, sort)
    }
}
