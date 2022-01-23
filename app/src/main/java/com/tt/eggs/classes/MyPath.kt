package com.tt.eggs.classes

import android.graphics.Path
import android.graphics.Point

class MyPath : Path() {
    fun cubic(a: Point, b: Point, radius:CurvedPoint){
        cubicTo(a.x.toFloat(), a.y.toFloat(),radius.x,radius.y, b.x.toFloat(), b.y.toFloat())
    }

    fun line(a: Point){
        lineTo(a.x.toFloat(), a.y.toFloat())
    }

    fun move(a:Point){
        moveTo(a.x.toFloat(), a.y.toFloat())
    }
}