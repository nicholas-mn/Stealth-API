package com.cosmos.stealth.services.lemmy.util.extension

import com.cosmos.stealth.core.common.util.extension.orFalse
import com.cosmos.stealth.core.model.api.PosterType
import com.cosmos.stealth.services.lemmy.data.model.Person
import com.cosmos.stealth.services.lemmy.data.model.PostView

fun PostView.getPosterType(person: Person): PosterType {
    return when {
        person.admin.orFalse() || creatorIsAdmin.orFalse() -> PosterType.admin
        creatorIsModerator.orFalse() -> PosterType.moderator
        creator.botAccount -> PosterType.bot
        else -> PosterType.regular
    }
}
