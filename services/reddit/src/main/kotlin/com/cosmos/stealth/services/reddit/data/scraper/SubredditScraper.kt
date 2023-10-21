package com.cosmos.stealth.services.reddit.data.scraper

import com.cosmos.stealth.core.network.data.scraper.Scraper
import com.cosmos.stealth.services.reddit.data.model.AboutChild
import com.cosmos.stealth.services.reddit.data.model.AboutData
import com.cosmos.stealth.services.reddit.data.model.Child
import kotlinx.coroutines.CoroutineDispatcher
import org.jsoup.nodes.Document

class SubredditScraper(
    ioDispatcher: CoroutineDispatcher
) : RedditScraper<Child>(ioDispatcher) {

    override suspend fun scrapDocument(document: Document): Child {
        val title = document.selectFirst(Scraper.Selector.Tag.TITLE)?.text().orEmpty()

        val redditName = document.selectFirst("h1.redditname")
            ?.selectFirst(Scraper.Selector.Tag.A)

        val displayName = redditName?.text().orEmpty()
        val link = redditName?.attr(Scraper.Selector.Attr.HREF).orEmpty()

        val communityIcon = document.selectFirst("img[id=header-img]")
            ?.attr(Scraper.Selector.Attr.SRC)
            ?.toValidLink()
            .orEmpty()

        val subscribers = document.selectFirst("span.subscribers")
            ?.selectFirst(Selector.NUMBER)
            ?.toInt()

        val activeUsers = document.selectFirst("p.users-online")
            ?.selectFirst(Selector.NUMBER)
            ?.toInt()

        val titleBox = document.selectFirst("div.titlebox")
        val descriptionHtml = titleBox
            ?.selectFirst(Selector.MD)
            ?.outerHtml()

        val name = titleBox
            ?.selectFirst("input[name=thing_id]")
            ?.attr("value")
            .orEmpty()

        val data = AboutData(
            name,
            null,
            displayName,
            null,
            title,
            null,
            activeUsers,
            null,
            subscribers,
            null,
            null,
            communityIcon,
            "",
            null,
            null,
            false,
            descriptionHtml,
            link,
            0L // TODO
        )

        return AboutChild(data)
    }
}
