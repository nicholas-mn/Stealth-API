package com.cosmos.stealth.core.data.model.dash

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
data class Representation(
    @SerialName("mimeType")
    val mimeType: String,

    @SerialName("height")
    val height: String? = null,

    @SerialName("width")
    val width: String? = null,

    @XmlElement(true)
    @SerialName("BaseURL")
    val baseUrl: String
)
