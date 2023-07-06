package com.cosmos.stealth.services.base.util.extension

import com.cosmos.stealth.core.model.api.Status
import java.net.HttpURLConnection

val Status.isFailure: Boolean
    get() = this.code != HttpURLConnection.HTTP_OK
