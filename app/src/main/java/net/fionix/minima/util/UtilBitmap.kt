package net.fionix.minima.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View

object UtilBitmap {
    fun renderFromView(height: Int, width: Int, view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }
}