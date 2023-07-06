package com.cosmos.stealth.core.network.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class UrlSubstitutorTest {

    private val testCoroutineScheduler = TestCoroutineScheduler()
    private val defaultTestDispatcher = StandardTestDispatcher(testCoroutineScheduler, "Default Dispatcher")

    private lateinit var urlSubstitutor: UrlSubstitutor

    @Before
    fun setUp() {
        Dispatchers.setMain(defaultTestDispatcher)
        urlSubstitutor = UrlSubstitutor(defaultTestDispatcher)
    }

    @Test
    fun testBuildUrl() = runTest {
        val endpoint = "/r/{subreddit}/{sort}"

        val subredditParam = "subreddit" to "privacy"
        val sortParam = "sort" to "hot"

        val url = urlSubstitutor.buildUrl(endpoint, subredditParam, sortParam)

        assertEquals("/r/privacy/hot", url)
    }

    @Test
    fun testBuildWholeUrl() = runTest {
        val baseUrl = "https://teddit.net"
        val baseHttpUrl = baseUrl.toHttpUrl()
        val endpoint = "/r/{subreddit}/{sort}"

        val subredditParam = "subreddit" to "privacy"
        val sortParam = "sort" to "hot"

        val url1 = urlSubstitutor.buildUrl(baseUrl, endpoint, subredditParam, sortParam)
        val url2 = urlSubstitutor.buildUrl(baseHttpUrl, endpoint, subredditParam, sortParam)

        assertEquals("https://teddit.net/r/privacy/hot", url1)
        assertEquals("https://teddit.net/r/privacy/hot", url2)
        assertFailsWith(IllegalStateException::class) {
            urlSubstitutor.buildUrl("invalidurl.com", endpoint, subredditParam, sortParam)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
