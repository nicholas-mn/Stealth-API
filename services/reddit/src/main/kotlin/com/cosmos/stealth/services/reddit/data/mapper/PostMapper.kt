package com.cosmos.stealth.services.reddit.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.util.extension.toMillis
import com.cosmos.stealth.core.data.repository.DashRepository
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.core.model.api.MediaType
import com.cosmos.stealth.core.model.api.Postable
import com.cosmos.stealth.core.model.api.Reactions
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.core.network.util.extension.isImage
import com.cosmos.stealth.core.network.util.extension.mime
import com.cosmos.stealth.core.network.util.extension.mimeType
import com.cosmos.stealth.core.network.util.extension.toMedia
import com.cosmos.stealth.core.network.util.getImageIdFromImgurLink
import com.cosmos.stealth.core.network.util.getImgurVideo
import com.cosmos.stealth.core.network.util.getLinkType
import com.cosmos.stealth.core.network.util.getUrlFromImgurId
import com.cosmos.stealth.services.reddit.data.model.PostChild
import com.cosmos.stealth.services.reddit.data.model.PostData
import com.cosmos.stealth.services.reddit.data.model.RedditVideoPreview
import com.cosmos.stealth.services.reddit.util.extension.toMedia
import com.cosmos.stealth.services.reddit.util.extension.toPosterType
import com.cosmos.stealth.services.reddit.util.extension.toReaction
import com.cosmos.stealth.services.reddit.util.toBadge
import com.cosmos.stealth.services.reddit.util.toMedia
import io.ktor.http.ContentType
import kotlinx.coroutines.CoroutineDispatcher

class PostMapper(
    private val dashRepository: DashRepository,
    defaultDispatcher: CoroutineDispatcher
) : Mapper<PostChild, Service, Feedable>(defaultDispatcher) {

    override suspend fun toEntity(from: PostChild, context: Service?): Feedable {
        return with(from.data) {
            val mediaType = getMediaType()
            val media = getMedia(mediaType)
            val gallery = getGallery()

            val preview = getPreview(media, gallery)

            Postable(
                context ?: Service(ServiceName.reddit),
                name,
                mediaType.toPostType(),
                subreddit,
                title,
                author,
                score,
                commentsNumber,
                url,
                permalink,
                created.toMillis(),
                selfTextHtml,
                null,
                ratio,
                domain,
                null,
                isOC,
                isSelf,
                isOver18,
                isSpoiler,
                isArchived,
                isLocked,
                isStickied,
                getReactions(),
                preview,
                media,
                gallery,
                toBadge(linkFlairRichText, flair),
                toBadge(authorFlairRichText, authorFlair),
                distinguished.toPosterType()
            )
        }
    }

    private fun PostData.getReactions(): Reactions {
        return Reactions(
            totalAwards,
            awardings.map { it.toReaction() }
        )
    }

    private fun PostData.getMediaType(): MediaType {
        return when {
            isSelf -> MediaType.NO_MEDIA

            isRedditGallery == true -> MediaType.REDDIT_GALLERY

            isVideo -> {
                if (media?.redditVideoPreview?.isGif == true) {
                    MediaType.REDDIT_GIF
                } else {
                    MediaType.REDDIT_VIDEO
                }
            }

            domain == "v.redd.it" -> {
                if (media?.redditVideoPreview?.isGif == true) {
                    MediaType.REDDIT_GIF
                } else {
                    MediaType.REDDIT_VIDEO
                }
            }

            media?.redditVideoPreview != null || mediaPreview?.videoPreview != null -> MediaType.VIDEO

            mediaPreview?.images?.getOrNull(0)?.variants?.mp4 != null -> MediaType.VIDEO

            domain == "i.redd.it" -> MediaType.IMAGE

            else -> getLinkType(url)
        }
    }

    @Suppress("CyclomaticComplexMethod")
    private suspend fun PostData.getMedia(mediaType: MediaType): Media? {
        return when (mediaType) {
            MediaType.REDDIT_VIDEO, MediaType.REDDIT_GIF -> {
                crossposts?.firstOrNull()?.getMedia(mediaType)
                    ?: media?.redditVideoPreview?.toMedia()
                    ?: mediaPreview?.videoPreview?.toMedia()
            }

            MediaType.VIDEO -> {
                crossposts?.firstOrNull()?.getMedia(mediaType)
                    ?: media?.redditVideoPreview?.toMedia()
                    ?: mediaPreview?.videoPreview?.toMedia()
                    ?: mediaPreview?.images?.getOrNull(0)?.variants?.mp4?.toMedia()
            }

            MediaType.IMGUR_LINK -> {
                crossposts?.firstOrNull()?.getMedia(mediaType)
                    ?: run {
                        val id = getImageIdFromImgurLink(url)
                        val imageUrl = getUrlFromImgurId(id)
                        Media(ContentType.Image.JPEG.mime, MediaSource(imageUrl))
                    }
            }

            MediaType.IMGUR_GIF -> Media(ContentType.Video.MP4.mime, MediaSource(getImgurVideo(url)))

            MediaType.IMGUR_IMAGE, MediaType.IMAGE -> url.mimeType?.mime?.run { Media(this, MediaSource(url)) }

            else -> null
        }
    }

    // Reddit's API provides two lists of gallery items:
    // - gallery_data which is ordered but only contains IDs and captions
    // - media_metadata which contains all the URLs but is unordered
    // Therefore, we need to map the IDs from gallery_data with the URLs from media_metadata
    // to have an ordered list of items
    private fun PostData.getGallery(): List<Media>? {
        val items = galleryData?.items?.associateWith { galleryDataItem ->
            mediaMetadata?.items?.find { galleryItem -> galleryItem.id == galleryDataItem.mediaId }
        }

        return items?.mapNotNull { entry ->
            entry.value?.let { value -> toMedia(entry.key, value) }
        }
    }

    private fun PostData.getPreview(media: Media?, gallery: List<Media>?): Media? {
        return mediaPreview?.images?.getOrNull(0)?.toMedia()
            ?: gallery?.firstOrNull()
            ?: mediaMetadata?.items?.getOrNull(0)?.toMedia()
            ?: media?.takeIf { it.mime.startsWith("image") }
            ?: url.takeIf { it.mimeType.isImage }?.run { toMedia() }
            ?: thumbnail?.toMedia()
    }

    private suspend fun RedditVideoPreview.toMedia(): Media? {
        val url = fallbackUrl.substringBeforeLast('/')

        val dashMedia = dashUrl
            ?.run { dashRepository.getDash(this, url) }
            ?.run { (this as? Resource.Success)?.data }

        return dashMedia ?: toFallbackMedia()
    }

    private fun RedditVideoPreview.toFallbackMedia(): Media? {
        val mime = fallbackUrl.mimeType?.mime ?: return null

        return Media(mime, MediaSource(fallbackUrl, width, height), null, null)
    }
}
