package com.tt.eggs.classes

import kotlin.random.Random

class Game {
        var gameState = Array(6) {BooleanArray(4)}

        var position = BooleanArray(4)

        private var lastNumber:Int

    init {
        for(x in 0..5){
            for(y in 0..3){
                gameState[x][y]=false
            }
        }

        for(i in 0..3){
            position[i]=false
        }
        position[3]=true

        lastNumber=0
    }


    fun displayCell(x:Int, y:Int):Boolean{
        return gameState[x][y]
    }

    fun clear(){
        for(x in 0..5){
            for(y in 0..3){
                gameState[x][y]=false
            }
        }
    }

    fun setBasket(i:Int){
        for(arg in 0..3){
            position[arg]=false
        }
        position[i]=true
    }

    fun moveDown(){
        for(i in 4 downTo 0){
            for(j in 0..3){
                gameState[i+1][j]=gameState[i][j]
            }
        }


        val random = Random.nextInt(0,99)
        var ran=random%4
        if(lastNumber==ran){
            ran += 1
            ran %= 4
        }
        lastNumber=ran
        gameState[0][0]=false
        gameState[0][1]=false
        gameState[0][2]=false
        gameState[0][3]=false
        gameState[0][ran]=true
    }



}