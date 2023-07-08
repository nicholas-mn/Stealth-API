package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.ServiceName

class ServicesService {

    fun getServices(): List<ServiceName> {
        return ServiceName.values().toList()
    }
}
