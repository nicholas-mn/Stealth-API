package com.cosmos.stealth.server.util.extension

import com.cosmos.stealth.core.common.data.Config
import io.ktor.server.application.Application

val Application.config: Config
    get() {
        val useOauth = this.environment.config.propertyOrNull("reddit.useOauth")?.getString().toBoolean()

        return Config(Config.Reddit(useOauth))
    }
