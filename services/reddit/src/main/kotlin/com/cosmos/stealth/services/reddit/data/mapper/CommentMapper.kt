package com.cosmos.stealth.services.reddit.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.extension.toMillis
import com.cosmos.stealth.core.model.api.Appendable
import com.cosmos.stealth.core.model.api.Commentable
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.Reactions
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.services.base.util.extension.orNull
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.ChildType
import com.cosmos.stealth.services.reddit.data.model.CommentChild
import com.cosmos.stealth.services.reddit.data.model.CommentData
import com.cosmos.stealth.services.reddit.data.model.MoreChild
import com.cosmos.stealth.services.reddit.data.model.MoreData
import com.cosmos.stealth.services.reddit.data.model.PostData
import com.cosmos.stealth.services.reddit.util.extension.getRefLink
import com.cosmos.stealth.services.reddit.util.extension.toPosterType
import com.cosmos.stealth.services.reddit.util.extension.toReaction
import com.cosmos.stealth.services.reddit.util.toBadge
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class CommentMapper(
    @Named(DEFAULT_DISPATCHER_QUALIFIER) defaultDispatcher: CoroutineDispatcher
) : Mapper<Child, Service, Feedable>(defaultDispatcher) {

    override suspend fun toEntity(from: Child, context: Service?): Feedable {
        throw UnsupportedOperationException()
    }

    suspend fun dataToEntities(
        data: List<Child>?,
        context: Service?,
        parent: PostData?,
        parentId: String?
    ): MutableList<Feedable> = withContext(defaultDispatcher) {
        data?.map {
            async { dataToEntity(it, context, parent, parentId) }
        }?.awaitAll()?.toMutableList() ?: mutableListOf()
    }

    private suspend fun dataToEntity(
        data: CommentData,
        context: Service?,
        parent: PostData?,
        parentId: String?
    ): Commentable = withContext(defaultDispatcher) {
        with(data) {
            val postRefLink = linkPermalink?.toHttpUrlOrNull()?.encodedPath ?: parent?.permalink

            Commentable(
                context ?: Service(ServiceName.reddit),
                name,
                linkId,
                subreddit,
                bodyHtml,
                author,
                score,
                permalink.getRefLink(context?.instance.orEmpty()),
                created.toMillis(),
                isSubmitter,
                distinguished.toPosterType(),
                depth,
                dataToEntities(replies?.data?.children, context, parent, parentId),
                edited.takeIf { it > -1 }?.toMillis(),
                stickied,
                controversiality > 0,
                getReactions(),
                toBadge(authorFlairRichText, flair),
                linkAuthor ?: parent?.author,
                linkTitle ?: parent?.title,
                postRefLink?.getRefLink(context?.instance.orEmpty())
            )
        }
    }

    private fun dataToEntity(data: MoreData, context: Service?, parent: PostData?, parentId: String?): Appendable {
        with(data) {
            return Appendable(
                context ?: Service(ServiceName.reddit),
                id,
                count,
                children,
                this.parentId,
                parentId.orEmpty(),
                parent?.permalink?.getRefLink(context?.instance.orEmpty()),
                depth ?: 0
            )
        }
    }

    private suspend fun dataToEntity(
        data: Child,
        context: Service?,
        parent: PostData?,
        parentId: String?
    ): Feedable {
        return when (data.kind) {
            ChildType.t1 -> dataToEntity((data as CommentChild).data, context, parent, parentId)
            ChildType.more -> dataToEntity((data as MoreChild).data, context, parent, parent?.name ?: parentId)
            else -> error("Unknown kind ${data.kind}")
        }
    }

    private fun CommentData.getReactions(): Reactions? {
        return Reactions(
            totalAwards,
            awardings.map { it.toReaction() }
        ).orNull()
    }
}
