package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class NormalEggDrawable(private val context: Context, private val size: Double):Drawable() {
        private var paint= Paint()



    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.strokeWidth = (size/8).toFloat()

        val a = Point((size*0.2).toInt(), (size*0.66).toInt())
        val b = Point((size*0.8).toInt(), (size*0.66).toInt())
        val curveRadiusBottom =size*0.6
        val curveRadiusTop = size
        val midX = a.x +((b.x-a.x)/2)
        val midY = a.y +((b.y-a.y)/2)
        val xDiff:Double = (midX-a.x).toDouble()
        val yDiff:Double = (midY-a.y).toDouble()
        val angle = (atan2(yDiff,xDiff) *(180/Math.PI))-90
        val angleRadius = Math.toRadians(angle)
        val pointXBottom: Float = (midX-curveRadiusBottom* cos(angleRadius)).toFloat()
        val pointYBottom: Float = (midY-curveRadiusBottom* sin(angleRadius)).toFloat()
        val pointXTop: Float = (midX+curveRadiusTop* cos(angleRadius)).toFloat()
        val pointYTop: Float = (midY+curveRadiusTop* sin(angleRadius)).toFloat()

        val path = Path()
        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.cubicTo(a.x.toFloat(), a.y.toFloat(),pointXBottom,pointYBottom, b.x.toFloat(), b.y.toFloat())
        path.cubicTo(b.x.toFloat(), b.y.toFloat(),pointXTop,pointYTop, a.x.toFloat(), a.y.toFloat())
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