package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPostsResponse(
    @Json(name = "posts")
    val posts: List<PostView>
)
