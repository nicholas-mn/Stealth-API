package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "type_")
    val type: SearchType,

    @Json(name = "comments")
    val comments: List<CommentView>,

    @Json(name = "posts")
    val posts: List<PostView>,

    @Json(name = "communities")
    val communities: List<CommunityView>,

    @Json(name = "users")
    val users: List<PersonView>
)
