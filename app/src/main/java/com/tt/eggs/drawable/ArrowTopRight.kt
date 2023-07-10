package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R

class ArrowTopRight (private val context: Context): Drawable(){

    private val paint = Paint()
    override fun draw(canvas: Canvas) {

        val widthUnit = bounds.width()/24
        val heightUnit = bounds.height()/16
        val radius1 = widthUnit*8.0
        val radius2 = widthUnit*7.6
        val radius3 = widthUnit*6.0
        val radius4 = widthUnit*5.2
        val o = Point((widthUnit*8), (heightUnit*8))

        val a = Point((widthUnit*17), (heightUnit*6))
        val b = Point((widthUnit*15), (heightUnit*2))
        val c = Point((widthUnit*24), (heightUnit*0))

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)
        canvas.drawCircle(
            o.x.toFloat(), o.y.toFloat(),
            radius1.toFloat(),paint)

        paint.shader = LinearGradient(0f,(o.y-radius2).toFloat(),0f,(o.y+radius2).toFloat(),ContextCompat.getColor(context,R.color.white),ContextCompat.getColor(context,R.color.gray_light),Shader.TileMode.MIRROR)
        canvas.drawCircle(
            o.x.toFloat(), o.y.toFloat(),
            radius2.toFloat(),paint)

        paint.shader = LinearGradient(0f,(o.y-radius3).toFloat(),0f,(o.y+radius3).toFloat(),ContextCompat.getColor(context,R.color.gray_middle),ContextCompat.getColor(context,R.color.black),Shader.TileMode.MIRROR)
        canvas.drawCircle(
            o.x.toFloat(), o.y.toFloat(),
            radius3.toFloat(),paint)

        paint.shader = LinearGradient(0f,(o.y-radius4).toFloat(),0f,(o.y+radius4).toFloat(),ContextCompat.getColor(context,R.color.red_light),ContextCompat.getColor(context,R.color.red_dark),Shader.TileMode.MIRROR)
        canvas.drawCircle(
            o.x.toFloat(), o.y.toFloat(),
            radius4.toFloat(),paint)
        paint.shader = null


        paint.color = ContextCompat.getColor(context, R.color.black)
        val path = Path()
        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.lineTo(b.x.toFloat(), b.y.toFloat())
        path.lineTo(c.x.toFloat(), c.y.toFloat())
        path.lineTo(a.x.toFloat(), a.y.toFloat())
        path.close()
        canvas.drawPath(path,paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }



}