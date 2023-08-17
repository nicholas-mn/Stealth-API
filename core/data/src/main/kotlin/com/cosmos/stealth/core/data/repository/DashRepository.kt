package com.cosmos.stealth.core.data.repository

import com.cosmos.stealth.core.data.di.DataModule.Qualifier.DASH_QUALIFIER
import com.cosmos.stealth.core.data.mapper.DashMapper
import com.cosmos.stealth.core.data.model.dash.MPD
import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.network.data.repository.NetworkRepository
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.core.network.util.extension.map
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class DashRepository(
    @Named(DASH_QUALIFIER) private val httpClient: HttpClient,
    private val dashMapper: DashMapper
) : NetworkRepository() {

    suspend fun getDash(url: String, baseUrl: String): Resource<Media?> {
        val resource = safeApiCall { httpClient.get(url).body<MPD>() }
        return resource.map { dashMapper.dataToEntity(it, baseUrl) }
    }
}
