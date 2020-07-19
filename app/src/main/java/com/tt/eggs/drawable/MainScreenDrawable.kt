package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

import com.tt.eggs.R
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
        drawGrass(canvas, margin)

    }




    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun getOpacity(): Int =
        PixelFormat.OPAQUE


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

    private fun drawGrass(canvas: Canvas, margin: Double) {
        paint.strokeWidth= (screenUnit/10).toFloat()
        paint.color= ContextCompat.getColor(context, R.color.green)
        paint.style = Paint.Style.STROKE




    }

}