package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R
import com.tt.eggs.classes.Functions

class FaultTopDrawable (private val context: Context, private val size: Double): Drawable(){
    private val paint = Paint()
    private val stroke = size/100


    override fun draw(canvas: Canvas) {
         paint.color = ContextCompat.getColor(context, R.color.black)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = stroke.toFloat()

        val a = Point((size*0.41).toInt(), (size*0.44).toInt())
        val b = Point((size*0.45).toInt(), (size*0.36).toInt())
        val c = Point((size*0.55).toInt(), (size*0.14).toInt())
        val d = Point((size*0.68).toInt(), (size*0.24).toInt())
        val e = Point((size*0.7).toInt(), (size*0.29).toInt())
        val f = Point((size*0.66).toInt(), (size*0.36).toInt())
        val g = Point((size*0.64).toInt(), (size*0.5).toInt())
        val h = Point((size*0.92).toInt(), (size*0.52).toInt())
        val i = Point((size*0.87).toInt(), (size*0.57).toInt())
        val j = Point((size*0.75).toInt(), (size*0.57).toInt())
        val k = Point((size*0.75).toInt(), (size*0.63).toInt())
        val l = Point((size*0.67).toInt(), (size*0.63).toInt())
        val m = Point((size*0.69).toInt(), (size*0.71).toInt())
        val n = Point((size*0.6).toInt(), (size*0.67).toInt())
        val o = Point((size*0.64).toInt(), (size*0.76).toInt())
        val p = Point((size*0.38).toInt(), (size*0.79).toInt())
        val q = Point((size*0.32).toInt(), (size*0.75).toInt())
        val r = Point((size*0.34).toInt(), (size*0.71).toInt())
        val s = Point((size*0.25).toInt(), (size*0.68).toInt())
        val t = Point((size*0.33).toInt(), (size*0.6).toInt())
        val u = Point((size*0.2).toInt(), (size*0.5).toInt())
        val v = Point((size*0.2).toInt(), (size*0.4).toInt())

        val radiusAB = Functions.curvedPath(a,b,size*0.1,false)
        val radiusBC = Functions.curvedPath(b,c,size*0.2,true)
        val radiusCD = Functions.curvedPath(c,d,size*0.1,true)
        val radiusDE = Functions.curvedPath(d,e,size*0.2,true)
        val radiusEF = Functions.curvedPath(e,f,size*0.2,true)
        val radiusFG = Functions.curvedPath(f,g,size*0.05,false)
        val radiusGH = Functions.curvedPath(g,h,size*0.17,true)
        val radiusHI = Functions.curvedPath(h,i,size*0.05,true)
        val radiusIJ = Functions.curvedPath(i,j,size*0.1,false)
        val radiusST = Functions.curvedPath(s,t,size*0.02,true)
        val radiusTU = Functions.curvedPath(t,u,size*0.1,false)
        val radiusUV = Functions.curvedPath(u,v,size*0.02,true)
        val radiusVA = Functions.curvedPath(v,a,size*0.1,true)

        val path = Path()

        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.cubicTo(a.x.toFloat(), a.y.toFloat(),radiusAB.x,radiusAB.y, b.x.toFloat(), b.y.toFloat())
        path.cubicTo(b.x.toFloat(), b.y.toFloat(),radiusBC.x,radiusBC.y, c.x.toFloat(), c.y.toFloat())
        path.cubicTo(c.x.toFloat(), c.y.toFloat(),radiusCD.x,radiusCD.y, d.x.toFloat(), d.y.toFloat())
        path.cubicTo(d.x.toFloat(), d.y.toFloat(),radiusDE.x,radiusDE.y, e.x.toFloat(), e.y.toFloat())
        path.cubicTo(e.x.toFloat(), e.y.toFloat(),radiusEF.x,radiusEF.y, f.x.toFloat(), f.y.toFloat())
        path.cubicTo(f.x.toFloat(), f.y.toFloat(),radiusFG.x,radiusFG.y, g.x.toFloat(), g.y.toFloat())
        path.cubicTo(g.x.toFloat(), g.y.toFloat(),radiusGH.x,radiusGH.y, h.x.toFloat(), h.y.toFloat())
        path.cubicTo(h.x.toFloat(), h.y.toFloat(),radiusHI.x,radiusHI.y, i.x.toFloat(), i.y.toFloat())
        path.cubicTo(i.x.toFloat(), i.y.toFloat(),radiusIJ.x,radiusIJ.y, j.x.toFloat(), j.y.toFloat())
        path.lineTo(k.x.toFloat(), k.y.toFloat())
        path.lineTo(l.x.toFloat(), l.y.toFloat())
        path.lineTo(m.x.toFloat(), m.y.toFloat())
        path.lineTo(n.x.toFloat(), n.y.toFloat())
        path.lineTo(o.x.toFloat(), o.y.toFloat())
        path.lineTo(p.x.toFloat(), p.y.toFloat())
        path.lineTo(q.x.toFloat(), q.y.toFloat())
        path.lineTo(r.x.toFloat(), r.y.toFloat())
        path.lineTo(s.x.toFloat(), s.y.toFloat())
        path.cubicTo(s.x.toFloat(), s.y.toFloat(),radiusST.x,radiusST.y, t.x.toFloat(), t.y.toFloat())
        path.cubicTo(t.x.toFloat(), t.y.toFloat(),radiusTU.x,radiusTU.y, u.x.toFloat(), u.y.toFloat())
        path.cubicTo(u.x.toFloat(), u.y.toFloat(),radiusUV.x,radiusUV.y, v.x.toFloat(), v.y.toFloat())
        path.cubicTo(v.x.toFloat(), v.y.toFloat(),radiusVA.x,radiusVA.y, a.x.toFloat(), a.y.toFloat())

// TODO finish this first!!!

        canvas.drawPath(path,paint)


        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (stroke*5).toFloat()

        val o1 = Point((size*0.54).toInt(), (size*0.77).toInt())
        val radiusKO1 = Functions.curvedPath(k,o1,size*0.5,true)

        val pathEgg1 = Path()

        pathEgg1.moveTo(k.x.toFloat(), k.y.toFloat())
        pathEgg1.cubicTo(k.x.toFloat(), k.y.toFloat(),radiusKO1.x,radiusKO1.y, o1.x.toFloat(), o1.y.toFloat())

        canvas.drawPath(pathEgg1,paint)

        val middle = Point((size*0.3).toInt(), (size*0.7).toInt())
        val curveRadius = size*0.14

        val mRect = RectF((middle.x-curveRadius).toFloat(),
            (middle.y-curveRadius).toFloat(), (middle.x+curveRadius).toFloat(), (middle.y+curveRadius).toFloat()
        )

        canvas.drawArc(mRect,45f,210f,false,paint)

        val pathEgg2 = Path()
        pathEgg2.moveTo(s.x.toFloat(), s.y.toFloat())
        pathEgg2.lineTo((s.x).toFloat(), (s.y-size*0.1).toFloat())

        canvas.drawPath(pathEgg2,paint)

    }


    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}