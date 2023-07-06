package com.cosmos.stealth.services.reddit.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.util.extension.toMillis
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.CommunityType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.network.util.extension.toMedia
import com.cosmos.stealth.services.reddit.data.model.AboutChild
import kotlinx.coroutines.CoroutineDispatcher

class CommunityMapper(
    defaultDispatcher: CoroutineDispatcher
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
                communityIcon.toMedia(),
                headerImg?.toMedia(),
                subscribers,
                activeUserCount,
                url,
                over18,
                primaryColor
            )
        }
    }
}
