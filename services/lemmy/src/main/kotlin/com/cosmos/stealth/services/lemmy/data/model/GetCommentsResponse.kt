package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetCommentsResponse(
    @Json(name = "comments")
    val comments: List<CommentView>
)
