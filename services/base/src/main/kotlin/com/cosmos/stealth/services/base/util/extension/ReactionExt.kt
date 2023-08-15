package com.cosmos.stealth.services.base.util.extension

import com.cosmos.stealth.core.model.api.Reactions

fun Reactions.orNull(): Reactions? {
    return takeUnless { it.reactions.isEmpty() }
}
