package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R

class TextViewDrawableWithBorder (private val context: Context, private val width:Double, private val height:Double): Drawable() {
    private val paint = Paint()

    private val marginFirst = height*0.05
    private val radius1 = height*0.25

    private val marginSecond = height*0.1
    private val radius2 = height*0.2

    private val marginThird = height*0.15
    private val radius3 = height*0.15

    private val marginVertical = height*0.2
    private val marginHorizontal = height*0.2

    override fun draw(canvas: Canvas) {
        val rectR1 = RectF(marginFirst.toFloat(), marginFirst.toFloat(),(width-marginFirst).toFloat(),(height-marginFirst).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)
        canvas.drawRoundRect(rectR1, radius1.toFloat(), radius1.toFloat(),paint)

        val rectR2 = RectF(marginSecond.toFloat(), marginSecond.toFloat(),(width-marginSecond).toFloat(),(height-marginSecond).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray_light)
        canvas.drawRoundRect(rectR2, radius2.toFloat(), radius2.toFloat(),paint)


        val rectR3 = RectF(marginThird.toFloat(), marginThird.toFloat(),(width-marginThird).toFloat(),(height-marginThird).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray_dark)
        canvas.drawRoundRect(rectR3, radius3.toFloat(), radius3.toFloat(),paint)

        val rectR4 = RectF(marginHorizontal.toFloat(), marginVertical.toFloat(),(width-marginHorizontal).toFloat(),(height-marginVertical).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray_LCD)
        canvas.drawRect(rectR4,paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }


}