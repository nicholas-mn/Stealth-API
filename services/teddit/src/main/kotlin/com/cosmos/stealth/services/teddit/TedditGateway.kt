package com.cosmos.stealth.services.teddit

import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.services.reddit.Gateway
import com.cosmos.stealth.services.teddit.data.repository.TedditRepository

class TedditGateway(tedditRepository: TedditRepository) : Gateway(tedditRepository) {

    override val name: ServiceName
        get() = ServiceName.reddit
}
