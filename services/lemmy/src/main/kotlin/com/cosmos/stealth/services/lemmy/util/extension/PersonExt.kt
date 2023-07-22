package com.cosmos.stealth.services.lemmy.util.extension

import com.cosmos.stealth.core.model.api.PosterType
import com.cosmos.stealth.services.lemmy.data.model.Person

fun Person.toPosterType(): PosterType {
    return when {
        admin -> PosterType.admin
        else -> PosterType.regular
    }
}
