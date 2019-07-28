package com.peteralexbizjak.chatapp_android.utils

import android.graphics.*
import com.squareup.picasso.Transformation

//Source: Stack Overflow thread number 26112150
public class CircleTransform: Transformation {
    override fun key(): String { return "circle" }

    override fun transform(source: Bitmap?): Bitmap {
        val size: Int = Math.min(source!!.width, source.height)
        val x: Int = (source.width - size) / 2
        val y: Int = (source.height - size) / 2

        val squareBitmap: Bitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squareBitmap != source) source.recycle()

        val bitmap: Bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()

        val bitmapShader = BitmapShader(squareBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = bitmapShader
        paint.isAntiAlias = true

        val radius: Float = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)

        squareBitmap.recycle()
        return bitmap
    }
}