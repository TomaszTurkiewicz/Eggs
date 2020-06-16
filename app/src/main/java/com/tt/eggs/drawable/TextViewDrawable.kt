package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R

class TextViewDrawable  (private val context: Context, private val width:Double, private val height:Double): Drawable() {

    private val paint = Paint()
    private val radius1 = height*0.3

    private val margin2 = height*0.05
    private val radius2 = height*0.25

    private val margin3 = height*0.1
    private val radius3 = height*0.2

    private val marginVertical = height*0.15
    private val marginHorizontal = height*0.2


    override fun draw(canvas: Canvas) {

        val rectR1 = RectF(0f, 0f,width.toFloat(),height.toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)
        canvas.drawRoundRect(rectR1, radius1.toFloat(), radius1.toFloat(),paint)

        val rectR2 = RectF(margin2.toFloat(), margin2.toFloat(),(width-margin2).toFloat(),(height-margin2).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray_light)
        canvas.drawRoundRect(rectR2, radius2.toFloat(), radius2.toFloat(),paint)


        val rectR3 = RectF(margin3.toFloat(), margin3.toFloat(),(width-margin3).toFloat(),(height-margin3).toFloat())
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