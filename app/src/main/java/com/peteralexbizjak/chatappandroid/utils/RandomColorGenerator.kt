package com.peteralexbizjak.chatappandroid.utils

import android.graphics.Color

import java.util.Random

object RandomColorGenerator {
    fun generateRandomColorInt(): Int {
        val random = Random()
        return Color.argb(200, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }
}
