package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.User
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.data.UserInfoRequest
import com.cosmos.stealth.core.model.data.UserRequest
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.server.data.manager.GatewayManager

class UserService(private val gatewayManager: GatewayManager) {

    @Suppress("LongParameterList")
    suspend fun getUser(userRequest: UserRequest): Resource<User> {
        return gatewayManager.getServiceGateway(userRequest.service).getUser(userRequest)
    }

    suspend fun getUserInfo(userInfoRequest: UserInfoRequest): Resource<UserInfo> {
        return gatewayManager.getServiceGateway(userInfoRequest.service).getUserInfo(userInfoRequest)
    }
}
