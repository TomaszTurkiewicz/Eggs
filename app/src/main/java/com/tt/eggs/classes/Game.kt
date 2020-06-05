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

    // distance between eggs
    private var distance:Int

    // number of eggs in row
    private var noOfEggs:Int

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

        distance=4

        noOfEggs=1
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
        generateEgg()
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

    // return integer 3 or 4 (game mode)
    private fun gameMode() = if(gameMode==Static.GAME_A) 3 else 4

    // set game mode
    fun setGameMode(mode:Boolean){
        gameMode=mode
    }

    // generate next egg or eggs
    private fun generateEgg(){
        when(score){
            in 0..3 -> generateOneEgg()
            in 4..12 -> generateTwoEggs()
            else -> generateRandomNumberOfEggs()
        }
    }

    // totally random
    private fun generateRandomNumberOfEggs() {

        generateEggUsingCounters()

        // set counters again
        if(noOfEggs==0&&distance==0){
            val random = Random.nextInt(0,99)
            val ranEggs = random%5+1
            noOfEggs=ranEggs
            val ranDistance = random%2
            distance=ranDistance
        }


    }

    // two eggs
    private fun generateTwoEggs() {
        generateEggUsingCounters()
        // set counters again
        if(noOfEggs==0&&distance==0){
            noOfEggs=2
            distance=3
        }
    }

    // one egg
    private fun generateOneEgg() {
       generateEggUsingCounters()
        // set counters again
        if(noOfEggs==0&&distance==0){
            noOfEggs=1
            distance=4
        }
    }

    // generate egg function
    private fun generateEggUsingCounters(){

        // generate egg
        if(noOfEggs>0) {
            val random = Random.nextInt(0, 99)
            var ran = random % gameMode()
            val ranCheck = random % 5

            // random not check if next egg from the same side
            if(ranCheck!=2) {
                if (lastNumber == ran) {
                    ran += 1
                    ran %= gameMode()
                }
            }

            lastNumber = ran

            gameState[0][Static.LEFT_BOTTOM] = Static.NO_EGG
            gameState[0][Static.LEFT_TOP] = Static.NO_EGG
            gameState[0][Static.RIGHT_TOP] = Static.NO_EGG
            gameState[0][Static.RIGHT_BOTTOM] = Static.NO_EGG
            gameState[0][ran] = Static.EGG
            noOfEggs -=1
        }

        // not generate egg
        else{
            gameState[0][Static.LEFT_BOTTOM] = Static.NO_EGG
            gameState[0][Static.LEFT_TOP] = Static.NO_EGG
            gameState[0][Static.RIGHT_TOP] = Static.NO_EGG
            gameState[0][Static.RIGHT_BOTTOM] = Static.NO_EGG
            distance -=1
        }

    }

}