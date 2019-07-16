package com.peteralexbizjak.chatappandroid.utils;

import android.graphics.Color;

import java.util.Random;

public final class RandomColorGenerator {
    public static int generateRandomColorInt() {
        Random random = new Random();
        return Color.argb(200, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
