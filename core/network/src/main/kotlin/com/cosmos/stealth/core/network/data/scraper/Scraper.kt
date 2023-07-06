package com.cosmos.stealth.core.network.data.scraper

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

abstract class Scraper<Result>(
    private val ioDispatcher: CoroutineDispatcher
) {

    protected var document: Document? = null

    open suspend fun scrap(body: String): Result = withContext(ioDispatcher) {
        val document = Jsoup.parse(body)
        this@Scraper.document = document
        scrapDocument(document)
    }

    protected abstract suspend fun scrapDocument(document: Document): Result

    protected object Selector {
        object Attr {
            const val HREF = "href"
            const val SRC = "src"
            const val ID = "id"
        }

        object Tag {
            const val A = "a"
            const val IMG = "img"
            const val TITLE = "title"
            const val H1 = "h1"
        }
    }
}
