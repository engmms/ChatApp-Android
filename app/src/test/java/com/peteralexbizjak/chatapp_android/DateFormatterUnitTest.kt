package com.peteralexbizjak.chatapp_android

import org.junit.Test
import java.util.*

class DateFormatterUnitTest {

    @Test
    fun void() {
        val timeRightNow: Long = Date().time
        println("Current time is ${timeRightNow}")
        assert(timeRightNow > 0)
    }
}