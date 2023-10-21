package com.cosmos.stealth.server.data.route.v1

import com.cosmos.stealth.core.model.api.FeedableType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.data.Default
import com.cosmos.stealth.core.model.data.Filtering
import com.cosmos.stealth.core.model.data.Path
import com.cosmos.stealth.core.model.data.Query
import com.cosmos.stealth.core.model.data.UserInfoRequest
import com.cosmos.stealth.core.model.data.UserRequest
import com.cosmos.stealth.server.data.service.UserService
import com.cosmos.stealth.server.util.extension.getQuery
import com.cosmos.stealth.server.util.extension.info
import com.cosmos.stealth.server.util.extension.respondWithResource
import com.cosmos.stealth.server.util.getAfter
import com.cosmos.stealth.server.util.getInstance
import com.cosmos.stealth.server.util.getLimit
import com.cosmos.stealth.server.util.getOrder
import com.cosmos.stealth.server.util.getService
import com.cosmos.stealth.server.util.getSort
import com.cosmos.stealth.server.util.getTime
import com.cosmos.stealth.server.util.getUser
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.userRouting() {
    val userService by inject<UserService>()

    route("/user") {

        get("/{${Path.USER}}") {
            val user = call.getUser()

            val serviceName = call.getService()

            val instance = call.getInstance()
            val sort = call.getSort()
            val order = call.getOrder()
            val time = call.getTime()
            val limit = call.getLimit()
            val after = call.getAfter()

            val type = FeedableType.decode(call.getQuery(Query.TYPE)) ?: Default.USER_FEEDABLE_TYPE

            val userRequest = UserRequest(
                call.info,
                user,
                Service(serviceName, instance),
                Filtering(sort, order, time),
                limit,
                after,
                type
            )

            val userResource = userService.getUser(userRequest)

            call.respondWithResource(userResource)
        }

        get("/{${Path.USER}}/info") {
            val user = call.getUser()

            val serviceName = call.getService()

            val instance = call.getInstance()

            val userInfoRequest = UserInfoRequest(call.info, user, Service(serviceName, instance))

            val userInfo = userService.getUserInfo(userInfoRequest)

            call.respondWithResource(userInfo)
        }
    }
}
