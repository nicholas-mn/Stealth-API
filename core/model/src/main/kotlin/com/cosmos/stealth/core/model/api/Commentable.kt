package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Comment
 *
 * @param service
 * @param id 
 * @param postId 
 * @param community 
 * @param body 
 * @param author 
 * @param score
 * @param refLink 
 * @param created
 * @param submitter
 * @param posterType
 * @param depth 
 * @param replies
 * @param edited 
 * @param pinned 
 * @param controversial 
 * @param reactions
 * @param authorBadge
 * @param postAuthor
 * @param postTitle
 * @param postRefLink
 */
@JsonClass(generateAdapter = true)
data class Commentable (
    @Json(name = "service")
    val service: Service,

    @Json(name = "id")
    val id: String,

    @Json(name = "postId")
    val postId: String,

    @Json(name = "community")
    val community: String,

    @Json(name = "body")
    val body: String,

    @Json(name = "author")
    val author: String,

    @Json(name = "score")
    val score: Int,

    @Json(name = "refLink")
    val refLink: String,

    @Json(name = "created")
    val created: Long,

    @Json(name = "submitter")
    val submitter: Boolean,

    @Json(name = "posterType")
    val posterType: PosterType = PosterType.regular,

    @Json(name = "depth")
    val depth: Int? = null,

    @Json(name = "replies")
    val replies: MutableList<Feedable>? = null,

    @Json(name = "edited")
    val edited: Long? = null,

    @Json(name = "pinned")
    val pinned: Boolean? = null,

    @Json(name = "controversial")
    val controversial: Boolean? = null,

    @Json(name = "reactions")
    val reactions: Reactions? = null,

    @Json(name = "authorBadge")
    val authorBadge: Badge? = null,

    @Json(name = "postAuthor")
    val postAuthor: String? = null,

    @Json(name = "postTitle")
    val postTitle: String? = null,

    @Json(name = "postRefLink")
    val postRefLink: String? = null
) : Feedable(FeedableType.comment)
