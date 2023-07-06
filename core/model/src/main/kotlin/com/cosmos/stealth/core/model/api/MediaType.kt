package com.cosmos.stealth.core.model.api

enum class MediaType {
    NO_MEDIA,

    // Reddit
    REDDIT_GALLERY, REDDIT_VIDEO, REDDIT_GIF,

    // Imgur
    IMGUR_GALLERY, IMGUR_ALBUM, IMGUR_IMAGE, IMGUR_VIDEO, IMGUR_GIF, IMGUR_LINK,

    // Gfycat
    GFYCAT, REDGIFS,

    // Streamable
    STREAMABLE,

    // Generic type
    IMAGE, VIDEO,

    LINK;

    fun toPostType(): PostType {
        return when (this) {
            NO_MEDIA -> PostType.text

            REDDIT_VIDEO,
            REDDIT_GIF,
            IMGUR_VIDEO,
            IMGUR_GIF,
            GFYCAT,
            REDGIFS,
            STREAMABLE,
            VIDEO -> PostType.video

            REDDIT_GALLERY,
            IMGUR_GALLERY,
            IMGUR_ALBUM,
            IMGUR_IMAGE,
            IMGUR_LINK,
            IMAGE -> PostType.image

            else -> PostType.text
        }
    }
}
