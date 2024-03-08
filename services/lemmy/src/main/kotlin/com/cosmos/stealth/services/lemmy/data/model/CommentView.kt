package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentView(
    @Json(name = "comment")
    val comment: Comment,

    @Json(name = "creator")
    val creator: Person,

    @Json(name = "post")
    val post: Post,

    @Json(name = "community")
    val community: Community,

    @Json(name = "counts")
    val counts: CommentAggregates,

    @Json(name = "creator_banned_from_community")
    val creatorBannedFromCommunity: Boolean,

    @Json(name = "saved")
    val saved: Boolean,

    @Json(name = "creator_blocked")
    val creatorBlocked: Boolean,

    @Json(name = "creator_is_moderator")
    val creatorIsModerator: Boolean?,

    @Json(name = "creator_is_admin")
    val creatorIsAdmin: Boolean?
)
