package com.cosmos.stealth.services.lemmy.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.MarkdownParser
import com.cosmos.stealth.core.model.api.Appendable
import com.cosmos.stealth.core.model.api.Commentable
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.services.lemmy.data.model.Comment
import com.cosmos.stealth.services.lemmy.data.model.CommentView
import com.cosmos.stealth.services.lemmy.di.LemmyModule.Qualifier.LEMMY_QUALIFIER
import com.cosmos.stealth.services.lemmy.util.extension.getAuthorName
import com.cosmos.stealth.services.lemmy.util.extension.toDateInMillis
import com.cosmos.stealth.services.lemmy.util.extension.toPosterType
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class CommentMapper(
    @Named(LEMMY_QUALIFIER) private val markdownParser: MarkdownParser,
    @Named(DEFAULT_DISPATCHER_QUALIFIER) defaultDispatcher: CoroutineDispatcher
) : Mapper<CommentView, Service, Feedable>(defaultDispatcher) {

    override suspend fun toEntity(from: CommentView, context: Service?): Feedable {
        return with(from) {
            Commentable(
                context ?: Service(ServiceName.lemmy),
                comment.id.toString(),
                post.id.toString(),
                community.name,
                markdownParser.parse(comment.content),
                creator.getAuthorName(context?.instance),
                counts.score,
                comment.apId,
                comment.published.toDateInMillis() ?: System.currentTimeMillis(),
                creator.id == post.creatorId,
                creator.toPosterType(),
                comment.depth,
                mutableListOf(),
                comment.updated.toDateInMillis(),
                null,
                counts.downvotes > counts.upvotes,
                null,
                null,
                null,
                post.name,
                post.apId
            )
        }
    }

    override suspend fun toEntities(from: List<CommentView>, context: Service?): List<Feedable> {
        // Comments from the API are flatten. We need to "rebuild" the tree manually.

        // Associate each comment's ID to the mapped entity
        val commentMap = from.associate { commentView ->
            val comment = toEntity(commentView, context)
            commentView.comment.id to CommentData(comment, commentView)
        }

        val comments = mutableListOf<Feedable>()

        from.forEach { commentView ->
            val comment = commentMap[commentView.comment.id] ?: return@forEach

            // For each comment, find its parent
            commentView.comment.parentId?.let { parentId ->
                // If the comment has a parent, it's a reply; add it to the replies of its parent
                val parent = commentMap[parentId]?.feedable as? Commentable?
                parent?.replies?.add(comment.feedable)
            } ?: run {
                // If the comment has no parent, it's a top-level comment; add it to the list of comments
                comments.add(comment.feedable)
            }
        }

        from.forEach { commentView ->
            val commentData = commentMap[commentView.comment.id] ?: return@forEach

            val comment = commentData.feedable as? Commentable ?: return@forEach

            val childCount = commentData.commentView.counts.childCount

            // Check if a comment has more replies that need to be fetched
            if (childCount > 0 && comment.replies.isNullOrEmpty()) {
                val moreCommentFeedable = Appendable(
                    context ?: Service(ServiceName.lemmy),
                    comment.id,
                    childCount,
                    listOf(),
                    comment.id,
                    comment.postId,
                    comment.postRefLink,
                    comment.depth ?: 0
                )
                comment.replies?.add(moreCommentFeedable)
            }
        }

        return comments
    }

    private data class CommentData(
        val feedable: Feedable,
        val commentView: CommentView
    )

    private val Comment.depth: Int
        get() = path.split(".").size.minus(2)

    private val Comment.parentId: Int?
        get() = path.split(".").run { getOrNull(lastIndex - 1) }?.toInt().takeIf { it != 0 }
}
