package com.cosmos.stealth.services.reddit

import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.services.reddit.data.repository.RedditRepository
import org.koin.core.annotation.Single

@Single
class RedditGateway(redditRepository: RedditRepository) : Gateway(redditRepository) {

    override val name: ServiceName
        get() = ServiceName.reddit
}
