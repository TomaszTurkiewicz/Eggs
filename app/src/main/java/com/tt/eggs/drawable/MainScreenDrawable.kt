package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

import com.tt.eggs.R
import com.tt.eggs.classes.Functions
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class MainScreenDrawable(private val context: Context, private val screenUnit:Int, private val width:Double, private val height:Double):Drawable() {
    private var paint = Paint()




    override fun draw(canvas: Canvas) {
        drawMainScreen(canvas)
        drawRounderFrames(canvas)
        val margin = drawLCD(canvas)
        drawRabbitWindow(canvas, margin)
        drawChickensLeftTop(canvas, margin)
        drawChickenLeftBottom(canvas,margin)
        drawChickenRightTop(canvas, margin)
        drawChickenRightBottom(canvas,margin)
        drawRoosts(canvas, margin)
        drawGrassLeft(canvas, margin)
        drawGrassRight(canvas, margin)
        drawGrassMiddle(canvas)
        coverEdges(canvas)

    }


    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    private fun drawMainScreen(canvas: Canvas){
        val rect = Rect(0,0, (screenUnit*width).toInt(), (screenUnit*height).toInt())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray)
        canvas.drawRect(rect,paint)
    }

    private fun drawRounderFrames(canvas: Canvas) {
        val stroke = screenUnit/10
        val margin = 0.2
        val rectR = RectF((screenUnit*margin).toFloat(), (screenUnit*margin).toFloat(),(screenUnit*(width-margin)).toFloat(),(screenUnit*(height-margin)).toFloat())
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (stroke).toFloat()
        paint.color = ContextCompat.getColor(context, R.color.black)
        canvas.drawRoundRect(rectR, screenUnit/2.toFloat(), screenUnit/2.toFloat(),paint)

        // draw rounded rectangle black
        val margin1 = 0.5
        val rectR1 = RectF((screenUnit*margin1).toFloat(), (screenUnit*margin1).toFloat(),(screenUnit*(width-margin1)).toFloat(),(screenUnit*(height-margin1)).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)
        canvas.drawRoundRect(rectR1, screenUnit/2.toFloat(), screenUnit/2.toFloat(),paint)

        // draw rounded rectangle light gray
        val margin2 = 0.55
        val rectR2 = RectF((screenUnit*margin2).toFloat(), (screenUnit*margin2).toFloat(),(screenUnit*(width-margin2)).toFloat(),(screenUnit*(height-margin2)).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray_light)
        canvas.drawRoundRect(rectR2, screenUnit/2.toFloat(), screenUnit/2.toFloat(),paint)

        // draw rounded rectangle dark gray
        val margin3 = 0.6
        val rectR3 = RectF((screenUnit*margin3).toFloat(), (screenUnit*margin3).toFloat(),(screenUnit*(width-margin3)).toFloat(),(screenUnit*(height-margin3)).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray_dark)
        canvas.drawRoundRect(rectR3, screenUnit/2.toFloat(), screenUnit/2.toFloat(),paint)

    }

    private fun drawLCD(canvas: Canvas):Double {
        val margin4 = 0.8
        val rectR4 = RectF((screenUnit*margin4).toFloat(), (screenUnit*margin4).toFloat(),(screenUnit*(width-margin4)).toFloat(),(screenUnit*(height-margin4)).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray_LCD)
        canvas.drawRect(rectR4,paint)
        return margin4
    }

    private fun drawRoosts(canvas: Canvas, margin:Double) {

        val stroke = screenUnit/7
        val topHeight = screenUnit*2
        val bottomHeight = screenUnit*3.5
        val leftMargin = margin*screenUnit
        val rightMargin = screenUnit*(width-margin)
        val horizontalLength = screenUnit*0.6
        val horizontalJump = screenUnit*2
        val verticalJump = screenUnit
        val h = screenUnit*0.5
        val lMarginHorizontal = leftMargin+horizontalJump
        val rMarginHorizontal = rightMargin-horizontalJump
        val topHeightMargin = topHeight+screenUnit*0.7
        val bottomHeightMargin = bottomHeight+screenUnit*0.7

        paint.style=Paint.Style.STROKE
        paint.strokeWidth= (stroke).toFloat()
        paint.color = ContextCompat.getColor(context, R.color.red)

        // top left
        canvas.drawLine((leftMargin).toFloat(),
            (topHeight).toFloat(), (horizontalLength+leftMargin).toFloat(),(topHeight).toFloat(),paint)

        canvas.drawLine((horizontalLength+leftMargin).toFloat(),
        (topHeight).toFloat(), (horizontalLength+leftMargin+horizontalJump).toFloat(),(topHeight+verticalJump).toFloat(),paint)

        canvas.drawLine(lMarginHorizontal.toFloat(),topHeightMargin.toFloat(),lMarginHorizontal.toFloat(), (topHeightMargin+h).toFloat(),paint)


        // bottom left
        canvas.drawLine((leftMargin).toFloat(),
            (bottomHeight).toFloat(), (horizontalLength+leftMargin).toFloat(),(bottomHeight).toFloat(),paint)

        canvas.drawLine((horizontalLength+leftMargin).toFloat(),
            (bottomHeight).toFloat(), (horizontalLength+leftMargin+horizontalJump).toFloat(),(bottomHeight+verticalJump).toFloat(),paint)

        canvas.drawLine(lMarginHorizontal.toFloat(),bottomHeightMargin.toFloat(),lMarginHorizontal.toFloat(), (bottomHeightMargin+h).toFloat(),paint)


        // top right
        canvas.drawLine((rightMargin).toFloat(),
            (topHeight).toFloat(), (rightMargin-horizontalLength).toFloat(),(topHeight).toFloat(),paint)

        canvas.drawLine((rightMargin-horizontalLength).toFloat(),
            (topHeight).toFloat(), (rightMargin-horizontalLength-horizontalJump).toFloat(),(topHeight+verticalJump).toFloat(),paint)

        canvas.drawLine((rMarginHorizontal).toFloat(),topHeightMargin.toFloat(),(rMarginHorizontal).toFloat(), (topHeightMargin+h).toFloat(),paint)



        // bottom right
        canvas.drawLine((rightMargin).toFloat(),
            (bottomHeight).toFloat(), (rightMargin-horizontalLength).toFloat(),(bottomHeight).toFloat(),paint)

        canvas.drawLine((rightMargin-horizontalLength).toFloat(),
            (bottomHeight).toFloat(), (rightMargin-horizontalLength-horizontalJump).toFloat(),(bottomHeight+verticalJump).toFloat(),paint)

        canvas.drawLine((rMarginHorizontal).toFloat(),bottomHeightMargin.toFloat(),(rMarginHorizontal).toFloat(), (bottomHeightMargin+h).toFloat(),paint)



    }

    private fun coverEdges(canvas: Canvas) {
        // draw rounded rectangle dark gray
        val margin1 = 0.7
        val margin2 = 0.8
        val rectR = RectF((screenUnit*margin1).toFloat(), screenUnit.toFloat(),(screenUnit*margin2).toFloat(),(screenUnit*(height-1)).toFloat())
        val rectR2 = RectF((screenUnit*(width-margin1)).toFloat(), screenUnit.toFloat(),(screenUnit*(width-margin2)).toFloat(),(screenUnit*(height-1)).toFloat())
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.gray_dark)
        canvas.drawRect(rectR,paint)
        canvas.drawRect(rectR2,paint)

    }

    private fun drawRabbitWindow(canvas: Canvas, margin: Double) {
        val stroke = screenUnit/7
        val leftMargin = margin*screenUnit
        val topHeight = screenUnit*0.9

        paint.style=Paint.Style.FILL_AND_STROKE
        paint.strokeWidth= (stroke).toFloat()
        paint.color = ContextCompat.getColor(context, R.color.red)


        canvas.drawLine((leftMargin).toFloat(),
            (topHeight).toFloat(), (leftMargin+screenUnit).toFloat(),(topHeight).toFloat(),paint)

        canvas.drawLine((leftMargin+screenUnit).toFloat(),
            (topHeight).toFloat(), (leftMargin+2*screenUnit).toFloat(),(topHeight+screenUnit).toFloat(),paint)

        val path = Path()
        path.moveTo((leftMargin+2*screenUnit).toFloat(), (topHeight+screenUnit).toFloat())
        path.lineTo((leftMargin+2*screenUnit).toFloat(), (topHeight+screenUnit/2).toFloat())
        path.lineTo((leftMargin+screenUnit*3/2).toFloat(), (topHeight+screenUnit/2).toFloat())
        path.lineTo((leftMargin+2*screenUnit).toFloat(), (topHeight+screenUnit).toFloat())
        path.close()
        canvas.drawPath(path,paint)



    }

    private fun drawChickensLeftTop(canvas: Canvas, margin: Double) {

        val paint1 = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (screenUnit/9).toFloat()
        paint.color = ContextCompat.getColor(context, R.color.white)
        val topHeight = screenUnit*2
        val leftMargin = margin*screenUnit

        val path = Path()
        val a = Point((leftMargin+screenUnit/6).toInt(),topHeight)
        val b = Point(a.x+screenUnit/3,a.y-screenUnit/2)
        val c = Point(leftMargin.toInt(),b.y)
        val curveRadius = screenUnit/2
        val midX = a.x +((b.x-a.x)/2)
        val midY = a.y +((b.y-a.y)/2)
        val xDiff:Double = (midX-a.x).toDouble()
        val yDiff:Double = (midY-a.y).toDouble()
        val angle = (atan2(yDiff,xDiff) *(180/Math.PI))-90
        val angleRadius = Math.toRadians(angle)
        val pointX: Float = (midX-curveRadius* cos(angleRadius)).toFloat()
        val pointY: Float = (midY-curveRadius* sin(angleRadius)).toFloat()

        val curveRadius1 = screenUnit*0.7
        val midX1 = b.x +((c.x-b.x)/2)
        val midY1 = b.y +((c.y-b.y)/2)
        val xDiff1:Double = (midX1-b.x).toDouble()
        val yDiff1:Double = (midY1-b.y).toDouble()
        val angle1 = (atan2(yDiff1,xDiff1) *(180/Math.PI))-90
        val angleRadius1 = Math.toRadians(angle1)
        val pointX1: Float = (midX1-curveRadius1* cos(angleRadius1)).toFloat()
        val pointY1: Float = (midY1-curveRadius1* sin(angleRadius1)).toFloat()

        val d = Point(a.x+screenUnit/11,c.y-screenUnit/9)

        val e = Point(a.x+screenUnit/4,d.y)
        val f = Point(e.x+screenUnit/8,e.y-screenUnit/4)
        val g = Point(e.x+screenUnit/4,e.y)
        val paint2 = Paint()


        paint1.style = Paint.Style.FILL
        paint1.strokeWidth = (screenUnit/9).toFloat()
        paint1.color = ContextCompat.getColor(context, R.color.white)

        paint2.style = Paint.Style.STROKE
        paint2.strokeWidth = (screenUnit/20).toFloat()
        paint2.color = ContextCompat.getColor(context, R.color.white)

        val paint3 = Paint()
        paint3.style = Paint.Style.FILL
        paint3.strokeWidth = (screenUnit/9).toFloat()
        paint3.color = ContextCompat.getColor(context, R.color.red)

        val h = Point(a.x,a.y-screenUnit+screenUnit/8)
        val i = Point(h.x+screenUnit/8,h.y)
        val j = Point(e.x+screenUnit/6,e.y+screenUnit/7)

        canvas.drawCircle(h.x.toFloat(), h.y.toFloat(), (screenUnit/10).toFloat(),paint3)
        canvas.drawCircle(i.x.toFloat(), i.y.toFloat(), (screenUnit/10).toFloat(),paint3)
        canvas.drawCircle(j.x.toFloat(), j.y.toFloat(), (screenUnit/15).toFloat(),paint3)


        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.cubicTo(a.x.toFloat(), a.y.toFloat(),pointX,pointY, b.x.toFloat(), b.y.toFloat())
        path.cubicTo(b.x.toFloat(), b.y.toFloat(),pointX1,pointY1, c.x.toFloat(), c.y.toFloat())
        canvas.drawPath(path,paint)

        canvas.drawCircle(d.x.toFloat(), d.y.toFloat(), (screenUnit/15).toFloat(),paint1)

        canvas.drawLine(e.x.toFloat(), e.y.toFloat(), f.x.toFloat(), f.y.toFloat(),paint2)
        canvas.drawLine(e.x.toFloat(), e.y.toFloat(), g.x.toFloat(), g.y.toFloat(),paint2)

    }

    private fun drawChickenLeftBottom(canvas: Canvas, margin: Double) {

        val paint1 = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (screenUnit/9).toFloat()
        paint.color = ContextCompat.getColor(context, R.color.white)
        val topHeight = screenUnit*3.5
        val leftMargin = margin*screenUnit

        val path = Path()
        val a = Point((leftMargin+screenUnit/6).toInt(), topHeight.toInt())
        val b = Point(a.x+screenUnit/3,a.y-screenUnit/2)
        val c = Point(leftMargin.toInt(),b.y)
        val curveRadius = screenUnit/2
        val midX = a.x +((b.x-a.x)/2)
        val midY = a.y +((b.y-a.y)/2)
        val xDiff:Double = (midX-a.x).toDouble()
        val yDiff:Double = (midY-a.y).toDouble()
        val angle = (atan2(yDiff,xDiff) *(180/Math.PI))-90
        val angleRadius = Math.toRadians(angle)
        val pointX: Float = (midX-curveRadius* cos(angleRadius)).toFloat()
        val pointY: Float = (midY-curveRadius* sin(angleRadius)).toFloat()

        val curveRadius1 = screenUnit*0.7
        val midX1 = b.x +((c.x-b.x)/2)
        val midY1 = b.y +((c.y-b.y)/2)
        val xDiff1:Double = (midX1-b.x).toDouble()
        val yDiff1:Double = (midY1-b.y).toDouble()
        val angle1 = (atan2(yDiff1,xDiff1) *(180/Math.PI))-90
        val angleRadius1 = Math.toRadians(angle1)
        val pointX1: Float = (midX1-curveRadius1* cos(angleRadius1)).toFloat()
        val pointY1: Float = (midY1-curveRadius1* sin(angleRadius1)).toFloat()

        val d = Point(a.x+screenUnit/11,c.y-screenUnit/9)

        val e = Point(a.x+screenUnit/4,d.y)
        val f = Point(e.x+screenUnit/8,e.y-screenUnit/4)
        val g = Point(e.x+screenUnit/4,e.y)
        val paint2 = Paint()


        paint1.style = Paint.Style.FILL
        paint1.strokeWidth = (screenUnit/9).toFloat()
        paint1.color = ContextCompat.getColor(context, R.color.white)

        paint2.style = Paint.Style.STROKE
        paint2.strokeWidth = (screenUnit/20).toFloat()
        paint2.color = ContextCompat.getColor(context, R.color.white)

        val paint3 = Paint()
        paint3.style = Paint.Style.FILL
        paint3.strokeWidth = (screenUnit/9).toFloat()
        paint3.color = ContextCompat.getColor(context, R.color.red)
        val h = Point(a.x,a.y-screenUnit+screenUnit/8)
        val i = Point(h.x+screenUnit/8,h.y)
        val j = Point(e.x+screenUnit/6,e.y+screenUnit/7)
        canvas.drawCircle(h.x.toFloat(), h.y.toFloat(), (screenUnit/10).toFloat(),paint3)
        canvas.drawCircle(i.x.toFloat(), i.y.toFloat(), (screenUnit/10).toFloat(),paint3)
        canvas.drawCircle(j.x.toFloat(), j.y.toFloat(), (screenUnit/15).toFloat(),paint3)
        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.cubicTo(a.x.toFloat(), a.y.toFloat(),pointX,pointY, b.x.toFloat(), b.y.toFloat())
        path.cubicTo(b.x.toFloat(), b.y.toFloat(),pointX1,pointY1, c.x.toFloat(), c.y.toFloat())
        canvas.drawPath(path,paint)
        canvas.drawCircle(d.x.toFloat(), d.y.toFloat(), (screenUnit/15).toFloat(),paint1)
        canvas.drawLine(e.x.toFloat(), e.y.toFloat(), f.x.toFloat(), f.y.toFloat(),paint2)
        canvas.drawLine(e.x.toFloat(), e.y.toFloat(), g.x.toFloat(), g.y.toFloat(),paint2)
    }

    private fun drawChickenRightTop(canvas: Canvas, margin: Double) {


        val paint1 = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (screenUnit/9).toFloat()
        paint.color = ContextCompat.getColor(context, R.color.white)
        val topHeight = screenUnit*2
        val rightMargin = screenUnit*(width-margin)

        val path = Path()
        val a = Point((rightMargin-screenUnit/6).toInt(),topHeight)
        val b = Point(a.x-screenUnit/3,a.y-screenUnit/2)
        val c = Point(rightMargin.toInt(),b.y)
        val curveRadius = screenUnit/2
        val midX = a.x +((b.x-a.x)/2)
        val midY = a.y +((b.y-a.y)/2)
        val xDiff:Double = (midX-a.x).toDouble()
        val yDiff:Double = (midY-a.y).toDouble()
        val angle = (atan2(yDiff,xDiff) *(180/Math.PI))-90
        val angleRadius = Math.toRadians(angle)
        val pointX: Float = (midX+curveRadius* cos(angleRadius)).toFloat()
        val pointY: Float = (midY+curveRadius* sin(angleRadius)).toFloat()

        val curveRadius1 = screenUnit*0.7
        val midX1 = b.x +((c.x-b.x)/2)
        val midY1 = b.y +((c.y-b.y)/2)
        val xDiff1:Double = (midX1-b.x).toDouble()
        val yDiff1:Double = (midY1-b.y).toDouble()
        val angle1 = (atan2(yDiff1,xDiff1) *(180/Math.PI))-90
        val angleRadius1 = Math.toRadians(angle1)
        val pointX1: Float = (midX1+curveRadius1* cos(angleRadius1)).toFloat()
        val pointY1: Float = (midY1+curveRadius1* sin(angleRadius1)).toFloat()

        val d = Point(a.x-screenUnit/11,c.y-screenUnit/9)

        val e = Point(a.x-screenUnit/4,d.y)
        val f = Point(e.x-screenUnit/8,e.y-screenUnit/4)
        val g = Point(e.x-screenUnit/4,e.y)
        val paint2 = Paint()


        paint1.style = Paint.Style.FILL
        paint1.strokeWidth = (screenUnit/9).toFloat()
        paint1.color = ContextCompat.getColor(context, R.color.white)

        paint2.style = Paint.Style.STROKE
        paint2.strokeWidth = (screenUnit/20).toFloat()
        paint2.color = ContextCompat.getColor(context, R.color.white)

        val paint3 = Paint()
        paint3.style = Paint.Style.FILL
        paint3.strokeWidth = (screenUnit/9).toFloat()
        paint3.color = ContextCompat.getColor(context, R.color.red)

        val h = Point(a.x,a.y-screenUnit+screenUnit/8)
        val i = Point(h.x-screenUnit/8,h.y)
        val j = Point(e.x-screenUnit/6,e.y+screenUnit/7)

        canvas.drawCircle(h.x.toFloat(), h.y.toFloat(), (screenUnit/10).toFloat(),paint3)
        canvas.drawCircle(i.x.toFloat(), i.y.toFloat(), (screenUnit/10).toFloat(),paint3)
        canvas.drawCircle(j.x.toFloat(), j.y.toFloat(), (screenUnit/15).toFloat(),paint3)
        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.cubicTo(a.x.toFloat(), a.y.toFloat(),pointX,pointY, b.x.toFloat(), b.y.toFloat())
        path.cubicTo(b.x.toFloat(), b.y.toFloat(),pointX1,pointY1, c.x.toFloat(), c.y.toFloat())
        canvas.drawPath(path,paint)
        canvas.drawCircle(d.x.toFloat(), d.y.toFloat(), (screenUnit/15).toFloat(),paint1)
        canvas.drawLine(e.x.toFloat(), e.y.toFloat(), f.x.toFloat(), f.y.toFloat(),paint2)
        canvas.drawLine(e.x.toFloat(), e.y.toFloat(), g.x.toFloat(), g.y.toFloat(),paint2)
    }

    private fun drawChickenRightBottom(canvas: Canvas, margin: Double) {

        val paint1 = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (screenUnit/9).toFloat()
        paint.color = ContextCompat.getColor(context, R.color.white)
        val topHeight = screenUnit*3.5
        val rightMargin = screenUnit*(width-margin)

        val path = Path()
        val a = Point((rightMargin-screenUnit/6).toInt(), topHeight.toInt())
        val b = Point(a.x-screenUnit/3,a.y-screenUnit/2)
        val c = Point(rightMargin.toInt(),b.y)
        val curveRadius = screenUnit/2
        val midX = a.x +((b.x-a.x)/2)
        val midY = a.y +((b.y-a.y)/2)
        val xDiff:Double = (midX-a.x).toDouble()
        val yDiff:Double = (midY-a.y).toDouble()
        val angle = (atan2(yDiff,xDiff) *(180/Math.PI))-90
        val angleRadius = Math.toRadians(angle)
        val pointX: Float = (midX+curveRadius* cos(angleRadius)).toFloat()
        val pointY: Float = (midY+curveRadius* sin(angleRadius)).toFloat()

        val curveRadius1 = screenUnit*0.7
        val midX1 = b.x +((c.x-b.x)/2)
        val midY1 = b.y +((c.y-b.y)/2)
        val xDiff1:Double = (midX1-b.x).toDouble()
        val yDiff1:Double = (midY1-b.y).toDouble()
        val angle1 = (atan2(yDiff1,xDiff1) *(180/Math.PI))-90
        val angleRadius1 = Math.toRadians(angle1)
        val pointX1: Float = (midX1+curveRadius1* cos(angleRadius1)).toFloat()
        val pointY1: Float = (midY1+curveRadius1* sin(angleRadius1)).toFloat()

        val d = Point(a.x-screenUnit/11,c.y-screenUnit/9)

        val e = Point(a.x-screenUnit/4,d.y)
        val f = Point(e.x-screenUnit/8,e.y-screenUnit/4)
        val g = Point(e.x-screenUnit/4,e.y)
        val paint2 = Paint()


        paint1.style = Paint.Style.FILL
        paint1.strokeWidth = (screenUnit/9).toFloat()
        paint1.color = ContextCompat.getColor(context, R.color.white)

        paint2.style = Paint.Style.STROKE
        paint2.strokeWidth = (screenUnit/20).toFloat()
        paint2.color = ContextCompat.getColor(context, R.color.white)

        val paint3 = Paint()
        paint3.style = Paint.Style.FILL
        paint3.strokeWidth = (screenUnit/9).toFloat()
        paint3.color = ContextCompat.getColor(context, R.color.red)

        val h = Point(a.x,a.y-screenUnit+screenUnit/8)
        val i = Point(h.x-screenUnit/8,h.y)
        val j = Point(e.x-screenUnit/6,e.y+screenUnit/7)

        canvas.drawCircle(h.x.toFloat(), h.y.toFloat(), (screenUnit/10).toFloat(),paint3)
        canvas.drawCircle(i.x.toFloat(), i.y.toFloat(), (screenUnit/10).toFloat(),paint3)
        canvas.drawCircle(j.x.toFloat(), j.y.toFloat(), (screenUnit/15).toFloat(),paint3)
        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.cubicTo(a.x.toFloat(), a.y.toFloat(),pointX,pointY, b.x.toFloat(), b.y.toFloat())
        path.cubicTo(b.x.toFloat(), b.y.toFloat(),pointX1,pointY1, c.x.toFloat(), c.y.toFloat())
        canvas.drawPath(path,paint)
        canvas.drawCircle(d.x.toFloat(), d.y.toFloat(), (screenUnit/15).toFloat(),paint1)
        canvas.drawLine(e.x.toFloat(), e.y.toFloat(), f.x.toFloat(), f.y.toFloat(),paint2)
        canvas.drawLine(e.x.toFloat(), e.y.toFloat(), g.x.toFloat(), g.y.toFloat(),paint2)

    }

    private fun drawGrassLeft(canvas: Canvas, margin: Double) {
        paint.strokeWidth= (screenUnit/20).toFloat()
        paint.color= ContextCompat.getColor(context, R.color.green)
        paint.style = Paint.Style.FILL_AND_STROKE

        val top = screenUnit*5.55
        val left = margin*screenUnit

        val a = Point(left.toInt(), top.toInt())
        val b = Point(((a.x+screenUnit*0.1).toInt()),(top-screenUnit*0.15).toInt())
        val curvedAB = Functions.curvedPath(a,b, screenUnit*0.05,false)
        val c = Point(((b.x+screenUnit*0.2).toInt()),(top-screenUnit*0.15).toInt())
        val curvedBC = Functions.curvedPath(b,c, screenUnit*0.3,false)
        val d = Point(((c.x+screenUnit*0.1).toInt()),(top+screenUnit*0.05).toInt())
        val curvedCD = Functions.curvedPath(c,d, screenUnit*0.05,true)
        val e = Point(((d.x+screenUnit*0.1).toInt()),(top).toInt())
        val curvedDE = Functions.curvedPath(d,e, screenUnit*0.05,false)
        val f = Point(((e.x+screenUnit*0.1).toInt()),(top).toInt())
        val curvedEF = Functions.curvedPath(e,f, screenUnit*0.05,true)
        val g = Point(((f.x+screenUnit*0.1).toInt()),(top-screenUnit*0.15).toInt())
        val curvedFG = Functions.curvedPath(f,g, screenUnit*0.05,true)
        val h = Point(((g.x+screenUnit*0.1).toInt()),(top-screenUnit*0.05).toInt())
        val curvedGH = Functions.curvedPath(g,h, screenUnit*0.05,false)
        val i = Point(((h.x+screenUnit*0.2).toInt()),(top-screenUnit*0.05).toInt())
        val curvedHI = Functions.curvedPath(h,i, screenUnit*0.05,false)
        val j = Point(((i.x+screenUnit*0.2).toInt()),(top-screenUnit*0.05).toInt())
        val curvedIJ = Functions.curvedPath(i,j, screenUnit*0.05,true)
        val k = Point(((j.x+screenUnit*0.2).toInt()),(top-screenUnit*0.2).toInt())
        val curvedJK = Functions.curvedPath(j,k, screenUnit*0.05,true)
        val l = Point(((k.x+screenUnit*0.1).toInt()),(top-screenUnit*0.1).toInt())
        val curvedKL = Functions.curvedPath(k,l, screenUnit*0.05,false)
        val m = Point(((l.x+screenUnit*0.2).toInt()),(top-screenUnit*0.05).toInt())
        val curvedLM = Functions.curvedPath(l,m, screenUnit*0.05,true)
        val n = Point(((m.x+screenUnit*0.2).toInt()),(top-screenUnit*0.1).toInt())
        val curvedMN = Functions.curvedPath(m,n, screenUnit*0.05,false)
        val o = Point(((n.x+screenUnit*0.1).toInt()),(top-screenUnit*0.05).toInt())
        val curvedNO = Functions.curvedPath(n,o, screenUnit*0.05,true)
        val p = Point(((o.x+screenUnit*0.1).toInt()),(top-screenUnit*0.25).toInt())
        val curvedOP = Functions.curvedPath(o,p, screenUnit*0.05,true)
        val q = Point(((p.x+screenUnit*0.1).toInt()),(top-screenUnit*0.1).toInt())
        val curvedPQ = Functions.curvedPath(p,q, screenUnit*0.05,true)
        val r = Point(((q.x+screenUnit*0.2).toInt()),(top-screenUnit*0.1).toInt())
        val curvedQR = Functions.curvedPath(q,r, screenUnit*0.05,true)
        val s = Point(((r.x-screenUnit*0.1).toInt()),(top+screenUnit*0.05).toInt())
        val curvedRS = Functions.curvedPath(r,s, screenUnit*0.05,false)
        val t = Point(((s.x-screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedST = Functions.curvedPath(s,t, screenUnit*0.05,true)
        val u = Point(((t.x-screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedTU = Functions.curvedPath(t,u, screenUnit*0.05,false)
        val v = Point(((u.x-screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedUV = Functions.curvedPath(u,v, screenUnit*0.05,true)
        val w = Point(((v.x-screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedVW = Functions.curvedPath(v,w, screenUnit*0.05,false)
        val x = Point(((w.x-screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedWX = Functions.curvedPath(w,x, screenUnit*0.05,true)
        val y = Point(((x.x-screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedXY = Functions.curvedPath(x,y, screenUnit*0.05,false)
        val z = Point(((y.x-screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedYZ = Functions.curvedPath(y,z, screenUnit*0.05,true)
        val a1 = Point(((z.x-screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedZA = Functions.curvedPath(z,a1, screenUnit*0.05,false)
        val b1 = Point(((a1.x-screenUnit*0.2).toInt()),(top+screenUnit*0.2).toInt())
        val curvedAB1 = Functions.curvedPath(a1,b1, screenUnit*0.05,true)
        val c1 = Point(((b1.x-screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedBC1 = Functions.curvedPath(b1,c1, screenUnit*0.05,true)
        val d1 = Point(((c1.x-screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedCD1 = Functions.curvedPath(c1,d1, screenUnit*0.05,false)

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
        path.cubicTo(m.x.toFloat(), m.y.toFloat(),curvedMN.x,curvedMN.y, n.x.toFloat(), n.y.toFloat())
        path.cubicTo(n.x.toFloat(), n.y.toFloat(),curvedNO.x,curvedNO.y, o.x.toFloat(), o.y.toFloat())
        path.cubicTo(o.x.toFloat(), o.y.toFloat(),curvedOP.x,curvedOP.y, p.x.toFloat(), p.y.toFloat())
        path.cubicTo(p.x.toFloat(), p.y.toFloat(),curvedPQ.x,curvedPQ.y, q.x.toFloat(), q.y.toFloat())
        path.cubicTo(q.x.toFloat(), q.y.toFloat(),curvedQR.x,curvedQR.y, r.x.toFloat(), r.y.toFloat())
        path.cubicTo(r.x.toFloat(), r.y.toFloat(),curvedRS.x,curvedRS.y, s.x.toFloat(), s.y.toFloat())
        path.cubicTo(s.x.toFloat(), s.y.toFloat(),curvedST.x,curvedST.y, t.x.toFloat(), t.y.toFloat())
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
        path.lineTo(a.x.toFloat(), a.y.toFloat())
        path.close()

        canvas.drawPath(path,paint)


    }

    private fun drawGrassRight(canvas: Canvas, margin: Double) {
        paint.strokeWidth= (screenUnit/20).toFloat()
        paint.color= ContextCompat.getColor(context, R.color.green)
        paint.style = Paint.Style.FILL_AND_STROKE

        val top = screenUnit*5.55
        val right = screenUnit*(width - margin)

        val a = Point(right.toInt(), top.toInt())
        val b = Point(((a.x-screenUnit*0.1).toInt()),(top-screenUnit*0.15).toInt())
        val curvedAB = Functions.curvedPath(a,b, screenUnit*0.05,true)
        val c = Point(((b.x-screenUnit*0.15).toInt()),(top-screenUnit*0.15).toInt())
        val curvedBC = Functions.curvedPath(b,c, screenUnit*0.3,true)
        val d = Point(((c.x-screenUnit*0.1).toInt()),(top+screenUnit*0.05).toInt())
        val curvedCD = Functions.curvedPath(c,d, screenUnit*0.05,false)
        val e = Point(((d.x-screenUnit*0.1).toInt()),(top).toInt())
        val curvedDE = Functions.curvedPath(d,e, screenUnit*0.05,true)
        val f = Point(((e.x-screenUnit*0.1).toInt()),(top).toInt())
        val curvedEF = Functions.curvedPath(e,f, screenUnit*0.05,false)
        val g = Point(((f.x-screenUnit*0.1).toInt()),(top-screenUnit*0.15).toInt())
        val curvedFG = Functions.curvedPath(f,g, screenUnit*0.05,false)
        val h = Point(((g.x-screenUnit*0.1).toInt()),(top-screenUnit*0.05).toInt())
        val curvedGH = Functions.curvedPath(g,h, screenUnit*0.05,true)
        val i = Point(((h.x-screenUnit*0.15).toInt()),(top-screenUnit*0.05).toInt())
        val curvedHI = Functions.curvedPath(h,i, screenUnit*0.05,true)
        val j = Point(((i.x-screenUnit*0.15).toInt()),(top-screenUnit*0.05).toInt())
        val curvedIJ = Functions.curvedPath(i,j, screenUnit*0.05,false)
        val k = Point(((j.x-screenUnit*0.15).toInt()),(top-screenUnit*0.2).toInt())
        val curvedJK = Functions.curvedPath(j,k, screenUnit*0.05,false)
        val l = Point(((k.x-screenUnit*0.1).toInt()),(top-screenUnit*0.1).toInt())
        val curvedKL = Functions.curvedPath(k,l, screenUnit*0.05,true)
        val m = Point(((l.x-screenUnit*0.2).toInt()),(top-screenUnit*0.05).toInt())
        val curvedLM = Functions.curvedPath(l,m, screenUnit*0.05,false)
        val n = Point(((m.x-screenUnit*0.2).toInt()),(top-screenUnit*0.1).toInt())
        val curvedMN = Functions.curvedPath(m,n, screenUnit*0.05,true)
        val o = Point(((n.x-screenUnit*0.1).toInt()),(top-screenUnit*0.05).toInt())
        val curvedNO = Functions.curvedPath(n,o, screenUnit*0.05,false)
        val p = Point(((o.x-screenUnit*0.1).toInt()),(top-screenUnit*0.25).toInt())
        val curvedOP = Functions.curvedPath(o,p, screenUnit*0.05,false)
        val q = Point(((p.x-screenUnit*0.1).toInt()),(top-screenUnit*0.1).toInt())
        val curvedPQ = Functions.curvedPath(p,q, screenUnit*0.05,false)
        val r = Point(((q.x-screenUnit*0.2).toInt()),(top-screenUnit*0.1).toInt())
        val curvedQR = Functions.curvedPath(q,r, screenUnit*0.05,false)
        val s = Point(((r.x+screenUnit*0.1).toInt()),(top+screenUnit*0.05).toInt())
        val curvedRS = Functions.curvedPath(r,s, screenUnit*0.05,true)
        val t = Point(((s.x+screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedST = Functions.curvedPath(s,t, screenUnit*0.05,false)
        val u = Point(((t.x+screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedTU = Functions.curvedPath(t,u, screenUnit*0.05,true)
        val v = Point(((u.x+screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedUV = Functions.curvedPath(u,v, screenUnit*0.05,false)
        val w = Point(((v.x+screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedVW = Functions.curvedPath(v,w, screenUnit*0.05,true)
        val x = Point(((w.x+screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedWX = Functions.curvedPath(w,x, screenUnit*0.05,false)
        val y = Point(((x.x+screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedXY = Functions.curvedPath(x,y, screenUnit*0.05,true)
        val z = Point(((y.x+screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedYZ = Functions.curvedPath(y,z, screenUnit*0.05,false)
        val a1 = Point(((z.x+screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedZA = Functions.curvedPath(z,a1, screenUnit*0.05,true)
        val b1 = Point(((a1.x+screenUnit*0.2).toInt()),(top+screenUnit*0.2).toInt())
        val curvedAB1 = Functions.curvedPath(a1,b1, screenUnit*0.05,false)
        val c1 = Point(((b1.x+screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedBC1 = Functions.curvedPath(b1,c1, screenUnit*0.05,false)
        val d1 = Point(((c1.x+screenUnit*0.2).toInt()),(top+screenUnit*0.15).toInt())
        val curvedCD1 = Functions.curvedPath(c1,d1, screenUnit*0.05,true)


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
        path.cubicTo(m.x.toFloat(), m.y.toFloat(),curvedMN.x,curvedMN.y, n.x.toFloat(), n.y.toFloat())
        path.cubicTo(n.x.toFloat(), n.y.toFloat(),curvedNO.x,curvedNO.y, o.x.toFloat(), o.y.toFloat())
        path.cubicTo(o.x.toFloat(), o.y.toFloat(),curvedOP.x,curvedOP.y, p.x.toFloat(), p.y.toFloat())
        path.cubicTo(p.x.toFloat(), p.y.toFloat(),curvedPQ.x,curvedPQ.y, q.x.toFloat(), q.y.toFloat())
        path.cubicTo(q.x.toFloat(), q.y.toFloat(),curvedQR.x,curvedQR.y, r.x.toFloat(), r.y.toFloat())
        path.cubicTo(r.x.toFloat(), r.y.toFloat(),curvedRS.x,curvedRS.y, s.x.toFloat(), s.y.toFloat())
        path.cubicTo(s.x.toFloat(), s.y.toFloat(),curvedST.x,curvedST.y, t.x.toFloat(), t.y.toFloat())
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
        path.lineTo(a.x.toFloat(), a.y.toFloat())
        path.close()

        canvas.drawPath(path,paint)

    }

    private fun drawGrassMiddle(canvas: Canvas){
        paint.strokeWidth= (screenUnit/20).toFloat()
        paint.color= ContextCompat.getColor(context, R.color.green)
        paint.style = Paint.Style.FILL_AND_STROKE

        val top = screenUnit*5.7

        val a = Point((screenUnit*width*0.5).toInt(), top.toInt())
        val b = Point((a.x-screenUnit*0.1).toInt(), (top-screenUnit*0.25).toInt())
        val curvedAB = Functions.curvedPath(a,b,screenUnit*0.05,true)
        val c = Point((b.x-screenUnit*0.1).toInt(), (top-screenUnit*0.05).toInt())
        val curvedBC = Functions.curvedPath(b,c,screenUnit*0.05,false)
        val d = Point((c.x-screenUnit*0.1).toInt(), (top-screenUnit*0.2).toInt())
        val curvedCD = Functions.curvedPath(c,d,screenUnit*0.05,false)
        val e = Point((d.x-screenUnit*0.1).toInt(), (top).toInt())
        val curvedDE = Functions.curvedPath(d,e,screenUnit*0.05,true)
        val f = Point((e.x-screenUnit*0.1).toInt(), (top-screenUnit*0.15).toInt())
        val curvedEF = Functions.curvedPath(e,f,screenUnit*0.05,false)
        val g = Point((f.x-screenUnit*0.1).toInt(), (top).toInt())
        val curvedFG = Functions.curvedPath(f,g,screenUnit*0.05,false)
        val h = Point((g.x-screenUnit*0.1).toInt(), (top-screenUnit*0.1).toInt())
        val curvedGH = Functions.curvedPath(g,h,screenUnit*0.05,true)
        val i = Point((h.x-screenUnit*0.1).toInt(), (top+screenUnit*0.05).toInt())
        val curvedHI = Functions.curvedPath(h,i,screenUnit*0.05,true)
        val j = Point((i.x-screenUnit*0.1).toInt(), (top-screenUnit*0.05).toInt())
        val curvedIJ = Functions.curvedPath(i,j,screenUnit*0.05,false)
        val k = Point((j.x-screenUnit*0.1).toInt(), (top+screenUnit*0.1).toInt())
        val curvedJK = Functions.curvedPath(j,k,screenUnit*0.05,false)
        val l = Point((k.x-screenUnit*0.1).toInt(), (top-screenUnit*0.15).toInt())
        val curvedKL = Functions.curvedPath(k,l,screenUnit*0.05,false)
        val m = Point((l.x-screenUnit*0.2).toInt(), (top-screenUnit*0.05).toInt())
        val curvedLM = Functions.curvedPath(l,m,screenUnit*0.05,false)
        val n = Point((m.x-screenUnit*0.8).toInt(), (top-screenUnit*0.05).toInt())
        val curvedMN = Functions.curvedPath(m,n,screenUnit*0.15,true)
        val o = Point((n.x-screenUnit*0.1).toInt(), (top-screenUnit*0.15).toInt())
        val curvedNO = Functions.curvedPath(n,o,screenUnit*0.05,false)
        val p = Point((o.x+screenUnit*0.1).toInt(), (top+screenUnit*0.15).toInt())
        val curvedOP = Functions.curvedPath(o,p,screenUnit*0.15,false)
        val q = Point((p.x+screenUnit*0.25).toInt(), (top+screenUnit*0.15).toInt())
        val curvedPQ = Functions.curvedPath(p,q,screenUnit*0.05,false)
        val r = Point((q.x+screenUnit*0.25).toInt(), (top+screenUnit*0.2).toInt())
        val curvedQR = Functions.curvedPath(q,r,screenUnit*0.05,true)
        val s = Point((r.x+screenUnit*0.25).toInt(), (top+screenUnit*0.2).toInt())
        val curvedRS = Functions.curvedPath(r,s,screenUnit*0.05,false)
        val t = Point((s.x+screenUnit*0.25).toInt(), (top+screenUnit*0.15).toInt())
        val curvedST = Functions.curvedPath(s,t,screenUnit*0.05,true)
        val u = Point((t.x+screenUnit*0.25).toInt(), (top+screenUnit*0.2).toInt())
        val curvedTU = Functions.curvedPath(t,u,screenUnit*0.05,false)
        val v = Point((u.x+screenUnit*0.25).toInt(), (top+screenUnit*0.2).toInt())
        val curvedUV = Functions.curvedPath(u,v,screenUnit*0.05,true)
        val w = Point((v.x+screenUnit*0.25).toInt(), (top+screenUnit*0.2).toInt())
        val curvedVW = Functions.curvedPath(v,w,screenUnit*0.05,false)
        val x = Point((w.x+screenUnit*0.25).toInt(), (top+screenUnit*0.15).toInt())
        val curvedWX = Functions.curvedPath(w,x,screenUnit*0.05,true)
        val y = Point((a.x).toInt(), (top+screenUnit*0.15).toInt())
        val curvedXY = Functions.curvedPath(x,y,screenUnit*0.05,false)

        val x1 = Point((y.x+screenUnit*0.25).toInt(), (top+screenUnit*0.15).toInt())
        val curvedYX1 = Functions.curvedPath(y,x1,screenUnit*0.05,false)
        val w1 = Point((x1.x+screenUnit*0.25).toInt(), (top+screenUnit*0.2).toInt())
        val curvedXW1 = Functions.curvedPath(x1,w1,screenUnit*0.05,true)
        val v1 = Point((w1.x+screenUnit*0.25).toInt(), (top+screenUnit*0.2).toInt())
        val curvedWV1 = Functions.curvedPath(w1,v1,screenUnit*0.05,false)
        val u1 = Point((v1.x+screenUnit*0.25).toInt(), (top+screenUnit*0.2).toInt())
        val curvedVU1 = Functions.curvedPath(v1,u1,screenUnit*0.05,true)
        val t1 = Point((u1.x+screenUnit*0.25).toInt(), (top+screenUnit*0.15).toInt())
        val curvedUT1 = Functions.curvedPath(u1,t1,screenUnit*0.05,false)
        val s1 = Point((t1.x+screenUnit*0.25).toInt(), (top+screenUnit*0.2).toInt())
        val curvedTS1 = Functions.curvedPath(t1,s1,screenUnit*0.05,true)
        val r1 = Point((s1.x+screenUnit*0.25).toInt(), (top+screenUnit*0.2).toInt())
        val curvedSR1 = Functions.curvedPath(s1,r1,screenUnit*0.05,false)
        val q1 = Point((r1.x+screenUnit*0.25).toInt(), (top+screenUnit*0.15).toInt())
        val curvedRQ1 = Functions.curvedPath(r1,q1,screenUnit*0.05,true)
        val p1 = Point((q1.x+screenUnit*0.25).toInt(), (top+screenUnit*0.15).toInt())
        val curvedQP1 = Functions.curvedPath(q1,p1,screenUnit*0.05,false)
        val o1 = Point((p1.x+screenUnit*0.1).toInt(), (top-screenUnit*0.15).toInt())
        val curvedPO1 = Functions.curvedPath(p1,o1,screenUnit*0.15,false)
        val n1 = Point((o1.x-screenUnit*0.1).toInt(), (top-screenUnit*0.05).toInt())
        val curvedON1 = Functions.curvedPath(o1,n1,screenUnit*0.05,false)
        val m1 = Point((n1.x-screenUnit*0.8).toInt(), (top-screenUnit*0.05).toInt())
        val curvedNM1 = Functions.curvedPath(n1,m1,screenUnit*0.15,true)
        val l1 = Point((m1.x-screenUnit*0.2).toInt(), (top-screenUnit*0.15).toInt())
        val curvedML1 = Functions.curvedPath(m1,l1,screenUnit*0.05,false)
        val k1 = Point((l1.x-screenUnit*0.1).toInt(), (top+screenUnit*0.1).toInt())
        val curvedLK1 = Functions.curvedPath(l1,k1,screenUnit*0.05,false)
        val j1 = Point((k1.x-screenUnit*0.1).toInt(), (top-screenUnit*0.05).toInt())
        val curvedKJ1 = Functions.curvedPath(k1,j1,screenUnit*0.05,false)
        val i1 = Point((j1.x-screenUnit*0.1).toInt(), (top+screenUnit*0.05).toInt())
        val curvedJI1 = Functions.curvedPath(j1,i1,screenUnit*0.05,false)
        val h1 = Point((i1.x-screenUnit*0.1).toInt(), (top-screenUnit*0.1).toInt())
        val curvedIH1 = Functions.curvedPath(i1,h1,screenUnit*0.05,true)
        val g1 = Point((h1.x-screenUnit*0.1).toInt(), (top).toInt())
        val curvedHG1 = Functions.curvedPath(h1,g1,screenUnit*0.05,true)
        val f1 = Point((g1.x-screenUnit*0.1).toInt(), (top-screenUnit*0.15).toInt())
        val curvedGF1 = Functions.curvedPath(g1,f1,screenUnit*0.05,false)
        val e1 = Point((f1.x-screenUnit*0.1).toInt(), (top).toInt())
        val curvedFE1 = Functions.curvedPath(f1,e1,screenUnit*0.05,false)
        val d1 = Point((e1.x-screenUnit*0.1).toInt(), (top-screenUnit*0.2).toInt())
        val curvedED1 = Functions.curvedPath(e1,d1,screenUnit*0.05,true)
        val c1 = Point((d1.x-screenUnit*0.1).toInt(), (top-screenUnit*0.05).toInt())
        val curvedDC1 = Functions.curvedPath(d1,c1,screenUnit*0.05,false)
        val b1 = Point((c1.x-screenUnit*0.1).toInt(), (top-screenUnit*0.25).toInt())
        val curvedCB1 = Functions.curvedPath(c1,b1,screenUnit*0.05,false)
        val curvedBA1 = Functions.curvedPath(b1,a,screenUnit*0.05,false)

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
        path.cubicTo(m.x.toFloat(), m.y.toFloat(),curvedMN.x,curvedMN.y, n.x.toFloat(), n.y.toFloat())
        path.cubicTo(n.x.toFloat(), n.y.toFloat(),curvedNO.x,curvedNO.y, o.x.toFloat(), o.y.toFloat())
        path.cubicTo(o.x.toFloat(), o.y.toFloat(),curvedOP.x,curvedOP.y, p.x.toFloat(), p.y.toFloat())
        path.cubicTo(p.x.toFloat(), p.y.toFloat(),curvedPQ.x,curvedPQ.y, q.x.toFloat(), q.y.toFloat())
        path.cubicTo(q.x.toFloat(), q.y.toFloat(),curvedQR.x,curvedQR.y, r.x.toFloat(), r.y.toFloat())
        path.cubicTo(r.x.toFloat(), r.y.toFloat(),curvedRS.x,curvedRS.y, s.x.toFloat(), s.y.toFloat())
        path.cubicTo(s.x.toFloat(), s.y.toFloat(),curvedST.x,curvedST.y, t.x.toFloat(), t.y.toFloat())
        path.cubicTo(t.x.toFloat(), t.y.toFloat(),curvedTU.x,curvedTU.y, u.x.toFloat(), u.y.toFloat())
        path.cubicTo(u.x.toFloat(), u.y.toFloat(),curvedUV.x,curvedUV.y, v.x.toFloat(), v.y.toFloat())
        path.cubicTo(v.x.toFloat(), v.y.toFloat(),curvedVW.x,curvedVW.y, w.x.toFloat(), w.y.toFloat())
        path.cubicTo(w.x.toFloat(), w.y.toFloat(),curvedWX.x,curvedWX.y, x.x.toFloat(), x.y.toFloat())
        path.cubicTo(x.x.toFloat(), x.y.toFloat(),curvedXY.x,curvedXY.y, y.x.toFloat(), y.y.toFloat())
        path.cubicTo(y.x.toFloat(), y.y.toFloat(),curvedYX1.x,curvedYX1.y, x1.x.toFloat(), x1.y.toFloat())
        path.cubicTo(x1.x.toFloat(), x1.y.toFloat(),curvedXW1.x,curvedXW1.y, w1.x.toFloat(), w1.y.toFloat())
        path.cubicTo(w1.x.toFloat(), w1.y.toFloat(),curvedWV1.x,curvedWV1.y, v1.x.toFloat(), v1.y.toFloat())
        path.cubicTo(v1.x.toFloat(), v1.y.toFloat(),curvedVU1.x,curvedVU1.y, u1.x.toFloat(), u1.y.toFloat())
        path.cubicTo(u1.x.toFloat(), u1.y.toFloat(),curvedUT1.x,curvedUT1.y, t1.x.toFloat(), t1.y.toFloat())
        path.cubicTo(t1.x.toFloat(), t1.y.toFloat(),curvedTS1.x,curvedTS1.y, s1.x.toFloat(), s1.y.toFloat())
        path.cubicTo(s1.x.toFloat(), s1.y.toFloat(),curvedSR1.x,curvedSR1.y, r1.x.toFloat(), r1.y.toFloat())
        path.cubicTo(r1.x.toFloat(), r1.y.toFloat(),curvedRQ1.x,curvedRQ1.y, q1.x.toFloat(), q1.y.toFloat())
        path.cubicTo(q1.x.toFloat(), q1.y.toFloat(),curvedQP1.x,curvedQP1.y, p1.x.toFloat(), p1.y.toFloat())
        path.cubicTo(p1.x.toFloat(), p1.y.toFloat(),curvedPO1.x,curvedPO1.y, o1.x.toFloat(), o1.y.toFloat())
        path.cubicTo(o1.x.toFloat(), o1.y.toFloat(),curvedON1.x,curvedON1.y, n1.x.toFloat(), n1.y.toFloat())
        path.cubicTo(n1.x.toFloat(), n1.y.toFloat(),curvedNM1.x,curvedNM1.y, m1.x.toFloat(), m1.y.toFloat())
        path.cubicTo(m1.x.toFloat(), m1.y.toFloat(),curvedML1.x,curvedML1.y, l1.x.toFloat(), l1.y.toFloat())
        path.cubicTo(l1.x.toFloat(), l1.y.toFloat(),curvedLK1.x,curvedLK1.y, k1.x.toFloat(), k1.y.toFloat())
        path.cubicTo(k1.x.toFloat(), k1.y.toFloat(),curvedKJ1.x,curvedKJ1.y, j1.x.toFloat(), j1.y.toFloat())
        path.cubicTo(j1.x.toFloat(), j1.y.toFloat(),curvedJI1.x,curvedJI1.y, i1.x.toFloat(), i1.y.toFloat())
        path.cubicTo(i1.x.toFloat(), i1.y.toFloat(),curvedIH1.x,curvedIH1.y, h1.x.toFloat(), h1.y.toFloat())
        path.cubicTo(h1.x.toFloat(), h1.y.toFloat(),curvedHG1.x,curvedHG1.y, g1.x.toFloat(), g1.y.toFloat())
        path.cubicTo(g1.x.toFloat(), g1.y.toFloat(),curvedGF1.x,curvedGF1.y, f1.x.toFloat(), f1.y.toFloat())
        path.cubicTo(f1.x.toFloat(), f1.y.toFloat(),curvedFE1.x,curvedFE1.y, e1.x.toFloat(), e1.y.toFloat())
        path.cubicTo(e1.x.toFloat(), e1.y.toFloat(),curvedED1.x,curvedED1.y, d1.x.toFloat(), d1.y.toFloat())
        path.cubicTo(d1.x.toFloat(), d1.y.toFloat(),curvedDC1.x,curvedDC1.y, c1.x.toFloat(), c1.y.toFloat())
        path.cubicTo(c1.x.toFloat(), c1.y.toFloat(),curvedCB1.x,curvedCB1.y, b1.x.toFloat(), b1.y.toFloat())
        path.cubicTo(b1.x.toFloat(), b1.y.toFloat(),curvedBA1.x,curvedBA1.y, a.x.toFloat(), a.y.toFloat())
        path.close()

        canvas.drawPath(path,paint)




    }
}