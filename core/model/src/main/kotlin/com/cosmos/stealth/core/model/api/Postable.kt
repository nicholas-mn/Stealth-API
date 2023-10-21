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
 * @param score
 * @param commentCount 
 * @param url 
 * @param refLink 
 * @param created
 * @param posterType
 * @param body 
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
 * @param reactions
 * @param media 
 * @param postBadge
 * @param authorBadge
 */
@JsonClass(generateAdapter = true)
data class Postable (
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

    @Json(name = "score")
    val score: Int,

    @Json(name = "commentCount")
    val commentCount: Int,

    @Json(name = "url")
    val url: String,

    @Json(name = "refLink")
    val refLink: String,

    @Json(name = "created")
    val created: Long,

    @Json(name = "posterType")
    val posterType: PosterType = PosterType.regular,

    @Json(name = "body")
    val body: String? = null,

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

    @Json(name = "reactions")
    val reactions: Reactions? = null,

    @Json(name = "preview")
    val preview: Media? = null,

    @Json(name = "media")
    val media: List<Media>? = null,

    @Json(name = "postBadge")
    val postBadge: Badge? = null,

    @Json(name = "authorBadge")
    val authorBadge: Badge? = null
) : Feedable(FeedableType.post)
