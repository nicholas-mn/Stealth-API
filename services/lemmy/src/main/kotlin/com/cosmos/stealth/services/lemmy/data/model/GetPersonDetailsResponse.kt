package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPersonDetailsResponse(
    @Json(name = "person_view")
    val personView: PersonView,

    @Json(name = "comments")
    val comments: List<CommentView>,

    @Json(name = "posts")
    val posts: List<PostView>,

    @Json(name = "moderates")
    val moderates: List<CommunityModeratorView>
)
