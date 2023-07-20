package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.FeedableType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.api.User
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.model.data.RequestInfo
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.server.data.manager.GatewayManager

class UserService(private val gatewayManager: GatewayManager) {

    @Suppress("LongParameterList")
    suspend fun getUser(
        requestInfo: RequestInfo,
        user: String,
        service: Service,
        sort: Sort,
        afterKey: AfterKey?,
        type: FeedableType
    ): Resource<User> {
        return gatewayManager.getServiceGateway(service)
            .getUser(Request(service, requestInfo), user, sort, afterKey, type)
    }

    suspend fun getUserInfo(
        requestInfo: RequestInfo,
        user: String,
        service: Service
    ): Resource<UserInfo> {
        return gatewayManager.getServiceGateway(service).getUserInfo(Request(service, requestInfo), user)
    }
}
