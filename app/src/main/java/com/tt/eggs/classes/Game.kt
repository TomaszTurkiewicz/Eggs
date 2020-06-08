package com.tt.eggs.classes

import kotlin.random.Random

class Game {

    // array representing eggs
        var gameState = Array(7) {BooleanArray(4)}

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
        for(x in 0..6){
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

        distance=0

        noOfEggs=0
    }


    /*------------------SETTERS AND ADDERS---------------------------*/

    // set basket at one in four positions
    fun setBasket(i:Int){
        for(arg in 0..3){
            position[arg]=Static.NO_BASKET
        }
        position[i]=Static.BASKET
    }

    // set game mode
    fun setGameMode(mode:Boolean){
        gameMode=mode
    }

    // add point when egg caught
    private fun addPoint(){
        score+=100
    }

    // add fault when egg not caught
    fun addFault(rabbitBoolean: Boolean){
        faults += if(rabbitBoolean) 1 else 2
    }

    fun setPoints( tPoints:Int){
        score=tPoints
    }

    fun setFaults(tFaults:Int){
        faults=tFaults
    }



    /*-------------------GETTERS AND CONDITIONS FUNCTIONS---------------------------------*/

    // return one position in egg array
    fun displayCell(x:Int, y:Int):Boolean{
        return gameState[x][y]
    }

    // return current score
    fun getScore():Int{
        return score
    }

    // return current faults
    fun getFault():Int{
        return faults
    }

    // return integer 3 or 4 (game mode)
    private fun gameMode() = if(gameMode==Static.GAME_A) 3 else 4

    // return boolean if under max score
    fun underMaxScore() = score<1000

    // return game mode A or B
    fun getGameMode():Boolean{
        return gameMode
    }





    /*-----------------CLEARING SECTION------------------------*/
    // clearing score
    private fun clearScore(){
        score=0
    }

    // clear egg array
    fun clearEggArray(){
        for(x in 0..6){
            for(y in 0..3){
                gameState[x][y]=Static.NO_EGG
            }
        }
    }

    // clear distance and egg
    fun clearDistanceAndNoOfEggs(){
        distance=0
        noOfEggs=0
    }

    //clear basket position
    private fun clearBasketPosition(){
        for(i in 0..3){
            position[i]=Static.NO_BASKET
        }
        position[Static.RIGHT_TOP]=Static.BASKET

    }

    // clear faults
    fun clearFaults() {
        faults=Static.FAULT_NO_FAULT
    }

    // clear everything
    fun clearEverything(){

        //clear eggs
        clearEggArray()

        // clear basket position
        clearBasketPosition()

        // clear score and faults
        lastNumber=0
        clearScore()
        clearFaults()
        clearDistanceAndNoOfEggs()
    }



    /*------------------------GENERATING EGGS-----------------------*/


    // generate next egg or eggs
    private fun generateEgg(){
        when(score){
            in 0..4 -> generateOneEgg()
            in 5..13 -> generateTwoEggs()
            else -> generateRandomNumberOfEggs()
        }
    }

    // totally random
    private fun generateRandomNumberOfEggs() {
        // set counters again
        if(noOfEggs==0&&distance==0){
            val random = Random.nextInt(0,99)

            // GAME A - 1-5eggs, GAME B 5-9eggs
            val ranEggs = if(gameMode==Static.GAME_A) random%5+1 else random%5+5
            noOfEggs=ranEggs
            val ranDistance = random%2
            distance=ranDistance
        }
        generateEggUsingCounters()

    }

    // two eggs
    private fun generateTwoEggs() {
        // set counters again
        if(noOfEggs==0&&distance==0){
            noOfEggs=2
            distance=4
        }

        generateEggUsingCounters()

    }

    // one egg
    private fun generateOneEgg() {
        // set counters again
        if(noOfEggs==0&&distance==0){
            noOfEggs=1
            distance=5
        }

        generateEggUsingCounters()

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
            // TODO for testing speed
            gameState[0][Static.LEFT_TOP] = Static.EGG
      //      gameState[0][ran] = Static.EGG
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



    /*---------------------GAME LOGIC---------------------*/

    // egg array move down
    fun moveDown():CaughtEgg{

        // move down
        for(i in 5 downTo 0){
            for(j in 0..3){
                gameState[i+1][j]=gameState[i][j]
            }
        }

        // check egg on last position
        val eggCheck = CaughtEgg()
        for(i in 0..3){
            if(gameState[6][i]||position[i]){
                eggCheck.logicSum+=1
            }
        }
        for(i in 0..3){
            if(gameState[6][i]&&position[i]){
                eggCheck.logicProduct+=1
            }
        }

        // check if egg outside basket
        if(eggCheck.logicSum==2){
            for(i in 0..3){
                if(gameState[6][i]){
                    eggCheck.positionFallenEgg=i
                }
            }
        }

        // no egg or egg in basket
        if(eggCheck.logicSum==1) {

            // egg in basket add points
            if(eggCheck.logicProduct==1){
                addPoint()
            }
            // generate new egg at random position
            generateEgg()
        }
        // return egg to check conditions in main activity
        return eggCheck
    }

}