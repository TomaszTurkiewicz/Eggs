package com.tt.eggs.classes

class FallenEgg {
    var fallenEgg = Array(7) {BooleanArray(2)}
    init {
        for(x in 0..4){
            for(y in 0..1){
                fallenEgg[x][y]=Static.NO_EGG
            }
        }
    }

    fun setFallenEgg (position:Int){
        fallenEgg[0][position]=Static.EGG
    }

    fun moveDown():Boolean{
        // move down
        for(i in 5 downTo 0){
            for(j in 0..1){
                fallenEgg[i+1][j]=fallenEgg[i][j]
            }
        }
        fallenEgg[0][0]=Static.NO_EGG
        fallenEgg[0][1]=Static.NO_EGG
        return fallenEgg[6][0]||fallenEgg[6][1]
    }

    fun getFallenEgg(i:Int,j:Int):Boolean{
        return fallenEgg[i][j]
    }
}