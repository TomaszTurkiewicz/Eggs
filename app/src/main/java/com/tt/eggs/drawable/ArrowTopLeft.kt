package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R

class ArrowTopLeft (private val context: Context, private val width:Double, private val height:Double):Drawable(){

    private val paint = Paint()

    private val widthUnit = width/24
    private val heightUnit = height/16
    private val radius1 = widthUnit*8.0
    private val radius2 = widthUnit*7.6
    private val radius3 = widthUnit*6.0
    private val radius4 = widthUnit*5.2
    private val o = Point((widthUnit*16).toInt(), (heightUnit*8).toInt())

    private val a = Point((widthUnit*7).toInt(), (heightUnit*6).toInt())
    private val b = Point((widthUnit*9).toInt(), (heightUnit*2).toInt())
    private val c = Point((widthUnit*0).toInt(), (heightUnit*0).toInt())


    override fun draw(canvas: Canvas) {


        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)

        canvas.drawCircle(
            o.x.toFloat(), o.y.toFloat(),
            radius1.toFloat(),paint)

        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray_light)

        canvas.drawCircle(
            o.x.toFloat(), o.y.toFloat(),
            radius2.toFloat(),paint)

        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)

        canvas.drawCircle(
            o.x.toFloat(), o.y.toFloat(),
            radius3.toFloat(),paint)

        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.red)

        canvas.drawCircle(
            o.x.toFloat(), o.y.toFloat(),
            radius4.toFloat(),paint)

        paint.style = Paint.Style.FILL
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