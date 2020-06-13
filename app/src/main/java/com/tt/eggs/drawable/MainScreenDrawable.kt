package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

import com.tt.eggs.R

class MainScreenDrawable(private val context: Context, private val screenUnit:Int, private val width:Int, private val height:Int):Drawable() {
    private var paint = Paint()




    override fun draw(canvas: Canvas) {
        drawMainScreen(canvas)
        drawRounderFrames(canvas)
        val margin = drawLCD(canvas)
        drawRoosts(canvas, margin)
        drawRabbitWindow(canvas, margin)

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
        val rect = Rect(0,0,screenUnit*width,screenUnit*height)
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
}