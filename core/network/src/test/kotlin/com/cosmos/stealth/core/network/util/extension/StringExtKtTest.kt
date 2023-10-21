package com.cosmos.stealth.core.network.util.extension

import com.cosmos.stealth.core.network.data.model.HostFormat
import org.junit.Test
import kotlin.test.assertEquals

class StringExtKtTest {

    @Test
    fun testHostFormat() {
        assertEquals(HostFormat.IPV6, "2001:db8:3333:4444:5555:6666:7777:8888".hostFormat)
        assertEquals(HostFormat.IPV4, "192.168.1.24".hostFormat)
        assertEquals(HostFormat.UNKNOWN, "aaa.bbb.ccc.ddd".hostFormat)
    }
}
