package com.cosmos.stealth.services.lemmy.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.MarkdownParser
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.CommunityType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.network.util.extension.toMedia
import com.cosmos.stealth.services.lemmy.data.model.CommunityView
import com.cosmos.stealth.services.lemmy.di.LemmyModule.Qualifier.LEMMY_QUALIFIER
import com.cosmos.stealth.services.lemmy.util.extension.toDateInMillis
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class CommunityMapper(
    @Named(LEMMY_QUALIFIER) private val markdownParser: MarkdownParser,
    @Named(DEFAULT_DISPATCHER_QUALIFIER) defaultDispatcher: CoroutineDispatcher
) : Mapper<CommunityView, Service, CommunityInfo>(defaultDispatcher) {

    override suspend fun toEntity(from: CommunityView, context: Service?): CommunityInfo {
        return with(from) {
            CommunityInfo(
                CommunityType.community,
                context ?: Service(ServiceName.lemmy),
                community.id.toString(),
                community.name,
                community.published.toDateInMillis() ?: System.currentTimeMillis(),
                community.title,
                null,
                community.description?.run { markdownParser.parse(this) },
                community.icon?.toMedia(),
                community.banner?.toMedia(),
                counts.subscribers,
                counts.usersActiveDay,
                community.actorId,
                community.nsfw,
                null
            )
        }
    }
}
