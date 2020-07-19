package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R
import com.tt.eggs.classes.Functions

class RunningChickenLeftThree  (private val context: Context, private val width: Double): Drawable() {
    private val paint = Paint()
    private val unit = width/200


    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.strokeWidth = unit.toFloat()

        val a = Point((width*0.19).toInt(), (width*0.21).toInt())
        val b = Point((width*0.45).toInt(), (width*0.05).toInt())
        val curvedAB = Functions.curvedPath(a,b,unit*20,true)
        val c = Point((width*0.65).toInt(), (width*0.15).toInt())
        val curvedBC = Functions.curvedPath(b,c,unit*10,true)
        val d = Point((width*0.64).toInt(), (width*0.46).toInt())
        val curvedCD = Functions.curvedPath(c,d,unit*15,true)
        val e = Point((width*0.53).toInt(), (width*0.58).toInt())
        val curvedDE = Functions.curvedPath(d,e,unit*9,false)
        val f = Point((width*0.55).toInt(), (width*0.68).toInt())
        val curvedEF = Functions.curvedPath(e,f,unit*3,false)
        val g = Point((width*0.66).toInt(), (width*0.69).toInt())
        val curvedFG = Functions.curvedPath(f,g,unit*15,false)
        val h = Point((width*0.73).toInt(), (width*0.6).toInt())
        val curvedGH = Functions.curvedPath(g,h,unit,false)
        val i = Point((width*0.82).toInt(), (width*0.6).toInt())
        val curvedHI = Functions.curvedPath(h,i,unit*25,true)
        val j = Point((width*0.9).toInt(), (width*0.65).toInt())
        val curvedIJ = Functions.curvedPath(i,j,unit*20,false)
        val k = Point((width*0.94).toInt(), (width*0.69).toInt())
        val curvedJK = Functions.curvedPath(j,k,unit*20,true)
        val l = Point((width*0.89).toInt(), (width*0.75).toInt())
        val curvedKL = Functions.curvedPath(k,l,unit*2,true)
        val m = Point((width*0.69).toInt(), (width*1.15).toInt())
        val curvedLM = Functions.curvedPath(l,m,unit*25,true)
        val n = Point((width*0.69).toInt(), (width*1.23).toInt())
        val o = Point((width*0.78).toInt(), (width*1.28).toInt())
        val curvedNO = Functions.curvedPath(n,o,unit*9,true)
        val p = Point((width*0.7).toInt(), (width*1.32).toInt())
        val curvedOP = Functions.curvedPath(o,p,unit*15,true)
        val q = Point((width*0.6).toInt(), (width*1.3).toInt())
        val curvedPQ = Functions.curvedPath(p,q,unit*5,false)
        val r = Point((width*0.5).toInt(), (width*1.3).toInt())
        val curvedQR = Functions.curvedPath(q,r,unit*9,true)
        val s = Point((width*0.57).toInt(), (width*1.23).toInt())
        val curvedRS = Functions.curvedPath(r,s,unit*15,true)
        val t = Point((width*0.57).toInt(), (width*1.15).toInt())
        val u = Point((width*0.35).toInt(), (width*1.03).toInt())
        val curvedTU = Functions.curvedPath(t,u,unit*15,true)
        val v = Point((width*0.25).toInt(), (width*1.03).toInt())
        val curvedUV = Functions.curvedPath(u,v,unit*3,false)
        val w = Point((width*0.17).toInt(), (width*1.2).toInt())
        val curvedVW = Functions.curvedPath(v,w,unit*3,true)
        val x = Point((width*0.11).toInt(), (width*1.15).toInt())
        val curvedWX = Functions.curvedPath(w,x,unit*25,true)
        val y = Point((width*0.11).toInt(), (width*1).toInt())
        val curvedXY = Functions.curvedPath(x,y,unit*5,false)
        val z = Point((width*0.07).toInt(), (width*0.9).toInt())
        val curvedYZ = Functions.curvedPath(y,z,unit*9,true)
        val a1 = Point((width*0.16).toInt(), (width*0.93).toInt())
        val curvedZA = Functions.curvedPath(z,a1,unit*24,true)
        val b1 = Point((width*0.28).toInt(), (width*0.94).toInt())
        val curvedAB1 = Functions.curvedPath(a1,b1,unit*5,false)
        val c1 = Point((width*0.28).toInt(), (width*0.8).toInt())
        val curvedBC1 = Functions.curvedPath(b1,c1,unit*5,true)
        val d1 = Point((width*0.35).toInt(), (width*0.67).toInt())
        val curvedCD1 = Functions.curvedPath(c1,d1,unit*7,true)
        val e1 = Point((width*0.35).toInt(), (width*0.6).toInt())
        val curvedDE1 = Functions.curvedPath(d1,e1,unit*3,false)
        val f1 = Point((width*0.21).toInt(), (width*0.48).toInt())
        val curvedEF1 = Functions.curvedPath(e1,f1,unit*5,true)
        val g1 = Point((width*0.16).toInt(), (width*0.48).toInt())
        val curvedFG1 = Functions.curvedPath(f1,g1,unit*3,false)
        val h1 = Point((width*0.1).toInt(), (width*0.44).toInt())
        val curvedGH1 = Functions.curvedPath(g1,h1,unit*11,true)
        val i1 = Point((width*0.16).toInt(), (width*0.38).toInt())
        val curvedHI1 = Functions.curvedPath(h1,i1,unit*9,true)
        val j1 = Point((width*0.12).toInt(), (width*0.33).toInt())
        val curvedIJ1 = Functions.curvedPath(i1,j1,unit*5,false)
        val k1 = Point((width*0.06).toInt(), (width*0.23).toInt())
        val curvedJK1 = Functions.curvedPath(j1,k1,unit*7,true)
        val l1 = Point((width*0.11).toInt(), (width*0.2).toInt())
        val curvedKL1 = Functions.curvedPath(k1,l1,unit*15,true)
        val curvedLA = Functions.curvedPath(l1,a,unit*5,false)

        val path = Path()
        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.cubicTo(a.x.toFloat(), a.y.toFloat(),curvedAB.x,curvedAB.y, b.x.toFloat(), b.y.toFloat())
        path.cubicTo(b.x.toFloat(), b.y.toFloat(),curvedBC.x,curvedBC.y, c.x.toFloat(), c.y.toFloat())
        path.cubicTo(c.x.toFloat(), c.y.toFloat(),curvedCD.x,curvedCD.y, d.x.toFloat(), d.y.toFloat())
        path.cubicTo(d.x.toFloat(), d.y.toFloat(),curvedDE.x,curvedDE.y, e.x.toFloat(), e.y.toFloat())
        path.cubicTo(e.x.toFloat(), e.y.toFloat(),curvedEF.x,curvedEF.y, f.x.toFloat(), f.y.toFloat())
        path.cubicTo(f.x.toFloat(), f.y.toFloat(),curvedFG.x,curvedFG.y, g.x.toFloat(), g.y.toFloat())
        path.cubicTo(g.x.toFloat(), g.y.toFloat(),curvedGH.x,curvedGH.y, h.x.toFloat(), h.y.toFloat())
        path.cubicTo(h.x.toFloat(), h.y.toFloat(),curvedHI.x,curvedHI.y, i.x.toFloat(), i.y.toFloat())
        path.cubicTo(i.x.toFloat(), i.y.toFloat(),curvedIJ.x,curvedIJ.y, j.x.toFloat(), j.y.toFloat())
        path.cubicTo(j.x.toFloat(), j.y.toFloat(),curvedJK.x,curvedJK.y, k.x.toFloat(), k.y.toFloat())
        path.cubicTo(k.x.toFloat(), k.y.toFloat(),curvedKL.x,curvedKL.y, l.x.toFloat(), l.y.toFloat())
        path.cubicTo(l.x.toFloat(), l.y.toFloat(),curvedLM.x,curvedLM.y, m.x.toFloat(), m.y.toFloat())
        path.lineTo(n.x.toFloat(), n.y.toFloat())
        path.cubicTo(n.x.toFloat(), n.y.toFloat(),curvedNO.x,curvedNO.y, o.x.toFloat(), o.y.toFloat())
        path.cubicTo(o.x.toFloat(), o.y.toFloat(),curvedOP.x,curvedOP.y, p.x.toFloat(), p.y.toFloat())
        path.cubicTo(p.x.toFloat(), p.y.toFloat(),curvedPQ.x,curvedPQ.y, q.x.toFloat(), q.y.toFloat())
        path.cubicTo(q.x.toFloat(), q.y.toFloat(),curvedQR.x,curvedQR.y, r.x.toFloat(), r.y.toFloat())
        path.cubicTo(r.x.toFloat(), r.y.toFloat(),curvedRS.x,curvedRS.y, s.x.toFloat(), s.y.toFloat())
        path.lineTo(t.x.toFloat(), t.y.toFloat())
        path.cubicTo(t.x.toFloat(), t.y.toFloat(),curvedTU.x,curvedTU.y, u.x.toFloat(), u.y.toFloat())
        path.cubicTo(u.x.toFloat(), u.y.toFloat(),curvedUV.x,curvedUV.y, v.x.toFloat(), v.y.toFloat())
        path.cubicTo(v.x.toFloat(), v.y.toFloat(),curvedVW.x,curvedVW.y, w.x.toFloat(), w.y.toFloat())
        path.cubicTo(w.x.toFloat(), w.y.toFloat(),curvedWX.x,curvedWX.y, x.x.toFloat(), x.y.toFloat())
        path.cubicTo(x.x.toFloat(), x.y.toFloat(),curvedXY.x,curvedXY.y, y.x.toFloat(), y.y.toFloat())
        path.cubicTo(y.x.toFloat(), y.y.toFloat(),curvedYZ.x,curvedYZ.y, z.x.toFloat(), z.y.toFloat())
        path.cubicTo(z.x.toFloat(), z.y.toFloat(),curvedZA.x,curvedZA.y, a1.x.toFloat(), a1.y.toFloat())
        path.cubicTo(a1.x.toFloat(), a1.y.toFloat(),curvedAB1.x,curvedAB1.y, b1.x.toFloat(), b1.y.toFloat())
        path.cubicTo(b1.x.toFloat(), b1.y.toFloat(),curvedBC1.x,curvedBC1.y, c1.x.toFloat(), c1.y.toFloat())
        path.cubicTo(c1.x.toFloat(), c1.y.toFloat(),curvedCD1.x,curvedCD1.y, d1.x.toFloat(), d1.y.toFloat())
        path.cubicTo(d1.x.toFloat(), d1.y.toFloat(),curvedDE1.x,curvedDE1.y, e1.x.toFloat(), e1.y.toFloat())
        path.cubicTo(e1.x.toFloat(), e1.y.toFloat(),curvedEF1.x,curvedEF1.y, f1.x.toFloat(), f1.y.toFloat())
        path.cubicTo(f1.x.toFloat(), f1.y.toFloat(),curvedFG1.x,curvedFG1.y, g1.x.toFloat(), g1.y.toFloat())
        path.cubicTo(g1.x.toFloat(), g1.y.toFloat(),curvedGH1.x,curvedGH1.y, h1.x.toFloat(), h1.y.toFloat())
        path.cubicTo(h1.x.toFloat(), h1.y.toFloat(),curvedHI1.x,curvedHI1.y, i1.x.toFloat(), i1.y.toFloat())
        path.cubicTo(i1.x.toFloat(), i1.y.toFloat(),curvedIJ1.x,curvedIJ1.y, j1.x.toFloat(), j1.y.toFloat())
        path.cubicTo(j1.x.toFloat(), j1.y.toFloat(),curvedJK1.x,curvedJK1.y, k1.x.toFloat(), k1.y.toFloat())
        path.cubicTo(k1.x.toFloat(), k1.y.toFloat(),curvedKL1.x,curvedKL1.y, l1.x.toFloat(), l1.y.toFloat())
        path.cubicTo(l1.x.toFloat(), l1.y.toFloat(),curvedLA.x,curvedLA.y, a.x.toFloat(), a.y.toFloat())
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