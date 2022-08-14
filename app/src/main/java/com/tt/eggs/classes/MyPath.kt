package com.tt.eggs.classes

import android.graphics.Path
import android.graphics.Point

class MyPath : Path() {

    fun line(a: Point){
        lineTo(a.x.toFloat(), a.y.toFloat())
    }

    fun move(a:Point){
        moveTo(a.x.toFloat(), a.y.toFloat())
    }
}