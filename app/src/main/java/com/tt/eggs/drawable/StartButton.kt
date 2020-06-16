package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R

class StartButton (private val context: Context, private val width:Double, private val height:Double): Drawable(){
    private val paint = Paint()

    private val widthUnit = width/10
    private val heightUnit = height/10
    private val width1 = 0.2
    private val width2 = 1.0
    private val width3 = 1.4


    override fun draw(canvas: Canvas) {
        val rect1 = RectF(0f, (heightUnit*2).toFloat(), width.toFloat(), (heightUnit*8).toFloat())
        val radius1 = heightUnit*3
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)
        canvas.drawRoundRect(rect1, radius1.toFloat(), radius1.toFloat(),paint)

        val rect2 = RectF((0f+width1*widthUnit).toFloat(), (heightUnit*(2+width1)).toFloat(), (width-(width1*widthUnit)).toFloat(), (heightUnit*8-(widthUnit*width1)).toFloat())
        val radius2 = heightUnit*3 - heightUnit*width1
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray_light)
        canvas.drawRoundRect(rect2, radius2.toFloat(), radius2.toFloat(),paint)

        val rect3 = RectF((0f+width2*widthUnit).toFloat(), (heightUnit*(2+width2)).toFloat(), (width-(width2*widthUnit)).toFloat(), (heightUnit*8-(widthUnit*width2)).toFloat())
        val radius3 = heightUnit*3 - heightUnit*width2
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)
        canvas.drawRoundRect(rect3, radius3.toFloat(), radius3.toFloat(),paint)

        val rect4 = RectF((0f+width3*widthUnit).toFloat(), (heightUnit*(2+width3)).toFloat(), (width-(width3*widthUnit)).toFloat(), (heightUnit*8-(widthUnit*width3)).toFloat())
        val radius4 = heightUnit*3 - heightUnit*width3
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.red)
        canvas.drawRoundRect(rect4, radius4.toFloat(), radius4.toFloat(),paint)

    }

    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }


}