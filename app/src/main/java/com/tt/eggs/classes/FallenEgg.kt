package com.tt.eggs.classes

class FallenEgg {
    var fallenEgg = Array(6) {BooleanArray(2)}
    init {
        for(x in 0..5){
            for(y in 0..1){
                fallenEgg[x][y]=Static.NO_EGG
            }
        }
    }

    // set fallen egg (right or left side)
    fun setFallenEgg (position:Int){
        fallenEgg[0][0]=Static.NO_EGG
        fallenEgg[0][1]=Static.NO_EGG
        fallenEgg[0][position]=Static.EGG
    }

    // move running chicken
    fun moveDown():Boolean{
        // move down
        for(i in 4 downTo 0){
            for(j in 0..1){
                fallenEgg[i+1][j]=fallenEgg[i][j]
            }
        }
        fallenEgg[0][0]=Static.NO_EGG
        fallenEgg[0][1]=Static.NO_EGG
        //return when chicken is at the end
        return fallenEgg[5][0]||fallenEgg[5][1]
    }

    // returns single position in fallen egg array (need it to display)
    fun getFallenEgg(i:Int,j:Int):Boolean{
        return fallenEgg[i][j]
    }
}