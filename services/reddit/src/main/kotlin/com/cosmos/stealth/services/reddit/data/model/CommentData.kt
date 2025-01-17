package com.cosmos.stealth.services.reddit.data.model

import com.cosmos.stealth.services.reddit.data.adapter.Edited
import com.cosmos.stealth.services.reddit.data.adapter.Replies
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Suppress("LongParameterList")
@JsonClass(generateAdapter = true)
class CommentData(
    @Json(name = "total_awards_received")
    val totalAwards: Int,

    @Json(name = "author_flair_richtext")
    val authorFlairRichText: List<RichText>?,

    @Json(name = "link_id")
    val linkId: String,

    @Json(name = "replies")
    @Replies
    val replies: Listing?,

    @Json(name = "author")
    val author: String,

    @Json(name = "score")
    val score: Int,

    @Json(name = "all_awardings")
    val awardings: List<Awarding>,

    @Json(name = "body_html")
    val bodyHtml: String,

    @Json(name = "edited")
    @Edited
    val edited: Long,

    @Json(name = "is_submitter")
    val isSubmitter: Boolean,

    @Json(name = "stickied")
    val stickied: Boolean,

    @Json(name = "score_hidden")
    val scoreHidden: Boolean,

    @Json(name = "permalink")
    val permalink: String,

    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "created_utc")
    val created: Long,

    @Json(name = "controversiality")
    val controversiality: Int,

    @Json(name = "author_flair_text")
    val flair: String?,

    @Json(name = "depth")
    val depth: Int?,

    @Json(name = "distinguished")
    val distinguished: String?,

    @Json(name = "subreddit")
    val subreddit: String,

    @Json(name = "link_title")
    val linkTitle: String?,

    @Json(name = "link_permalink")
    val linkPermalink: String?,

    @Json(name = "link_author")
    val linkAuthor: String?
)
