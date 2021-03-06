package com.tt.eggs


import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.eggs.classes.*
import com.tt.eggs.drawable.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.random.Random


class MainActivity : AppCompatActivity(),UpdateHelper.OnUpdateNeededListener{


    /**--------------------------- var and val-----------------------------**/
    // loggedInState
    var loggedInStatus = LoggedInStatus()

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

    // for pause loop
    private var pauseState = Static.ON
    private val mHandlerPause = Handler()

    // for update loop
    private var updateState = Static.ON
    private val mHandlerUpdate = Handler()

    // for high score loop
    private var highScoreState = Static.ON
    private val mHandlerHighScore = Handler()

    private lateinit var mInterstitialAd: InterstitialAd

    private var screenHeight = 0
    private var screenWidth = 0
    private var screenUnit = 0

    private val screenSize = Dimension()
    private val eggSize = Dimension()
    private val arrowSize = Dimension()
    private val digitSize = Dimension()
    private val startButtonSize = Dimension()
    private val userIdSize = Dimension()
    private val rabbitSize = Dimension()
    private val faultSize = Dimension()
    private val bottomFaultSizeSmall = Dimension()
    private val bottomFaultSizeSmallDifferent = Dimension()
    private val bottomFaultSizeFirst = Dimension()
    private val wolfSize = Dimension()

    private var runningEggFirstSound: MediaPlayer?=null
    private var runningEggSecondSound: MediaPlayer?=null
    private var caughtEggSound: MediaPlayer?=null
    private var faultSound: MediaPlayer?=null
    private var brokenEggSound: MediaPlayer?=null
    private var runningChickenSound: MediaPlayer?=null
    private var winningSound: MediaPlayer?=null
    private var runningEggSoundBoolean = true

    private var chickenPlace = 0

    /**---------------------- activity life cycle methods---------------------------**/

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // make full screen
        fullScreen()
        setContentView(R.layout.activity_main)

//        val intent = Intent(this,Test::class.java)
//        startActivity(intent)

        // makeUI
        makeUI()

        // update eggs positions
        updateArray()

        // update basket position
        displayBasket()

        // check if loggedIn
        checkLoggedInState()

        // set button listeners and text view displays
        buttonsOnClickListeners()


        Functions.saveUpdateToSharedPreferences(context = this,isUpdate = false)

        UpdateHelper.with(this).onUpdateNeeded(this).check()




    }


    private fun checkLoggedInState() {
        loggedInStatus = Functions.readLoggedInStatusFromSharedPreferences(this)

    }

    // check if game hasn't been finished
    override fun onResume() {
        super.onResume()
        // check if there is stored game
        checkGameState()
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.admob_big)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    // stop game loop when activity is disrupted by anything else (another app)
    override fun onPause() {
        super.onPause()
        when(gameState){
            // if game is playing pause it
            Static.PLAY_A -> pauseGameA()
            Static.PLAY_B -> pauseGameB()
        }
        mHandlerUpdate.removeCallbacksAndMessages(null)
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
            //sound of running eggs
            makeSoundRunningEggs()

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


            displayScoreImageViews(oneDigit = 0,tenDigit = 0,hundredDigit = 0, thousandDigit = 1)

            winLoopCounter=0
            winLoop().run()

            stopAllSounds()
            winningSound = MediaPlayer.create(this,R.raw.win_sound_first)
            winningSound?.start()
            winningSound?.setOnCompletionListener {
                winningSound?.stop()
                winningSound?.release()
                winningSound = MediaPlayer.create(this,R.raw.win_sound_second)
                winningSound?.start()
                winningSound?.setOnCompletionListener {
                    winningSound?.stop()
                    winningSound?.release()
                    winningSound=null
                }
            }



        }
    }

    private fun makeSoundRunningEggs() {
        stopAllSounds()
        runningEggSoundBoolean = if(runningEggSoundBoolean){
            runningEggFirstSound = MediaPlayer.create(this, R.raw.egg_sound_first)
            runningEggFirstSound?.start()
            !runningEggSoundBoolean
        } else{
            runningEggSecondSound = MediaPlayer.create(this, R.raw.egg_sound_second)
            runningEggSecondSound?.start()
            !runningEggSoundBoolean
        }

    }

    private fun stopAllSounds() {
        try{

            runningEggFirstSound?.stop()
            runningEggFirstSound?.release()
            runningEggFirstSound=null


            runningEggSecondSound?.stop()
            runningEggSecondSound?.release()
            runningEggSecondSound=null


            caughtEggSound?.stop()
            caughtEggSound?.release()
            caughtEggSound=null

            faultSound?.stop()
            faultSound?.release()
            faultSound=null

            brokenEggSound?.stop()
            brokenEggSound?.release()
            brokenEggSound=null

            runningChickenSound?.stop()
            runningChickenSound?.release()
            runningChickenSound=null
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }

    // win loop
    private fun winLoop(): Runnable = Runnable {

        game.eggArrayWinAnimation()
        displayState()
        winLoopCounter+=1
        if(winLoopCounter<10){
            mHandlerWin.postDelayed(winLoop(),500)
        }else{
            mHandlerWin.removeCallbacks(winLoop())

            demoMode()
            if(mInterstitialAd.isLoaded){
                mInterstitialAd.show()
            }
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
            imageView.setImageDrawable(FaultTopDrawable(this,faultSize.height))
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
        stopAllSounds()
        if(chickenPlace==0){
            brokenEggSound = MediaPlayer.create(this,R.raw.broken_egg)
            brokenEggSound?.start()
        }
        else {
            runningChickenSound = MediaPlayer.create(this, R.raw.running_chicken)
            runningChickenSound?.start()
        }
        displayRunningChicken(fallenEgg)
        chickenPlace+=1

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
            if(mInterstitialAd.isLoaded){
                mInterstitialAd.show()
            }



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

    private fun pauseA():Runnable = Runnable {
        if(pauseState==Static.ON){
            start_A.setImageDrawable(StartButtonGreen(this,startButtonSize.width*screenUnit,startButtonSize.height*screenUnit))
        }else {
            start_A.setImageDrawable(StartButton(this,startButtonSize.width*screenUnit,startButtonSize.height*screenUnit))
        }
        pauseState=!pauseState
        mHandlerPause.postDelayed(pauseA(),500)
    }

    private fun pauseB():Runnable = Runnable {
        if(pauseState==Static.ON){
            start_B.setImageDrawable(StartButtonGreen(this,startButtonSize.width*screenUnit,startButtonSize.height*screenUnit))
        }else {
            start_B.setImageDrawable(StartButton(this,startButtonSize.width*screenUnit,startButtonSize.height*screenUnit))
        }
        pauseState=!pauseState
        mHandlerPause.postDelayed(pauseB(),500)
    }

    private fun highScore(highScore:Int):Runnable = Runnable {
        if(highScoreState==Static.ON){
            digitOne.visibility = View.GONE
            digitTen.visibility = View.GONE
            digitHundred.visibility = View.GONE
            digitThousand.visibility = View.GONE
        }else {
            digitOne.visibility = View.VISIBLE
            digitTen.visibility = View.VISIBLE
            digitHundred.visibility = View.VISIBLE
            digitThousand.visibility = View.VISIBLE
        }
        highScoreState = !highScoreState
        mHandlerHighScore.postDelayed(highScore(highScore),500)
    }


    private fun update():Runnable = Runnable {
        if(updateState==Static.ON){
            account.setImageDrawable(StartButtonGreen(this,screenUnit*userIdSize.height,
                screenUnit*userIdSize.height
            ))
        }else{
            account.setImageDrawable(StartButton(this,screenUnit*userIdSize.height,
                screenUnit*userIdSize.height
            ))
        }
        updateState=!updateState
        mHandlerUpdate.postDelayed(update(),500)
    }


    /**----------------------- read and write to shared preferences -------------------**/

    // save points game A after lose
    private fun savePointsLoseA():Boolean {
        return if(loggedInStatus.loggedIn){
            val newHighScore = Functions.savePointsLoseAToSharedPreferences(this,loggedInStatus.userid,game.getScore())
            saveUserToFirebaseDatabase()
            newHighScore
        }else{
            false
        }
    }

    private fun saveUserToFirebaseDatabase() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            if (currentUser.uid.equals(loggedInStatus.userid)) {
                val userDB =
                    User(
                        id = loggedInStatus.userid,
                        userName = Functions.checkUserNameFromSharedPreferences(this, loggedInStatus.userid),
                        gameA = Functions.readGameAFromSharedPreferences(this, loggedInStatus.userid),
                        gameB = Functions.readGameBFromSharedPreferences(this, loggedInStatus.userid)
                    )
                val dbRef = Firebase.database.getReference("user").child(loggedInStatus.userid)
                dbRef.setValue(userDB)
            }
        }
    }

    // save points game B after lose
    private fun savePointsLoseB():Boolean {
        return if(loggedInStatus.loggedIn){
            val newHighScore = Functions.savePointsLoseBToSharedPreferences(this,loggedInStatus.userid,game.getScore())
            saveUserToFirebaseDatabase()
            newHighScore
        }else{
            false
        }
    }

    // save points game A after win (1000)
    private fun savePointsWinA() {
        if(loggedInStatus.loggedIn){
            Functions.savePointsWinAToSharedPreferences(this,loggedInStatus.userid,game.getScore())
            saveUserToFirebaseDatabase()
        }

    }

    // save points game B after win (1000)
    private fun savePointsWinB() {
        if(loggedInStatus.loggedIn){
            Functions.savePointsWinBToSharedPreferences(this,loggedInStatus.userid,game.getScore())
            saveUserToFirebaseDatabase()
        }
    }

    // clear game state in shared preferences
    private fun clearSavedGame() {

        if(loggedInStatus.loggedIn){
            Functions.clearSavedUserGameInSharedPreferences(this,loggedInStatus.userid)
        }else{
            Functions.clearSavedGameInSharedPreferences(this)
        }

    }

    // store points, faults and game mode to shared preferences
    private fun saveGameState() {
        val gameState = GameState(game.getScore(),game.getFault(),game.getGameMode())
        if(loggedInStatus.loggedIn){
            Functions.saveUserGameStateToSharedPreferences(this,loggedInStatus.userid,gameState)
        }else{
            Functions.saveGameStateToSharedPreferences(this,gameState)
        }


    }

    // check if there is stored game
    private fun checkGameState() {
        // read from shared preferences
        val gameState = if(loggedInStatus.loggedIn) Functions.checkUserGameStateFromSharedPreferences(this,loggedInStatus.userid) else Functions.checkGameStateFromSharedPreferences(this)


        // points or faults are larger than 0
        if(gameState.score>0||gameState.fault>0){

            //set points, faults and game mode
            game.setPoints(gameState.score)
            game.setFaults(gameState.fault)
            game.setGameMode(if(gameState.gameMode==Static.GAME_A) Static.GAME_A else Static.GAME_B)
            when(gameState.gameMode){
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

    // constraintSet
    private fun makeUI() {

        getScreenHighAndWidth()

        setViewSizes()

        setDrawable()

        val eggJumpDown = screenUnit*0.25
        val set = ConstraintSet()
        set.clone(main_activity_layout)

        set.connect(screen.id,ConstraintSet.LEFT,main_activity_layout.id,ConstraintSet.LEFT,0)
        set.connect(screen.id,ConstraintSet.RIGHT,main_activity_layout.id,ConstraintSet.RIGHT,0)
        set.connect(screen.id,ConstraintSet.TOP,main_activity_layout.id,ConstraintSet.TOP,0)
        set.connect(screen.id,ConstraintSet.BOTTOM,main_activity_layout.id,ConstraintSet.BOTTOM,0)

        set.connect(eggTopLeftFirst.id,ConstraintSet.LEFT,screen.id,ConstraintSet.LEFT,screenUnit+screenUnit/2)
        set.connect(eggTopLeftFirst.id,ConstraintSet.TOP,screen.id,ConstraintSet.TOP, (screenUnit*1.6).toInt())

        set.connect(eggTopLeftSecond.id,ConstraintSet.LEFT,eggTopLeftFirst.id,ConstraintSet.RIGHT,0)
        set.connect(eggTopLeftSecond.id,ConstraintSet.TOP,eggTopLeftFirst.id,ConstraintSet.TOP, eggJumpDown.toInt())

        set.connect(eggTopLeftThird.id,ConstraintSet.LEFT,eggTopLeftSecond.id,ConstraintSet.RIGHT,0)
        set.connect(eggTopLeftThird.id,ConstraintSet.TOP,eggTopLeftSecond.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(eggTopLeftFourth.id,ConstraintSet.LEFT,eggTopLeftThird.id,ConstraintSet.RIGHT,0)
        set.connect(eggTopLeftFourth.id,ConstraintSet.TOP,eggTopLeftThird.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(eggTopLeftFifth.id,ConstraintSet.LEFT,eggTopLeftFourth.id,ConstraintSet.RIGHT,0)
        set.connect(eggTopLeftFifth.id,ConstraintSet.TOP,eggTopLeftFourth.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(eggBottomLeftFirst.id,ConstraintSet.LEFT,screen.id,ConstraintSet.LEFT,screenUnit+screenUnit/2)
        set.connect(eggBottomLeftFirst.id,ConstraintSet.TOP,screen.id,ConstraintSet.TOP, (screenUnit*3.1).toInt())

        set.connect(eggBottomLeftSecond.id,ConstraintSet.LEFT,eggBottomLeftFirst.id,ConstraintSet.RIGHT,0)
        set.connect(eggBottomLeftSecond.id,ConstraintSet.TOP,eggBottomLeftFirst.id,ConstraintSet.TOP, eggJumpDown.toInt())

        set.connect(eggBottomLeftThird.id,ConstraintSet.LEFT,eggBottomLeftSecond.id,ConstraintSet.RIGHT,0)
        set.connect(eggBottomLeftThird.id,ConstraintSet.TOP,eggBottomLeftSecond.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(eggBottomLeftFourth.id,ConstraintSet.LEFT,eggBottomLeftThird.id,ConstraintSet.RIGHT,0)
        set.connect(eggBottomLeftFourth.id,ConstraintSet.TOP,eggBottomLeftThird.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(eggBottomLeftFifth.id,ConstraintSet.LEFT,eggBottomLeftFourth.id,ConstraintSet.RIGHT,0)
        set.connect(eggBottomLeftFifth.id,ConstraintSet.TOP,eggBottomLeftFourth.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(eggTopRightFirst.id,ConstraintSet.RIGHT,screen.id,ConstraintSet.RIGHT,screenUnit+screenUnit/2)
        set.connect(eggTopRightFirst.id,ConstraintSet.TOP,screen.id,ConstraintSet.TOP, (screenUnit*1.6).toInt())

        set.connect(eggTopRightSecond.id,ConstraintSet.RIGHT,eggTopRightFirst.id,ConstraintSet.LEFT,0)
        set.connect(eggTopRightSecond.id,ConstraintSet.TOP,eggTopRightFirst.id,ConstraintSet.TOP, eggJumpDown.toInt())

        set.connect(eggTopRightThird.id,ConstraintSet.RIGHT,eggTopRightSecond.id,ConstraintSet.LEFT,0)
        set.connect(eggTopRightThird.id,ConstraintSet.TOP,eggTopRightSecond.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(eggTopRightFourth.id,ConstraintSet.RIGHT,eggTopRightThird.id,ConstraintSet.LEFT,0)
        set.connect(eggTopRightFourth.id,ConstraintSet.TOP,eggTopRightThird.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(eggTopRightFifth.id,ConstraintSet.RIGHT,eggTopRightFourth.id,ConstraintSet.LEFT,0)
        set.connect(eggTopRightFifth.id,ConstraintSet.TOP,eggTopRightFourth.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(eggBottomRightFirst.id,ConstraintSet.RIGHT,screen.id,ConstraintSet.RIGHT,screenUnit+screenUnit/2)
        set.connect(eggBottomRightFirst.id,ConstraintSet.TOP,screen.id,ConstraintSet.TOP, (screenUnit*3.1).toInt())

        set.connect(eggBottomRightSecond.id,ConstraintSet.RIGHT,eggBottomRightFirst.id,ConstraintSet.LEFT,0)
        set.connect(eggBottomRightSecond.id,ConstraintSet.TOP,eggBottomRightFirst.id,ConstraintSet.TOP, eggJumpDown.toInt())

        set.connect(eggBottomRightThird.id,ConstraintSet.RIGHT,eggBottomRightSecond.id,ConstraintSet.LEFT,0)
        set.connect(eggBottomRightThird.id,ConstraintSet.TOP,eggBottomRightSecond.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(eggBottomRightFourth.id,ConstraintSet.RIGHT,eggBottomRightThird.id,ConstraintSet.LEFT,0)
        set.connect(eggBottomRightFourth.id,ConstraintSet.TOP,eggBottomRightThird.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(eggBottomRightFifth.id,ConstraintSet.RIGHT,eggBottomRightFourth.id,ConstraintSet.LEFT,0)
        set.connect(eggBottomRightFifth.id,ConstraintSet.TOP,eggBottomRightFourth.id,ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(digitTen.id,ConstraintSet.TOP,screen.id,ConstraintSet.TOP, (screenUnit*0.9).toInt())
        set.connect(digitTen.id,ConstraintSet.LEFT,screen.id,ConstraintSet.LEFT,0)
        set.connect(digitTen.id,ConstraintSet.RIGHT,screen.id,ConstraintSet.RIGHT,0)

        set.connect(digitOne.id,ConstraintSet.TOP,digitTen.id,ConstraintSet.TOP,0)
        set.connect(digitOne.id,ConstraintSet.LEFT,digitTen.id,ConstraintSet.RIGHT, (screenUnit*0.1).toInt())

        set.connect(digitHundred.id,ConstraintSet.TOP,digitTen.id,ConstraintSet.TOP,0)
        set.connect(digitHundred.id,ConstraintSet.RIGHT,digitTen.id,ConstraintSet.LEFT, (screenUnit*0.1).toInt())

        set.connect(digitThousand.id,ConstraintSet.TOP,digitTen.id,ConstraintSet.TOP,0)
        set.connect(digitThousand.id,ConstraintSet.RIGHT,digitHundred.id,ConstraintSet.LEFT, (screenUnit*0.1).toInt())

        set.connect(buttonBottomLeft.id,ConstraintSet.RIGHT,screen.id,ConstraintSet.LEFT,0)
        set.connect(buttonBottomLeft.id,ConstraintSet.LEFT,main_activity_layout.id,ConstraintSet.LEFT,0)
        set.connect(buttonBottomLeft.id,ConstraintSet.BOTTOM,main_activity_layout.id,ConstraintSet.BOTTOM,screenUnit)

        set.connect(buttonTopLeft.id,ConstraintSet.RIGHT,screen.id,ConstraintSet.LEFT,0)
        set.connect(buttonTopLeft.id,ConstraintSet.LEFT,main_activity_layout.id,ConstraintSet.LEFT,0)
        set.connect(buttonTopLeft.id,ConstraintSet.BOTTOM,buttonBottomLeft.id,ConstraintSet.TOP,screenUnit)

        set.connect(buttonBottomRight.id,ConstraintSet.LEFT,screen.id,ConstraintSet.RIGHT,0)
        set.connect(buttonBottomRight.id,ConstraintSet.RIGHT,main_activity_layout.id,ConstraintSet.RIGHT,0)
        set.connect(buttonBottomRight.id,ConstraintSet.BOTTOM,main_activity_layout.id,ConstraintSet.BOTTOM,screenUnit)

        set.connect(buttonTopRight.id,ConstraintSet.LEFT,screen.id,ConstraintSet.RIGHT,0)
        set.connect(buttonTopRight.id,ConstraintSet.RIGHT,main_activity_layout.id,ConstraintSet.RIGHT,0)
        set.connect(buttonTopRight.id,ConstraintSet.BOTTOM,buttonBottomRight.id,ConstraintSet.TOP,screenUnit)

        set.connect(start_A.id,ConstraintSet.LEFT,buttonTopRight.id,ConstraintSet.LEFT,0)
        set.connect(start_A.id,ConstraintSet.TOP,main_activity_layout.id,ConstraintSet.TOP,screenUnit)

        set.connect(start_B.id,ConstraintSet.LEFT,buttonTopRight.id,ConstraintSet.LEFT,0)
        set.connect(start_B.id,ConstraintSet.TOP,start_A.id,ConstraintSet.BOTTOM,screenUnit/2)

        set.connect(letterA.id,ConstraintSet.LEFT,start_A.id,ConstraintSet.LEFT,0)
        set.connect(letterA.id,ConstraintSet.RIGHT,start_A.id,ConstraintSet.RIGHT,0)
        set.connect(letterA.id,ConstraintSet.BOTTOM,start_A.id,ConstraintSet.BOTTOM,screenUnit)

        set.connect(letterB.id,ConstraintSet.LEFT,start_B.id,ConstraintSet.LEFT,0)
        set.connect(letterB.id,ConstraintSet.RIGHT,start_B.id,ConstraintSet.RIGHT,0)
        set.connect(letterB.id,ConstraintSet.BOTTOM,start_B.id,ConstraintSet.BOTTOM,screenUnit)

        set.connect(closeApp.id,ConstraintSet.RIGHT,buttonTopLeft.id,ConstraintSet.RIGHT,0)
        set.connect(closeApp.id,ConstraintSet.TOP,start_A.id,ConstraintSet.TOP,0)

        set.connect(exit.id,ConstraintSet.LEFT,closeApp.id,ConstraintSet.LEFT,0)
        set.connect(exit.id,ConstraintSet.RIGHT,closeApp.id,ConstraintSet.RIGHT,0)
        set.connect(exit.id,ConstraintSet.BOTTOM,closeApp.id,ConstraintSet.BOTTOM,screenUnit)

        set.connect(account.id,ConstraintSet.TOP,main_activity_layout.id,ConstraintSet.TOP,0)
        set.connect(account.id,ConstraintSet.RIGHT,screen.id,ConstraintSet.RIGHT,0)
        set.connect(account.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.TOP,0)

        set.connect(userID.id,ConstraintSet.TOP,main_activity_layout.id,ConstraintSet.TOP,0)
        set.connect(userID.id,ConstraintSet.LEFT,screen.id,ConstraintSet.LEFT,0)
        set.connect(userID.id,ConstraintSet.RIGHT,screen.id,ConstraintSet.RIGHT,0)
        set.connect(userID.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.TOP,0)

        set.connect(rabbit.id,ConstraintSet.TOP,screen.id,ConstraintSet.TOP, (screenUnit*0.8).toInt())
        set.connect(rabbit.id,ConstraintSet.LEFT,screen.id,ConstraintSet.LEFT,
            (2.9*screenUnit).toInt()
        )

        set.connect(middle_fault.id,ConstraintSet.TOP,screen.id,ConstraintSet.TOP, (1.6*screenUnit).toInt())
        set.connect(middle_fault.id,ConstraintSet.LEFT,screen.id,ConstraintSet.LEFT,0)
        set.connect(middle_fault.id,ConstraintSet.RIGHT,screen.id,ConstraintSet.RIGHT,0)

        set.connect(left_fault.id,ConstraintSet.TOP,middle_fault.id,ConstraintSet.TOP,0)
        set.connect(left_fault.id,ConstraintSet.RIGHT,middle_fault.id,ConstraintSet.LEFT, 0)

        set.connect(right_fault.id,ConstraintSet.TOP,middle_fault.id,ConstraintSet.TOP,0)
        set.connect(right_fault.id,ConstraintSet.LEFT,middle_fault.id,ConstraintSet.RIGHT, 0)

        set.connect(faultLeftSecond.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.BOTTOM,(screenUnit*1.5).toInt())
        set.connect(faultLeftSecond.id,ConstraintSet.LEFT,faultLeftThird.id,ConstraintSet.RIGHT,(screenUnit*0.1).toInt())

        set.connect(faultLeftThird.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.BOTTOM,(screenUnit*1.5).toInt())
        set.connect(faultLeftThird.id,ConstraintSet.LEFT,faultLeftFourth.id,ConstraintSet.RIGHT,(screenUnit*0.1).toInt())

        set.connect(faultLeftFourth.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.BOTTOM, (screenUnit*1.5).toInt())
        set.connect(faultLeftFourth.id,ConstraintSet.LEFT,screen.id,ConstraintSet.LEFT,(screenUnit*0.9).toInt())

        set.connect(faultLeftFirst.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.BOTTOM, (screenUnit*0.83).toInt())
        set.connect(faultLeftFirst.id,ConstraintSet.LEFT,faultLeftSecond.id,ConstraintSet.RIGHT,(screenUnit*0.2).toInt())

        set.connect(faultRightSecond.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.BOTTOM,(screenUnit*1.5).toInt())
        set.connect(faultRightSecond.id,ConstraintSet.RIGHT,faultRightThird.id,ConstraintSet.LEFT,(screenUnit*0.1).toInt())

        set.connect(faultRightThird.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.BOTTOM,(screenUnit*1.5).toInt())
        set.connect(faultRightThird.id,ConstraintSet.RIGHT,faultRightFourth.id,ConstraintSet.LEFT,(screenUnit*0.1).toInt())

        set.connect(faultRightFourth.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.BOTTOM, (screenUnit*1.5).toInt())
        set.connect(faultRightFourth.id,ConstraintSet.RIGHT,screen.id,ConstraintSet.RIGHT,(screenUnit*0.9).toInt())

        set.connect(faultRightFirst.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.BOTTOM, (screenUnit*0.83).toInt())
        set.connect(faultRightFirst.id,ConstraintSet.RIGHT,faultRightSecond.id,ConstraintSet.LEFT,(screenUnit*0.2).toInt())

        set.connect(left_wolf.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.BOTTOM, (screenUnit*1.2).toInt())
        set.connect(left_wolf.id,ConstraintSet.LEFT,eggBottomLeftFifth.id,ConstraintSet.LEFT, (screenUnit*0.13).toInt())

        set.connect(right_wolf.id,ConstraintSet.BOTTOM,screen.id,ConstraintSet.BOTTOM, (screenUnit*1.2).toInt())
        set.connect(right_wolf.id,ConstraintSet.RIGHT,eggBottomRightFifth.id,ConstraintSet.RIGHT, (screenUnit*0.13).toInt())

        set.connect(linearLayoutMusic.id,ConstraintSet.TOP,screen.id,ConstraintSet.BOTTOM,0)
        set.connect(linearLayoutMusic.id,ConstraintSet.BOTTOM,main_activity_layout.id,ConstraintSet.BOTTOM,0)
        set.connect(linearLayoutMusic.id,ConstraintSet.LEFT,main_activity_layout.id,ConstraintSet.LEFT,0)
        set.connect(linearLayoutMusic.id,ConstraintSet.RIGHT,main_activity_layout.id,ConstraintSet.RIGHT,0)

        set.applyTo(main_activity_layout)


    }

    private fun setDrawable() {
        val mainScreen = MainScreenDrawable(this,screenUnit,screenSize.width,screenSize.height)
        screen.setImageDrawable(mainScreen)

        buttonBottomLeft.setImageDrawable(ArrowBottomLeft(this,screenUnit*arrowSize.width,
            screenUnit*arrowSize.height
        ))

        buttonTopLeft.setImageDrawable(ArrowTopLeft(this,screenUnit*arrowSize.width,
            screenUnit*arrowSize.height
        ))

        buttonTopRight.setImageDrawable(ArrowTopRight(this,screenUnit*arrowSize.width,
            screenUnit*arrowSize.height
        ))

        buttonBottomRight.setImageDrawable(ArrowBottomRight(this,screenUnit*arrowSize.width,
            screenUnit*arrowSize.height
        ))

        start_A.setImageDrawable(StartButton(this,screenUnit*startButtonSize.width,
            screenUnit*startButtonSize.height
        ))

        start_B.setImageDrawable(StartButton(this,screenUnit*startButtonSize.width,
            screenUnit*startButtonSize.height
        ))

        closeApp.setImageDrawable(StartButton(this,screenUnit*startButtonSize.width,
            screenUnit*startButtonSize.height
        ))

        account.setImageDrawable(StartButton(this,screenUnit*userIdSize.height,
            screenUnit*userIdSize.height
        ))

        userID.background = TextViewDrawable(this,userIdSize.width*screenUnit,userIdSize.height*screenUnit)




    }

    private fun setViewSizes() {
        screenSize.width = 14.0
        screenSize.height = 7.0
        screen.layoutParams = ConstraintLayout.LayoutParams((screenSize.width*screenUnit).toInt(),(screenSize.height*screenUnit).toInt())

        eggSize.width=0.5
        eggSize.height=0.5
        eggTopLeftFirst.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggTopLeftSecond.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggTopLeftThird.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggTopLeftFourth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggTopLeftFifth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggBottomLeftFirst.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggBottomLeftSecond.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggBottomLeftThird.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggBottomLeftFourth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggBottomLeftFifth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggTopRightFirst.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggTopRightSecond.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggTopRightThird.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggTopRightFourth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggTopRightFifth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggBottomRightFirst.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggBottomRightSecond.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggBottomRightThird.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggBottomRightFourth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        eggBottomRightFifth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())


        digitSize.width = 0.3
        digitSize.height = digitSize.width*2

        digitOne.layoutParams = ConstraintLayout.LayoutParams((digitSize.width*screenUnit).toInt(),(digitSize.height*screenUnit).toInt())
        digitTen.layoutParams = ConstraintLayout.LayoutParams((digitSize.width*screenUnit).toInt(),(digitSize.height*screenUnit).toInt())
        digitHundred.layoutParams = ConstraintLayout.LayoutParams((digitSize.width*screenUnit).toInt(),(digitSize.height*screenUnit).toInt())
        digitThousand.layoutParams = ConstraintLayout.LayoutParams((digitSize.width*screenUnit).toInt(),(digitSize.height*screenUnit).toInt())


        arrowSize.width= 2.0
        arrowSize.height=arrowSize.width*2/3

        buttonBottomLeft.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width*screenUnit).toInt(), (arrowSize.height*screenUnit).toInt())
        buttonTopLeft.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width*screenUnit).toInt(), (arrowSize.height*screenUnit).toInt())
        buttonBottomRight.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width*screenUnit).toInt(), (arrowSize.height*screenUnit).toInt())
        buttonTopRight.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width*screenUnit).toInt(), (arrowSize.height*screenUnit).toInt())

        startButtonSize.width = arrowSize.height
        startButtonSize.height = arrowSize.height

        start_A.layoutParams = ConstraintLayout.LayoutParams((startButtonSize.width*screenUnit).toInt(), (startButtonSize.height*screenUnit).toInt())
        start_B.layoutParams = ConstraintLayout.LayoutParams((startButtonSize.width*screenUnit).toInt(), (startButtonSize.height*screenUnit).toInt())

        letterA.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        letterB.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())


        closeApp.layoutParams = ConstraintLayout.LayoutParams((startButtonSize.width*screenUnit).toInt(), (startButtonSize.height*screenUnit).toInt())
        exit.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        userIdSize.height = arrowSize.height
        userIdSize.width = screenSize.width-3*userIdSize.height

        account.layoutParams = ConstraintLayout.LayoutParams((userIdSize.height*screenUnit).toInt(), (userIdSize.height*screenUnit).toInt())
        userID.layoutParams = ConstraintLayout.LayoutParams((userIdSize.width*screenUnit).toInt(), (userIdSize.height*screenUnit).toInt())
        userID.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        rabbitSize.width = screenUnit*1.4
        rabbitSize.height = screenUnit*1.4

        rabbit.layoutParams = ConstraintLayout.LayoutParams(rabbitSize.width.toInt(),rabbitSize.height.toInt())

        faultSize.width = (screenUnit*0.6).toDouble()
        faultSize.height = faultSize.width
        middle_fault.layoutParams = ConstraintLayout.LayoutParams(faultSize.width.toInt(),faultSize.height.toInt())
        left_fault.layoutParams = ConstraintLayout.LayoutParams(faultSize.width.toInt(),faultSize.height.toInt())
        right_fault.layoutParams = ConstraintLayout.LayoutParams(faultSize.width.toInt(),faultSize.height.toInt())

        bottomFaultSizeSmall.width = (screenUnit/2).toDouble()
        bottomFaultSizeSmall.height=bottomFaultSizeSmall.width*1.5

        bottomFaultSizeSmallDifferent.height = bottomFaultSizeSmall.height
        bottomFaultSizeSmallDifferent.width = bottomFaultSizeSmallDifferent.height/1.2



        faultLeftSecond.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmallDifferent.width.toInt(),bottomFaultSizeSmallDifferent.height.toInt())
        faultLeftThird.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmall.width.toInt(),bottomFaultSizeSmall.height.toInt())
        faultLeftFourth.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmall.width.toInt(),bottomFaultSizeSmall.height.toInt())
        faultRightSecond.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmallDifferent.width.toInt(),bottomFaultSizeSmallDifferent.height.toInt())
        faultRightThird.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmall.width.toInt(),bottomFaultSizeSmall.height.toInt())
        faultRightFourth.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmall.width.toInt(),bottomFaultSizeSmall.height.toInt())

        bottomFaultSizeFirst.width = (screenUnit*2).toDouble()
        bottomFaultSizeFirst.height = (screenUnit*1.5).toDouble()
        faultLeftFirst.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeFirst.width.toInt(),bottomFaultSizeFirst.height.toInt())
        faultRightFirst.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeFirst.width.toInt(),bottomFaultSizeFirst.height.toInt())

        wolfSize.width = (screenUnit*3.5).toDouble()
        wolfSize.height = wolfSize.width

        left_wolf.layoutParams = ConstraintLayout.LayoutParams(wolfSize.width.toInt(),wolfSize.height.toInt())
        right_wolf.layoutParams = ConstraintLayout.LayoutParams(wolfSize.width.toInt(),wolfSize.height.toInt())

        music_from.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        music_link.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

    }

    private fun getScreenHighAndWidth() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels
        val unitWidth = screenWidth/20
        val unitHeight = screenHeight/10
        screenUnit=if(unitWidth>unitHeight)unitHeight else unitWidth

    }

    // display all fallen eggs
    private fun displayState(){
        eggTopLeftFirst.setImageDrawable(if(game.displayCell(1,Static.LEFT_TOP)) NormalEggDrawable(this,screenUnit*eggSize.width) else null)
        eggTopLeftSecond.setImageDrawable(if(game.displayCell(2,Static.LEFT_TOP)) EggPlus45(this,screenUnit*eggSize.width) else null)
        eggTopLeftThird.setImageDrawable(if(game.displayCell(3,Static.LEFT_TOP)) EggPlus90(this,screenUnit*eggSize.width) else null)
        eggTopLeftFourth.setImageDrawable(if(game.displayCell(4,Static.LEFT_TOP)) EggPlus135(this,screenUnit*eggSize.width) else null)
        eggTopLeftFifth.setImageDrawable(if(game.displayCell(5,Static.LEFT_TOP)) EggPlus225(this,screenUnit*eggSize.width) else null)


        eggBottomLeftFirst.setImageDrawable(if(game.displayCell(1,Static.LEFT_BOTTOM)) NormalEggDrawable(this,screenUnit*eggSize.width) else null)
        eggBottomLeftSecond.setImageDrawable(if(game.displayCell(2,Static.LEFT_BOTTOM)) EggPlus45(this,screenUnit*eggSize.width) else null)
        eggBottomLeftThird.setImageDrawable(if(game.displayCell(3,Static.LEFT_BOTTOM)) EggPlus90(this,screenUnit*eggSize.width) else null)
        eggBottomLeftFourth.setImageDrawable(if(game.displayCell(4,Static.LEFT_BOTTOM)) EggPlus135(this,screenUnit*eggSize.width) else null)
        eggBottomLeftFifth.setImageDrawable(if(game.displayCell(5,Static.LEFT_BOTTOM)) EggPlus225(this,screenUnit*eggSize.width) else null)


        eggBottomRightFirst.setImageDrawable(if(game.displayCell(1,Static.RIGHT_BOTTOM)) NormalEggDrawable(this,screenUnit*eggSize.width) else null)
        eggBottomRightSecond.setImageDrawable(if(game.displayCell(2,Static.RIGHT_BOTTOM)) EggMinus45(this,screenUnit*eggSize.width) else null)
        eggBottomRightThird.setImageDrawable(if(game.displayCell(3,Static.RIGHT_BOTTOM)) EggMinus90(this,screenUnit*eggSize.width) else null)
        eggBottomRightFourth.setImageDrawable(if(game.displayCell(4,Static.RIGHT_BOTTOM)) EggPlus225(this,screenUnit*eggSize.width) else null)
        eggBottomRightFifth.setImageDrawable(if(game.displayCell(5,Static.RIGHT_BOTTOM)) EggPlus135(this,screenUnit*eggSize.width) else null)


        eggTopRightFirst.setImageDrawable(if(game.displayCell(1,Static.RIGHT_TOP)) NormalEggDrawable(this,screenUnit*eggSize.width) else null)
        eggTopRightSecond.setImageDrawable(if(game.displayCell(2,Static.RIGHT_TOP)) EggMinus45(this,screenUnit*eggSize.width) else null)
        eggTopRightThird.setImageDrawable(if(game.displayCell(3,Static.RIGHT_TOP)) EggMinus90(this,screenUnit*eggSize.width) else null)
        eggTopRightFourth.setImageDrawable(if(game.displayCell(4,Static.RIGHT_TOP)) EggPlus225(this,screenUnit*eggSize.width) else null)
        eggTopRightFifth.setImageDrawable(if(game.displayCell(5,Static.RIGHT_TOP)) EggPlus135(this,screenUnit*eggSize.width) else null)


    }



    // display rabbit
    private fun displayRabbit(rabbitBoolean: Boolean){
       if(rabbitBoolean){
           rabbit.setImageDrawable(RabbitDrawable(this,rabbitSize.width))
       }
       else{
           rabbit.setImageDrawable(null)
       }
   }


    // display running chicken during animation
    private fun displayRunningChicken(fallenEgg: FallenEgg) {
        faultLeftFirst.setImageDrawable(if(fallenEgg.getFallenEgg(1,0))BrokenEggLeft(this,bottomFaultSizeFirst.width)else null)
        faultLeftSecond.setImageDrawable(if(fallenEgg.getFallenEgg(2,0))RunningChickenLeftFirst(this,bottomFaultSizeSmallDifferent.width)else null)
        faultLeftThird.setImageDrawable(if(fallenEgg.getFallenEgg(3,0))RunningChickenLeftTwo(this,bottomFaultSizeSmall.width)else null)
        faultLeftFourth.setImageDrawable(if(fallenEgg.getFallenEgg(4,0))RunningChickenLeftThree(this,bottomFaultSizeSmall.width)else null)


        faultRightFirst.setImageDrawable(if(fallenEgg.getFallenEgg(1,1))BrokenEggRight(this,bottomFaultSizeFirst.width)else null)
        faultRightSecond.setImageDrawable(if(fallenEgg.getFallenEgg(2,1))RunningChickenRightFirst(this,bottomFaultSizeSmallDifferent.width)else null)
        faultRightThird.setImageDrawable(if(fallenEgg.getFallenEgg(3,1))RunningChickenRightTwo(this,bottomFaultSizeSmall.width)else null)
        faultRightFourth.setImageDrawable(if(fallenEgg.getFallenEgg(4,1))RunningChickenRightThree(this,bottomFaultSizeSmall.width)else null)


    }

    // display demo basket
    private fun displayDemoBasket() {
        if(game.position[Static.LEFT_TOP]){
            left_wolf.setImageDrawable(WolfLeftTop(this,wolfSize.width))
            right_wolf.setImageDrawable(null)
        }

        if(game.position[Static.LEFT_BOTTOM]){
            left_wolf.setImageDrawable(WolfLeftBottom(this,wolfSize.width))
            right_wolf.setImageDrawable(null)
        }
        if(game.position[Static.RIGHT_BOTTOM]){
            left_wolf.setImageDrawable(null)
            right_wolf.setImageDrawable(WolfRightBottom(this,wolfSize.width))
        }
        if(game.position[Static.RIGHT_TOP]){
            left_wolf.setImageDrawable(null)
            right_wolf.setImageDrawable(WolfRightTop(this,wolfSize.width))
        }

    }

    // update score in text view
    private fun updateScoreTextView(){

        val thousand = game.getScore()/1000
        var reszta = game.getScore()%1000
        val hundred = reszta/100
        reszta %= 100
        val ten = reszta/10
        reszta %= 10
        val one = reszta

        when(game.getScore()){
            in 0..9 -> displayScoreImageViews(oneDigit = one,tenDigit = null,hundredDigit = null,thousandDigit = null)
            in 10..99 -> displayScoreImageViews(oneDigit = one,tenDigit = ten,hundredDigit = null,thousandDigit = null)
            in 100..999 -> displayScoreImageViews(oneDigit = one,tenDigit = ten,hundredDigit = hundred,thousandDigit = null)
            1000 -> displayScoreImageViews(oneDigit = one,tenDigit = ten,hundredDigit = hundred,thousandDigit = thousand)
        }
    }

    private fun displayScoreImageViews(oneDigit:Int?,tenDigit:Int?,hundredDigit:Int?, thousandDigit:Int?) {
        if(thousandDigit!=null) digitThousand.setImageDrawable(Digit(this,
            (digitSize.width*screenUnit).toInt(),thousandDigit))
        else digitThousand.setImageDrawable(null)

        if(hundredDigit!=null) digitHundred.setImageDrawable(Digit(this,
            (digitSize.width*screenUnit).toInt(),hundredDigit))
        else digitHundred.setImageDrawable(null)

        if(tenDigit!=null) digitTen.setImageDrawable(Digit(this,
            (digitSize.width*screenUnit).toInt(),tenDigit))
        else digitTen.setImageDrawable(null)

        if(oneDigit!=null) digitOne.setImageDrawable(Digit(this,
            (digitSize.width*screenUnit).toInt(),oneDigit))
        else digitOne.setImageDrawable(null)


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
            left_wolf.setImageDrawable(WolfLeftTop(this,wolfSize.width))
            right_wolf.setImageDrawable(null)
        }
        if(game.position[Static.LEFT_BOTTOM]){
            left_wolf.setImageDrawable(WolfLeftBottom(this,wolfSize.width))
            right_wolf.setImageDrawable(null)
        }
        if(game.position[Static.RIGHT_BOTTOM]){
            left_wolf.setImageDrawable(null)
            right_wolf.setImageDrawable(WolfRightBottom(this,wolfSize.width))
        }
        if(game.position[Static.RIGHT_TOP]){
            left_wolf.setImageDrawable(null)
            right_wolf.setImageDrawable(WolfRightTop(this,wolfSize.width))
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

        closeApp.setOnClickListener {
            finish()
        }
        account.setOnClickListener {
            if(gameState==Static.DEMO||gameState==Static.PAUSE_A||gameState==Static.PAUSE_B){
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        music_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.zapsplat.com"))
            startActivity(intent)
        }



        if(loggedInStatus.loggedIn) {
            userID.text = Functions.checkUserNameFromSharedPreferences(this, loggedInStatus.userid)
        }
        else
        {
            userID.text="NOT LOGGED IN"
        }



    }


    /**------------------------ game state logic and functions ----------------**/

    // demo mode
    private fun demoMode() {

        digitOne.visibility = View.VISIBLE
        digitTen.visibility = View.VISIBLE
        digitHundred.visibility = View.VISIBLE
        digitThousand.visibility = View.VISIBLE
        displayScoreImageViews(0,0,0,0)

        start_A.setImageDrawable(StartButton(this,startButtonSize.width*screenUnit,startButtonSize.height*screenUnit))
        start_B.setImageDrawable(StartButton(this,startButtonSize.width*screenUnit,startButtonSize.height*screenUnit))

        linearLayoutMusic.visibility = View.VISIBLE
        mHandler.removeCallbacksAndMessages(null)
        mHandlerDemo.removeCallbacksAndMessages(null)
        mHandlerPause.removeCallbacksAndMessages(null)
        mHandlerWin.removeCallbacksAndMessages(null)
        mHandlerRabbit.removeCallbacksAndMessages(null)
        mHandlerFlash.removeCallbacksAndMessages(null)
        mHandlerLostEgg.removeCallbacksAndMessages(null)
        mHandlerHighScore.removeCallbacksAndMessages(null)
        gameState=Static.DEMO
        game.clearEverything()
        displayState()
        displayBasket()
        updateFaultsView()

        loopCounter = 0
        demo().run()

    }


    // play game A
    private fun startGameA() {

        linearLayoutMusic.visibility = View.GONE
        mHandlerPause.removeCallbacksAndMessages(null)
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
        start_A.setImageDrawable(StartButtonGreen(this,startButtonSize.width*screenUnit,startButtonSize.height*screenUnit))

    }

    // play game B
    private fun startGameB() {
        linearLayoutMusic.visibility = View.GONE
        mHandlerPause.removeCallbacksAndMessages(null)
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
        start_B.setImageDrawable(StartButtonGreen(this,startButtonSize.width*screenUnit,startButtonSize.height*screenUnit))

    }

    // pause game A
    private fun pauseGameA() {
        linearLayoutMusic.visibility = View.GONE
        gameState=Static.PAUSE_A
        updateFaultsView()
        updateScoreTextView()
        // stop all runnable
        mHandlerRabbit.removeCallbacksAndMessages(null)
        mHandler.removeCallbacksAndMessages(null)
        mHandlerFlash.removeCallbacksAndMessages(null)
        mHandlerLostEgg.removeCallbacksAndMessages(null)
        clearAnimationFallenEgg()
        // save game state to shared preferences
        saveGameState()
        pauseA().run()
    }


    private fun unPauseGameA() {
        startGameA()
    }


    private fun pauseGameB() {
        linearLayoutMusic.visibility = View.GONE
        gameState=Static.PAUSE_B
        updateFaultsView()
        updateScoreTextView()
        mHandlerRabbit.removeCallbacksAndMessages(null)
        mHandler.removeCallbacksAndMessages(null)
        mHandlerFlash.removeCallbacksAndMessages(null)
        mHandlerLostEgg.removeCallbacksAndMessages(null)
        clearAnimationFallenEgg()
        saveGameState()
        pauseB().run()
    }

    private fun clearAnimationFallenEgg() {
        val fallenEgg=FallenEgg()
        displayRunningChicken(fallenEgg)

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

               caughtEggSound = MediaPlayer.create(this,R.raw.caught_egg_sound)
                caughtEggSound?.start()
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
            displayState()
            mHandler.removeCallbacks(gameLoop())
            game.addFault(rabbitBoolean)
            updateFaultsView()

            if(game.getFault()<=Static.FAULT_TWO_AND_HALF) {
                stopAllSounds()
                chickenPlace = 0

                lostEggAnimation(eggCaught.positionFallenEgg)
            }else{
                stopAllSounds()
                faultSound = MediaPlayer.create(this,R.raw.fault_sound)
                faultSound?.start()
                lostEggAnimationEndGame(eggCaught.positionFallenEgg)

            }
        }
    }

    // game A has finished because of 3 faults
    private fun loseA() {
        val newHighScore = savePointsLoseA()
        if(newHighScore){
            val score = game.getScore()
            showNewHighScore(score)
        }
        game.clearEverything()
        clearSavedGame()
        gameState=Static.LOSE_A

    }

    private fun showNewHighScore(score:Int) {
        highScore(score).run()
    }

    // game B has finished because of 3 faults
    private fun loseB() {

        val newHighScore = savePointsLoseB()
        if(newHighScore){
            val score = game.getScore()
            showNewHighScore(score)
        }

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
        right_fault.setImageDrawable(FaultTopDrawable(this,faultSize.height))
    }

    private fun threeFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        flashFault(middle_fault).run()
        left_fault.setImageDrawable(null)
        right_fault.setImageDrawable(FaultTopDrawable(this,faultSize.height))
    }

    private fun fourFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        left_fault.setImageDrawable(null)
        middle_fault.setImageDrawable(FaultTopDrawable(this,faultSize.height))
        right_fault.setImageDrawable(FaultTopDrawable(this,faultSize.height))
    }

    private fun fiveFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        flashFault(left_fault).run()
        middle_fault.setImageDrawable(FaultTopDrawable(this,faultSize.height))
        right_fault.setImageDrawable(FaultTopDrawable(this,faultSize.height))
    }

    private fun sixFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        left_fault.setImageDrawable(FaultTopDrawable(this,faultSize.height))
        middle_fault.setImageDrawable(FaultTopDrawable(this,faultSize.height))
        right_fault.setImageDrawable(FaultTopDrawable(this,faultSize.height))
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

    override fun onUpdateNeeded(updateUrl: String) {
        Functions.saveUpdateToSharedPreferences(context = this,isUpdate = true,url = updateUrl)
        update().run()
    }

}





