package com.cosmos.stealth.core.data.model.dash

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdaptationSet(
    @SerialName("contentType")
    val contentType: String,

    @SerialName("Representation")
    val representations: List<Representation>
)
