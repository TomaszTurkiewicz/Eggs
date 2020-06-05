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

    // store faults
    private var faults:Int

    // game mode (A or B)
    private var gameMode: Boolean

    // initialization
    init {
        for(x in 0..5){
            for(y in 0..3){
                gameState[x][y]=Static.NO_EGG
            }
        }

        for(i in 0..3){
            position[i]=Static.NO_BASKET
        }

        position[Static.RIGHT_TOP]=Static.BASKET

        lastNumber=0

        score=0

        faults=Static.FAULT_NO_FAULT

        gameMode=Static.GAME_A

    }


    // return one position in egg array
    fun displayCell(x:Int, y:Int):Boolean{
        return gameState[x][y]
    }

    // clear egg array
    fun clear(){
        for(x in 0..5){
            for(y in 0..3){
                gameState[x][y]=Static.NO_EGG
            }
        }
    }

    // placing basket at one in four positions
    fun setBasket(i:Int){
        for(arg in 0..3){
            position[arg]=Static.NO_BASKET
        }
        position[i]=Static.BASKET
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
        var ran=random%gameMode()
        if(lastNumber==ran){
            ran += 1
            ran %= gameMode()
        }
        lastNumber=ran
        gameState[0][Static.LEFT_BOTTOM]=Static.NO_EGG
        gameState[0][Static.LEFT_TOP]=Static.NO_EGG
        gameState[0][Static.RIGHT_TOP]=Static.NO_EGG
        gameState[0][Static.RIGHT_BOTTOM]=Static.NO_EGG
        gameState[0][ran]=Static.EGG
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

    // clear everything
    fun reset(){

        //clear eggs
        for(x in 0..5){
            for(y in 0..3){
                gameState[x][y]=Static.NO_EGG
            }
        }

        // clear basket position
        for(i in 0..3){
            position[i]=Static.NO_BASKET
        }
        position[Static.RIGHT_TOP]=Static.BASKET

        // clear score and faults
        lastNumber=0
        score=0
        faults=Static.FAULT_NO_FAULT

    }

    private fun gameMode() :Int{
        return when(gameMode){
            Static.GAME_A-> 3
            else-> 4
        }

    }

    fun setGameMode(mode:Boolean){
        gameMode=mode
    }


}