package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R
import com.tt.eggs.classes.Functions
import kotlinx.android.synthetic.main.activity_other_games.*

class RabbitDrawable(private val context: Context, private val size: Double): Drawable() {
    private var paint= Paint()
    private val stroke = size/100



    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.strokeWidth = (stroke*3).toFloat()




        val pathAB = Path()
        val a = Point(0, (size*0.6).toInt())
        val b = Point((size*0.1).toInt(), (size*0.6).toInt())
        val c = Point((size*0.39).toInt(), (size*0.61).toInt())
        val d = Point((size*0.36).toInt(), (size*0.67).toInt())
        val e = Point((size*0.3).toInt(), (size*0.75).toInt())
        val f = Point((size*0.34).toInt(), (size*0.81).toInt())
        val g = Point((size*0.2).toInt(), (size*0.82).toInt())
        val h = Point((size*0.12).toInt(), (size*0.86).toInt())
        val i = Point(0, (size*0.9).toInt())

        val curvedAB = Functions.curvedPath(a,b,size*0.05,true)
        val curvedBC = Functions.curvedPath(b,c,size*0.1,true)
        val curvedCD = Functions.curvedPath(c,d,size*0.02,true)
        val curvedDE = Functions.curvedPath(d,e,size*0.04,true)
        val curvedFG = Functions.curvedPath(f,g,size*0.04,true)
        val curvedGH = Functions.curvedPath(g,h,size*0.1,false)
        val curvedHI = Functions.curvedPath(h,i,size*0.03,true)

        pathAB.moveTo(a.x.toFloat(), a.y.toFloat())
        pathAB.cubicTo(a.x.toFloat(), a.y.toFloat(),curvedAB.x,curvedAB.y, b.x.toFloat(), b.y.toFloat())
        pathAB.cubicTo(b.x.toFloat(), b.y.toFloat(),curvedBC.x,curvedBC.y, c.x.toFloat(), c.y.toFloat())
        pathAB.cubicTo(c.x.toFloat(), c.y.toFloat(),curvedCD.x,curvedCD.y, d.x.toFloat(), d.y.toFloat())
        pathAB.cubicTo(d.x.toFloat(), d.y.toFloat(),curvedDE.x,curvedDE.y, e.x.toFloat(), e.y.toFloat())
        pathAB.lineTo(f.x.toFloat(), f.y.toFloat())
        pathAB.cubicTo(f.x.toFloat(), f.y.toFloat(),curvedFG.x,curvedFG.y, g.x.toFloat(), g.y.toFloat())
        pathAB.cubicTo(g.x.toFloat(), g.y.toFloat(),curvedGH.x,curvedGH.y, h.x.toFloat(), h.y.toFloat())
        pathAB.cubicTo(h.x.toFloat(), h.y.toFloat(),curvedHI.x,curvedHI.y, i.x.toFloat(), i.y.toFloat())
        pathAB.close()
        canvas.drawPath(pathAB,paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (stroke*3).toFloat()
        val j = Point((size*0.32).toInt(), (size*0.45).toInt())
        val k = Point((size*0.34).toInt(), (size*0.47).toInt())
        val l = Point((size*0.34).toInt(), (size*0.39).toInt())
        val m = Point((size*0.36).toInt(), (size*0.42).toInt())
        val n = Point((size*0.46).toInt(), (size*0.29).toInt())
        val o = Point((size*0.37).toInt(), (size*0.11).toInt())
        val p = Point((size*0.44).toInt(), (size*0.07).toInt())
        val r = Point((size*0.6).toInt(), (size*0.3).toInt())
        val s = Point((size*0.72).toInt(), (size*0.53).toInt())
        val t = Point((size*0.67).toInt(), (size*0.59).toInt())
        val u = Point((size*0.63).toInt(), (size*0.69).toInt())
        val v = Point((size*0.54).toInt(), (size*0.65).toInt())
        val w = Point((size*0.57).toInt(), (size*0.71).toInt())
        val x = Point((size*0.47).toInt(), (size*0.65).toInt())
        val curvedCJ = Functions.curvedPath(c,j,size*0.08,true)
        val curvedMN = Functions.curvedPath(m,n,size*0.04,true)
        val curvedOP = Functions.curvedPath(o,p,size*0.1,true)
        val curvedPR = Functions.curvedPath(p,r,size*0.05,true)
        val curvedRS = Functions.curvedPath(r,s,size*0.1,true)
        val curvedTU = Functions.curvedPath(t,u,size*0.04,true)
        val curvedUV = Functions.curvedPath(u,v,size*0.04,false)
        val curvedWX = Functions.curvedPath(w,x,size*0.04,true)
        val curvedXF = Functions.curvedPath(x,f,size*0.04,true)

        val pathHead = Path()
        pathHead.moveTo(c.x.toFloat(), c.y.toFloat())
        pathHead.cubicTo(c.x.toFloat(), c.y.toFloat(),curvedCJ.x,curvedCJ.y, j.x.toFloat(), j.y.toFloat())
        pathHead.lineTo(k.x.toFloat(), k.y.toFloat())
        pathHead.lineTo(l.x.toFloat(), l.y.toFloat())
        pathHead.lineTo(m.x.toFloat(), m.y.toFloat())
        pathHead.cubicTo(m.x.toFloat(), m.y.toFloat(),curvedMN.x,curvedMN.y, n.x.toFloat(), n.y.toFloat())
        pathHead.lineTo(o.x.toFloat(), o.y.toFloat())
        pathHead.cubicTo(o.x.toFloat(), o.y.toFloat(),curvedOP.x,curvedOP.y, p.x.toFloat(), p.y.toFloat())
        pathHead.cubicTo(p.x.toFloat(), p.y.toFloat(),curvedPR.x,curvedPR.y, r.x.toFloat(), r.y.toFloat())
        pathHead.cubicTo(r.x.toFloat(), r.y.toFloat(),curvedRS.x,curvedRS.y, s.x.toFloat(), s.y.toFloat())
        pathHead.lineTo(t.x.toFloat(), t.y.toFloat())
        pathHead.cubicTo(t.x.toFloat(), t.y.toFloat(),curvedTU.x,curvedTU.y, u.x.toFloat(), u.y.toFloat())
        pathHead.cubicTo(u.x.toFloat(), u.y.toFloat(),curvedUV.x,curvedUV.y, v.x.toFloat(), v.y.toFloat())
        pathHead.lineTo(w.x.toFloat(), w.y.toFloat())
        pathHead.cubicTo(w.x.toFloat(), w.y.toFloat(),curvedWX.x,curvedWX.y, x.x.toFloat(), x.y.toFloat())
        pathHead.cubicTo(x.x.toFloat(), x.y.toFloat(),curvedXF.x,curvedXF.y, f.x.toFloat(), f.y.toFloat())
        canvas.drawPath(pathHead,paint)

        val y1 = Point((size*0.52).toInt(), (size*0.15).toInt())
        val y = Point((size*0.52).toInt(), (size*0.1).toInt())
        val z = Point((size*0.58).toInt(), (size*0.07).toInt())
        val z1 = Point((size*0.64).toInt(), (size*0.32).toInt())
        val z2 = Point((size*0.51).toInt(), (size*0.27).toInt())
        val z3 = Point((size*0.41).toInt(), (size*0.1).toInt())
        val curvedYZ = Functions.curvedPath(y,z,size*0.1,true)
        val curvedZZ1 = Functions.curvedPath(z,z1,size*0.05,true)
        val curvedZ1Z2 = Functions.curvedPath(z1,z2,size*0.02,false)
        val curvedZ3Z2 = Functions.curvedPath(z3,z2,size*0.03,true)

        val pathEars = Path()



        pathEars.moveTo(y1.x.toFloat(), y1.y.toFloat())
        pathEars.lineTo(y.x.toFloat(), y.y.toFloat())
        pathEars.cubicTo(y.x.toFloat(), y.y.toFloat(),curvedYZ.x,curvedYZ.y, z.x.toFloat(), z.y.toFloat())
        pathEars.cubicTo(z.x.toFloat(), z.y.toFloat(),curvedZZ1.x,curvedZZ1.y, z1.x.toFloat(), z1.y.toFloat())
        pathEars.cubicTo(z1.x.toFloat(), z1.y.toFloat(),curvedZ1Z2.x,curvedZ1Z2.y, z2.x.toFloat(), z2.y.toFloat())
        pathEars.lineTo(z3.x.toFloat(), z3.y.toFloat())
        pathEars.cubicTo(z3.x.toFloat(), z3.y.toFloat(),curvedZ3Z2.x,curvedZ3Z2.y, z2.x.toFloat(), z2.y.toFloat())

        canvas.drawPath(pathEars,paint)


        paint.strokeWidth = (stroke*2).toFloat()

        val b1 = Point((size*0.53).toInt(),(size*0.55).toInt())
        val a1 = Point((size*0.7).toInt(), (size*0.64).toInt())
        val c1 = Point((size*0.45).toInt(), (size*0.49).toInt())
        val d1 = Point((size*0.58).toInt(), (size*0.54).toInt())

        val curvedA1B1 = Functions.curvedPath(a1,b1,size*0.1,false)
        val curvedC1D1 = Functions.curvedPath(c1,d1,size*0.3,true)

        val path1 = Path()

        path1.moveTo(a1.x.toFloat(), a1.y.toFloat())
        path1.cubicTo(a1.x.toFloat(), a1.y.toFloat(),curvedA1B1.x,curvedA1B1.y, b1.x.toFloat(), b1.y.toFloat())
        path1.lineTo(c1.x.toFloat(), c1.y.toFloat())
        path1.cubicTo(c1.x.toFloat(), c1.y.toFloat(),curvedC1D1.x,curvedC1D1.y, d1.x.toFloat(), d1.y.toFloat())

        canvas.drawPath(path1,paint)


        paint.style = Paint.Style.FILL_AND_STROKE

        val e1 = Point((size*0.51).toInt(), (size*0.52).toInt())
        val curvedE1D1 = Functions.curvedPath(e1,d1,size*0.2,true)
        val pathEye = Path()

        pathEye.moveTo(e1.x.toFloat(), e1.y.toFloat())
        pathEye.cubicTo(e1.x.toFloat(), e1.y.toFloat(),curvedE1D1.x,curvedE1D1.y, d1.x.toFloat(), d1.y.toFloat())
        pathEye.close()

        canvas.drawPath(pathEye,paint)


        paint.style = Paint.Style.STROKE

        val f1 = Point((size*0.64).toInt(), (size*0.55).toInt())
        val g1 = Point((size*0.71).toInt(), (size*0.46).toInt())
        val h1 = Point((size*0.75).toInt(), (size*0.5).toInt())

        val pathEye2 = Path()

        pathEye2.moveTo(f1.x.toFloat(), f1.y.toFloat())
        pathEye2.lineTo(g1.x.toFloat(), g1.y.toFloat())
        pathEye2.lineTo(h1.x.toFloat(), h1.y.toFloat())

        canvas.drawPath(pathEye2,paint)

        paint.strokeWidth = (stroke*3).toFloat()

        val i1 = Point((size*0.59).toInt(), (size*0.65).toInt())
        val j1 = Point((size*0.62).toInt(), (size*0.6).toInt())

        val pathNos = Path()

        pathNos.moveTo(i1.x.toFloat(), i1.y.toFloat())
        pathNos.lineTo(j1.x.toFloat(), j1.y.toFloat())

        canvas.drawPath(pathNos,paint)


        paint.strokeWidth = (stroke*2).toFloat()

        val k1 = Point((size*0.54).toInt(), (size*0.65).toInt())
        val l1 = Point((size*0.5).toInt(), (size*0.59).toInt())

        val pathMouth = Path()
        pathMouth.moveTo(k1.x.toFloat(), k1.y.toFloat())
        pathMouth.lineTo(l1.x.toFloat(), l1.y.toFloat())

        canvas.drawPath(pathMouth,paint)


        paint.strokeWidth = (stroke*3).toFloat()

        val pathArm = Path()
        val h2 = Point((size*0.22).toInt(), (size*0.95).toInt())
        val g2 = Point((size*0.5).toInt(), (size*0.95).toInt())
        val i2 = Point((size*0.5).toInt(), (size*0.85).toInt())
        val j2 = Point((size*0.4).toInt(), (size*0.85).toInt())
        val curvedHH2 = Functions.curvedPath(h,h2,size*0.05,false)
        val curvedG2I2 = Functions.curvedPath(g2,i2,size*0.1,false)
        val curvedJ2G = Functions.curvedPath(j2,g,size*0.1,true)

        pathArm.moveTo(h.x.toFloat(), h.y.toFloat())
        pathArm.cubicTo(h.x.toFloat(), h.y.toFloat(),curvedHH2.x,curvedHH2.y, h2.x.toFloat(), h2.y.toFloat())
        pathArm.lineTo(g2.x.toFloat(), g2.y.toFloat())
        pathArm.cubicTo(g2.x.toFloat(), g2.y.toFloat(),curvedG2I2.x,curvedG2I2.y, i2.x.toFloat(), i2.y.toFloat())
        pathArm.lineTo(j2.x.toFloat(), j2.y.toFloat())
        pathArm.cubicTo(j2.x.toFloat(), j2.y.toFloat(),curvedJ2G.x,curvedJ2G.y, g.x.toFloat(), g.y.toFloat())

        canvas.drawPath(pathArm,paint)












    }


    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}