package com.cosmos.stealth.services.base.util.extension

import com.cosmos.stealth.core.model.api.After
import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.Service

fun Any.toAfter(service: Service): After {
    return After(service, AfterKey(this))
}
