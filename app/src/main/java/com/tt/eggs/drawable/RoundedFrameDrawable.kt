package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R

class RoundedFrameDrawable (private val context: Context, private val width:Double, private val height:Double, private val stroke:Double, private val radius:Double): Drawable() {
    private val paint = Paint()




    override fun draw(canvas: Canvas) {

        val stroke = stroke
        val radius = radius
        val rectR = RectF(stroke.toFloat(), stroke.toFloat(),(width-stroke).toFloat(),(height-stroke).toFloat())
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (stroke).toFloat()
        paint.color = ContextCompat.getColor(context, R.color.black)
        canvas.drawRoundRect(rectR, radius.toFloat(), radius.toFloat(),paint)

    }


    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

}