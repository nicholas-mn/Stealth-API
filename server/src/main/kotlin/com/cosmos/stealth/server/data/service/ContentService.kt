package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.MoreContentFeedable
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.model.data.RequestInfo
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.services.reddit.RedditGateway
import com.cosmos.stealth.services.teddit.TedditGateway

class ContentService(
    redditGateway: RedditGateway,
    tedditGateway: TedditGateway
) : BaseService(redditGateway, tedditGateway) {

    suspend fun getMoreContent(
        requestInfo: RequestInfo,
        moreContentFeedable: MoreContentFeedable
    ): Resource<List<Feedable>> {
        val service = moreContentFeedable.service
        return getServiceGateway(service).getMoreContent(Request(service, requestInfo), moreContentFeedable)
    }
}
