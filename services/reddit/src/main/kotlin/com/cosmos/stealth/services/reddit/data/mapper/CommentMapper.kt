package com.cosmos.stealth.services.reddit.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.util.extension.toMillis
import com.cosmos.stealth.core.model.api.Awards
import com.cosmos.stealth.core.model.api.CommentFeedable
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.MoreContentFeedable
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.ChildType
import com.cosmos.stealth.services.reddit.data.model.CommentChild
import com.cosmos.stealth.services.reddit.data.model.CommentData
import com.cosmos.stealth.services.reddit.data.model.MoreChild
import com.cosmos.stealth.services.reddit.data.model.MoreData
import com.cosmos.stealth.services.reddit.data.model.PostData
import com.cosmos.stealth.services.reddit.util.extension.toAward
import com.cosmos.stealth.services.reddit.util.toFlair
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CommentMapper(
    defaultDispatcher: CoroutineDispatcher
) : Mapper<Child, Service, Feedable>(defaultDispatcher) {

    override suspend fun toEntity(from: Child, context: Service?): Feedable {
        throw UnsupportedOperationException()
    }

    suspend fun dataToEntities(
        data: List<Child>?,
        context: Service?,
        parent: PostData?
    ): List<Feedable> = withContext(defaultDispatcher) {
        data?.map { dataToEntity(it, context, parent) } ?: listOf()
    }

    private suspend fun dataToEntity(
        data: CommentData,
        context: Service?,
        parent: PostData?
    ): CommentFeedable = withContext(defaultDispatcher) {
        with(data) {
            CommentFeedable(
                context ?: Service(ServiceName.reddit),
                name,
                linkId,
                subreddit,
                bodyHtml,
                author,
                score,
                permalink,
                created.toMillis(),
                depth ?: 0,
                null,
                dataToEntities(replies?.data?.children, context, parent),
                edited.takeIf { it > -1 },
                stickied,
                controversiality > 0,
                getAwards(),
                toFlair(authorFlairRichText, flair),
                isSubmitter
            )
        }
    }

    private fun dataToEntity(data: MoreData, context: Service?): MoreContentFeedable {
        with(data) {
            return MoreContentFeedable(
                context ?: Service(ServiceName.reddit),
                id,
                count,
                children,
                parentId,
                depth ?: 0
            )
        }
    }

    private suspend fun dataToEntity(
        data: Child,
        context: Service?,
        parent: PostData? = null
    ): Feedable = withContext(defaultDispatcher) {
        when (data.kind) {
            ChildType.t1 -> dataToEntity((data as CommentChild).data, context, parent)
            ChildType.more -> dataToEntity((data as MoreChild).data, context)
            else -> error("Unknown kind ${data.kind}")
        }
    }

    private fun CommentData.getAwards(): Awards {
        return Awards(
            totalAwards,
            awardings.map { it.toAward() }
        )
    }
}
