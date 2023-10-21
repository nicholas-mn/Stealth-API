package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.ServiceName
import org.koin.core.annotation.Single

@Single
class ServicesService {

    fun getServices(): List<ServiceName> {
        return ServiceName.values().toList()
    }
}
