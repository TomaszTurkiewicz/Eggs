package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R
import com.tt.eggs.classes.Functions

class RunningChickenRightTwo(private val context: Context, private val width: Double): Drawable() {
    private val paint = Paint()
    private val unit = width/100

    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.strokeWidth = (width/100).toFloat()

        val a = Point((width-(width*0.03)).toInt(), (width*0.5).toInt())
        val b = Point((width-(width*0.25)).toInt(), (width*0.27).toInt())
        val radiusAB = Functions.curvedPath(a,b,unit*17,false)
        val c = Point((width-(width*0.35)).toInt(), (width*0.3).toInt())
        val radiusBC = Functions.curvedPath(b,c,unit,false)
        val d = Point((width-(width*0.46)).toInt(), (width*0.29).toInt())
        val radiusCD = Functions.curvedPath(c,d,unit*3,true)
        val e = Point((width-(width*0.52)).toInt(), (width*0.33).toInt())
        val radiusDE = Functions.curvedPath(d,e,unit*17,false)
        val f = Point((width-(width*0.56)).toInt(), (width*0.43).toInt())
        val radiusEF = Functions.curvedPath(e,f,unit*7,true)
        val g = Point((width-(width*0.57)).toInt(), (width*0.51).toInt())
        val radiusFG = Functions.curvedPath(f,g,unit*13,false)
        val h = Point((width-(width*0.54)).toInt(), (width*0.52).toInt())
        val radiusGH = Functions.curvedPath(g,h,unit,false)
        val i = Point((width-(width*0.4)).toInt(), (width*0.68).toInt())
        val radiusHI = Functions.curvedPath(h,i,unit*5,false)
        val j = Point((width-(width*0.32)).toInt(), (width*0.77).toInt())
        val radiusIJ = Functions.curvedPath(i,j,unit*3,false)
        val k = Point((width-(width*0.37)).toInt(), (width*0.88).toInt())
        val radiusJK = Functions.curvedPath(j,k,unit*7,true)
        val l = Point((width-(width*0.63)).toInt(), (width*0.8).toInt())
        val radiusKL = Functions.curvedPath(k,l,unit*11,true)
        val m = Point((width-(width*0.75)).toInt(), (width*0.77).toInt())
        val radiusLM = Functions.curvedPath(l,m,unit*13,false)
        val n = Point((width-(width*0.8)).toInt(), (width*1.0).toInt())
        val radiusMN = Functions.curvedPath(m,n,unit*15,false)
        val o = Point((width-(width*0.78)).toInt(), (width*1.1).toInt())
        val radiusNO = Functions.curvedPath(n,o,unit,false)
        val p = Point((width-(width*0.85)).toInt(), (width*1.14).toInt())
        val radiusOP = Functions.curvedPath(o,p,unit*7,true)
        val q = Point((width-(width*0.93)).toInt(), (width*1.17).toInt())
        val radiusPQ = Functions.curvedPath(p,q,unit*9,false)
        val r = Point((width-(width*0.9)).toInt(), (width*1.22).toInt())
        val radiusQR = Functions.curvedPath(q,r,unit,false)
        val s = Point((width-(width*0.85)).toInt(), (width*1.35).toInt())
        val radiusRS = Functions.curvedPath(r,s,unit*3,true)
        val t = Point((width-(width*0.76)).toInt(), (width*1.35).toInt())
        val radiusST = Functions.curvedPath(s,t,unit*15,false)
        val u = Point((width-(width*0.78)).toInt(), (width*1.3).toInt())
        val radiusTU = Functions.curvedPath(t,u,unit,false)
        val v = Point((width-(width*0.72)).toInt(), (width*1.23).toInt())
        val radiusUV = Functions.curvedPath(u,v,unit*7,true)
        val w = Point((width-(width*0.56)).toInt(), (width*1.33).toInt())
        val radiusVW = Functions.curvedPath(v,w,unit*5,false)
        val x = Point((width-(width*0.56)).toInt(), (width*1.4).toInt())
        val radiusWX = Functions.curvedPath(w,x,unit*5,true)
        val y = Point((width-(width*0.56)).toInt(), (width*1.47).toInt())
        val radiusXY = Functions.curvedPath(x,y,unit*9,false)
        val z = Point((width-(width*0.53)).toInt(), (width*1.47).toInt())
        val radiusYZ = Functions.curvedPath(y,z,unit,false)
        val a1 = Point((width-(width*0.3)).toInt(), (width*1.47).toInt())
        val radiusZA = Functions.curvedPath(z,a1,unit,true)
        val b1 = Point((width-(width*0.3)).toInt(), (width*1.4).toInt())
        val radiusAB1 = Functions.curvedPath(a1,b1,unit*15,false)
        val c1 = Point((width-(width*0.31)).toInt(), (width*1.29).toInt())
        val radiusBC1 = Functions.curvedPath(b1,c1,unit*20,true)
        val d1 = Point((width-(width*0.15)).toInt(), (width*0.8).toInt())
        val radiusCD1 = Functions.curvedPath(c1,d1,unit*25,false)
        val e1 = Point((width-(width*0.1)).toInt(), (width*0.65).toInt())
        val radiusDE1 = Functions.curvedPath(d1,e1,unit*5,true)
        val radiusEA1 = Functions.curvedPath(e1,a,unit*5,false)

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
        path.cubicTo(j.x.toFloat(), j.y.toFloat(),radiusJK.x,radiusJK.y, k.x.toFloat(), k.y.toFloat())
        path.cubicTo(k.x.toFloat(), k.y.toFloat(),radiusKL.x,radiusKL.y, l.x.toFloat(), l.y.toFloat())
        path.cubicTo(l.x.toFloat(), l.y.toFloat(),radiusLM.x,radiusLM.y, m.x.toFloat(), m.y.toFloat())
        path.cubicTo(m.x.toFloat(), m.y.toFloat(),radiusMN.x,radiusMN.y, n.x.toFloat(), n.y.toFloat())
        path.cubicTo(n.x.toFloat(), n.y.toFloat(),radiusNO.x,radiusNO.y, o.x.toFloat(), o.y.toFloat())
        path.cubicTo(o.x.toFloat(), o.y.toFloat(),radiusOP.x,radiusOP.y, p.x.toFloat(), p.y.toFloat())
        path.cubicTo(p.x.toFloat(), p.y.toFloat(),radiusPQ.x,radiusPQ.y, q.x.toFloat(), q.y.toFloat())
        path.cubicTo(q.x.toFloat(), q.y.toFloat(),radiusQR.x,radiusQR.y, r.x.toFloat(), r.y.toFloat())
        path.cubicTo(r.x.toFloat(), r.y.toFloat(),radiusRS.x,radiusRS.y, s.x.toFloat(), s.y.toFloat())
        path.cubicTo(s.x.toFloat(), s.y.toFloat(),radiusST.x,radiusST.y, t.x.toFloat(), t.y.toFloat())
        path.cubicTo(t.x.toFloat(), t.y.toFloat(),radiusTU.x,radiusTU.y, u.x.toFloat(), u.y.toFloat())
        path.cubicTo(u.x.toFloat(), u.y.toFloat(),radiusUV.x,radiusUV.y, v.x.toFloat(), v.y.toFloat())
        path.cubicTo(v.x.toFloat(), v.y.toFloat(),radiusVW.x,radiusVW.y, w.x.toFloat(), w.y.toFloat())
        path.cubicTo(w.x.toFloat(), w.y.toFloat(),radiusWX.x,radiusWX.y, x.x.toFloat(), x.y.toFloat())
        path.cubicTo(x.x.toFloat(), x.y.toFloat(),radiusXY.x,radiusXY.y, y.x.toFloat(), y.y.toFloat())
        path.cubicTo(y.x.toFloat(), y.y.toFloat(),radiusYZ.x,radiusYZ.y, z.x.toFloat(), z.y.toFloat())
        path.cubicTo(z.x.toFloat(), z.y.toFloat(),radiusZA.x,radiusZA.y, a1.x.toFloat(), a1.y.toFloat())
        path.cubicTo(a1.x.toFloat(), a1.y.toFloat(),radiusAB1.x,radiusAB1.y, b1.x.toFloat(), b1.y.toFloat())
        path.cubicTo(b1.x.toFloat(), b1.y.toFloat(),radiusBC1.x,radiusBC1.y, c1.x.toFloat(), c1.y.toFloat())
        path.cubicTo(c1.x.toFloat(), c1.y.toFloat(),radiusCD1.x,radiusCD1.y, d1.x.toFloat(), d1.y.toFloat())
        path.cubicTo(d1.x.toFloat(), d1.y.toFloat(),radiusDE1.x,radiusDE1.y, e1.x.toFloat(), e1.y.toFloat())
        path.cubicTo(e1.x.toFloat(), e1.y.toFloat(),radiusEA1.x,radiusEA1.y, a.x.toFloat(), a.y.toFloat())

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