package com.tt.eggs


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.tt.eggs.classes.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {


    /**--------------------------- var and val-----------------------------**/
    // game state
    private var gameState = Static.DEMO

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

    // for win loop
    private var winLoopCounter = 0
    private val mHandlerWin = Handler()
    private var display = Static.ON

    // for game loop
    private val mHandler = Handler()

    // for rabbit loop
    private val mHandlerRabbit = Handler()

    // for faults loop
    private val mHandlerFlash = Handler()
    private var faultFlash = Static.ON

    // for lost egg loops
    private val mHandlerLostEgg = Handler()

    // for demo loop
    private var loopCounter=0
    private val mHandlerDemo = Handler()



    /**---------------------- activity life cycle methods---------------------------**/

    // on create
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

    // check if game hasn't been finished
    override fun onResume() {
        super.onResume()
        // check if there is stored game
        checkGameState()
    }

    // stop game loop when activity is disrupted by anything else (another app)
    override fun onPause() {
        super.onPause()
        when(gameState){
            // if game is playing pause it
            Static.PLAY_A -> pauseGameA()
            Static.PLAY_B -> pauseGameB()
        }
    }


    /**------------------------ runnable -------------------------------------------**/


    // game loop
    private fun gameLoop(): Runnable = Runnable {
        // less then 1000 points
        if(game.underMaxScore()) {
            // make move down
            eggCaught = game.moveDown()
            // display move
            displayState()
            //check fault, egg in basket
            checkNextMove(eggCaught)
        }
        else{


            mHandlerFlash.removeCallbacksAndMessages(null)
            mHandlerRabbit.removeCallbacksAndMessages(null)
            when(gameState){
                Static.PLAY_A -> winA()
                Static.PLAY_B -> winB()
            }
            game.clearEggArray()
            game.setWinEggArray()
            score.text=Static.MAX_POINTS.toString()
            winLoopCounter=0
            winLoop().run()



        }
    }

    // win loop
    private fun winLoop(): Runnable = Runnable {
        score.visibility=if(display) View.VISIBLE else View.GONE
        display = !display
        game.eggArrayWinAnimation()
        displayState()
        winLoopCounter+=1
        if(winLoopCounter<10){
            mHandlerWin.postDelayed(winLoop(),500)
        }else{
            mHandlerWin.removeCallbacks(winLoop())
            score.visibility=View.VISIBLE
            demoMode()
        }

    }

    // displaying rabbit
    private fun rabbitShow():Runnable = Runnable {
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

    // flashing fault
    private fun flashFault(imageView: ImageView):Runnable = Runnable {
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

    // fallen egg runnable
    private fun fallenEgg(fallenEgg: FallenEgg):Runnable = Runnable {
        val finished = fallenEgg.moveDown()
        displayRunningChicken(fallenEgg)
        if(!finished){
            mHandlerLostEgg.postDelayed(fallenEgg(fallenEgg),1000)
        }
        else{
            mHandlerLostEgg.removeCallbacksAndMessages(null)
            mHandler.postDelayed(gameLoop(),delay())
        }
    }
    private fun fallenEggEndGame(fallenEgg: FallenEgg): Runnable = Runnable{
        val finished = fallenEgg.moveDown()
        displayRunningChicken(fallenEgg)
        if(!finished){
            mHandlerLostEgg.postDelayed(fallenEggEndGame(fallenEgg),1000)
        }
        else{
            mHandlerLostEgg.removeCallbacksAndMessages(null)
            mHandlerRabbit.removeCallbacksAndMessages(null)
            mHandlerFlash.removeCallbacksAndMessages(null)

            demoMode()

        }

    }

    // demo runnable
    private fun demo():Runnable = Runnable {
        game.moveDownDemo()
        displayState()
        displayDemoBasket()
        displayRabbit(loopCounter%10>4)
        loopCounter+=1
        mHandlerDemo.postDelayed(demo(),600)
    }


    /**----------------------- read and write to shared preferences -------------------**/

    // save points game A after lose
    private fun savePointsLoseA() {
        val sharedPreferences = getSharedPreferences("GAME_A", Context.MODE_PRIVATE)
        var tPoints = sharedPreferences.getInt("TOTAL", 0)
        var tHigh = sharedPreferences.getInt("HIGH", 0)

        tPoints += game.getScore()

        if(tHigh<game.getScore()){
            tHigh=game.getScore()
        }

        val editor = sharedPreferences.edit()
        editor.putInt("TOTAL",tPoints)
        editor.putInt("HIGH",tHigh)

        editor.apply()

    }

    // save points game B after lose
    private fun savePointsLoseB() {
        val sharedPreferences = getSharedPreferences("GAME_B", Context.MODE_PRIVATE)
        var tPoints = sharedPreferences.getInt("TOTAL", 0)
        var tHigh = sharedPreferences.getInt("HIGH", 0)
        tPoints += game.getScore()
        if(tHigh<game.getScore()){
            tHigh=game.getScore()
        }
        val editor = sharedPreferences.edit()
        editor.putInt("TOTAL",tPoints)
        editor.putInt("HIGH",tHigh)
        editor.apply()
    }

    // save points game A after win (1000)
    private fun savePointsWinA() {
        val sharedPreferences = getSharedPreferences("GAME_A", Context.MODE_PRIVATE)
        var tPoints = sharedPreferences.getInt("TOTAL", 0)
        var tHigh = sharedPreferences.getInt("HIGH", 0)
        var tHighCounter = sharedPreferences.getInt("COUNTER", 0)
        tPoints += game.getScore()
        if(tHigh!=1000){
            tHigh=1000
        }
        tHighCounter+=1
        val editor = sharedPreferences.edit()
        editor.putInt("TOTAL",tPoints)
        editor.putInt("HIGH",tHigh)
        editor.putInt("COUNTER",tHighCounter)
        editor.apply()
    }

    // save points game B after win (1000)
    private fun savePointsWinB() {
        val sharedPreferences = getSharedPreferences("GAME_B", Context.MODE_PRIVATE)
        var tPoints = sharedPreferences.getInt("TOTAL", 0)
        var tHigh = sharedPreferences.getInt("HIGH", 0)
        var tHighCounter = sharedPreferences.getInt("COUNTER", 0)
        tPoints += game.getScore()
        if(tHigh!=1000){
            tHigh=1000
        }
        tHighCounter+=1
        val editor = sharedPreferences.edit()
        editor.putInt("TOTAL",tPoints)
        editor.putInt("HIGH",tHigh)
        editor.putInt("COUNTER",tHighCounter)
        editor.apply()
    }

    // clear game state in shared preferences
    private fun clearSavedGame() {
        val sharedPreferences = getSharedPreferences("GAME_STATE", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("POINTS",0)
        editor.putInt("FAULTS",0)
        editor.putBoolean("GAME_MODE",false)
        editor.apply()

    }

    // store points, faults and game mode to shared preferences
    private fun saveGameState() {
        val sharedPreferences = getSharedPreferences("GAME_STATE", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("POINTS",game.getScore())
        editor.putInt("FAULTS",game.getFault())
        editor.putBoolean("GAME_MODE",game.getGameMode())
        editor.apply()
    }

    // check if there is stored game
    private fun checkGameState() {
        // read from shared preferences
        val sharedPreferences = getSharedPreferences("GAME_STATE", Context.MODE_PRIVATE)
        val tPoints = sharedPreferences.getInt("POINTS", 0)
        val tFaults = sharedPreferences.getInt("FAULTS", 0)
        val tGame = sharedPreferences.getBoolean("GAME_MODE", false)

        // points or faults are larger than 0
        if(tPoints>0||tFaults>0){

            //set points, faults and game mode
            game.setPoints(tPoints)
            game.setFaults(tFaults)
            game.setGameMode(if(tGame==Static.GAME_A) Static.GAME_A else Static.GAME_B)
            when(tGame){
                // activate proper pause mode
                Static.GAME_A -> pauseGameA()
                Static.GAME_B -> pauseGameB()
            }
        }
        else demoMode()
    }


    /**-------------------------- displaying functions -------------------------------**/

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

    // display rabbit
    private fun displayRabbit(rabbitBoolean: Boolean){
       if(rabbitBoolean){
           rabbit.setImageDrawable(getDrawable(R.drawable.rabbit))
       }
       else{
           rabbit.setImageDrawable(null)
       }
   }


    // display running chicken during animation
    private fun displayRunningChicken(fallenEgg: FallenEgg) {
        faultLeftFirst.setImageDrawable(if(fallenEgg.getFallenEgg(1,0))getDrawable(R.drawable.full_fault)else null)
        faultLeftSecond.setImageDrawable(if(fallenEgg.getFallenEgg(2,0))getDrawable(R.drawable.full_fault)else null)
        faultLeftThird.setImageDrawable(if(fallenEgg.getFallenEgg(3,0))getDrawable(R.drawable.full_fault)else null)


        faultRightFirst.setImageDrawable(if(fallenEgg.getFallenEgg(1,1))getDrawable(R.drawable.full_fault)else null)
        faultRightSecond.setImageDrawable(if(fallenEgg.getFallenEgg(2,1))getDrawable(R.drawable.full_fault)else null)
        faultRightThird.setImageDrawable(if(fallenEgg.getFallenEgg(3,1))getDrawable(R.drawable.full_fault)else null)


    }

    // display demo basket
    private fun displayDemoBasket() {
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

    // first displaying array (empty) only in onCreate
    private fun updateArray(){
        game.clearEverything()
        displayState()
    }


    /** --------------------- buttons listeners -------------------------------**/

    // set click listeners for all buttons
    private fun buttonsOnClickListeners(){
        buttonTopLeft.setOnClickListener {
            if(playOrPause()) {
                basket = Static.LEFT_TOP
                displayBasket()
            }
        }
        buttonBottomLeft.setOnClickListener {
            if(playOrPause()){
                basket=Static.LEFT_BOTTOM
                displayBasket()
            }
        }
        buttonBottomRight.setOnClickListener {
            if(playOrPause()) {
                basket = Static.RIGHT_BOTTOM
                displayBasket()
            }
        }
        buttonTopRight.setOnClickListener {
            if(playOrPause()){
                basket=Static.RIGHT_TOP
                displayBasket()
            }
        }

        start_A.setOnClickListener {
            when (gameState) {
                Static.DEMO -> {
                    mHandlerDemo.removeCallbacksAndMessages(null)
                    startGameA()
                }
                Static.PLAY_A -> {
                    pauseGameA()
                }
                Static.PAUSE_A -> {
                    unPauseGameA()
                }
                else -> {/* do nothing*/}
            }
        }
        start_B.setOnClickListener {
            when (gameState) {
                Static.DEMO -> {
                    mHandlerDemo.removeCallbacksAndMessages(null)
                    startGameB()
                }
                Static.PLAY_B -> {
                    pauseGameB()
                }
                Static.PAUSE_B -> {
                    unPauseGameB()
                }
                else -> {/* do nothing*/}
            }
        }
 //       updateScoreTextView()
        closeApp.setOnClickListener {
            finish()
        }
        account.setOnClickListener {
            if(gameState==Static.DEMO){
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


    }


    /**------------------------ game state logic and functions ----------------**/

    // demo mode
    private fun demoMode() {
        gameState=Static.DEMO
        game.clearEverything()
        displayState()
        displayBasket()
        updateFaultsView()
        score.text=getString(R.string.demo)
        loopCounter = 0
        demo().run()

    }

    // play game A
    private fun startGameA() {
        gameState=Static.PLAY_A
        game.clearEggArray()
        game.clearDistanceAndNoOfEggs()
        game.setGameMode(Static.GAME_A)
        updateFaultsView()
        updateScoreTextView()
        displayBasket()
        displayState()
        gameLoop().run()
        rabbitShow().run()
    }

    // play game B
    private fun startGameB() {
        gameState=Static.PLAY_B
        game.clearEggArray()
        game.clearDistanceAndNoOfEggs()
        game.setGameMode(Static.GAME_B)
        updateFaultsView()
        updateScoreTextView()
        displayBasket()
        displayState()
        gameLoop().run()
        rabbitShow().run()

    }

    // pause game A
    private fun pauseGameA() {
        gameState=Static.PAUSE_A
        updateFaultsView()
        updateScoreTextView()
        // stop all runnable
        mHandlerRabbit.removeCallbacksAndMessages(null)
        mHandler.removeCallbacksAndMessages(null)
        mHandlerFlash.removeCallbacksAndMessages(null)
        // save game state to shared preferences
        saveGameState()
    }


    private fun unPauseGameA() {
        startGameA()
    }


    private fun pauseGameB() {
        gameState=Static.PAUSE_B
        updateFaultsView()
        updateScoreTextView()
        mHandlerRabbit.removeCallbacksAndMessages(null)
        mHandler.removeCallbacksAndMessages(null)
        mHandlerFlash.removeCallbacksAndMessages(null)
        saveGameState()
    }


    private fun unPauseGameB() {
        startGameB()
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
            if(game.getFault()<=Static.FAULT_TWO_AND_HALF) {
                lostEggAnimation(eggCaught.positionFallenEgg)
            }else{
                lostEggAnimationEndGame(eggCaught.positionFallenEgg)

            }
        }
    }

    // game A has finished because of 3 faults
    private fun loseA() {
        savePointsLoseA()
        game.clearEverything()
        clearSavedGame()
        gameState=Static.LOSE_A

    }

    // game B has finished because of 3 faults
    private fun loseB() {

        savePointsLoseB()
        clearSavedGame()
        gameState=Static.LOSE_B
        game.clearEverything()
    }

    // game A has finished because of 1000 points
    private fun winA() {

        savePointsWinA()
        clearSavedGame()
        gameState=Static.WIN_A
        game.clearFaults()
        game.clearScore()
        game.clearDistanceAndNoOfEggs()

    }

    // game B has finished because of 1000 points
    private fun winB() {

        savePointsWinB()
        clearSavedGame()
        gameState= Static.WIN_B
        game.clearFaults()
        game.clearScore()
        game.clearDistanceAndNoOfEggs()

    }

    // return boolean if game is played or paused
    private fun playOrPause(): Boolean {
        return gameState==Static.PLAY_A||gameState==Static.PAUSE_A||gameState==Static.PLAY_B||gameState==Static.PAUSE_B

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

    // lost egg animation end game
    private fun lostEggAnimationEndGame(positionFallenEgg: Int) {
        val fallenEgg = FallenEgg()
        when(gameState){
            Static.PLAY_A -> loseA()
            Static.PLAY_B -> loseB()
        }
        fallenEgg.setFallenEgg(positionFallenEgg/2)
        fallenEggEndGame(fallenEgg).run()

    }

    // lost egg animation
    private fun lostEggAnimation(positionFallenEgg: Int) {

        val fallenEgg = FallenEgg()
        fallenEgg.setFallenEgg(positionFallenEgg/2)
        fallenEgg(fallenEgg).run()


    }

    }


/*
TODO when fallen egg animation pause doesn't work!!!
TODO firebase - auth, save data, store picture
TODO new record displayed in score
TODO add sounds
TODO login
TODO user page with achievements
TODO change UI
TODO add ranking (individual highest points, points in total)
TODO add admob after gameover or 1000 points
*/


