package com.cosmos.stealth.server.data.config

import com.cosmos.stealth.core.network.data.converter.MoshiContentConverter
import com.cosmos.stealth.core.network.data.converter.moshi
import com.cosmos.stealth.core.network.di.NetworkModule.Qualifier.STEALTH_QUALIFIER
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.koin.ktor.ext.inject

fun Application.configureContentNegotiation() {
    val moshiContentConverter by inject<MoshiContentConverter>(STEALTH_QUALIFIER)

    install(ContentNegotiation) {
        moshi(moshiContentConverter)
    }
}
