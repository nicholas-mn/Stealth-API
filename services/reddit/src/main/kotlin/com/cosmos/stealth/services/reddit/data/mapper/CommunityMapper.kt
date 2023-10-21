package com.cosmos.stealth.services.reddit.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.extension.toMillis
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.CommunityType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.network.util.extension.mime
import com.cosmos.stealth.core.network.util.extension.toMedia
import com.cosmos.stealth.services.reddit.data.model.AboutChild
import com.cosmos.stealth.services.reddit.util.extension.getRefLink
import io.ktor.http.ContentType
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class CommunityMapper(
    @Named(DEFAULT_DISPATCHER_QUALIFIER) defaultDispatcher: CoroutineDispatcher
) : Mapper<AboutChild, Service, CommunityInfo>(defaultDispatcher) {

    override suspend fun toEntity(from: AboutChild, context: Service?): CommunityInfo {
        return with(from.data) {
            CommunityInfo(
                CommunityType.community,
                context ?: Service(ServiceName.reddit),
                name,
                displayName,
                created.toMillis(),
                title,
                publicDescriptionHtml,
                descriptionHtml,
                iconImg?.takeIf { it.isNotBlank() }?.toMedia(ContentType.Image.PNG.mime),
                headerImg?.takeIf { it.isNotBlank() }?.toMedia(ContentType.Image.PNG.mime),
                subscribers,
                activeUserCount,
                url.getRefLink(context?.instance.orEmpty()),
                over18,
                primaryColor?.ifBlank { null }
            )
        }
    }
}
