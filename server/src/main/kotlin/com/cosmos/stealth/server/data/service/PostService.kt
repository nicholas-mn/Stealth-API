package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.Post
import com.cosmos.stealth.core.model.data.PostRequest
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.server.data.manager.GatewayManager

class PostService(private val gatewayManager: GatewayManager) {

    suspend fun getPost(postRequest: PostRequest): Resource<Post> {
        return gatewayManager.getServiceGateway(postRequest.service).getPost(postRequest)
    }
}
