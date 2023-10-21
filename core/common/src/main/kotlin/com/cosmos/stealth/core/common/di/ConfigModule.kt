package com.cosmos.stealth.core.common.di

import com.cosmos.stealth.core.common.data.Config
import org.koin.dsl.module

class ConfigModule(config: Config) {

    val module = module { single { config } }
}
