package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R

class RunningChickenLeftThree  (private val context: Context, private val width: Double): Drawable() {
    private val paint = Paint()


    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = ContextCompat.getColor(context, R.color.black)


        val a = Point((width*0.45).toInt(), (width*0.5).toInt())
        val radius = width*0.22
        canvas.drawCircle(a.x.toFloat(), a.y.toFloat(), radius.toFloat(),paint)


        val b = Point((width*0.55).toInt(), (width*1.05).toInt())
        val radius1 = width*0.25
        canvas.drawCircle(b.x.toFloat(), b.y.toFloat(), radius1.toFloat(),paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (width*0.15).toFloat()

        val c = Point((width*0.34).toInt(), (width*1.05).toInt())
        val d = Point((width*0.15).toInt(), (width*1.05).toInt())
        val e = Point((width*0.13).toInt(), (width*0.83).toInt())
        val f = Point((width*0.17).toInt(), (width*1.27).toInt())

        val g = Point((width*0.62).toInt(), (width*1.25).toInt())
        val h = Point((width*0.7).toInt(), (width*1.4).toInt())
        val i = Point((width*0.92).toInt(), (width*1.35).toInt())
        val j = Point((width*0.48).toInt(), (width*1.45).toInt())

        val k = Point((width*0.72).toInt(), (width*1).toInt())
        val l = Point((width*0.8).toInt(), (width*0.7).toInt())
        val m = Point((width*0.8).toInt(), (width*1).toInt())
        val n = Point((width*0.9).toInt(), (width*1).toInt())

        val o = Point((width*0.3).toInt(), (width*0.2).toInt())
        val p = Point((width*0.3).toInt(), (width*0.35).toInt())
        val q = Point((width*0.1).toInt(), (width*0.35).toInt())






        val path = Path()
        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.lineTo(b.x.toFloat(), b.y.toFloat())
        canvas.drawPath(path,paint)

        paint.strokeWidth = (width*0.1).toFloat()

        val path1 = Path()
        path1.moveTo(c.x.toFloat(), c.y.toFloat())
        path1.lineTo(d.x.toFloat(), d.y.toFloat())
        path1.moveTo(e.x.toFloat(), e.y.toFloat())
        path1.lineTo(f.x.toFloat(), f.y.toFloat())

        path1.moveTo(g.x.toFloat(), g.y.toFloat())
        path1.lineTo(h.x.toFloat(), h.y.toFloat())
        path1.moveTo(i.x.toFloat(), i.y.toFloat())
        path1.lineTo(j.x.toFloat(), j.y.toFloat())

        path1.moveTo(k.x.toFloat(), k.y.toFloat())
        path1.lineTo(l.x.toFloat(), l.y.toFloat())
        path1.lineTo(m.x.toFloat(), m.y.toFloat())
        path1.lineTo(n.x.toFloat(), n.y.toFloat())

        path1.moveTo(o.x.toFloat(), o.y.toFloat())
        path1.lineTo(p.x.toFloat(), p.y.toFloat())
        path1.lineTo(q.x.toFloat(), q.y.toFloat())


        canvas.drawPath(path1,paint)

    }


    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }


}