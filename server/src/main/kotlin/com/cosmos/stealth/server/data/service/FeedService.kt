package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.common.util.extension.interlace
import com.cosmos.stealth.core.model.api.After
import com.cosmos.stealth.core.model.api.Feed
import com.cosmos.stealth.core.model.api.FeedRequest
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.Status
import com.cosmos.stealth.core.model.data.Default
import com.cosmos.stealth.core.model.data.Filtering
import com.cosmos.stealth.core.model.data.RequestInfo
import com.cosmos.stealth.core.model.data.SingleFeedRequest
import com.cosmos.stealth.core.network.util.LinkValidator
import com.cosmos.stealth.server.data.manager.GatewayManager
import com.cosmos.stealth.services.base.util.extension.orInternalError
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import org.koin.core.annotation.Single
import kotlin.math.ceil

@Single
class FeedService(private val gatewayManager: GatewayManager) {

    suspend fun getFeed(requestInfo: RequestInfo, feedRequest: FeedRequest): Feed = supervisorScope {
        val requests = feedRequest.services.associateWith { serviceRequest ->
            feedRequest.after?.find { it.service.name == serviceRequest.service.name }
        }

        val splitLimit = ceil((feedRequest.limit ?: Default.LIMIT) / requests.size.toDouble()).toInt()

        val responses = requests.map { requestEntry ->
            async {
                val service = requestEntry.key.service

                val instance = LinkValidator(service.instance).validUrl?.host

                val singleFeedRequest = SingleFeedRequest(
                    requestInfo,
                    requestEntry.key.communities ?: emptyList(),
                    service.copy(instance = instance),
                    Filtering(
                        feedRequest.sort ?: Default.SORT,
                        feedRequest.order ?: Default.ORDER,
                        feedRequest.time ?: Default.TIME
                    ),
                    splitLimit,
                    requestEntry.value?.key
                )

                val response = gatewayManager.getServiceGateway(service).getFeed(singleFeedRequest)

                requestEntry.key.service to response
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
