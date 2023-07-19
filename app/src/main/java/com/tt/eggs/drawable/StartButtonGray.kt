package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R
import com.tt.eggs.classes.Theme

class StartButtonGray (private val context: Context): Drawable() {
    private val paint = Paint()

    override fun draw(canvas: Canvas) {

        val width = bounds.width()
        val widthUnit = bounds.width().toDouble()/10
        val heightUnit = bounds.height().toDouble()/10
        val width1 = 0.2
        val width2 = 1.0
        val width3 = 1.4

        val rect1 = RectF(0f, (heightUnit*2).toFloat(), width.toFloat(), (heightUnit*8).toFloat())
        val radius1 = heightUnit*3
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)
        canvas.drawRoundRect(rect1, radius1.toFloat(), radius1.toFloat(),paint)

//        paint.shader = LinearGradient(0f,(heightUnit*(2+width1)).toFloat(),0f,(heightUnit*8-(widthUnit*width1)).toFloat(),
//            ContextCompat.getColor(context, R.color.white),
//            ContextCompat.getColor(context, R.color.gray_light),
//            Shader.TileMode.MIRROR)
        paint.shader = LinearGradient(0f,(heightUnit*(2+width1)).toFloat(),0f,(heightUnit*8-(widthUnit*width1)).toFloat(),
            ContextCompat.getColor(context, Theme(context).getWhiteColor()),
            ContextCompat.getColor(context, Theme(context).getAccentLightColor()),
            Shader.TileMode.MIRROR)
        val rect2 = RectF((0f+width1*widthUnit).toFloat(), (heightUnit*(2+width1)).toFloat(), (width-(width1*widthUnit)).toFloat(), (heightUnit*8-(widthUnit*width1)).toFloat())
        val radius2 = heightUnit*3 - heightUnit*width1
        canvas.drawRoundRect(rect2, radius2.toFloat(), radius2.toFloat(),paint)

//        paint.shader = LinearGradient(0f,(heightUnit*(2+width2)).toFloat(),0f,(heightUnit*8-(widthUnit*width2)).toFloat(),
//            ContextCompat.getColor(context, R.color.gray_middle),
//            ContextCompat.getColor(context, R.color.black),
//            Shader.TileMode.MIRROR)
        paint.shader = LinearGradient(0f,(heightUnit*(2+width2)).toFloat(),0f,(heightUnit*8-(widthUnit*width2)).toFloat(),
            ContextCompat.getColor(context, Theme(context).getAccentMiddleColor()),
            ContextCompat.getColor(context, R.color.black),
            Shader.TileMode.MIRROR)
        val rect3 = RectF((0f+width2*widthUnit).toFloat(), (heightUnit*(2+width2)).toFloat(), (width-(width2*widthUnit)).toFloat(), (heightUnit*8-(widthUnit*width2)).toFloat())
        val radius3 = heightUnit*3 - heightUnit*width2
        canvas.drawRoundRect(rect3, radius3.toFloat(), radius3.toFloat(),paint)

//        paint.shader = LinearGradient(0f,(heightUnit*(2+width3)).toFloat(),0f,(heightUnit*8-(widthUnit*width3)).toFloat(),
//            ContextCompat.getColor(context, R.color.gray_light),
//            ContextCompat.getColor(context, R.color.gray_dark),
//            Shader.TileMode.MIRROR)
        paint.shader = LinearGradient(0f,(heightUnit*(2+width3)).toFloat(),0f,(heightUnit*8-(widthUnit*width3)).toFloat(),
            ContextCompat.getColor(context, Theme(context).getAccentLightColor()),
            ContextCompat.getColor(context, Theme(context).getAccentDarkColor()),
            Shader.TileMode.MIRROR)
        val rect4 = RectF((0f+width3*widthUnit).toFloat(), (heightUnit*(2+width3)).toFloat(), (width-(width3*widthUnit)).toFloat(), (heightUnit*8-(widthUnit*width3)).toFloat())
        val radius4 = heightUnit*3 - heightUnit*width3
        canvas.drawRoundRect(rect4, radius4.toFloat(), radius4.toFloat(),paint)
        paint.shader = null
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}