package com.cosmos.stealth.core.data.model.dash

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName(value = "MPD", namespace = "urn:mpeg:dash:schema:mpd:2011")
data class MPD(
    @SerialName("Period")
    val period: Period
)
