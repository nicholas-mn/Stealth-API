package com.cosmos.stealth.services.base.util.extension

import com.cosmos.stealth.core.model.api.After
import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.Service

fun String.toAfter(service: Service): After {
    return After(service, AfterKey(this))
}

fun String.toAfterKey(): AfterKey {
    return this.toIntOrNull()?.run { AfterKey(this) } ?: AfterKey(this)
}
