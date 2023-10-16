package com.cosmos.stealth.services.reddit.data.scraper

import com.cosmos.stealth.core.network.data.scraper.Scraper
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.ListingData
import com.cosmos.stealth.services.reddit.data.model.Media
import com.cosmos.stealth.services.reddit.data.model.PostChild
import com.cosmos.stealth.services.reddit.data.model.PostData
import com.cosmos.stealth.services.reddit.data.model.RedditVideoPreview
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class PostSearchScraper(ioDispatcher: CoroutineDispatcher) : RedditScraper<Listing>(ioDispatcher) {

    override suspend fun scrapDocument(document: Document): Listing {
        val posts = document.select("div.search-result-link")

        val children = posts.map { it.toPost() }
        val after = getNextKey()

        return Listing(
            KIND,
            ListingData(
                null,
                null,
                children,
                after,
                null
            )
        )
    }

    private fun Element.toPost(): PostChild {
        val name = attr(Selector.Attr.FULLNAME)

        val titleHeader = selectFirst("header.search-result-header")

        val title = titleHeader?.selectFirst(Scraper.Selector.Tag.A)?.text().orEmpty()

        val flairRichText = titleHeader
            ?.toRichFlairText()
            ?: emptyList()

        val flair = titleHeader
            ?.selectFirst("span.linkflairlabel")
            ?.text()

        val prefixedSubreddit = selectFirst("a.search-subreddit-link")?.text().orEmpty()
        val subreddit = prefixedSubreddit.removePrefix("r/")

        val score = selectFirst("span.search-score")
            ?.text()
            ?.removeSuffix("points")
            ?.trim()
            ?.toIntOrNull()
            ?: 0

        val url = selectFirst("a.search-link")?.text().orEmpty()
        val domain = url.toHttpUrlOrNull()?.host.orEmpty()

        val isSelf = domain.isBlank()

        val isOver18 = selectFirst("span.nsfw-stamp") != null
        val isSpoiler = selectFirst("span.spoiler-stamp") != null

        val commentsNumber = selectFirst("a.search-comments")
            ?.text()
            ?.removeSuffix("comments")
            ?.trim()
            ?.toIntOrNull()
            ?: 0

        val thumbnailLink = selectFirst("a.thumbnail")

        val permalink = thumbnailLink?.attr(Scraper.Selector.Attr.HREF).orEmpty()

        val created = selectFirst("span.search-time")
            ?.selectFirst("time")
            ?.toTimeInSeconds()
            ?: 0L

        val thumbnail = thumbnailLink
            ?.selectFirst(Scraper.Selector.Tag.IMG)
            ?.attr(Scraper.Selector.Attr.SRC)
            ?.toValidLink()

        val authorClass = selectFirst("a.author")
        val author = authorClass?.text() ?: "[deleted]"

        val selfTextHtml = selectFirst("div.search-result-body")
            ?.selectFirst(Selector.MD)?.outerHtml()

        val media = when {
            domain == "v.redd.it" -> {
                Media(
                    null,
                    null,
                    RedditVideoPreview(
                        "$url/DASH_720.mp4",
                        0,
                        0,
                        0,
                        false,
                        "$url/DASHPlaylist.mpd"
                    )
                )
            }
            else -> null
        }

        val postData = PostData(
            subreddit,
            flairRichText,
            authorFlairRichText = null,
            title,
            prefixedSubreddit,
            name,
            null,
            0,
            false,
            flair,
            null,
            null,
            score,
            null,
            isSelf,
            null,
            domain,
            selfTextHtml,
            null,
            false,
            isOver18,
            null,
            emptyList(),
            isSpoiler,
            false,
            null,
            author,
            commentsNumber,
            permalink,
            false,
            url,
            created,
            media,
            null,
            null,
            false
        ).apply {
            this.thumbnail = thumbnail
        }

        return PostChild(postData)
    }

    companion object {
        private const val KIND = "t3"
    }
}
