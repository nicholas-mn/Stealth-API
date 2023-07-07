package com.cosmos.stealth.services.teddit

import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.services.reddit.Gateway
import com.cosmos.stealth.services.teddit.data.repository.TedditRepository
import kotlinx.coroutines.CoroutineDispatcher

class TedditGateway(
    tedditRepository: TedditRepository,
    mainImmediateDispatcher: CoroutineDispatcher
) : Gateway(tedditRepository, mainImmediateDispatcher) {

    override val name: ServiceName
        get() = ServiceName.reddit
}
