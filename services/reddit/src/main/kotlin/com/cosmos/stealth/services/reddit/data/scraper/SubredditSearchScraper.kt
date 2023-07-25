package com.cosmos.stealth.services.reddit.data.scraper

import com.cosmos.stealth.core.network.data.scraper.Scraper
import com.cosmos.stealth.services.reddit.data.model.AboutChild
import com.cosmos.stealth.services.reddit.data.model.AboutData
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.ListingData
import kotlinx.coroutines.CoroutineDispatcher
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class SubredditSearchScraper(
    ioDispatcher: CoroutineDispatcher
) : RedditScraper<Listing>(ioDispatcher) {

    override suspend fun scrapDocument(document: Document): Listing {
        val subreddits = document.select("div.search-result-subreddit")

        val children = subreddits.map { it.toSubreddit() }
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

    private fun Element.toSubreddit(): AboutChild {
        val subscribeButton = selectFirst("span.search-subscribe-button")
        val name = subscribeButton?.attr(Selector.Attr.SR_NAME).orEmpty()

        val title = selectFirst("a.search-title")?.text().orEmpty()

        val over18 = selectFirst("span.nsfw-stamp") != null

        val link = selectFirst("a.search-subreddit-link")
            ?.attr(Scraper.Selector.Attr.HREF)
            .orEmpty()

        val subscribers = selectFirst("span.search-subscribers")
            ?.text()
            ?.run { SUBSCRIBERS_REGEX.find(this)?.value }
            ?.run { replace(",", "") }
            ?.run { toIntOrNull() }

        val data = AboutData(
            null,
            name,
            null,
            title,
            null,
            null,
            null,
            subscribers,
            null,
            null,
            "",
            "",
            null,
            null,
            over18,
            null,
            link,
            0L // TODO
        )

        return AboutChild(data)
    }

    companion object {
        private const val KIND = "t5"

        private val SUBSCRIBERS_REGEX = Regex("(\\d+,?)+")
    }
}
