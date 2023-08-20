@file:Suppress("TooManyFunctions")

package com.cosmos.stealth.server.util

import com.cosmos.stealth.core.common.util.MessageHandler
import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.Order
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.api.Time
import com.cosmos.stealth.core.model.data.Default
import com.cosmos.stealth.core.model.data.Path
import com.cosmos.stealth.core.model.data.Query
import com.cosmos.stealth.server.util.extension.getPath
import com.cosmos.stealth.server.util.extension.getQuery
import com.cosmos.stealth.services.base.util.extension.toAfterKey
import io.ktor.server.application.ApplicationCall
import java.util.Locale

fun ApplicationCall.getCommunity(): String {
    return requirePath(Path.COMMUNITY) { MessageHandler.getString(Locale.ENGLISH, "model.error.missing_community") }
}

fun ApplicationCall.getPost(): String {
    return requirePath(Path.POST) { MessageHandler.getString(Locale.ENGLISH, "model.error.missing_post_id") }
}

fun ApplicationCall.getUser(): String {
    return requirePath(Path.USER) { MessageHandler.getString(Locale.ENGLISH, "model.error.missing_user_id") }
}

fun ApplicationCall.getService(): ServiceName {
    val service = requireQuery(Query.SERVICE) {
        MessageHandler.getString(Locale.ENGLISH, "model.error.missing_service")
    }

    return requireNotNull(ServiceName.decode(service)) {
        MessageHandler.getString(Locale.ENGLISH, "model.error.unknown_service", service)
    }
}

fun ApplicationCall.getInstance(): String? {
    return getQuery(Query.INSTANCE)
}

fun ApplicationCall.getSort(default: Sort = Default.SORT): Sort {
    return Sort.decode(getQuery(Query.SORT)) ?: default
}

fun ApplicationCall.getOrder(default: Order = Default.ORDER): Order {
    return Order.decode(getQuery(Query.ORDER)) ?: default
}

fun ApplicationCall.getTime(default: Time = Default.TIME): Time {
    return Time.decode(getQuery(Query.TIME)) ?: default
}

fun ApplicationCall.getLimit(default: Int = Default.LIMIT): Int {
    return getQuery(Query.LIMIT)?.toIntOrNull() ?: default
}

fun ApplicationCall.getAfter(): AfterKey? {
    return getQuery(Query.AFTER)?.toAfterKey()
}

fun ApplicationCall.requirePath(name: String, message: () -> Any): String {
    return requireNotNull(getPath(name), message)
}

fun ApplicationCall.requireQuery(name: String, message: () -> Any): String {
    return requireNotNull(getQuery(name), message)
}
