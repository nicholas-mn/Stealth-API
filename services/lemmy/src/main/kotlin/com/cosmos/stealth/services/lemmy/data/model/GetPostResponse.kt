package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPostResponse(
    @Json(name = "post_view")
    val postView: PostView,

    @Json(name = "community_view")
    val communityView: CommunityView,

    @Json(name = "moderators")
    val moderators: List<CommunityModeratorView>,

    @Json(name = "cross_posts")
    val crossposts: List<PostView>,
)
