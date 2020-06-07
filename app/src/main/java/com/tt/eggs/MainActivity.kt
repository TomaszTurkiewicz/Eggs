package com.tt.eggs


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.tt.eggs.classes.CaughtEgg
import com.tt.eggs.classes.Game
import com.tt.eggs.classes.Static
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private var gameLoopCounterInt=0
    private var faultLoopCounterInt=0
    private var rabbitLoopCounterInt=0



    // rabbit state
    private var rabbitBoolean = Static.OFF

    // counter for rabbit show
    private var rabbitOn = 0

    // counter for rabbit not show
    private var rabbitOff = 0

    // game
    private var game = Game()

    // basket position
    private var basket = Static.RIGHT_TOP

    // egg sum and product
    private var eggCaught = CaughtEgg()

    // game loop
    private val mHandler = Handler()
    private fun gameLoop(): Runnable = Runnable {
        // less then 1000 points
        if(game.underMaxScore()) {
            gameLoopCounterInt+=1
            // make move down
            eggCaught = game.moveDown()
            // display move
            displayState()
            //check fault, egg in basket
            checkNextMove(eggCaught)
        }
        else{
            // TODO winning animation
            // TODO save points in total
            // TODO add counter of high score (A or B)
            mHandlerFlash.removeCallbacksAndMessages(null)
            mHandlerRabbit.removeCallbacksAndMessages(null)
            mHandlerTotal.removeCallbacksAndMessages(null)
            game.clearEverything()
            gameInProgress=false
            gameStatus.text=if(game.getGameMode()==Static.GAME_A)"WIN A" else "WIN B"


        }
    }

    // if game is in progress
    private var gameInProgress = false

    // displaying rabbit
    private val mHandlerRabbit = Handler()
    private fun rabbitShow():Runnable = Runnable {
            rabbitLoopCounterInt+=1
                if(rabbitOn>0){
                    rabbitBoolean=Static.ON
                    displayRabbit(rabbitBoolean)
                    rabbitOn -=1
                    mHandlerRabbit.postDelayed(rabbitShow(),1000)
                }
                else if(rabbitOff>0){
                    rabbitBoolean=Static.OFF
                    displayRabbit(rabbitBoolean)
                    rabbitOff -=1
                    mHandlerRabbit.postDelayed(rabbitShow(),1000)

                }else{
                    val random = Random.nextInt(0,99)
                    rabbitOn=random%3+2
                    val random1 = Random.nextInt(0,99)
                    rabbitOff=random1%3+8
                    mHandlerRabbit.postDelayed(rabbitShow(),1000)
                }
        }
    private fun displayRabbit(rabbitBoolean: Boolean){
        if(rabbitBoolean){
            rabbit.setImageDrawable(getDrawable(R.drawable.rabbit))
        }
        else{
            rabbit.setImageDrawable(null)
        }
    }

    // display testing info
    private val mHandlerTotal = Handler()
    private fun showCounter():Runnable = Runnable {
        gameLoopCounter.text = gameLoopCounterInt.toString()
        faultLoopCounter.text = faultLoopCounterInt.toString()
        rabbitLoopCounter.text = rabbitLoopCounterInt.toString()
        speed.text = delay().toString()
        mHandlerTotal.postDelayed(showCounter(),100)
    }


    // flashing fault
    private val mHandlerFlash = Handler()
    private var faultFlash = Static.ON

    private fun flashFault(imageView: ImageView):Runnable = Runnable {
        faultLoopCounterInt +=1
        if(faultFlash==Static.ON){
            imageView.setImageDrawable(getDrawable(R.drawable.full_fault))
            faultFlash=Static.OFF
            mHandlerFlash.postDelayed(flashFault(imageView),500)
        }else{
            imageView.setImageDrawable(null)
            faultFlash=Static.ON
            mHandlerFlash.postDelayed(flashFault(imageView),500)
        }
    }

    // if egg at last position check if it is in the basket
    private fun checkNextMove(eggCaught: CaughtEgg) {

        // egg in basket or no egg
        if(eggCaught.logicSum==1){

            // egg has been caught
            if(eggCaught.logicProduct==1){
                updateScoreTextView()

                // clear faults when get 200 or 500 points
                if(game.getScore()==200||game.getScore()==500){
                    game.clearFaults()
                    updateFaultsView()
                }
            }

            mHandler.postDelayed(gameLoop(),delay())
        }

        // egg outside the basket
        else{
            // clear egg array
            game.clearEggArray()
            mHandler.removeCallbacks(gameLoop())
            game.addFault(rabbitBoolean)
            updateFaultsView()
            if(game.getFault()<=Static.FAULT_TWO_AND_HALF){
                game.clearDistanceAndNoOfEggs()
                // TODO animation when fault
                mHandler.postDelayed(gameLoop(), delay())
            }
            else{
                gameInProgress=false
                // clear whole game
                mHandlerRabbit.removeCallbacksAndMessages(null)
                game.clearEverything()
                gameStatus.text=if(game.getGameMode()==Static.GAME_A)"LOSE A" else "LOSE B"
                // TODO animation when lose
                // TODO save points in total
                // TODO check if new high score

            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // make full screen
        fullScreen()
        setContentView(R.layout.activity_main)

        // update eggs positions
        updateArray()

        // update basket position
        displayBasket()

        // set button listeners and text view displays
        buttonsOnClickListeners()


    }

    // stop game loop when activity is disrupted by anything else (another app)
    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacksAndMessages(null)
        mHandlerFlash.removeCallbacksAndMessages(null)
        mHandlerRabbit.removeCallbacksAndMessages(null)
    }

    // full screen
    private fun fullScreen(){
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        val decorView:View = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if(visibility and View.SYSTEM_UI_FLAG_FULLSCREEN==0){
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            }
        }
    }

    // display all fallen eggs
    private fun displayState(){
        displayEgg(eggTopLeftFirst,1,Static.LEFT_TOP)
        displayEgg(eggTopLeftSecond,2,Static.LEFT_TOP)
        displayEgg(eggTopLeftThird,3,Static.LEFT_TOP)
        displayEgg(eggTopLeftFourth,4,Static.LEFT_TOP)
        displayEgg(eggTopLeftFifth,5,Static.LEFT_TOP)

        displayEgg(eggBottomLeftFirst,1,Static.LEFT_BOTTOM)
        displayEgg(eggBottomLeftSecond,2,Static.LEFT_BOTTOM)
        displayEgg(eggBottomLeftThird,3,Static.LEFT_BOTTOM)
        displayEgg(eggBottomLeftFourth,4,Static.LEFT_BOTTOM)
        displayEgg(eggBottomLeftFifth,5,Static.LEFT_BOTTOM)

        displayEgg(eggBottomRightFirst,1,Static.RIGHT_BOTTOM)
        displayEgg(eggBottomRightSecond,2,Static.RIGHT_BOTTOM)
        displayEgg(eggBottomRightThird,3,Static.RIGHT_BOTTOM)
        displayEgg(eggBottomRightFourth,4,Static.RIGHT_BOTTOM)
        displayEgg(eggBottomRightFifth,5,Static.RIGHT_BOTTOM)

        displayEgg(eggTopRightFirst,1,Static.RIGHT_TOP)
        displayEgg(eggTopRightSecond,2,Static.RIGHT_TOP)
        displayEgg(eggTopRightThird,3,Static.RIGHT_TOP)
        displayEgg(eggTopRightFourth,4,Static.RIGHT_TOP)
        displayEgg(eggTopRightFifth,5,Static.RIGHT_TOP)

    }

    // display single egg
    private fun displayEgg( imageView: ImageView, x:Int, y:Int){
        if(game.displayCell(x,y)){
            imageView.setImageDrawable(getDrawable(R.drawable.button))
        }
        else{
            imageView.setImageDrawable(null)
        }
    }

    // display position of basket
    private fun displayBasket(){
        game.setBasket(basket)
        if(game.position[Static.LEFT_TOP]){
            basketTopLeft.setImageDrawable(getDrawable(R.drawable.basket))
        }else{
            basketTopLeft.setImageDrawable(null)
        }
        if(game.position[Static.LEFT_BOTTOM]){
            basketBottomLeft.setImageDrawable(getDrawable(R.drawable.basket))
        }else{
            basketBottomLeft.setImageDrawable(null)
        }
        if(game.position[Static.RIGHT_BOTTOM]){
            basketBottomRight.setImageDrawable(getDrawable(R.drawable.basket))
        }else{
            basketBottomRight.setImageDrawable(null)
        }
        if(game.position[Static.RIGHT_TOP]){
            basketTopRight.setImageDrawable(getDrawable(R.drawable.basket))
        }else{
            basketTopRight.setImageDrawable(null)
        }
    }

    // first displaying array (empty)
    private fun updateArray(){
        game.clearEverything()
        displayState()

    }

    // set click listeners for all buttons
    private fun buttonsOnClickListeners(){
        buttonTopLeft.setOnClickListener {
            basket=Static.LEFT_TOP
            displayBasket()

        }
        buttonBottomLeft.setOnClickListener {
            basket=Static.LEFT_BOTTOM
            displayBasket()
        }
        buttonBottomRight.setOnClickListener {
            basket=Static.RIGHT_BOTTOM
            displayBasket()
        }
        buttonTopRight.setOnClickListener {
            basket=Static.RIGHT_TOP
            displayBasket()
        }

        start_A.setOnClickListener {
            if(!gameInProgress){
                // check if paused (points or faults more than 0)

                if(!((game.getScore()>0||game.getFault()>0)&&game.getGameMode()==Static.GAME_B)){
                gameInProgress=true
                game.setGameMode(Static.GAME_A)
                    game.clearEggArray()
                    game.clearDistanceAndNoOfEggs()
                updateFaultsView()
                updateScoreTextView()
                displayBasket()
                displayState()
                gameLoop().run()
            rabbitShow().run()
            showCounter().run()
                    gameStatus.text="PLAY A"
                }
            }
            else{
                if(game.getGameMode()==Static.GAME_A){
                    gameInProgress=false
                    mHandlerRabbit.removeCallbacksAndMessages(null)
                    mHandler.removeCallbacksAndMessages(null)
                    mHandlerFlash.removeCallbacksAndMessages(null)
                    gameStatus.text="PAUSE A"
                }
            }
        }
        start_B.setOnClickListener {
            if(!gameInProgress){
                // check if paused (points or faults more than 0)
                if(!((game.getScore()>0||game.getFault()>0)&&game.getGameMode()==Static.GAME_A)){
                    gameInProgress=true
                    game.setGameMode(Static.GAME_B)
                    game.clearEggArray()
                    game.clearDistanceAndNoOfEggs()
                    updateFaultsView()
                    updateScoreTextView()
                    displayBasket()
                    displayState()
                    gameLoop().run()
                    rabbitShow().run()
                    showCounter().run()
                    gameStatus.text="PLAY B"
                }}
            else{
                if(game.getGameMode()==Static.GAME_B){
                    gameInProgress=false
                    mHandlerRabbit.removeCallbacksAndMessages(null)
                    mHandler.removeCallbacksAndMessages(null)
                    mHandlerFlash.removeCallbacksAndMessages(null)
                    gameStatus.text="PAUSE B"
                }
            }
        }
        updateScoreTextView()

    }

    // update score in text view
    private fun updateScoreTextView(){
        score.text = game.getScore().toString()
    }

    // display faults
    private fun updateFaultsView(){

        when(game.getFault()){
            Static.FAULT_NO_FAULT->zeroFault()
            Static.FAULT_HALF->oneFault()
            Static.FAULT_ONE->twoFault()
            Static.FAULT_ONE_AND_HALF->threeFault()
            Static.FAULT_TWO->fourFault()
            Static.FAULT_TWO_AND_HALF->fiveFault()
            else->sixFault()
        }
    }

    // set delay in game loop
    private fun delay():Long = when(game.getScore()){
        in 0..100 -> 1000
        in 101..200 -> 900
        in 201..300 -> if(game.getGameMode()==Static.GAME_A) 850 else 800
        in 301..400 -> if(game.getGameMode()==Static.GAME_A) 800 else 750
        in 401..500 -> if(game.getGameMode()==Static.GAME_A) 750 else 700
        in 501..600 -> if(game.getGameMode()==Static.GAME_A) 700 else 650
        in 601..700 -> if(game.getGameMode()==Static.GAME_A) 650 else 600
        in 701..800 -> if(game.getGameMode()==Static.GAME_A) 600 else 550
        in 801..900 -> if(game.getGameMode()==Static.GAME_A) 550 else 500
        else -> if(game.getGameMode()==Static.GAME_A) 500 else 450
    }

    private fun zeroFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        right_fault.setImageDrawable(null)
        middle_fault.setImageDrawable(null)
        left_fault.setImageDrawable(null)
    }

    private fun oneFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        flashFault(right_fault).run()
        left_fault.setImageDrawable(null)
        middle_fault.setImageDrawable(null)

    }

    private fun twoFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        left_fault.setImageDrawable(null)
        middle_fault.setImageDrawable(null)
        right_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
    }
    private fun threeFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        flashFault(middle_fault).run()
        left_fault.setImageDrawable(null)
        right_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
    }
    private fun fourFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        left_fault.setImageDrawable(null)
        middle_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
        right_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
    }
    private fun fiveFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        flashFault(left_fault).run()
        middle_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
        right_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
    }
    private fun sixFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        left_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
        middle_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
        right_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
    }




    }




// TODO add sounds
// TODO login
// TODO add running chicken when fault
// TODO win when points 1000
// TODO save points (highest score (if 1000 - how many times), score in total)
// TODO change UI
// TODO add ranking (individual highest points, points in total)
// TODO onStop remove callback from game loop and save state to storage
// TODO restarting game with count down from 3 to prepare user
// TODO add admob after gameover or 1000 points


