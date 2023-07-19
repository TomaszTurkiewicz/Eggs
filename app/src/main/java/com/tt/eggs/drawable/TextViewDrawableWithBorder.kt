package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R
import com.tt.eggs.classes.MyPath
import com.tt.eggs.classes.Theme

class TextViewDrawableWithBorder (private val context: Context): Drawable() {
    private val paint = Paint()



    override fun draw(canvas: Canvas) {

        val width = bounds.width()
        val height = bounds.height()
        val marginFirst = height*0.05
        val radius1 = height*0.25

        val marginSecond = height*0.1
        val radius2 = height*0.2

        val marginThirdTop = height*0.16
        val marginThirdLeft = height*0.16


        val marginVertical = height*0.2
        val marginHorizontal = height*0.2
        val additionalMargin = height*0.06

        paint.isAntiAlias = true
        val rectR1 = RectF(marginFirst.toFloat(), marginFirst.toFloat(),(width-marginFirst).toFloat(),(height-marginFirst).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)
        canvas.drawRoundRect(rectR1, radius1.toFloat(), radius1.toFloat(),paint)

        paint.shader = LinearGradient(0f,
            marginSecond.toFloat(),
            0f,
            (height-marginSecond).toFloat(),
//            ContextCompat.getColor(context,R.color.white),
            ContextCompat.getColor(context, Theme(context).getWhiteColor()),
//            ContextCompat.getColor(context,R.color.gray_light),
            ContextCompat.getColor(context,Theme(context).getAccentLightColor()),
            Shader.TileMode.MIRROR)
        val rectR2 = RectF(marginSecond.toFloat(), marginSecond.toFloat(),(width-marginSecond).toFloat(),(height-marginSecond).toFloat())
        canvas.drawRoundRect(rectR2, radius2.toFloat(), radius2.toFloat(),paint)

        paint.shader = LinearGradient(0f,
            marginThirdTop.toFloat(),
            0f,
            (height-marginThirdTop).toFloat(),
//            ContextCompat.getColor(context,R.color.gray_dark),
            ContextCompat.getColor(context,Theme(context).getAccentDarkColor()),
//            ContextCompat.getColor(context,R.color.gray_middle),
            ContextCompat.getColor(context,Theme(context).getAccentMiddleColor()),
            Shader.TileMode.MIRROR)
        val rectR3 = RectF(marginThirdLeft.toFloat(), marginThirdTop.toFloat(),(width-marginThirdLeft).toFloat(),(height-marginThirdTop).toFloat())
        canvas.drawRect(rectR3,paint)
        paint.shader = null

        val rectR4 = RectF(marginHorizontal.toFloat(), marginVertical.toFloat(),(width-marginHorizontal).toFloat(),(height-marginVertical).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, Theme(context).getLCDColor())
        canvas.drawRect(rectR4,paint)

        val topLeftExternal = Point(marginHorizontal.toInt(),marginVertical.toInt())
        val topLeftInternal = Point((marginHorizontal+additionalMargin).toInt(),(marginVertical+additionalMargin).toInt())

        val topRightExternal = Point((width-marginHorizontal).toInt(),marginVertical.toInt())
        val topRightInternal = Point((width-(marginHorizontal+additionalMargin)).toInt(),(marginVertical+additionalMargin).toInt())

        val bottomLeftExternal = Point(marginHorizontal.toInt(),(height-marginVertical).toInt())
        val bottomLeftInternal = Point((marginHorizontal+additionalMargin).toInt(),(height-(marginVertical+additionalMargin)).toInt())

        val bottomRightExternal = Point((width-marginHorizontal).toInt(),(height-marginVertical).toInt())
        val bottomRightInternal = Point((width-(marginHorizontal+additionalMargin)).toInt(),(height-(marginVertical+additionalMargin)).toInt())

        paint.shader = LinearGradient(0f,
            topLeftExternal.y.toFloat(),
            0f,
            topLeftInternal.y.toFloat(),
            ContextCompat.getColor(context,R.color.black),
            ContextCompat.getColor(context,Theme(context).getLCDColor()),
            Shader.TileMode.MIRROR)
        val pathTop = MyPath()
        pathTop.move(topLeftExternal)
        pathTop.line(topLeftInternal)
        pathTop.line(topRightInternal)
        pathTop.line(topRightExternal)
        pathTop.close()
        canvas.drawPath(pathTop,paint)

        paint.shader = LinearGradient(0f,
            bottomLeftExternal.y.toFloat(),
            0f,
            bottomLeftInternal.y.toFloat(),
            ContextCompat.getColor(context,R.color.black),
            ContextCompat.getColor(context,Theme(context).getLCDColor()),
            Shader.TileMode.MIRROR)
        val pathBottom = MyPath()
        pathBottom.move(bottomLeftExternal)
        pathBottom.line(bottomLeftInternal)
        pathBottom.line(bottomRightInternal)
        pathBottom.line(bottomRightExternal)
        pathBottom.close()
        canvas.drawPath(pathBottom,paint)

        paint.shader = LinearGradient(topLeftExternal.x.toFloat(),
            0f,
            topLeftInternal.x.toFloat(),
            0f,
            ContextCompat.getColor(context,R.color.black),
            ContextCompat.getColor(context,Theme(context).getLCDColor()),
            Shader.TileMode.MIRROR)
        val pathLeft = MyPath()
        pathLeft.move(bottomLeftExternal)
        pathLeft.line(bottomLeftInternal)
        pathLeft.line(topLeftInternal)
        pathLeft.line(topLeftExternal)
        pathLeft.close()
        canvas.drawPath(pathLeft,paint)

        paint.shader = LinearGradient(topRightExternal.x.toFloat(),
            0f,
            topRightInternal.x.toFloat(),
            0f,
            ContextCompat.getColor(context,R.color.black),
            ContextCompat.getColor(context,Theme(context).getLCDColor()),
            Shader.TileMode.MIRROR)
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