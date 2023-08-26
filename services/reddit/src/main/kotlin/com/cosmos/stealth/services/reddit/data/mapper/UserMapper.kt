package com.cosmos.stealth.services.reddit.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.extension.asList
import com.cosmos.stealth.core.common.util.extension.toMillis
import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.api.UserType
import com.cosmos.stealth.core.network.util.extension.mime
import com.cosmos.stealth.core.network.util.extension.mimeType
import com.cosmos.stealth.services.reddit.data.model.AboutUserChild
import com.cosmos.stealth.services.reddit.data.model.AboutUserData
import com.cosmos.stealth.services.reddit.util.extension.getRefLink
import io.ktor.http.ContentType
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class UserMapper(
    @Named(DEFAULT_DISPATCHER_QUALIFIER) defaultDispatcher: CoroutineDispatcher
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
                getHeader(),
                subreddit?.publicDescription,
                null,
                null,
                null,
                null,
                null,
                totalKarma,
                subreddit?.url?.getRefLink(context?.instance.orEmpty())
            )
        }
    }

    private fun AboutUserData.getIcon(): Media? {
        fun getSnooAvatar(): Media? {
            return snoovatarImg?.run {
                Media(
                    ContentType.Image.PNG.mime,
                    MediaSource(this, snoovatarSize?.getOrNull(0), snoovatarSize?.getOrNull(1))
                )
            }
        }

        return when {
            iconImg != null -> {
                Media(
                    ContentType.Image.PNG.mime,
                    MediaSource(iconImg, subreddit?.iconSize?.getOrNull(0), subreddit?.iconSize?.getOrNull(1)),
                    null,
                    null,
                    getSnooAvatar()?.asList()
                )
            }

            snoovatarImg != null -> getSnooAvatar()

            else -> null
        }
    }

    private fun AboutUserData.getHeader(): Media? {
        val mime = subreddit?.bannerImg?.mimeType?.mime ?: return null

        return Media(
            mime,
            MediaSource(subreddit.bannerImg, subreddit.bannerSize?.getOrNull(0), subreddit.bannerSize?.getOrNull(1))
        )
    }
}
