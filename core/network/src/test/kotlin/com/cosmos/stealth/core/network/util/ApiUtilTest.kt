package com.cosmos.stealth.core.network.util

import com.cosmos.stealth.core.network.data.annotation.GET
import com.cosmos.stealth.core.network.data.annotation.Header
import com.cosmos.stealth.core.network.data.annotation.Path
import com.cosmos.stealth.core.network.data.annotation.Query
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ApiUtilTest {

    @Test
    fun testGetEndpoint() {
        val endpoint = ApiUtilTest::testGetMethod.getEndpoint()

        assertEquals("/endpoint", endpoint)
        assertFailsWith(IllegalStateException::class) { ApiUtilTest::testNoMethod.getEndpoint() }
        assertFailsWith(IllegalStateException::class) { ApiUtilTest::testUnknownMethod.getEndpoint() }
    }

    @Test
    fun testGetPathParameter() {
        val path1 = ApiUtilTest::testMethod.getPathParameter(0)
        val path2 = ApiUtilTest::testMethod.getPathParameter(1)

        assertEquals("path1", path1)
        assertEquals("path2", path2)
        assertFailsWith(IllegalStateException::class) { ApiUtilTest::testMethod.getPathParameter(2) }
    }

    @Test
    fun testGetQueryParameter() {
        val query1 = ApiUtilTest::testMethod.getQueryParameter(0)
        val query2 = ApiUtilTest::testMethod.getQueryParameter(1)

        assertEquals("query1", query1)
        assertEquals("query2", query2)
        assertFailsWith(IllegalStateException::class) { ApiUtilTest::testMethod.getQueryParameter(2) }
    }

    @Test
    fun testGetHeader() {
        val header1 = ApiUtilTest::testMethod.getHeader(0)
        val header2 = ApiUtilTest::testMethod.getHeader(1)

        assertEquals("header1", header1)
        assertEquals("header2", header2)
        assertFailsWith(IllegalStateException::class) { ApiUtilTest::testMethod.getHeader(2) }
    }

    @GET("/endpoint")
    private fun testGetMethod() {
        // ignore
    }

    private fun testNoMethod() {
        // ignore
    }

    @Dummy("dummy")
    private fun testUnknownMethod() {
        // ignore
    }

    @Suppress("UnusedParameter", "LongParameterList")
    @GET("/endpoint")
    private fun testMethod(
        @Header("header1") header1: String,
        @Path("path1") path1: String,
        @Query("query1") query1: String,
        @Path("path2") path2: String,
        @Query("query2") query2: String,
        @Header("header2") header2: String
    ) {
        // ignore
    }

    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Dummy(val value: String)
}
