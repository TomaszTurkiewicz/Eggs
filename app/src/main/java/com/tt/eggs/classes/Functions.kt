package com.tt.eggs.classes

import android.content.Context
import android.content.SharedPreferences

class Functions {
    companion object{

        fun readGameAFromSharedPreferences(context: Context?, userId:String):GameA{
            var tPoints = 0
            var tHigh = 0
            var tCounter = 0
            if(context!=null){
            val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
            tPoints = sharedPreferences.getInt("TOTAL_A",0)
            tHigh =  sharedPreferences.getInt("HIGH_A",0)
            tCounter =  sharedPreferences.getInt("COUNTER_A",0)
        }
            return GameA(tPoints,tHigh,tCounter)

        }

        fun readGameBFromSharedPreferences(context: Context?, userId:String):GameB{
            var tPoints = 0
            var tHigh = 0
            var tCounter = 0
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
                tPoints = sharedPreferences.getInt("TOTAL_B",0)
                tHigh =  sharedPreferences.getInt("HIGH_B",0)
                tCounter =  sharedPreferences.getInt("COUNTER_B",0)
            }
            return GameB(tPoints,tHigh,tCounter)

        }


    }
}