package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R
import com.tt.eggs.classes.Functions

class BrokenEggLeft (private val context: Context, private val width: Double): Drawable() {
    private val paint = Paint()
    private val unit = width/4


    override fun draw(canvas: Canvas) {

        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = ContextCompat.getColor(context, R.color.black)


        val a = Point((unit*0.55).toInt(), (unit*0.4).toInt())
        val radius = unit*0.22
        canvas.drawCircle(a.x.toFloat(), a.y.toFloat(), radius.toFloat(),paint)

        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = (unit*0.02).toFloat()
        val b = Point((unit*0.7).toInt(), (unit*0.55).toInt())
        val c = Point((unit*0.8).toInt(), (unit*0.65).toInt())
        val curvedRadiusBC = Functions.curvedPath(b,c,unit*0.1,false)
        val d = Point((unit*0.9).toInt(), (unit*0.5).toInt())
        val e = Point((unit*1).toInt(), (unit*0.5).toInt())
        val curvedRadiusDE = Functions.curvedPath(d,e,unit*0.1,true)
        val f = Point((unit*0.95).toInt(), (unit*0.85).toInt())
        val curvedRadiusEF = Functions.curvedPath(e,f,unit*0.15,true)
        val g = Point((unit*1).toInt(), (unit*0.9).toInt())
        val curvedRadiusFG = Functions.curvedPath(f,g,unit*0.1,false)
        val h = Point((unit*1.15).toInt(), (unit*0.9).toInt())
        val curvedRadiusGH = Functions.curvedPath(g,h,unit*0.2,true)
        val i = Point((unit*1.1).toInt(), (unit*1.05).toInt())
        val curvedRadiusHI = Functions.curvedPath(h,i,unit*0.05,true)
        val j = Point((unit*1.15).toInt(), (unit*1.12).toInt())
        val curvedRadiusIJ = Functions.curvedPath(i,j,unit*0.1,false)
        val k = Point((unit*1.22).toInt(), (unit*1.2).toInt())
        val curvedRadiusJK = Functions.curvedPath(j,k,unit*0.2,true)
        val l = Point((unit*1.11).toInt(), (unit*1.35).toInt())
        val m = Point((unit*1.02).toInt(), (unit*1.25).toInt())
        val curvedRadiusLM = Functions.curvedPath(l,m,unit*0.2,true)
        val n = Point((unit*0.67).toInt(), (unit*1.3).toInt())
        val curvedRadiusMN = Functions.curvedPath(m,n,unit*0.1,true)
        val o = Point((unit*0.55).toInt(), (unit*1.4).toInt())
        val curvedRadiusNO = Functions.curvedPath(n,o,unit*0.25,true)
        val p = Point((unit*0.4).toInt(), (unit*1.3).toInt())
        val q = Point((unit*0.5).toInt(), (unit*1.25).toInt())
        val curvedRadiusPQ = Functions.curvedPath(p,q,unit*0.2,true)
        val r = Point((unit*0.55).toInt(), (unit*1.2).toInt())
        val curvedRadiusQR = Functions.curvedPath(q,r,unit*0.1,false)
        val s = Point((unit*0.47).toInt(), (unit*1.05).toInt())
        val curvedRadiusRS = Functions.curvedPath(r,s,unit*0.05,true)
        val t = Point((unit*0.3).toInt(), (unit*1.1).toInt())
        val curvedRadiusST = Functions.curvedPath(s,t,unit*0.1,false)
        val u = Point((unit*0).toInt(), (unit*0.85).toInt())
        val curvedRadiusTU = Functions.curvedPath(t,u,unit*0.1,true)
        val v = Point((unit*0.43).toInt(), (unit*0.76).toInt())
        val curvedRadiusUV = Functions.curvedPath(u,v,unit*0.14,true)
        val w = Point((unit*0.5).toInt(), (unit*0.8).toInt())
        val curvedRadiusVW = Functions.curvedPath(v,w,unit*0.1,false)
        val x = Point((unit*0.58).toInt(), (unit*0.73).toInt())
        val curvedRadiusWX = Functions.curvedPath(w,x,unit*0.02,true)
        val y = Point((unit*0.5).toInt(), (unit*0.6).toInt())
        val curvedRadiusXY = Functions.curvedPath(x,y,unit*0.05,false)
        val z = Point((unit*0.25).toInt(), (unit*0.35).toInt())
        val curvedRadiusYZ = Functions.curvedPath(y,z,unit*0.2,false)
        val z1 = Point((unit*0.35).toInt(), (unit*0.25).toInt())
        val curvedRadiusZZ1 = Functions.curvedPath(z,z1,unit*0.1,true)
        val z2 = Point((unit*0.45).toInt(), (unit*0.2).toInt())
        val curvedRadiusZZ2 = Functions.curvedPath(z1,z2,unit*0.2,true)

        val path = Path()

        path.moveTo(b.x.toFloat(), b.y.toFloat())
        path.cubicTo(b.x.toFloat(), b.y.toFloat(),curvedRadiusBC.x,curvedRadiusBC.y, c.x.toFloat(), c.y.toFloat())
        path.lineTo(d.x.toFloat(), d.y.toFloat())
        path.cubicTo(d.x.toFloat(), d.y.toFloat(),curvedRadiusDE.x,curvedRadiusDE.y, e.x.toFloat(), e.y.toFloat())
        path.cubicTo(e.x.toFloat(), e.y.toFloat(),curvedRadiusEF.x,curvedRadiusEF.y, f.x.toFloat(), f.y.toFloat())
        path.cubicTo(f.x.toFloat(), f.y.toFloat(),curvedRadiusFG.x,curvedRadiusFG.y, g.x.toFloat(), g.y.toFloat())
        path.cubicTo(g.x.toFloat(), g.y.toFloat(),curvedRadiusGH.x,curvedRadiusGH.y, h.x.toFloat(), h.y.toFloat())
        path.cubicTo(h.x.toFloat(), h.y.toFloat(),curvedRadiusHI.x,curvedRadiusHI.y, i.x.toFloat(), i.y.toFloat())
        path.cubicTo(i.x.toFloat(), i.y.toFloat(),curvedRadiusIJ.x,curvedRadiusIJ.y, j.x.toFloat(), j.y.toFloat())
        path.cubicTo(j.x.toFloat(), j.y.toFloat(),curvedRadiusJK.x,curvedRadiusJK.y, k.x.toFloat(), k.y.toFloat())
        path.lineTo(l.x.toFloat(), l.y.toFloat())
        path.cubicTo(l.x.toFloat(), l.y.toFloat(),curvedRadiusLM.x,curvedRadiusLM.y, m.x.toFloat(), m.y.toFloat())
        path.cubicTo(m.x.toFloat(), m.y.toFloat(),curvedRadiusMN.x,curvedRadiusMN.y, n.x.toFloat(), n.y.toFloat())
        path.cubicTo(n.x.toFloat(), n.y.toFloat(),curvedRadiusNO.x,curvedRadiusNO.y, o.x.toFloat(), o.y.toFloat())
        path.lineTo(p.x.toFloat(), p.y.toFloat())
        path.cubicTo(p.x.toFloat(), p.y.toFloat(),curvedRadiusPQ.x,curvedRadiusPQ.y, q.x.toFloat(), q.y.toFloat())
        path.cubicTo(q.x.toFloat(), q.y.toFloat(),curvedRadiusQR.x,curvedRadiusQR.y, r.x.toFloat(), r.y.toFloat())
        path.cubicTo(r.x.toFloat(), r.y.toFloat(),curvedRadiusRS.x,curvedRadiusRS.y, s.x.toFloat(), s.y.toFloat())
        path.cubicTo(s.x.toFloat(), s.y.toFloat(),curvedRadiusST.x,curvedRadiusST.y, t.x.toFloat(), t.y.toFloat())
        path.cubicTo(t.x.toFloat(), t.y.toFloat(),curvedRadiusTU.x,curvedRadiusTU.y, u.x.toFloat(), u.y.toFloat())
        path.cubicTo(u.x.toFloat(), u.y.toFloat(),curvedRadiusUV.x,curvedRadiusUV.y, v.x.toFloat(), v.y.toFloat())
        path.cubicTo(v.x.toFloat(), v.y.toFloat(),curvedRadiusVW.x,curvedRadiusVW.y, w.x.toFloat(), w.y.toFloat())
        path.cubicTo(w.x.toFloat(), w.y.toFloat(),curvedRadiusWX.x,curvedRadiusWX.y, x.x.toFloat(), x.y.toFloat())
        path.cubicTo(x.x.toFloat(), x.y.toFloat(),curvedRadiusXY.x,curvedRadiusXY.y, y.x.toFloat(), y.y.toFloat())
        path.cubicTo(y.x.toFloat(), y.y.toFloat(),curvedRadiusYZ.x,curvedRadiusYZ.y, z.x.toFloat(), z.y.toFloat())
        path.cubicTo(z.x.toFloat(), z.y.toFloat(),curvedRadiusZZ1.x,curvedRadiusZZ1.y, z1.x.toFloat(), z1.y.toFloat())
        path.cubicTo(z1.x.toFloat(), z1.y.toFloat(),curvedRadiusZZ2.x,curvedRadiusZZ2.y, z2.x.toFloat(), z2.y.toFloat())
        path.close()

        canvas.drawPath(path,paint)


        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (unit*0.1).toFloat()

        val a1 = Point((unit*1.5).toInt(), (unit*1.9).toInt())
        val b1 = Point((unit*1.85).toInt(), (unit*2.1).toInt())
        val c1 = Point((unit*2).toInt(), (unit*1.8).toInt())
        val d1 = Point((unit*2.15).toInt(), (unit*2.1).toInt())
        val e1 = Point((unit*2.3).toInt(), (unit*1.9).toInt())
        val f1 = Point((unit*2.2).toInt(), (unit*2.3).toInt())
        val g1 = Point((unit*2.6).toInt(), (unit*2.5).toInt())
        val h1 = Point((unit*2.3).toInt(), (unit*2.6).toInt())
        val i1 = Point((unit*2.4).toInt(), (unit*2.8).toInt())
        val j1 = Point((unit*2.1).toInt(), (unit*2.7).toInt())
        val k1 = Point((unit*2).toInt(), (unit*2.95).toInt())
        val l1 = Point((unit*1.85).toInt(), (unit*2.75).toInt())
        val m1 = Point((unit*1.6).toInt(), (unit*2.95).toInt())
        val n1 = Point((unit*1.7).toInt(), (unit*2.5).toInt())
        val o1 = Point((unit*1.4).toInt(), (unit*2.4).toInt())
        val p1 = Point((unit*1.65).toInt(), (unit*2.25).toInt())

        val path1 = Path()
        path1.moveTo(a1.x.toFloat(), a1.y.toFloat())
        path1.lineTo(b1.x.toFloat(), b1.y.toFloat())
        path1.lineTo(c1.x.toFloat(), c1.y.toFloat())
        path1.lineTo(d1.x.toFloat(), d1.y.toFloat())
        path1.lineTo(e1.x.toFloat(), e1.y.toFloat())
        path1.lineTo(f1.x.toFloat(), f1.y.toFloat())
        path1.lineTo(g1.x.toFloat(), g1.y.toFloat())
        path1.lineTo(h1.x.toFloat(), h1.y.toFloat())
        path1.lineTo(i1.x.toFloat(), i1.y.toFloat())
        path1.lineTo(j1.x.toFloat(), j1.y.toFloat())
        path1.lineTo(k1.x.toFloat(), k1.y.toFloat())
        path1.lineTo(l1.x.toFloat(), l1.y.toFloat())
        path1.lineTo(m1.x.toFloat(), m1.y.toFloat())
        path1.lineTo(n1.x.toFloat(), n1.y.toFloat())
        path1.lineTo(o1.x.toFloat(), o1.y.toFloat())
        path1.lineTo(p1.x.toFloat(), p1.y.toFloat())
        path1.close()


        canvas.drawPath(path1,paint)


        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (unit*0.07).toFloat()

        val a2 = Point((unit*3).toInt(), (unit*2).toInt())
        val b2 = Point((unit*3).toInt(), (unit*2.5).toInt())
        val radiusAB2 = Functions.curvedPath(a2,b2,unit,true)
        val radiusBA2 = Functions.curvedPath(b2,a2,unit*0.2,false)

        val path2 = Path()
        path2.moveTo(a2.x.toFloat(), a2.y.toFloat())
        path2.cubicTo(a2.x.toFloat(), a2.y.toFloat(),radiusAB2.x,radiusAB2.y, b2.x.toFloat(), b2.y.toFloat())
        path2.cubicTo(b2.x.toFloat(), b2.y.toFloat(),radiusBA2.x,radiusBA2.y, a2.x.toFloat(), a2.y.toFloat())
        path2.close()

        canvas.drawPath(path2,paint)


        val c2 = Point((unit).toInt(), (unit*2.6).toInt())
        val d2 = Point((unit*0.8).toInt(), (unit*2.2).toInt())
        val radiusCD2 = Functions.curvedPath(c2,d2,unit*0.8,true)
        val e2 = Point((unit).toInt(), (unit*2.3).toInt())
        val f2 = Point((unit*0.9).toInt(), (unit*2.5).toInt())
        val g2 = Point((unit*1.1).toInt(), (unit*2.5).toInt())

        val path3 = Path()
        path3.moveTo(c2.x.toFloat(), c2.y.toFloat())
        path3.cubicTo(c2.x.toFloat(), c2.y.toFloat(),radiusCD2.x,radiusCD2.y, d2.x.toFloat(), d2.y.toFloat())
        path3.lineTo(e2.x.toFloat(), e2.y.toFloat())
        path3.lineTo(f2.x.toFloat(), f2.y.toFloat())
        path3.lineTo(g2.x.toFloat(), g2.y.toFloat())
        path3.close()

        canvas.drawPath(path3,paint)


        paint.strokeWidth = (unit*0.1).toFloat()
        val c3 = Point((unit*1.3).toInt(), (unit*1.2).toInt())
        val d3 = Point((unit*1.6).toInt(), (unit*1.5).toInt())
        val e3 = Point((unit*1.15).toInt(), (unit*1.4).toInt())
        val f3 = Point((unit*1.4).toInt(), (unit*1.7).toInt())


        val path4 = Path()
        path4.moveTo(c3.x.toFloat(), c3.y.toFloat())
        path4.lineTo(d3.x.toFloat(), d3.y.toFloat())
        path4.moveTo(e3.x.toFloat(), e3.y.toFloat())
        path4.lineTo(f3.x.toFloat(), f3.y.toFloat())

        canvas.drawPath(path4,paint)


    }


    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }


}