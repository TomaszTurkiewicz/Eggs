package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R

class ArrowUp (private val context: Context, private val width:Double, private val height:Double): Drawable() {
    private val paint = Paint()

    private val widthUnit = width/16
    private val heightUnit = height/32

    private val radius1 = widthUnit*8.0
    private val radius2 = widthUnit*7.6
    private val radius3 = widthUnit*6.0
    private val radius4 = widthUnit*5.2

    private val a = Point((widthUnit*8).toInt(), 0)
    private val b = Point((widthUnit*12).toInt(), (heightUnit*15).toInt())
    private val c = Point((widthUnit*4).toInt(), (heightUnit*15).toInt())

    private val o = Point((widthUnit*8).toInt(),(heightUnit*24).toInt())

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