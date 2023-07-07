package com.cosmos.stealth.services.reddit

import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.services.reddit.data.repository.RedditRepository
import kotlinx.coroutines.CoroutineDispatcher

class RedditGateway(
    redditRepository: RedditRepository,
    mainImmediateDispatcher: CoroutineDispatcher
) : Gateway(redditRepository, mainImmediateDispatcher) {

    override val name: ServiceName
        get() = ServiceName.reddit
}
