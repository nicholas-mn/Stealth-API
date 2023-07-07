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
 * @param upvotes 
 * @param refLink 
 * @param created 
 * @param depth 
 * @param downvotes 
 * @param replies 
 * @param edited 
 * @param pinned 
 * @param controversial 
 * @param awards 
 * @param authorFlair 
 */
@JsonClass(generateAdapter = true)
data class CommentFeedable (
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

    @Json(name = "upvotes")
    val upvotes: Int,

    @Json(name = "refLink")
    val refLink: String,

    @Json(name = "created")
    val created: Long,

    @Json(name = "depth")
    val depth: Int,

    @Json(name = "downvotes")
    val downvotes: Int? = null,

    @Json(name = "replies")
    val replies: List<Feedable>? = null,

    @Json(name = "edited")
    val edited: Long? = null,

    @Json(name = "pinned")
    val pinned: Boolean? = null,

    @Json(name = "controversial")
    val controversial: Boolean? = null,

    @Json(name = "awards")
    val awards: Awards? = null,

    @Json(name = "authorFlair")
    val authorFlair: Flair? = null,

    @Json(name = "submitter")
    val submitter: Boolean,
) : Feedable(FeedableType.comment)
