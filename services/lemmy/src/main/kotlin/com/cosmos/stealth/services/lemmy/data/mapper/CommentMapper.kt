package com.cosmos.stealth.services.lemmy.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.util.MarkdownParser
import com.cosmos.stealth.core.model.api.CommentFeedable
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.MoreContentFeedable
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.services.lemmy.data.model.Comment
import com.cosmos.stealth.services.lemmy.data.model.CommentView
import com.cosmos.stealth.services.lemmy.util.extension.toDateInMillis
import kotlinx.coroutines.CoroutineDispatcher

class CommentMapper(
    private val markdownParser: MarkdownParser,
    defaultDispatcher: CoroutineDispatcher
) : Mapper<CommentView, Service, Feedable>(defaultDispatcher) {

    override suspend fun toEntity(from: CommentView, context: Service?): Feedable {
        return with(from) {
            CommentFeedable(
                context ?: Service(ServiceName.lemmy),
                comment.id.toString(),
                post.id.toString(),
                community.name,
                markdownParser.parse(comment.content),
                creator.name,
                counts.upvotes,
                comment.apId,
                comment.published.toDateInMillis(),
                comment.depth,
                counts.downvotes,
                mutableListOf(),
                comment.updated.toDateInMillis(),
                null,
                counts.downvotes > counts.upvotes,
                null,
                null,
                creator.id == post.creatorId
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
                val parent = commentMap[parentId]?.feedable as? CommentFeedable?
                parent?.replies?.add(comment.feedable)
            } ?: run {
                // If the comment has no parent, it's a top-level comment; add it to the list of comments
                comments.add(comment.feedable)
            }
        }

        from.forEach { commentView ->
            val commentData = commentMap[commentView.comment.id] ?: return@forEach

            val comment = commentData.feedable as? CommentFeedable ?: return@forEach

            val childCount = commentData.commentView.counts.childCount

            // Check if a comment has more replies that need to be fetched
            if (childCount > 0 && comment.replies.isNullOrEmpty()) {
                val moreCommentFeedable = MoreContentFeedable(
                    context ?: Service(ServiceName.lemmy),
                    comment.id,
                    childCount,
                    listOf(),
                    comment.id,
                    comment.depth
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
        get() = path.split(".").run { this[lastIndex - 1] }.toInt().takeIf { it != 0 }
}
