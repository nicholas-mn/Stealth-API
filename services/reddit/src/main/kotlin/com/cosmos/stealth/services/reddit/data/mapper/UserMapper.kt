package com.cosmos.stealth.services.reddit.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.util.extension.toMillis
import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.api.UserType
import com.cosmos.stealth.core.network.util.extension.mime
import com.cosmos.stealth.core.network.util.extension.mimeType
import com.cosmos.stealth.core.network.util.extension.toMedia
import com.cosmos.stealth.services.reddit.data.model.AboutUserChild
import com.cosmos.stealth.services.reddit.data.model.AboutUserData
import kotlinx.coroutines.CoroutineDispatcher

class UserMapper(
    defaultDispatcher: CoroutineDispatcher
) : Mapper<AboutUserChild, Service, UserInfo>(defaultDispatcher) {

    override suspend fun toEntity(from: AboutUserChild, context: Service?): UserInfo {
        return with(from.data) {
            UserInfo(
                UserType.user,
                context ?: Service(ServiceName.reddit),
                id.orEmpty(),
                name,
                created.toMillis(),
                getIcon(),
                null,
                null,
                null,
                null,
                null,
                null,
                totalKarma
            )
        }
    }

    private fun AboutUserData.getIcon(): Media? {
        return when {
            iconImg != null -> {
                val mime = iconImg.mimeType?.mime ?: return null
                val snooMedia = snoovatarImg?.toMedia()
                Media(mime, MediaSource(iconImg), null, null, snooMedia?.run { listOf(this) })
            }
            snoovatarImg != null -> snoovatarImg.toMedia()
            else -> null
        }
    }
}
