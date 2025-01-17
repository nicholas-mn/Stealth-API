package com.cosmos.stealth.services.lemmy.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.MarkdownParser
import com.cosmos.stealth.core.common.util.extension.asList
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.core.model.api.PostType
import com.cosmos.stealth.core.model.api.Postable
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.network.util.extension.isImage
import com.cosmos.stealth.core.network.util.extension.mime
import com.cosmos.stealth.core.network.util.extension.mimeType
import com.cosmos.stealth.core.network.util.extension.toMedia
import com.cosmos.stealth.services.lemmy.data.model.PostView
import com.cosmos.stealth.services.lemmy.di.LemmyModule.Qualifier.LEMMY_QUALIFIER
import com.cosmos.stealth.services.lemmy.util.extension.getAuthorName
import com.cosmos.stealth.services.lemmy.util.extension.getPosterType
import com.cosmos.stealth.services.lemmy.util.extension.toDateInMillis
import io.ktor.http.ContentType
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.jsoup.parser.Parser
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class PostMapper(
    @Named(LEMMY_QUALIFIER) private val markdownParser: MarkdownParser,
    @Named(DEFAULT_DISPATCHER_QUALIFIER) defaultDispatcher: CoroutineDispatcher
) : Mapper<PostView, Service, Feedable>(defaultDispatcher) {

    override suspend fun toEntity(from: PostView, context: Service?): Feedable {
        return with(from) {
            val httpUrl = post.url?.toHttpUrlOrNull()
            val mimeType = httpUrl?.mimeType

            val postType = getPostType(mimeType)

            val media = when (postType) {
                PostType.image, PostType.video -> mimeType?.run { Media(this.mime, MediaSource(post.url.orEmpty())) }
                else -> null
            }

            Postable(
                context ?: Service(ServiceName.lemmy),
                post.id.toString(),
                postType,
                community.name,
                Parser.unescapeEntities(post.name, true),
                creator.getAuthorName(context?.instance),
                counts.score,
                counts.comments,
                post.url?.takeIf { it.isNotBlank() } ?: post.apId,
                post.apId,
                post.published.toDateInMillis() ?: System.currentTimeMillis(),
                getPosterType(creator),
                post.body?.takeIf { it.isNotEmpty() }?.run { markdownParser.parse(this) },
                null, // TODO
                httpUrl?.host,
                post.updated.toDateInMillis(),
                null,
                post.url == null,
                post.nsfw,
                null,
                null,
                post.locked,
                post.featuredLocal || post.featuredCommunity,
                null,
                post.thumbnailUrl?.toMedia() ?: media,
                media?.asList(),
                null,
                null
            )
        }
    }

    private fun PostView.getPostType(mime: ContentType?): PostType {
        return when {
            mime.isImage -> PostType.image
            post.url == null -> PostType.text
            else -> PostType.link
        }
    }
}
