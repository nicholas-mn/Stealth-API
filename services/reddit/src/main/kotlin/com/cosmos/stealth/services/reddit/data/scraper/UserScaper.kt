package com.cosmos.stealth.services.reddit.data.scraper

import com.cosmos.stealth.core.network.data.scraper.Scraper
import com.cosmos.stealth.services.reddit.data.model.AboutUserChild
import com.cosmos.stealth.services.reddit.data.model.AboutUserData
import com.cosmos.stealth.services.reddit.data.model.Child
import kotlinx.coroutines.CoroutineDispatcher
import org.jsoup.nodes.Document

class UserScaper(
    ioDispatcher: CoroutineDispatcher
) : RedditScraper<Child>(ioDispatcher) {

    override suspend fun scrapDocument(document: Document): Child {
        val name = document.selectFirst("div.titlebox")
            ?.selectFirst(Scraper.Selector.Tag.H1)
            ?.text()
            .orEmpty()

        val postKarma = document.selectFirst("span.karma")
            ?.toInt()
            ?: 0

        val commentKarma = document.selectFirst("span.karma.comment-karma")
            ?.toInt()
            ?: 0

        val created = document.selectFirst("span.age")
            ?.selectFirst("time")
            ?.toTimeInSeconds()
            ?: 0L

        val data = AboutUserData(
            isSuspended = false,
            isEmployee = false,
            null,
            null,
            null,
            postKarma,
            -1,
            name,
            created,
            null,
            commentKarma
        )

        return AboutUserChild(data)
    }
}
