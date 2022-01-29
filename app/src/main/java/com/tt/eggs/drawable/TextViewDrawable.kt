package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R
import com.tt.eggs.classes.MyPath

class TextViewDrawable  (private val context: Context, private val width:Double, private val height:Double): Drawable() {

    private val paint = Paint()
    private val radius1 = height*0.3

    private val margin2 = height*0.04
    private val radius2 = height*0.25

    private val margin3top = height*0.1
    private val margin3left = height*0.13


    private val marginVertical = height*0.15
    private val marginHorizontal = height*0.2
    private val additionalMargin = height*0.06


    override fun draw(canvas: Canvas) {

        paint.isAntiAlias = true
        val rectR1 = RectF(0f, 0f,width.toFloat(),height.toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)
        canvas.drawRoundRect(rectR1, radius1.toFloat(), radius1.toFloat(),paint)

        paint.shader = LinearGradient(0f,margin2.toFloat(),0f,(height-margin2).toFloat(),ContextCompat.getColor(context,R.color.white),ContextCompat.getColor(context,R.color.gray_light),Shader.TileMode.MIRROR)
        val rectR2 = RectF(margin2.toFloat(), margin2.toFloat(),(width-margin2).toFloat(),(height-margin2).toFloat())
        canvas.drawRoundRect(rectR2, radius2.toFloat(), radius2.toFloat(),paint)


        paint.shader = LinearGradient(0f,margin3top.toFloat(),0f,(height-margin3top).toFloat(),ContextCompat.getColor(context,R.color.gray_dark),ContextCompat.getColor(context,R.color.gray_middle),Shader.TileMode.MIRROR)
        val rectR3 = RectF(margin3left.toFloat(), margin3top.toFloat(),(width-margin3left).toFloat(),(height-margin3top).toFloat())
        canvas.drawRect(rectR3,paint)
        paint.shader = null

        val rectR4 = RectF(marginHorizontal.toFloat(), marginVertical.toFloat(),(width-marginHorizontal).toFloat(),(height-marginVertical).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray_LCD)
        canvas.drawRect(rectR4,paint)

        val topLeftExternal = Point(marginHorizontal.toInt(),marginVertical.toInt())
        val topLeftInternal = Point((marginHorizontal+additionalMargin).toInt(),(marginVertical+additionalMargin).toInt())

        val topRightExternal = Point((width-marginHorizontal).toInt(),marginVertical.toInt())
        val topRightInternal = Point((width-(marginHorizontal+additionalMargin)).toInt(),(marginVertical+additionalMargin).toInt())

        val bottomLeftExternal = Point(marginHorizontal.toInt(),(height-marginVertical).toInt())
        val bottomLeftInternal = Point((marginHorizontal+additionalMargin).toInt(),(height-(marginVertical+additionalMargin)).toInt())

        val bottomRightExternal = Point((width-marginHorizontal).toInt(),(height-marginVertical).toInt())
        val bottomRightInternal = Point((width-(marginHorizontal+additionalMargin)).toInt(),(height-(marginVertical+additionalMargin)).toInt())

        paint.shader = LinearGradient(0f,topLeftExternal.y.toFloat(),0f,topLeftInternal.y.toFloat(),ContextCompat.getColor(context,R.color.black),ContextCompat.getColor(context,R.color.gray_LCD),Shader.TileMode.MIRROR)
        val pathTop = MyPath()
        pathTop.move(topLeftExternal)
        pathTop.line(topLeftInternal)
        pathTop.line(topRightInternal)
        pathTop.line(topRightExternal)
        pathTop.close()
        canvas.drawPath(pathTop,paint)

        paint.shader = LinearGradient(0f,bottomLeftExternal.y.toFloat(),0f,bottomLeftInternal.y.toFloat(),ContextCompat.getColor(context,R.color.black),ContextCompat.getColor(context,R.color.gray_LCD),Shader.TileMode.MIRROR)
        val pathBottom = MyPath()
        pathBottom.move(bottomLeftExternal)
        pathBottom.line(bottomLeftInternal)
        pathBottom.line(bottomRightInternal)
        pathBottom.line(bottomRightExternal)
        pathBottom.close()
        canvas.drawPath(pathBottom,paint)

        paint.shader = LinearGradient(topLeftExternal.x.toFloat(),0f,topLeftInternal.x.toFloat(),0f,ContextCompat.getColor(context,R.color.black),ContextCompat.getColor(context,R.color.gray_LCD),Shader.TileMode.MIRROR)
        val pathLeft = MyPath()
        pathLeft.move(bottomLeftExternal)
        pathLeft.line(bottomLeftInternal)
        pathLeft.line(topLeftInternal)
        pathLeft.line(topLeftExternal)
        pathLeft.close()
        canvas.drawPath(pathLeft,paint)

        paint.shader = LinearGradient(topRightExternal.x.toFloat(),0f,topRightInternal.x.toFloat(),0f,ContextCompat.getColor(context,R.color.black),ContextCompat.getColor(context,R.color.gray_LCD),Shader.TileMode.MIRROR)
        val pathRight = MyPath()
        pathRight.move(bottomRightExternal)
        pathRight.line(bottomRightInternal)
        pathRight.line(topRightInternal)
        pathRight.line(topRightExternal)
        pathRight.close()
        canvas.drawPath(pathRight,paint)
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