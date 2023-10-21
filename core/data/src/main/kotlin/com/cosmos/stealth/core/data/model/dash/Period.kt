package com.cosmos.stealth.core.data.model.dash

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Period(
    @SerialName("AdaptationSet")
    val sets: List<AdaptationSet>
)
