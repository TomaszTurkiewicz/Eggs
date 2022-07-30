package com.tt.eggs.classes

import kotlin.random.Random

class Game {

    // array representing eggs
        var gameState = Array(Static.GAME_SIZE) {BooleanArray(4)}

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

    private var step:Int

    private var stepDemo:Int

    // initialization
    init {
        for(x in 0 until Static.GAME_SIZE){
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

//        step=Static.GAME_SIZE-1
        step=0

        stepDemo=1
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

    fun setGeneratingEggStraightAway(){
        this.step=Static.GAME_SIZE-1
        this.distance=0
        this.noOfEggs=0
    }

    // add point when egg caught
    private fun addPoint(){
        score+=1
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

    // set egg array during win animation (every second egg is set)
    fun setWinEggArray() {
        for(x in 0 until Static.GAME_SIZE){
            for(y in 0..3){
                gameState[x][y]=if((x+y)%2==0) Static.NO_EGG else Static.EGG
            }
        }

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
    fun underMaxScore() = score< Static.MAX_POINTS

    // return game mode A or B
    fun getGameMode():Boolean{
        return gameMode
    }





    /*-----------------CLEARING SECTION------------------------*/
    // clearing score
    fun clearScore(){
        score=0
    }

    // clear egg array
    fun clearEggArray(){
        for(x in 0 until Static.GAME_SIZE){
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
    private fun generateEgg():Boolean{

        val egg = when(score){
            in 0..4 -> generateOneEgg()
            in 5..13 -> generateTwoEggs()
            else -> generateRandomNumberOfEggs()
        }
        return egg
    }

    // totally random
    private fun generateRandomNumberOfEggs():Boolean {
        // set counters again
        if(noOfEggs==0&&distance==0){
            val random = Random.nextInt(0,99)

            // GAME A - 1-5eggs, GAME B 5-9eggs
            val ranEggs = if(gameMode==Static.GAME_A) random%5+1 else random%5+5
            noOfEggs=ranEggs
            val ranDistance = random%2
            distance=ranDistance
        }
        return generateEggUsingCounters()

    }

    // two eggs
    private fun generateTwoEggs():Boolean {
        // set counters again
        if(noOfEggs==0&&distance==0){
            noOfEggs=2
            distance=3
        }

        return generateEggUsingCounters()

    }

    // one egg
    private fun generateOneEgg():Boolean {
        // set counters again
        if(noOfEggs==0&&distance==0){
            noOfEggs=1
            distance=3
        }

        return generateEggUsingCounters()

    }

    // generate egg function
    private fun generateEggUsingCounters():Boolean{
        var egg = false
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
            egg=true
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
        return egg
    }

    // generates eggs during demo
    private fun generateEggDemo() {
        val random = Random.nextInt(0, 99)
        val ran = random % 5
        gameState[0][Static.LEFT_BOTTOM] = Static.NO_EGG
        gameState[0][Static.LEFT_TOP] = Static.NO_EGG
        gameState[0][Static.RIGHT_TOP] = Static.NO_EGG
        gameState[0][Static.RIGHT_BOTTOM] = Static.NO_EGG
        if(ran!=4){
            gameState[0][ran]=Static.EGG
        }



    }


    /*---------------------GAME LOGIC---------------------*/



    fun moveDownStep():MoveProduct{
        val moveProduct = MoveProduct()
        when(step){
            0->{
                decreaseStep()
            }
//            0 ->{
//                //generate egg, make sound,decrease step
//                moveProduct.sound=generateEgg()
//                moveProduct.step=step
//                decreaseStep()
//            }
            Static.GAME_SIZE-1 ->{
                for(j in 0..3){
                    gameState[step][j]=gameState[step-1][j]
                    gameState[step-1][j] = Static.NO_EGG
                }
                moveProduct.step=step

//                moveProduct.sound = gameState[step][0]||gameState[step][1]||gameState[step][2]||gameState[step][3]
                moveProduct.sound=generateEgg()
                decreaseStep()
                // check egg on last position
                for(i in 0..3){
                    if(gameState[Static.GAME_SIZE-1][i]||position[i]){
                        moveProduct.logicSum+=1
                    }
                }
                for(i in 0..3){
                    if(gameState[Static.GAME_SIZE-1][i]&&position[i]){
                        moveProduct.logicProduct+=1
                    }
                }

                // check if egg outside basket
                if(moveProduct.logicSum==2){
                    for(i in 0..3){
                        if(gameState[Static.GAME_SIZE-1][i]){
                            moveProduct.positionFallenEgg=i
                        }
                    }
                    step=0
                }

                // no egg or egg in basket
                if(moveProduct.logicSum==1) {
                    // egg in basket add points
                    if(moveProduct.logicProduct==1){
                        addPoint()
                    }
                }
            }
            else ->{
                for(j in 0..3){
                    gameState[step][j]=gameState[step-1][j]
                    gameState[step-1][j] = Static.NO_EGG
                }
                moveProduct.step=step

                moveProduct.sound = gameState[step][0]||gameState[step][1]||gameState[step][2]||gameState[step][3]
                decreaseStep()
            }
        }
        return moveProduct
    }

    private fun decreaseStep(){
        if(step<=1){
            step=Static.GAME_SIZE-1
        }else{
            step-=1
        }
    }



    // reverse egg array
    fun eggArrayWinAnimation() {
        for(x in 0 until Static.GAME_SIZE){
            for(y in 0..3){
                gameState[x][y]= !gameState[x][y]
            }
        }
    }

    // move down plus move basket during demo
    fun moveDownDemo() {
        // move down
        for(i in Static.GAME_SIZE-2 downTo 0){
            for(j in 0..3){
                gameState[i+1][j]=gameState[i][j]
            }
        }
        // move basket
        for(i in 0..3){
            if(gameState[Static.GAME_SIZE-1][i])
                setBasket(i)
        }
        // generate egg
        generateEggDemo()
    }

    fun moveDownDemoStep(){
        when(stepDemo){
            Static.GAME_SIZE-1 ->{

                generateEggDemo()
                // move basket
                for(i in 0..3){
                    if(gameState[stepDemo-1][i])
                        setBasket(i)
                }
                for(i in 0..3){
                    gameState[stepDemo-1][i]=Static.NO_EGG
                }

                decreaseStepDemo()
            }
            else ->{
                for(i in 0..3){
                    gameState[stepDemo][i]=gameState[stepDemo-1][i]
                    gameState[stepDemo-1][i]=Static.NO_EGG
                }
                decreaseStepDemo()
            }
        }
    }

    private fun decreaseStepDemo(){
        if(stepDemo<=1){
            stepDemo=Static.GAME_SIZE-1
        }else{
            stepDemo-=1
        }
    }

}