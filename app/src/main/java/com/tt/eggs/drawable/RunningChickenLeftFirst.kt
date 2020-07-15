package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R
import com.tt.eggs.classes.Functions

class RunningChickenLeftFirst (private val context: Context, private val width: Double): Drawable() {
    private val paint = Paint()
    private val unit = width/200


    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = unit.toFloat()
        paint.color = ContextCompat.getColor(context, R.color.green)

        val a = Point((width*0.18).toInt(), (width*0.13).toInt())
        val b = Point((width*0.25).toInt(), (width*0.13).toInt())
        val curvedAB = Functions.curvedPath(a,b,unit*11,false)

        val path = Path()
        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.cubicTo(a.x.toFloat(), a.y.toFloat(),curvedAB.x,curvedAB.y, b.x.toFloat(), b.y.toFloat())

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