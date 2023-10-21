package com.cosmos.stealth.core.network.util.extension

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpHeaders
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ClientExtKtTest {

    @Test
    fun testForwardIPv6() {
        val ipv6Host = "2001:db8:3333:4444:5555:6666:7777:8888"

        val builder = HttpRequestBuilder()
        builder.forward(ipv6Host, false)

        assertEquals("for=\"[2001:db8:3333:4444:5555:6666:7777:8888]\"", builder.headers[HttpHeaders.Forwarded])
        assertEquals("2001:db8:3333:4444:5555:6666:7777:8888", builder.headers[HttpHeaders.XForwardedFor])
    }

    @Test
    fun testForwardIPv4() {
        val ipv4Host = "192.168.1.24"

        val builder = HttpRequestBuilder()
        builder.forward(ipv4Host, false)

        assertEquals("for=192.168.1.24", builder.headers[HttpHeaders.Forwarded])
        assertEquals("192.168.1.24", builder.headers[HttpHeaders.XForwardedFor])
    }

    @Test
    fun testForwardUnknown() {
        val ipv4Host = "aaa.bbb.ccc.ddd"

        val builder = HttpRequestBuilder()
        builder.forward(ipv4Host, false)

        assertNull(builder.headers[HttpHeaders.Forwarded])
        assertNull(builder.headers[HttpHeaders.XForwardedFor])
    }

    @Test
    fun testForwardNull() {
        val builder = HttpRequestBuilder()

        builder.forward(null, false)
        assertNull(builder.headers[HttpHeaders.Forwarded])
        assertNull(builder.headers[HttpHeaders.XForwardedFor])

        builder.forward(null, true)
        assertNull(builder.headers[HttpHeaders.Forwarded])
        assertNull(builder.headers[HttpHeaders.XForwardedFor])
    }

    @Test
    fun testForwardProxyMode() {
        val ipv4Host = "1.2.3.4"

        val builder = HttpRequestBuilder()
        builder.forward(ipv4Host, true)

        assertNull(builder.headers[HttpHeaders.Forwarded])
        assertNull(builder.headers[HttpHeaders.XForwardedFor])
    }
}
