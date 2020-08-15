package com.tt.eggs.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.eggs.R

class Digit(private val context: Context, private val width: Int, private val number:Int): Drawable(){

    private val paint = Paint()
    val unit = width*0.03

    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.strokeWidth = (width*0.01).toFloat()

        if(number==0||number==2||number==3||number==5||number==6||number==7||number==8||number==9){
            drawUpperSegment(canvas)
        }

        if(number==0||number==2||number==3||number==5||number==6||number==8||number==9) {
            drawLowerSegment(canvas)
        }

        if(number==0||number==1||number==2||number==3||number==4||number==7||number==8||number==9) {
            drawUpperRightSegment(canvas)
        }

        if(number==0||number==1||number==3||number==4||number==5||number==6||number==7||number==8||number==9) {
            drawLowerRightSegment(canvas)
        }

        if(number==0||number==2||number==6||number==8) {
            drawLowerLeftSegment(canvas)
        }

        if(number==0||number==4||number==5||number==6||number==8||number==9) {
            drawUpperLeftSegment(canvas)
        }

        if(number==2||number==3||number==4||number==5||number==6||number==8||number==9) {
            drawMiddleSegment(canvas)
        }



    }

    private fun drawMiddleSegment(canvas: Canvas) {
        val path = Path()

        path.moveTo((1.5*unit).toFloat(), width.toFloat())
        path.lineTo((6*unit).toFloat(), (width-3.5*unit).toFloat())
        path.lineTo((width-6*unit).toFloat(), (width-3.5*unit).toFloat())
        path.lineTo((width-1.5*unit).toFloat(), width.toFloat())
        path.lineTo((width-6*unit).toFloat(), (width+3.5*unit).toFloat())
        path.lineTo((6*unit).toFloat(), (width+3.5*unit).toFloat())
        path.lineTo((1.5*unit).toFloat(), width.toFloat())
        path.close()
        canvas.drawPath(path,paint)
    }

    private fun drawUpperLeftSegment(canvas: Canvas) {
        val path = Path()

        path.moveTo(0F, unit.toFloat())
        path.lineTo(0F, (width-0.5*unit).toFloat())
        path.lineTo((6*unit).toFloat(), (width-4.5*unit).toFloat())
        path.lineTo((6*unit).toFloat(), (7*unit).toFloat())
        path.lineTo(0F, unit.toFloat())
        path.close()
        canvas.drawPath(path,paint)

    }

    private fun drawLowerLeftSegment(canvas: Canvas) {
        val path = Path()

        path.moveTo(0F, (width+0.5*unit).toFloat())
        path.lineTo(0F, (2*width-unit).toFloat())
        path.lineTo((6*unit).toFloat(), (2*width-7*unit).toFloat())
        path.lineTo((6*unit).toFloat(), (width+4.5*unit).toFloat())
        path.lineTo(0F, (width+0.5*unit).toFloat())
        path.close()
        canvas.drawPath(path,paint)


    }

    private fun drawLowerSegment(canvas: Canvas) {
        val path = Path()

        path.moveTo(unit.toFloat(), (2*width).toFloat())
        path.lineTo((width-unit).toFloat(), (2*width).toFloat())
        path.lineTo((width-7*unit).toFloat(), (2*width-6*unit).toFloat())
        path.lineTo((7*unit).toFloat(), (2*width-6*unit).toFloat())
        path.lineTo(unit.toFloat(), (2*width).toFloat())
        path.close()
        canvas.drawPath(path,paint)

    }

    private fun drawLowerRightSegment(canvas: Canvas) {
        val path = Path()

        path.moveTo(width.toFloat(), (width+0.5*unit).toFloat())
        path.lineTo(width.toFloat(), (2*width-unit).toFloat())
        path.lineTo((width-6*unit).toFloat(), (2*width-7*unit).toFloat())
        path.lineTo((width-6*unit).toFloat(), (width+4.5*unit).toFloat())
        path.lineTo(width.toFloat(), (width+0.5*unit).toFloat())
        path.close()
        canvas.drawPath(path,paint)

    }

    private fun drawUpperRightSegment(canvas: Canvas) {
        val path = Path()

        path.moveTo(width.toFloat(), unit.toFloat())
        path.lineTo(width.toFloat(), (width-0.5*unit).toFloat())
        path.lineTo((width-6*unit).toFloat(), (width-4.5*unit).toFloat())
        path.lineTo((width-6*unit).toFloat(), (7*unit).toFloat())
        path.lineTo(width.toFloat(), unit.toFloat())
        path.close()
        canvas.drawPath(path,paint)
    }

    private fun drawUpperSegment(canvas: Canvas) {
        val path = Path()

        path.moveTo(unit.toFloat(), 0F)
        path.lineTo((width-unit).toFloat(), 0F)
        path.lineTo((width-7*unit).toFloat(), (6*unit).toFloat())
        path.lineTo((7*unit).toFloat(), (6*unit).toFloat())
        path.lineTo(unit.toFloat(), 0F)
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