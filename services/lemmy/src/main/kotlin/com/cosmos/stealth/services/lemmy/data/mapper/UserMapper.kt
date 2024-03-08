package com.cosmos.stealth.services.lemmy.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.MarkdownParser
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.api.UserType
import com.cosmos.stealth.core.network.util.extension.toMedia
import com.cosmos.stealth.services.lemmy.data.model.PersonView
import com.cosmos.stealth.services.lemmy.di.LemmyModule.Qualifier.LEMMY_QUALIFIER
import com.cosmos.stealth.services.lemmy.util.extension.getAuthorName
import com.cosmos.stealth.services.lemmy.util.extension.toDateInMillis
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class UserMapper(
    @Named(LEMMY_QUALIFIER) private val markdownParser: MarkdownParser,
    @Named(DEFAULT_DISPATCHER_QUALIFIER) defaultDispatcher: CoroutineDispatcher
) : Mapper<PersonView, Service, UserInfo>(defaultDispatcher) {

    override suspend fun toEntity(from: PersonView, context: Service?): UserInfo {
        return with(from) {
            UserInfo(
                UserType.user,
                context ?: Service(ServiceName.lemmy),
                person.id.toString(),
                person.getAuthorName(context?.instance),
                person.published.toDateInMillis() ?: System.currentTimeMillis(),
                person.avatar?.toMedia(),
                person.banner?.toMedia(),
                person.bio?.run { markdownParser.parse(this) },
                null,
                null,
                null,
                counts.postCount,
                counts.commentCount,
                0, // TODO
                person.actorId
            )
        }
    }
}
