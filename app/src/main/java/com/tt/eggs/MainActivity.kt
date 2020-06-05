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


class MainActivity : AppCompatActivity() {

    private var game = Game()
    private var basket = Static.RIGHT_TOP
    private val mHandler = Handler()
    private var eggCaught = CaughtEgg()
    private var gameLoop: Runnable = Runnable {
        game.moveDown()
        displayState()
        eggCaught = checkCatch()
        checkNextMove(eggCaught)
    }
    private var gameInProgress = false

    // if egg at last position check if it is in the basket
    private fun checkNextMove(eggCaught: CaughtEgg) {

        // egg in basket or no egg
        if(eggCaught.logicSum==1){

            // egg has been caught
            if(eggCaught.logicProduct==1){
                game.addPoint()
                updateScoreTextView()

                // clear faults when get 200 or 500 points
                if(game.getScore()==200||game.getScore()==500){
                    game.clearFaults()
                    updateFaultsView()
                }
            }

            mHandler.postDelayed(gameLoop,deelay())
        }

        // egg outside the basket
        else{
            // clear egg array
            game.clear()
            mHandler.removeCallbacks(gameLoop)
            game.addFault()
            updateFaultsView()
            if(game.getFault()<=Static.FAULT_TWO_AND_HALF){
                mHandler.postDelayed(gameLoop, deelay())
            }
            else{
                gameInProgress=false
                // clear whole game
                game.reset()

            }
        }
    }

    // check if egg is in the basket
    private fun checkCatch(): CaughtEgg {
        val eggCheck = CaughtEgg()
        for(i in 0..3){
            if(game.gameState[6][i]||game.position[i]){
                eggCheck.logicSum+=1
            }
        }
        for(i in 0..3){
            if(game.gameState[6][i]&&game.position[i]){
                eggCheck.logicProduct+=1
            }
        }
        return eggCheck
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
        mHandler.removeCallbacks(gameLoop)
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
        game.clear()
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
                gameInProgress=true
                updateFaultsView()
                game.setGameMode(Static.GAME_A)
                updateScoreTextView()
                gameLoop.run()}
        }
        start_B.setOnClickListener {
            if(!gameInProgress){
                gameInProgress=true
                updateFaultsView()
                game.setGameMode(Static.GAME_B)
                updateScoreTextView()
                gameLoop.run()}
        }
        updateScoreTextView()
    }

    // update score in text view
    private fun updateScoreTextView(){
        score.text = game.getScore().toString()
    }

    // display faults
    private fun updateFaultsView(){
        // clear views
        right_fault.setImageDrawable(null)
        middle_fault.setImageDrawable(null)
        left_fault.setImageDrawable(null)

        // set current no of faults
        if(game.getFault()>=Static.FAULT_HALF){
            right_fault.setImageDrawable(getDrawable(R.drawable.half_fault))
        }
        if(game.getFault()>=Static.FAULT_ONE){
            right_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
        }

        if(game.getFault()>=Static.FAULT_ONE_AND_HALF){
            middle_fault.setImageDrawable(getDrawable(R.drawable.half_fault))
        }
        if(game.getFault()>=Static.FAULT_TWO){
            middle_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
        }
        if(game.getFault()>=Static.FAULT_TWO_AND_HALF){
            left_fault.setImageDrawable(getDrawable(R.drawable.half_fault))
        }
        if(game.getFault()>=Static.FAULT_THREE){
            left_fault.setImageDrawable(getDrawable(R.drawable.full_fault))
        }
    }

    private fun deelay():Long = when(game.getScore()){
        in 0..100 -> 1000
        in 101..200 -> 900
        in 201..400 -> 800
        in 401..600 -> 700
        in 601..800 -> 600
        else -> 500
    }



    }




// TODO start_pause
// TODO add rabbit (if rabbit and fault - counts as a half)
// TODO add one egg to each side... should be 5 in total
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


