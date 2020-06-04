package com.tt.eggs.classes

import kotlin.random.Random

class Game {

    // array representing eggs
        var gameState = Array(6) {BooleanArray(4)}

    // array representing basket
        var position = BooleanArray(4)

    // to avoid two eggs next to each other
        private var lastNumber:Int

    // to store score
    private var score:Int

    private var faults:Int

    // initialization
    init {
        for(x in 0..5){
            for(y in 0..3){
                gameState[x][y]=false
            }
        }

        for(i in 0..3){
            position[i]=false
        }

        position[Static.RIGHT_TOP]=true

        lastNumber=0

        score=0

        faults=0

    }


    // return one position in egg array
    fun displayCell(x:Int, y:Int):Boolean{
        return gameState[x][y]
    }

    // clear egg array
    fun clear(){
        for(x in 0..5){
            for(y in 0..3){
                gameState[x][y]=false
            }
        }
    }

    // placing basket at one in four positions
    fun setBasket(i:Int){
        for(arg in 0..3){
            position[arg]=false
        }
        position[i]=true
    }

    // egg array move down
    fun moveDown(){

        // move down
        for(i in 4 downTo 0){
            for(j in 0..3){
                gameState[i+1][j]=gameState[i][j]
            }
        }

        // generate new egg at random postion
        val random = Random.nextInt(0,99)
        var ran=random%4
        if(lastNumber==ran){
            ran += 1
            ran %= 4
        }
        lastNumber=ran
        gameState[0][Static.LEFT_BOTTOM]=false
        gameState[0][Static.LEFT_TOP]=false
        gameState[0][Static.RIGHT_TOP]=false
        gameState[0][Static.RIGHT_BOTTOM]=false
        gameState[0][ran]=true
    }

    // return current score
    fun getScore():Int{
        return score
    }

    // add point when egg caught
    fun addPoint(){
        score+=1
    }

    // add fault when egg not caught
    fun addFault(){
        faults+=1
    }

    // return current faults
    fun getFault():Int{
        return faults
    }

    fun reset(){
        for(x in 0..5){
            for(y in 0..3){
                gameState[x][y]=false
            }
        }

        for(i in 0..3){
            position[i]=false
        }
        position[Static.RIGHT_TOP]=true
        lastNumber=0
        score=0
        faults=0

    }



}