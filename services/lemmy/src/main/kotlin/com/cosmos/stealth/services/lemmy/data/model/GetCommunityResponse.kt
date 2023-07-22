package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetCommunityResponse(
    @Json(name = "community_view")
    val communityView: CommunityView,

    @Json(name = "site")
    val site: Site? = null,

    @Json(name = "moderators")
    val moderators: List<CommunityModeratorView>,

    @Json(name = "discussion_languages")
    val discussionLanguages: List<Int>,
)
