package com.cosmos.stealth.services.reddit.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.util.extension.toMillis
import com.cosmos.stealth.core.model.api.CommentFeedable
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.MoreContentFeedable
import com.cosmos.stealth.core.model.api.Reactions
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.ChildType
import com.cosmos.stealth.services.reddit.data.model.CommentChild
import com.cosmos.stealth.services.reddit.data.model.CommentData
import com.cosmos.stealth.services.reddit.data.model.MoreChild
import com.cosmos.stealth.services.reddit.data.model.MoreData
import com.cosmos.stealth.services.reddit.data.model.PostData
import com.cosmos.stealth.services.reddit.util.extension.toReaction
import com.cosmos.stealth.services.reddit.util.toBadge
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
    ): MutableList<Feedable> = withContext(defaultDispatcher) {
        dataToEntities(data, context, parent?.name)
    }

    suspend fun dataToEntities(
        data: List<Child>?,
        context: Service?,
        parentId: String?
    ): MutableList<Feedable> = withContext(defaultDispatcher) {
        data?.map { dataToEntity(it, context, parentId) }?.toMutableList() ?: mutableListOf()
    }

    private suspend fun dataToEntity(
        data: CommentData,
        context: Service?,
        parentId: String?
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
                dataToEntities(replies?.data?.children, context, parentId),
                edited.takeIf { it > -1 },
                stickied,
                controversiality > 0,
                getReactions(),
                toBadge(authorFlairRichText, flair),
                isSubmitter
            )
        }
    }

    private fun dataToEntity(data: MoreData, context: Service?, parentId: String?): MoreContentFeedable {
        with(data) {
            return MoreContentFeedable(
                context ?: Service(ServiceName.reddit),
                id,
                count,
                children,
                parentId.orEmpty(),
                depth ?: 0
            )
        }
    }

    private suspend fun dataToEntity(
        data: Child,
        context: Service?,
        parentId: String? = null
    ): Feedable = withContext(defaultDispatcher) {
        when (data.kind) {
            ChildType.t1 -> dataToEntity((data as CommentChild).data, context, parentId)
            ChildType.more -> dataToEntity((data as MoreChild).data, context, parentId)
            else -> error("Unknown kind ${data.kind}")
        }
    }

    private fun CommentData.getReactions(): Reactions {
        return Reactions(
            totalAwards,
            awardings.map { it.toReaction() }
        )
    }
}
