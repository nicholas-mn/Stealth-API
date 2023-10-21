package com.cosmos.stealth.core.network.util

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LinkValidatorTest {

    @Test
    fun testLinkValidator() {
        val link = "https://teddit.net"
        val linkValidator = LinkValidator(link)

        assertTrue(linkValidator.isValid)
        assertNotNull(linkValidator.validUrl)
        assertEquals("https://teddit.net/", linkValidator.validUrl.toString())
    }

    @Test
    fun testNoHttpLinkValidator() {
        val link = "teddit.net"
        val linkValidator = LinkValidator(link)

        assertTrue(linkValidator.isValid)
        assertNotNull(linkValidator.validUrl)
        assertEquals("https://teddit.net/", linkValidator.validUrl.toString())
    }

    @Test
    fun testNullLinkValidator() {
        val linkValidator = LinkValidator(null)

        assertFalse(linkValidator.isValid)
        assertNull(linkValidator.validUrl)
    }
}
