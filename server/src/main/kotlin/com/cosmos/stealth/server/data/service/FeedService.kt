package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.common.util.extension.interlace
import com.cosmos.stealth.core.model.api.After
import com.cosmos.stealth.core.model.api.Feed
import com.cosmos.stealth.core.model.api.FeedRequest
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.api.Status
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.model.data.RequestInfo
import com.cosmos.stealth.services.base.util.extension.orInternalError
import com.cosmos.stealth.services.reddit.RedditGateway
import com.cosmos.stealth.services.teddit.TedditGateway
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope

class FeedService(redditGateway: RedditGateway, tedditGateway: TedditGateway) : Service(redditGateway, tedditGateway) {

    suspend fun getFeed(requestInfo: RequestInfo, feedRequest: FeedRequest): Feed = supervisorScope {
        val requests = feedRequest.services.associateWith { serviceRequest ->
            feedRequest.after?.find { it.service.name == serviceRequest.service.name }
        }

        val responses = requests.map { requestEntry ->
            async {
                val service = requestEntry.key.service
                val request = Request(requestEntry.key.service, requestInfo)

                val response = getServiceGateway(service)
                    .getFeed(
                        request,
                        requestEntry.key.communities,
                        feedRequest.sort ?: Sort.best,
                        requestEntry.value?.key
                    )

                Pair(requestEntry.key.service, response)
            }
        }.awaitAll()

        val feedables = mutableListOf<List<Feedable>>()
        val after = mutableListOf<After>()
        val status = mutableListOf<Status>()

        responses.forEach { response ->
            val feed = response.second

            feedables.add(feed.items)
            feed.after?.firstOrNull()?.let { after.add(it) }
            status.add(feed.status.firstOrNull().orInternalError(response.first))
        }

        val items = feedables.interlace()

        Feed(items.toList(), after.toList(), status.toList())
    }
}
