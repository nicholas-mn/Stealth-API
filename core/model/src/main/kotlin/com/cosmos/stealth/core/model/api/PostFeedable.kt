package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Post
 *
 * @param service
 * @param id
 * @param postType
 * @param community 
 * @param title 
 * @param author 
 * @param upvotes 
 * @param commentCount 
 * @param url 
 * @param refLink 
 * @param created 
 * @param body 
 * @param downvotes 
 * @param ratio 
 * @param domain 
 * @param edited 
 * @param oc 
 * @param self 
 * @param nsfw 
 * @param spoiler 
 * @param archived 
 * @param locked 
 * @param pinned 
 * @param awards 
 * @param media 
 * @param gallery 
 * @param postFlair 
 * @param authorFlair 
 * @param posterType 
 */
@JsonClass(generateAdapter = true)
data class PostFeedable (
    @Json(name = "service")
    val service: Service,

    @Json(name = "id")
    val id: String,

    @Json(name = "postType")
    val postType: PostType,

    @Json(name = "community")
    val community: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "author")
    val author: String,

    @Json(name = "upvotes")
    val upvotes: Int,

    @Json(name = "commentCount")
    val commentCount: Int,

    @Json(name = "url")
    val url: String,

    @Json(name = "refLink")
    val refLink: String,

    @Json(name = "created")
    val created: Long,

    @Json(name = "body")
    val body: String? = null,

    @Json(name = "downvotes")
    val downvotes: Int? = null,

    @Json(name = "ratio")
    val ratio: Double? = null,

    @Json(name = "domain")
    val domain: String? = null,

    @Json(name = "edited")
    val edited: Long? = null,

    @Json(name = "oc")
    val oc: Boolean? = null,

    @Json(name = "self")
    val self: Boolean? = null,

    @Json(name = "nsfw")
    val nsfw: Boolean? = null,

    @Json(name = "spoiler")
    val spoiler: Boolean? = null,

    @Json(name = "archived")
    val archived: Boolean? = null,

    @Json(name = "locked")
    val locked: Boolean? = null,

    @Json(name = "pinned")
    val pinned: Boolean? = null,

    @Json(name = "awards")
    val awards: Awards? = null,

    @Json(name = "preview")
    val preview: Media? = null,

    @Json(name = "media")
    val media: Media? = null,

    @Json(name = "gallery")
    val gallery: List<Media>? = null,

    @Json(name = "postFlair")
    val postFlair: Flair? = null,

    @Json(name = "authorFlair")
    val authorFlair: Flair? = null,

    @Json(name = "PosterType")
    val posterType: PosterType? = PosterType.regular
) : Feedable(FeedableType.post)
