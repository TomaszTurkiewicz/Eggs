package com.tt.eggs.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.eggs.LoginActivity
import com.tt.eggs.R
import com.tt.eggs.classes.*
import com.tt.eggs.databinding.FragmentMainBinding
import com.tt.eggs.drawable.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.random.Random


class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var screenUnit = 0
    private val screenSize = Dimension()
    private val wholeScreenSize = Dimension()
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
    private var game = Game()
    // for win loop
    private var winLoopCounter = 0
    private val mHandlerWin = Handler(Looper.getMainLooper())
    private var moveProduct = MoveProduct()
    // basket position
    private var basket = Static.RIGHT_TOP

    private var chickenPlace = 0

    // loggedInState
    private var loggedInStatus = LoggedInStatus()

    // game state
    private var gameState = Static.DEMO
    private var highScoreState = Static.ON
    private var loopCounter=0

    private var mInterstitialAd: InterstitialAd? = null
    // for game loop
    private val mHandler = Handler(Looper.getMainLooper())
    private val mHandlerDemo = Handler(Looper.getMainLooper())
    private val mHandlerPause = Handler(Looper.getMainLooper())
    private val mHandlerFlash = Handler(Looper.getMainLooper())
    // for lost egg loops
    private val mHandlerLostEgg = Handler(Looper.getMainLooper())
    private val mHandlerHighScore = Handler(Looper.getMainLooper())
    // for rabbit loop
    private val mHandlerRabbit = Handler(Looper.getMainLooper())
    private var faultFlash = Static.ON
    // counter for rabbit show
    private var rabbitOn = 0
    // counter for rabbit not show
    private var rabbitOff = 0
    // rabbit state
    private var rabbitBoolean = Static.OFF
    private var pauseState = Static.ON

    private var runningEggFirstSound: MediaPlayer?=null
    private var caughtEggSound: MediaPlayer?=null
    private var faultSound: MediaPlayer?=null
    private var brokenEggSound: MediaPlayer?=null
    private var winningSound: MediaPlayer?=null

    private var runningEggSound1: MediaPlayer?=null
    private var runningEggSound2: MediaPlayer?=null
    private var runningEggSound3: MediaPlayer?=null
    private var runningEggSound4: MediaPlayer?=null
    private var runningEggSound5: MediaPlayer?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runningEggSound1 = MediaPlayer.create(requireContext(),R.raw.empty_move)
        runningEggSound2 = MediaPlayer.create(requireContext(),R.raw.empty_move)
        runningEggSound3 = MediaPlayer.create(requireContext(),R.raw.empty_move)
        runningEggSound4 = MediaPlayer.create(requireContext(),R.raw.empty_move)
        runningEggSound5 = MediaPlayer.create(requireContext(),R.raw.empty_move)
    }

    override fun onResume() {
        super.onResume()
        checkGameState()
    }

    override fun onPause() {
        super.onPause()
        when(gameState){
            // if game is playing pause it
            Static.PLAY_A -> pauseGameA()
            Static.PLAY_B -> pauseGameB()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runningEggSound1?.stop()
        runningEggSound1?.release()
        runningEggSound1 = null

        runningEggSound2?.stop()
        runningEggSound2?.release()
        runningEggSound2 = null

        runningEggSound3?.stop()
        runningEggSound3?.release()
        runningEggSound3 = null

        runningEggSound4?.stop()
        runningEggSound4?.release()
        runningEggSound4 = null

        runningEggSound5?.stop()
        runningEggSound5?.release()
        runningEggSound5 = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    // check if there is stored game
    private fun checkGameState() {
        // read from shared preferences
        val gameState = if(loggedInStatus.loggedIn) Functions.checkUserGameStateFromSharedPreferences(requireContext(),loggedInStatus.userid) else Functions.checkGameStateFromSharedPreferences(requireContext())


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
                else -> {
                    // do nothing
                }
            }
        }
        else demoMode()
    }

    // set click listeners for all buttons
    @SuppressLint("ClickableViewAccessibility")
    private fun buttonsOnClickListeners(){

        binding.buttonTopLeft.setOnTouchListener { _, event ->
            if (playOrPause()){
                when (event.action){
                    MotionEvent.ACTION_DOWN -> {
                        basket = Static.LEFT_TOP
                        displayBasket()
                    }
                    else ->{ }
                }
            }
            true
        }

        binding.buttonBottomLeft.setOnTouchListener { _, event ->
            if (playOrPause()){
                when (event.action){
                    MotionEvent.ACTION_DOWN -> {
                        basket = Static.LEFT_BOTTOM
                        displayBasket()
                    }
                    else ->{
                    }
                }
            }
            true
        }

        binding.buttonBottomRight.setOnTouchListener { _, event ->
            if (playOrPause()){
                when (event.action){
                    MotionEvent.ACTION_DOWN -> {
                        basket = Static.RIGHT_BOTTOM
                        displayBasket()
                    }
                    else ->{
                    }
                }
            }
            true
        }

        binding.buttonTopRight.setOnTouchListener { _, event ->
            if (playOrPause()){
                when (event.action){
                    MotionEvent.ACTION_DOWN -> {
                        basket = Static.RIGHT_TOP
                        displayBasket()
                    }
                    else ->{
                    }
                }
            }
            true
        }

        binding.startA.setOnClickListener {
            loadAdvert()

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

        binding.startB.setOnClickListener {

            loadAdvert()

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

        binding.closeApp.setOnClickListener {
            activity?.finish()
        }

        binding.account.setOnClickListener {
            if(gameState==Static.DEMO||gameState==Static.PAUSE_A||gameState==Static.PAUSE_B){
                val action = MainFragmentDirections.actionMainFragmentToLoginFragment()
                findNavController().navigate(action)

            }
        }

        if(loggedInStatus.loggedIn) {
            binding.userID.text = Functions.checkUserNameFromSharedPreferences(requireContext(), loggedInStatus.userid)
        }
        else
        {
            binding.userID.text=getString(R.string.not_logged_in)
        }



    }

    // store points, faults and game mode to shared preferences
    private fun saveGameState() {
        val gameState = GameState(game.getScore(),game.getFault(),game.getGameMode())
        if(loggedInStatus.loggedIn){
            Functions.saveUserGameStateToSharedPreferences(requireContext(),loggedInStatus.userid,gameState)
        }else{
            Functions.saveGameStateToSharedPreferences(requireContext(),gameState)
        }


    }

    private fun unPauseGameB() {
        startGameB()
    }

    private fun pauseGameB() {
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

    private fun pauseB():Runnable = Runnable {
        view?.let {
            if (pauseState == Static.ON) {
                binding.startB.setImageDrawable(
                    StartButtonGreen(
                        requireContext(),
                        startButtonSize.width * screenUnit,
                        startButtonSize.height * screenUnit
                    )
                )
            } else {
                binding.startB.setImageDrawable(
                    StartButton(
                        requireContext(),
                        startButtonSize.width * screenUnit,
                        startButtonSize.height * screenUnit
                    )
                )
            }
            pauseState = !pauseState
            mHandlerPause.postDelayed(pauseB(), 500)

        }
    }

    // play game B
    private fun startGameB() {
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
        view?.let {
            binding.startB.setImageDrawable(
                StartButtonGreen(
                    requireContext(),
                    startButtonSize.width * screenUnit,
                    startButtonSize.height * screenUnit
                )
            )
        }
    }

    private fun unPauseGameA() {
        startGameA()
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
        mHandlerLostEgg.removeCallbacksAndMessages(null)
        clearAnimationFallenEgg()
        // save game state to shared preferences
        saveGameState()
        pauseA().run()
    }

    private fun pauseA():Runnable = Runnable {
        view?.let {
            if (pauseState == Static.ON) {
                binding.startA.setImageDrawable(
                    StartButtonGreen(
                        requireContext(),
                        startButtonSize.width * screenUnit,
                        startButtonSize.height * screenUnit
                    )
                )
            } else {
                binding.startA.setImageDrawable(
                    StartButton(
                        requireContext(),
                        startButtonSize.width * screenUnit,
                        startButtonSize.height * screenUnit
                    )
                )
            }
        }

        pauseState=!pauseState
        mHandlerPause.postDelayed(pauseA(),500)
    }

    // play game A
    private fun startGameA() {

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
        view?.let {
            binding.startA.setImageDrawable(
                StartButtonGreen(
                    requireContext(),
                    startButtonSize.width * screenUnit,
                    startButtonSize.height * screenUnit
                )
            )
        }
    }

    private fun clearAnimationFallenEgg() {
        val fallenEgg=FallenEgg()
        displayRunningChicken(fallenEgg)

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

    // display rabbit
    private fun displayRabbit(rabbitBoolean: Boolean){
        view?.let {
            binding.rabbit.setImageDrawable(
                if (rabbitBoolean) RabbitDrawable(
                    requireContext(),
                    rabbitSize.width,
                    true
                ) else RabbitDrawable(requireContext(), rabbitSize.width, false)
            )
        }
    }

    // display running chicken during animation
    private fun displayRunningChicken(fallenEgg: FallenEgg) {
        view?.let {
            binding.faultLeftFirst.setImageDrawable(
                if (fallenEgg.getFallenEgg(1, 0)) BrokenEggLeft(
                    requireContext(),
                    bottomFaultSizeFirst.width,
                    true
                ) else BrokenEggLeft(requireContext(), bottomFaultSizeFirst.width, false)
            )
            binding.faultLeftSecond.setImageDrawable(
                if (fallenEgg.getFallenEgg(
                        2,
                        0
                    )
                ) RunningChickenLeftFirst(
                    requireContext(),
                    bottomFaultSizeSmallDifferent.width,
                    true
                ) else RunningChickenLeftFirst(
                    requireContext(),
                    bottomFaultSizeSmallDifferent.width,
                    false
                )
            )
            binding.faultLeftThird.setImageDrawable(
                if (fallenEgg.getFallenEgg(
                        3,
                        0
                    )
                ) RunningChickenLeftTwo(
                    requireContext(),
                    bottomFaultSizeSmall.width,
                    true
                ) else RunningChickenLeftTwo(requireContext(), bottomFaultSizeSmall.width, false)
            )
            binding.faultLeftFourth.setImageDrawable(
                if (fallenEgg.getFallenEgg(
                        4,
                        0
                    )
                ) RunningChickenLeftThree(
                    requireContext(),
                    bottomFaultSizeSmall.width,
                    true
                ) else RunningChickenLeftThree(requireContext(), bottomFaultSizeSmall.width, false)
            )


            binding.faultRightFirst.setImageDrawable(
                if (fallenEgg.getFallenEgg(
                        1,
                        1
                    )
                ) BrokenEggRight(
                    requireContext(),
                    bottomFaultSizeFirst.width,
                    true
                ) else BrokenEggRight(requireContext(), bottomFaultSizeFirst.width, false)
            )
            binding.faultRightSecond.setImageDrawable(
                if (fallenEgg.getFallenEgg(
                        2,
                        1
                    )
                ) RunningChickenRightFirst(
                    requireContext(),
                    bottomFaultSizeSmallDifferent.width,
                    true
                ) else RunningChickenRightFirst(
                    requireContext(),
                    bottomFaultSizeSmallDifferent.width,
                    false
                )
            )
            binding.faultRightThird.setImageDrawable(
                if (fallenEgg.getFallenEgg(
                        3,
                        1
                    )
                ) RunningChickenRightTwo(
                    requireContext(),
                    bottomFaultSizeSmall.width,
                    true
                ) else RunningChickenRightTwo(requireContext(), bottomFaultSizeSmall.width, false)
            )
            binding.faultRightFourth.setImageDrawable(
                if (fallenEgg.getFallenEgg(
                        4,
                        1
                    )
                ) RunningChickenRightThree(
                    requireContext(),
                    bottomFaultSizeSmall.width,
                    true
                ) else RunningChickenRightThree(requireContext(), bottomFaultSizeSmall.width, false)
            )

        }
    }

    // game loop
    private fun gameLoop(): Runnable = Runnable {
        clearAnimationFallenEgg()
        // less then 1000 points
        if(game.underMaxScore()) {

            // make move down
//            eggCaught = game.moveDown()
            moveProduct = game.moveDownStep()

            // display move
            displayState()

            //sound of running eggs
//            makeSoundRunningEggs()
            if(moveProduct.sound) {
                makeSoundRunningEggStep(moveProduct.step)
            }


            //check fault, egg in basket
//            checkNextMove(eggCaught)
            if(moveProduct.step==Static.GAME_SIZE-1){
                stopAllSounds()
                checkNextMoveStep(moveProduct)
            }else{
                mHandler.postDelayed(gameLoop(),delayStep())
            }


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
            winningSound = MediaPlayer.create(requireContext(),R.raw.fault)
            winningSound?.start()

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
            if(mInterstitialAd != null){
                mInterstitialAd?.show(requireActivity())
            }
        }

    }

    // demo mode
    private fun demoMode() {

        binding.digitOne.visibility = View.VISIBLE
        binding.digitTen.visibility = View.VISIBLE
        binding.digitHundred.visibility = View.VISIBLE
        binding.digitThousand.visibility = View.VISIBLE
        displayScoreImageViews(0,0,0,0)

        view?.let {
            binding.startA.setImageDrawable(
                StartButton(
                    requireContext(),
                    startButtonSize.width * screenUnit,
                    startButtonSize.height * screenUnit
                )
            )
            binding.startB.setImageDrawable(
                StartButton(
                    requireContext(),
                    startButtonSize.width * screenUnit,
                    startButtonSize.height * screenUnit
                )
            )
        }
        mHandler.removeCallbacksAndMessages(null)
        mHandlerDemo.removeCallbacksAndMessages(null)
        mHandlerPause.removeCallbacksAndMessages(null)
        mHandlerWin.removeCallbacksAndMessages(null)
        mHandlerRabbit.removeCallbacksAndMessages(null)
        mHandlerFlash.removeCallbacksAndMessages(null)
        mHandlerLostEgg.removeCallbacksAndMessages(null)
        mHandlerHighScore.removeCallbacksAndMessages(null)
        gameState=Static.DEMO
        clearAnimationFallenEgg()
        game.clearEverything()
        displayState()
        displayBasket()
        updateFaultsView()

        loopCounter = 0
        demo().run()

    }

    // demo runnable
    private fun demo():Runnable = Runnable {
//        game.moveDownDemo()
        game.moveDownDemoStep()
        displayState()
        displayDemoBasket()
        displayRabbit(loopCounter%30>12)
        loopCounter+=1
        mHandlerDemo.postDelayed(demo(),200)
    }

    // display demo basket
    private fun displayDemoBasket() {
        view?.let {
            if (game.position[Static.LEFT_TOP]) {
                binding.leftWolf.setImageDrawable(
                    WolfLeft(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_UP
                    )
                )
                binding.rightWolf.setImageDrawable(
                    WolfRight(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_SHADOW
                    )
                )
            }

            if (game.position[Static.LEFT_BOTTOM]) {
                binding.leftWolf.setImageDrawable(
                    WolfLeft(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_DOWN
                    )
                )
                binding.rightWolf.setImageDrawable(
                    WolfRight(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_SHADOW
                    )
                )
            }
            if (game.position[Static.RIGHT_BOTTOM]) {
                binding.leftWolf.setImageDrawable(
                    WolfLeft(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_SHADOW
                    )
                )
                binding.rightWolf.setImageDrawable(
                    WolfRight(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_DOWN
                    )
                )
            }
            if (game.position[Static.RIGHT_TOP]) {
                binding.leftWolf.setImageDrawable(
                    WolfLeft(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_SHADOW
                    )
                )
                binding.rightWolf.setImageDrawable(
                    WolfRight(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_UP
                    )
                )
            }
        }
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

    // save points game B after win (1000)
    private fun savePointsWinB() {
        if(loggedInStatus.loggedIn){
            Functions.savePointsWinBToSharedPreferences(requireContext(),loggedInStatus.userid,game.getScore())

            //TODO!!!
//            saveUserToFirebaseDatabase()


        }
    }

    // save points game A after win (1000)
    private fun savePointsWinA() {
        if(loggedInStatus.loggedIn){
            Functions.savePointsWinAToSharedPreferences(requireContext(),loggedInStatus.userid,game.getScore())

            //TODO!!!
            //saveUserToFirebaseDatabase()

        }

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

    // clear game state in shared preferences
    private fun clearSavedGame() {

        if(loggedInStatus.loggedIn){
            Functions.clearSavedUserGameInSharedPreferences(requireContext(),loggedInStatus.userid)
        }else{
            Functions.clearSavedGameInSharedPreferences(requireContext())
        }

    }

    // if egg at last position check if it is in the basket
    private fun checkNextMoveStep(moveProduct: MoveProduct) {

        // egg in basket or no egg
        if(moveProduct.logicSum==1){

            // egg has been caught
            if(moveProduct.logicProduct==1){

                GlobalScope.launch(Dispatchers.Default){
                    caughtEggSound = MediaPlayer.create(requireContext(),R.raw.score_move)
                    caughtEggSound?.start()
                }

                updateScoreTextView()

                // clear faults when get 200 or 500 points
                if(game.getScore()==200||game.getScore()==500){
                    game.clearFaults()
                    updateFaultsView()
                }
            }

            mHandler.postDelayed(gameLoop(),delayStep())
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

                lostEggAnimation(moveProduct.positionFallenEgg)
            }else{
                stopAllSounds()
                faultSound = MediaPlayer.create(requireContext(),R.raw.fault)
                faultSound?.start()
                lostEggAnimationEndGame(moveProduct.positionFallenEgg)

            }
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
            if(mInterstitialAd != null){
                mInterstitialAd?.show(requireActivity())
            }
        }
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

    // save points game B after lose
    private fun savePointsLoseB():Boolean {
        return if(loggedInStatus.loggedIn){
            val newHighScore = Functions.savePointsLoseBToSharedPreferences(requireContext(),loggedInStatus.userid,game.getScore())
            //TODO!!
//            saveUserToFirebaseDatabase()
            newHighScore
        }else{
            false
        }
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

    private fun highScore(highScore:Int):Runnable = Runnable {
        if(highScoreState==Static.ON){
            binding.digitOne.visibility = View.GONE
            binding.digitTen.visibility = View.GONE
            binding.digitHundred.visibility = View.GONE
            binding.digitThousand.visibility = View.GONE
        }else {
            binding.digitOne.visibility = View.VISIBLE
            binding.digitTen.visibility = View.VISIBLE
            binding.digitHundred.visibility = View.VISIBLE
            binding.digitThousand.visibility = View.VISIBLE
        }
        highScoreState = !highScoreState
        mHandlerHighScore.postDelayed(highScore(highScore),500)
    }

    // save points game A after lose
    private fun savePointsLoseA():Boolean {
        return if(loggedInStatus.loggedIn){
            val newHighScore = Functions.savePointsLoseAToSharedPreferences(requireContext(),loggedInStatus.userid,game.getScore())
            //TODO!!!
//            saveUserToFirebaseDatabase()
            newHighScore
        }else{
            false
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

    // fallen egg runnable
    private fun fallenEgg(fallenEgg: FallenEgg):Runnable = Runnable {
        val finished = fallenEgg.moveDown()
        stopAllSounds()
        if(chickenPlace==0){
            brokenEggSound = MediaPlayer.create(requireContext(),R.raw.broken_egg)
            brokenEggSound?.start()
        }

        displayRunningChicken(fallenEgg)
        chickenPlace+=1

        if(!finished){
            mHandlerLostEgg.postDelayed(fallenEgg(fallenEgg),1000)
        }
        else{
            mHandlerLostEgg.removeCallbacksAndMessages(null)
            game.setGeneratingEggStraightAway()
            mHandler.postDelayed(gameLoop(),delay())
        }
    }

    // lost egg animation
    private fun lostEggAnimation(positionFallenEgg: Int) {

        val fallenEgg = FallenEgg()
        fallenEgg.setFallenEgg(positionFallenEgg/2)
        fallenEgg(fallenEgg).run()


    }

    // set delay in game loop
    private fun delayStep():Long = when(game.getScore()){
        in 0..100 -> 240
//        in 0..100 -> 105
        in 101..200 -> 214
        in 201..300 -> if(game.getGameMode()==Static.GAME_A) 205 else 191
        in 301..400 -> if(game.getGameMode()==Static.GAME_A) 191 else 178
        in 401..500 -> if(game.getGameMode()==Static.GAME_A) 178 else 165
        in 501..600 -> if(game.getGameMode()==Static.GAME_A) 165 else 154
        in 601..700 -> if(game.getGameMode()==Static.GAME_A) 154 else 141
        in 701..800 -> if(game.getGameMode()==Static.GAME_A) 141 else 130
        in 801..900 -> if(game.getGameMode()==Static.GAME_A) 130 else 118
        else -> if(game.getGameMode()==Static.GAME_A) 118 else 105
    }

    private fun stopAllSounds() {
        try{

            runningEggFirstSound?.stop()
            runningEggFirstSound?.release()
            runningEggFirstSound=null

            caughtEggSound?.stop()
            caughtEggSound?.release()
            caughtEggSound=null

            faultSound?.stop()
            faultSound?.release()
            faultSound=null

            brokenEggSound?.stop()
            brokenEggSound?.release()
            brokenEggSound=null

        }
        catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun makeSoundRunningEggStep(step:Int){
        GlobalScope.launch(Dispatchers.Default){
            when(step){
                1->{
                    runningEggSound1?.start()
                }
                2->{
                    runningEggSound2?.start()
                }
                3->{
                    runningEggSound3?.start()
                }
                4->{
                    runningEggSound4?.start()
                }
                5->{
                    runningEggSound5?.start()
                }
            }
        }
    }


    // update score in text view
    private fun updateScoreTextView(){

        val thousand = game.getScore()/1000
        var rest = game.getScore()%1000
        val hundred = rest/100
        rest %= 100
        val ten = rest/10
        rest %= 10
        val one = rest



        when(game.getScore()){
            in 0..9 -> displayScoreImageViews(oneDigit = one,tenDigit = null,hundredDigit = null,thousandDigit = null)
            in 10..99 -> displayScoreImageViews(oneDigit = one,tenDigit = ten,hundredDigit = null,thousandDigit = null)
            in 100..999 -> displayScoreImageViews(oneDigit = one,tenDigit = ten,hundredDigit = hundred,thousandDigit = null)
            1000 -> displayScoreImageViews(oneDigit = one,tenDigit = ten,hundredDigit = hundred,thousandDigit = thousand)
        }
    }

    private fun displayScoreImageViews(oneDigit:Int?,tenDigit:Int?,hundredDigit:Int?, thousandDigit:Int?) {

        view?.let {
            if (thousandDigit != null) binding.digitThousand.setImageDrawable(
                Digit(
                    requireContext(),
                    (digitSize.width * screenUnit).toInt(), thousandDigit
                )
            )
            else binding.digitThousand.setImageDrawable(
                Digit(
                    requireContext(),
                    (digitSize.width * screenUnit).toInt(), null
                )
            )

            if (hundredDigit != null) binding.digitHundred.setImageDrawable(
                Digit(
                    requireContext(),
                    (digitSize.width * screenUnit).toInt(), hundredDigit
                )
            )
            else binding.digitHundred.setImageDrawable(
                Digit(
                    requireContext(),
                    (digitSize.width * screenUnit).toInt(), null
                )
            )

            if (tenDigit != null) binding.digitTen.setImageDrawable(
                Digit(
                    requireContext(),
                    (digitSize.width * screenUnit).toInt(), tenDigit
                )
            )
            else binding.digitTen.setImageDrawable(
                Digit(
                    requireContext(),
                    (digitSize.width * screenUnit).toInt(), null
                )
            )

            if (oneDigit != null) binding.digitOne.setImageDrawable(
                Digit(
                    requireContext(),
                    (digitSize.width * screenUnit).toInt(), oneDigit
                )
            )
            else binding.digitOne.setImageDrawable(
                Digit(
                    requireContext(),
                    (digitSize.width * screenUnit).toInt(), null
                )
            )
        }

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

    private fun zeroFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        view?.let {
        binding.rightFault.setImageDrawable(FaultTopDrawable(requireContext(),faultSize.height,false))
        binding.middleFault.setImageDrawable(FaultTopDrawable(requireContext(),faultSize.height,false))
        binding.leftFault.setImageDrawable(FaultTopDrawable(requireContext(),faultSize.height,false))
        }
    }

    private fun oneFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        flashFault(binding.rightFault).run()
        view?.let {
            binding.leftFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    false
                )
            )
            binding.middleFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    false
                )
            )
        }
    }

    private fun twoFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        view?.let {
            binding.leftFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    false
                )
            )
            binding.middleFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    false
                )
            )
            binding.rightFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    true
                )
            )
        }
    }

    private fun threeFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        flashFault(binding.middleFault).run()
        view?.let {
            binding.leftFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    false
                )
            )
            binding.rightFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    true
                )
            )
        }
    }

    private fun fourFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        view?.let {
            binding.leftFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    false
                )
            )
            binding.middleFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    true
                )
            )
            binding.rightFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    true
                )
            )
        }
    }

    private fun fiveFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        flashFault(binding.leftFault).run()
        view?.let {
            binding.middleFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    true
                )
            )
            binding.rightFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    true
                )
            )
        }
    }

    private fun sixFault(){
        mHandlerFlash.removeCallbacksAndMessages(null)
        view?.let {
            binding.leftFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    true
                )
            )
            binding.middleFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    true
                )
            )
            binding.rightFault.setImageDrawable(
                FaultTopDrawable(
                    requireContext(),
                    faultSize.height,
                    true
                )
            )
        }
    }

    // flashing fault
    private fun flashFault(imageView: ImageView):Runnable = Runnable {
        if(faultFlash==Static.ON){
            view?.let {
                imageView.setImageDrawable(
                    FaultTopDrawable(
                        requireContext(),
                        faultSize.height,
                        true
                    )
                )
            }
                faultFlash=Static.OFF
            mHandlerFlash.postDelayed(flashFault(imageView),500)
        }else{
            view?.let {
                imageView.setImageDrawable(
                    FaultTopDrawable(
                        requireContext(),
                        faultSize.height,
                        false
                    )
                )
            }
                faultFlash=Static.ON
            mHandlerFlash.postDelayed(flashFault(imageView),500)
        }
    }

    private fun loadAdvert() {
        val adRequest = AdRequest.Builder().build()
        val adId = getString(R.string.admob_big)
        InterstitialAd.load(requireContext(),adId,adRequest, object  : InterstitialAdLoadCallback(){
            override fun onAdFailedToLoad(adError: LoadAdError) {
                loadAdvert()
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })

    }

    // return boolean if game is played or paused
    private fun playOrPause(): Boolean {
        return gameState==Static.PLAY_A||gameState==Static.PAUSE_A||gameState==Static.PLAY_B||gameState==Static.PAUSE_B

    }

    // first displaying array (empty) only in onCreate
    private fun updateArray(){
        game.clearEverything()
        displayState()
    }

    // display position of basket
    private fun displayBasket(){
        game.setBasket(basket)

        view?.let {
            if (game.position[Static.LEFT_TOP]) {
                binding.leftWolf.setImageDrawable(
                    WolfLeft(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_UP
                    )
                )
                binding.rightWolf.setImageDrawable(
                    WolfRight(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_SHADOW
                    )
                )
            }
            if (game.position[Static.LEFT_BOTTOM]) {
                binding.leftWolf.setImageDrawable(
                    WolfLeft(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_DOWN
                    )
                )
                binding.rightWolf.setImageDrawable(
                    WolfRight(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_SHADOW
                    )
                )
            }
            if (game.position[Static.RIGHT_BOTTOM]) {
                binding.leftWolf.setImageDrawable(
                    WolfLeft(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_SHADOW
                    )
                )
                binding.rightWolf.setImageDrawable(
                    WolfRight(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_DOWN
                    )
                )
            }
            if (game.position[Static.RIGHT_TOP]) {
                binding.leftWolf.setImageDrawable(
                    WolfLeft(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_SHADOW
                    )
                )
                binding.rightWolf.setImageDrawable(
                    WolfRight(
                        requireContext(),
                        wolfSize.width,
                        Static.WOLF_UP
                    )
                )
            }
        }
    }

    private fun checkLoggedInState() {
        loggedInStatus = Functions.readLoggedInStatusFromSharedPreferences(requireContext())
    }

    // display all fallen eggs
    private fun displayState(){
        view?.let {
            binding.eggTopLeftFirst.setImageDrawable(
                if (game.displayCell(
                        0,
                        Static.LEFT_TOP
                    )
                ) NormalEggDrawable(
                    requireContext(),
                    screenUnit * eggSize.width,
                    true
                ) else NormalEggDrawable(requireContext(), screenUnit * eggSize.width, false)
            )
            binding.eggTopLeftSecond.setImageDrawable(
                if (game.displayCell(
                        1,
                        Static.LEFT_TOP
                    )
                ) EggPlus45(requireContext(), screenUnit * eggSize.width, true) else EggPlus45(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggTopLeftThird.setImageDrawable(
                if (game.displayCell(
                        2,
                        Static.LEFT_TOP
                    )
                ) EggPlus90(requireContext(), screenUnit * eggSize.width, true) else EggPlus90(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggTopLeftFourth.setImageDrawable(
                if (game.displayCell(
                        3,
                        Static.LEFT_TOP
                    )
                ) EggPlus135(requireContext(), screenUnit * eggSize.width, true) else EggPlus135(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggTopLeftFifth.setImageDrawable(
                if (game.displayCell(
                        4,
                        Static.LEFT_TOP
                    )
                ) EggPlus225(requireContext(), screenUnit * eggSize.width, true) else EggPlus225(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )


            binding.eggBottomLeftFirst.setImageDrawable(
                if (game.displayCell(
                        0,
                        Static.LEFT_BOTTOM
                    )
                ) NormalEggDrawable(
                    requireContext(),
                    screenUnit * eggSize.width,
                    true
                ) else NormalEggDrawable(requireContext(), screenUnit * eggSize.width, false)
            )
            binding.eggBottomLeftSecond.setImageDrawable(
                if (game.displayCell(
                        1,
                        Static.LEFT_BOTTOM
                    )
                ) EggPlus45(requireContext(), screenUnit * eggSize.width, true) else EggPlus45(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggBottomLeftThird.setImageDrawable(
                if (game.displayCell(
                        2,
                        Static.LEFT_BOTTOM
                    )
                ) EggPlus90(requireContext(), screenUnit * eggSize.width, true) else EggPlus90(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggBottomLeftFourth.setImageDrawable(
                if (game.displayCell(
                        3,
                        Static.LEFT_BOTTOM
                    )
                ) EggPlus135(requireContext(), screenUnit * eggSize.width, true) else EggPlus135(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggBottomLeftFifth.setImageDrawable(
                if (game.displayCell(
                        4,
                        Static.LEFT_BOTTOM
                    )
                ) EggPlus225(requireContext(), screenUnit * eggSize.width, true) else EggPlus225(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )


            binding.eggBottomRightFirst.setImageDrawable(
                if (game.displayCell(
                        0,
                        Static.RIGHT_BOTTOM
                    )
                ) NormalEggDrawable(
                    requireContext(),
                    screenUnit * eggSize.width,
                    true
                ) else NormalEggDrawable(requireContext(), screenUnit * eggSize.width, false)
            )
            binding.eggBottomRightSecond.setImageDrawable(
                if (game.displayCell(
                        1,
                        Static.RIGHT_BOTTOM
                    )
                ) EggMinus45(requireContext(), screenUnit * eggSize.width, true) else EggMinus45(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggBottomRightThird.setImageDrawable(
                if (game.displayCell(
                        2,
                        Static.RIGHT_BOTTOM
                    )
                ) EggMinus90(requireContext(), screenUnit * eggSize.width, true) else EggMinus90(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggBottomRightFourth.setImageDrawable(
                if (game.displayCell(
                        3,
                        Static.RIGHT_BOTTOM
                    )
                ) EggPlus225(requireContext(), screenUnit * eggSize.width, true) else EggPlus225(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggBottomRightFifth.setImageDrawable(
                if (game.displayCell(
                        4,
                        Static.RIGHT_BOTTOM
                    )
                ) EggPlus135(requireContext(), screenUnit * eggSize.width, true) else EggPlus135(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )


            binding.eggTopRightFirst.setImageDrawable(
                if (game.displayCell(
                        0,
                        Static.RIGHT_TOP
                    )
                ) NormalEggDrawable(
                    requireContext(),
                    screenUnit * eggSize.width,
                    true
                ) else NormalEggDrawable(requireContext(), screenUnit * eggSize.width, false)
            )
            binding.eggTopRightSecond.setImageDrawable(
                if (game.displayCell(
                        1,
                        Static.RIGHT_TOP
                    )
                ) EggMinus45(requireContext(), screenUnit * eggSize.width, true) else EggMinus45(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggTopRightThird.setImageDrawable(
                if (game.displayCell(
                        2,
                        Static.RIGHT_TOP
                    )
                ) EggMinus90(requireContext(), screenUnit * eggSize.width, true) else EggMinus90(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggTopRightFourth.setImageDrawable(
                if (game.displayCell(
                        3,
                        Static.RIGHT_TOP
                    )
                ) EggPlus225(requireContext(), screenUnit * eggSize.width, true) else EggPlus225(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
            binding.eggTopRightFifth.setImageDrawable(
                if (game.displayCell(
                        4,
                        Static.RIGHT_TOP
                    )
                ) EggPlus135(requireContext(), screenUnit * eggSize.width, true) else EggPlus135(
                    requireContext(),
                    screenUnit * eggSize.width,
                    false
                )
            )
        }

    }

    // constraintSet
    private fun makeUI() {

        getScreenHighAndWidth()

        setViewSizes()

        setDrawable()

        val eggJumpDown = screenUnit*0.25
        val set = ConstraintSet()
        set.clone(binding.mainFragment)


        set.connect(binding.mainScreenContainer.id,
            ConstraintSet.LEFT,binding.mainFragment.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.mainScreenContainer.id,
            ConstraintSet.RIGHT,binding.mainFragment.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.mainScreenContainer.id,
            ConstraintSet.TOP,binding.mainFragment.id,
            ConstraintSet.TOP,0)
        set.connect(binding.mainScreenContainer.id,
            ConstraintSet.BOTTOM,binding.mainFragment.id,
            ConstraintSet.BOTTOM,0)


        set.connect(binding.screenBack.id,
            ConstraintSet.LEFT,binding.mainScreenContainer.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.screenBack.id,
            ConstraintSet.RIGHT,binding.mainScreenContainer.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.screenBack.id,
            ConstraintSet.TOP,binding.mainScreenContainer.id,
            ConstraintSet.TOP,0)
        set.connect(binding.screenBack.id,
            ConstraintSet.BOTTOM,binding.mainScreenContainer.id,
            ConstraintSet.BOTTOM,0)

        set.connect(binding.eggTopLeftFirst.id,
            ConstraintSet.LEFT,binding.screenBack.id,
            ConstraintSet.LEFT,screenUnit+screenUnit/2)
        set.connect(binding.eggTopLeftFirst.id,
            ConstraintSet.TOP,binding.screenBack.id,
            ConstraintSet.TOP, (screenUnit*1.6).toInt())

        set.connect(binding.eggTopLeftSecond.id,
            ConstraintSet.LEFT,binding.eggTopLeftFirst.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.eggTopLeftSecond.id,
            ConstraintSet.TOP,binding.eggTopLeftFirst.id,
            ConstraintSet.TOP, eggJumpDown.toInt())

        set.connect(binding.eggTopLeftThird.id,
            ConstraintSet.LEFT,binding.eggTopLeftSecond.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.eggTopLeftThird.id,
            ConstraintSet.TOP,binding.eggTopLeftSecond.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.eggTopLeftFourth.id,
            ConstraintSet.LEFT,binding.eggTopLeftThird.id,
            ConstraintSet.RIGHT, 0)
        set.connect(binding.eggTopLeftFourth.id,
            ConstraintSet.TOP,binding.eggTopLeftThird.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.eggTopLeftFifth.id,
            ConstraintSet.LEFT,binding.eggTopLeftThird.id,
            ConstraintSet.RIGHT,(eggSize.width*screenUnit*0.9).toInt())
        set.connect(binding.eggTopLeftFifth.id,
            ConstraintSet.TOP,binding.eggTopLeftFourth.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.eggBottomLeftFirst.id,
            ConstraintSet.LEFT,binding.screenBack.id,
            ConstraintSet.LEFT,screenUnit+screenUnit/2)
        set.connect(binding.eggBottomLeftFirst.id,
            ConstraintSet.TOP,binding.screenBack.id,
            ConstraintSet.TOP, (screenUnit*3.1).toInt())

        set.connect(binding.eggBottomLeftSecond.id,
            ConstraintSet.LEFT,binding.eggBottomLeftFirst.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.eggBottomLeftSecond.id,
            ConstraintSet.TOP,binding.eggBottomLeftFirst.id,
            ConstraintSet.TOP, eggJumpDown.toInt())

        set.connect(binding.eggBottomLeftThird.id,
            ConstraintSet.LEFT,binding.eggBottomLeftSecond.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.eggBottomLeftThird.id,
            ConstraintSet.TOP,binding.eggBottomLeftSecond.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.eggBottomLeftFourth.id,
            ConstraintSet.LEFT,binding.eggBottomLeftThird.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.eggBottomLeftFourth.id,
            ConstraintSet.TOP,binding.eggBottomLeftThird.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.eggBottomLeftFifth.id,
            ConstraintSet.LEFT,binding.eggBottomLeftFourth.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.eggBottomLeftFifth.id,
            ConstraintSet.TOP,binding.eggBottomLeftFourth.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.eggTopRightFirst.id,
            ConstraintSet.RIGHT,binding.screenBack.id,
            ConstraintSet.RIGHT,screenUnit+screenUnit/2)
        set.connect(binding.eggTopRightFirst.id,
            ConstraintSet.TOP,binding.screenBack.id,
            ConstraintSet.TOP, (screenUnit*1.6).toInt())

        set.connect(binding.eggTopRightSecond.id,
            ConstraintSet.RIGHT,binding.eggTopRightFirst.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.eggTopRightSecond.id,
            ConstraintSet.TOP,binding.eggTopRightFirst.id,
            ConstraintSet.TOP, eggJumpDown.toInt())

        set.connect(binding.eggTopRightThird.id,
            ConstraintSet.RIGHT,binding.eggTopRightSecond.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.eggTopRightThird.id,
            ConstraintSet.TOP,binding.eggTopRightSecond.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.eggTopRightFourth.id,
            ConstraintSet.RIGHT,binding.eggTopRightThird.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.eggTopRightFourth.id,
            ConstraintSet.TOP,binding.eggTopRightThird.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.eggTopRightFifth.id,
            ConstraintSet.RIGHT,binding.eggTopRightThird.id,
            ConstraintSet.LEFT, (eggSize.width*screenUnit*0.9).toInt())
        set.connect(binding.eggTopRightFifth.id,
            ConstraintSet.TOP,binding.eggTopRightFourth.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.eggBottomRightFirst.id,
            ConstraintSet.RIGHT,binding.screenBack.id,
            ConstraintSet.RIGHT,screenUnit+screenUnit/2)
        set.connect(binding.eggBottomRightFirst.id,
            ConstraintSet.TOP,binding.screenBack.id,
            ConstraintSet.TOP, (screenUnit*3.1).toInt())

        set.connect(binding.eggBottomRightSecond.id,
            ConstraintSet.RIGHT,binding.eggBottomRightFirst.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.eggBottomRightSecond.id,
            ConstraintSet.TOP,binding.eggBottomRightFirst.id,
            ConstraintSet.TOP, eggJumpDown.toInt())

        set.connect(binding.eggBottomRightThird.id,
            ConstraintSet.RIGHT,binding.eggBottomRightSecond.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.eggBottomRightThird.id,
            ConstraintSet.TOP,binding.eggBottomRightSecond.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.eggBottomRightFourth.id,
            ConstraintSet.RIGHT,binding.eggBottomRightThird.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.eggBottomRightFourth.id,
            ConstraintSet.TOP,binding.eggBottomRightThird.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.eggBottomRightFifth.id,
            ConstraintSet.RIGHT,binding.eggBottomRightFourth.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.eggBottomRightFifth.id,
            ConstraintSet.TOP,binding.eggBottomRightFourth.id,
            ConstraintSet.TOP,eggJumpDown.toInt())

        set.connect(binding.digitEmpty.id,
            ConstraintSet.TOP,binding.screenBack.id,
            ConstraintSet.TOP, (screenUnit*0.9).toInt())
        set.connect(binding.digitEmpty.id,
            ConstraintSet.LEFT,binding.screenBack.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.digitEmpty.id,
            ConstraintSet.RIGHT,binding.screenBack.id,
            ConstraintSet.RIGHT,0)

        set.connect(binding.digitTen.id,
            ConstraintSet.TOP,binding.digitEmpty.id,
            ConstraintSet.TOP, 0)
        set.connect(binding.digitTen.id,
            ConstraintSet.LEFT,binding.digitEmpty.id,
            ConstraintSet.RIGHT, (screenUnit*0.05).toInt())

        set.connect(binding.digitOne.id,
            ConstraintSet.TOP,binding.digitEmpty.id,
            ConstraintSet.TOP,0)
        set.connect(binding.digitOne.id,
            ConstraintSet.LEFT,binding.digitTen.id,
            ConstraintSet.RIGHT, (screenUnit*0.1).toInt())

        set.connect(binding.digitHundred.id,
            ConstraintSet.TOP,binding.digitEmpty.id,
            ConstraintSet.TOP,0)
        set.connect(binding.digitHundred.id,
            ConstraintSet.RIGHT,binding.digitEmpty.id,
            ConstraintSet.LEFT, (screenUnit*0.05).toInt())

        set.connect(binding.digitThousand.id,
            ConstraintSet.TOP,binding.digitEmpty.id,
            ConstraintSet.TOP,0)
        set.connect(binding.digitThousand.id,
            ConstraintSet.RIGHT,binding.digitHundred.id,
            ConstraintSet.LEFT, (screenUnit*0.1).toInt())

        set.connect(binding.buttonBottomLeft.id,
            ConstraintSet.RIGHT,binding.screenBack.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.buttonBottomLeft.id,
            ConstraintSet.LEFT,binding.mainScreenContainer.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.buttonBottomLeft.id,
            ConstraintSet.BOTTOM,binding.mainScreenContainer.id,
            ConstraintSet.BOTTOM,screenUnit)

        set.connect(binding.buttonTopLeft.id,
            ConstraintSet.RIGHT,binding.screenBack.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.buttonTopLeft.id,
            ConstraintSet.LEFT,binding.mainScreenContainer.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.buttonTopLeft.id,
            ConstraintSet.BOTTOM,binding.buttonBottomLeft.id,
            ConstraintSet.TOP,screenUnit)

        set.connect(binding.buttonBottomRight.id,
            ConstraintSet.LEFT,binding.screenBack.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.buttonBottomRight.id,
            ConstraintSet.RIGHT,binding.mainScreenContainer.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.buttonBottomRight.id,
            ConstraintSet.BOTTOM,binding.mainScreenContainer.id,
            ConstraintSet.BOTTOM,screenUnit)

        set.connect(binding.buttonTopRight.id,
            ConstraintSet.LEFT,binding.screenBack.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.buttonTopRight.id,
            ConstraintSet.RIGHT,binding.mainScreenContainer.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.buttonTopRight.id,
            ConstraintSet.BOTTOM,binding.buttonBottomRight.id,
            ConstraintSet.TOP,screenUnit)

        set.connect(binding.startA.id,
            ConstraintSet.LEFT,binding.buttonTopRight.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.startA.id,
            ConstraintSet.TOP,binding.mainScreenContainer.id,
            ConstraintSet.TOP,screenUnit)

        set.connect(binding.startB.id,
            ConstraintSet.LEFT,binding.buttonTopRight.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.startB.id,
            ConstraintSet.TOP,binding.startA.id,
            ConstraintSet.BOTTOM,screenUnit/2)

        set.connect(binding.letterA.id, ConstraintSet.LEFT,binding.startA.id, ConstraintSet.LEFT,0)
        set.connect(binding.letterA.id, ConstraintSet.RIGHT,binding.startA.id, ConstraintSet.RIGHT,0)
        set.connect(binding.letterA.id,
            ConstraintSet.BOTTOM,binding.startA.id,
            ConstraintSet.BOTTOM,screenUnit)

        set.connect(binding.letterB.id, ConstraintSet.LEFT,binding.startB.id, ConstraintSet.LEFT,0)
        set.connect(binding.letterB.id, ConstraintSet.RIGHT,binding.startB.id, ConstraintSet.RIGHT,0)
        set.connect(binding.letterB.id,
            ConstraintSet.BOTTOM,binding.startB.id,
            ConstraintSet.BOTTOM,screenUnit)

        set.connect(binding.closeApp.id,
            ConstraintSet.RIGHT,binding.buttonTopLeft.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.closeApp.id, ConstraintSet.TOP,binding.startA.id, ConstraintSet.TOP,0)

        set.connect(binding.exit.id, ConstraintSet.LEFT,binding.closeApp.id, ConstraintSet.LEFT,0)
        set.connect(binding.exit.id, ConstraintSet.RIGHT,binding.closeApp.id, ConstraintSet.RIGHT,0)
        set.connect(binding.exit.id,
            ConstraintSet.BOTTOM,binding.closeApp.id,
            ConstraintSet.BOTTOM,screenUnit)

        set.connect(binding.account.id,
            ConstraintSet.TOP,binding.mainScreenContainer.id,
            ConstraintSet.TOP,0)
        set.connect(binding.account.id,
            ConstraintSet.RIGHT,binding.screenBack.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.account.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.TOP,0)

        set.connect(binding.userID.id,
            ConstraintSet.TOP,binding.mainScreenContainer.id,
            ConstraintSet.TOP,0)
        set.connect(binding.userID.id,
            ConstraintSet.LEFT,binding.screenBack.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.userID.id,
            ConstraintSet.RIGHT,binding.screenBack.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.userID.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.TOP,0)

        set.connect(binding.rabbit.id, ConstraintSet.TOP,binding.screenBack.id, ConstraintSet.TOP, (screenUnit*0.8).toInt())
        set.connect(binding.rabbit.id, ConstraintSet.LEFT,binding.screenBack.id, ConstraintSet.LEFT,
            (2.9*screenUnit).toInt()
        )

        set.connect(binding.middleFault.id,
            ConstraintSet.TOP,binding.screenBack.id,
            ConstraintSet.TOP, (1.6*screenUnit).toInt())
        set.connect(binding.middleFault.id,
            ConstraintSet.LEFT,binding.screenBack.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.middleFault.id,
            ConstraintSet.RIGHT,binding.screenBack.id,
            ConstraintSet.RIGHT,0)

        set.connect(binding.leftFault.id,
            ConstraintSet.TOP,binding.middleFault.id,
            ConstraintSet.TOP,0)
        set.connect(binding.leftFault.id,
            ConstraintSet.RIGHT,binding.middleFault.id,
            ConstraintSet.LEFT, 0)

        set.connect(binding.rightFault.id,
            ConstraintSet.TOP,binding.middleFault.id,
            ConstraintSet.TOP,0)
        set.connect(binding.rightFault.id,
            ConstraintSet.LEFT,binding.middleFault.id,
            ConstraintSet.RIGHT, 0)

        set.connect(binding.faultLeftSecond.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.BOTTOM,(screenUnit*1.5).toInt())
        set.connect(binding.faultLeftSecond.id,
            ConstraintSet.LEFT,binding.faultLeftThird.id,
            ConstraintSet.RIGHT,(screenUnit*0.1).toInt())

        set.connect(binding.faultLeftThird.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.BOTTOM,(screenUnit*1.5).toInt())
        set.connect(binding.faultLeftThird.id,
            ConstraintSet.LEFT,binding.faultLeftFourth.id,
            ConstraintSet.RIGHT,(screenUnit*0.1).toInt())

        set.connect(binding.faultLeftFourth.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.BOTTOM, (screenUnit*1.5).toInt())
        set.connect(binding.faultLeftFourth.id,
            ConstraintSet.LEFT,binding.screenBack.id,
            ConstraintSet.LEFT,(screenUnit*0.9).toInt())

        set.connect(binding.faultLeftFirst.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.BOTTOM, (screenUnit*0.83).toInt())
        set.connect(binding.faultLeftFirst.id,
            ConstraintSet.LEFT,binding.faultLeftSecond.id,
            ConstraintSet.RIGHT,(screenUnit*0.2).toInt())

        set.connect(binding.faultRightSecond.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.BOTTOM,(screenUnit*1.5).toInt())
        set.connect(binding.faultRightSecond.id,
            ConstraintSet.RIGHT,binding.faultRightThird.id,
            ConstraintSet.LEFT,(screenUnit*0.1).toInt())

        set.connect(binding.faultRightThird.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.BOTTOM,(screenUnit*1.5).toInt())
        set.connect(binding.faultRightThird.id,
            ConstraintSet.RIGHT,binding.faultRightFourth.id,
            ConstraintSet.LEFT,(screenUnit*0.1).toInt())

        set.connect(binding.faultRightFourth.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.BOTTOM, (screenUnit*1.5).toInt())
        set.connect(binding.faultRightFourth.id,
            ConstraintSet.RIGHT,binding.screenBack.id,
            ConstraintSet.RIGHT,(screenUnit*0.9).toInt())

        set.connect(binding.faultRightFirst.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.BOTTOM, (screenUnit*0.83).toInt())
        set.connect(binding.faultRightFirst.id,
            ConstraintSet.RIGHT,binding.faultRightSecond.id,
            ConstraintSet.LEFT,(screenUnit*0.1).toInt())

        set.connect(binding.leftWolf.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.BOTTOM, (screenUnit*1.2).toInt())
        set.connect(binding.leftWolf.id,
            ConstraintSet.LEFT,binding.eggBottomLeftFifth.id,
            ConstraintSet.LEFT, (screenUnit*0.13).toInt())

        set.connect(binding.rightWolf.id,
            ConstraintSet.BOTTOM,binding.screenBack.id,
            ConstraintSet.BOTTOM, (screenUnit*1.2).toInt())
        set.connect(binding.rightWolf.id,
            ConstraintSet.RIGHT,binding.eggBottomRightFifth.id,
            ConstraintSet.RIGHT, (screenUnit*0.13).toInt())

        set.connect(binding.screenFront.id,
            ConstraintSet.LEFT,binding.mainScreenContainer.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.screenFront.id,
            ConstraintSet.RIGHT,binding.mainScreenContainer.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.screenFront.id,
            ConstraintSet.TOP,binding.mainScreenContainer.id,
            ConstraintSet.TOP,0)
        set.connect(binding.screenFront.id,
            ConstraintSet.BOTTOM,binding.mainScreenContainer.id,
            ConstraintSet.BOTTOM,0)

        set.applyTo(binding.mainFragment)


    }

    private fun getScreenHighAndWidth() {
            screenUnit = ScreenMetricsCompat.getScreenSize(requireContext())
        }

    private fun setViewSizes() {
        screenSize.width = 14.0
        screenSize.height = 7.0
        binding.screenBack.layoutParams = ConstraintLayout.LayoutParams((screenSize.width*screenUnit).toInt(),(screenSize.height*screenUnit).toInt())
        binding.screenFront.layoutParams = ConstraintLayout.LayoutParams((screenSize.width*screenUnit).toInt(),(screenSize.height*screenUnit).toInt())

        wholeScreenSize.width = 20.0
        wholeScreenSize.height = 10.0

        eggSize.width=0.5
        eggSize.height=0.5
        binding.mainScreenContainer.layoutParams = ConstraintLayout.LayoutParams((wholeScreenSize.width*screenUnit).toInt(),(wholeScreenSize.height*screenUnit).toInt())
        binding.eggTopLeftFirst.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggTopLeftSecond.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggTopLeftThird.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggTopLeftFourth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggTopLeftFifth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggBottomLeftFirst.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggBottomLeftSecond.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggBottomLeftThird.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggBottomLeftFourth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggBottomLeftFifth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggTopRightFirst.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggTopRightSecond.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggTopRightThird.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggTopRightFourth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggTopRightFifth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggBottomRightFirst.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggBottomRightSecond.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggBottomRightThird.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggBottomRightFourth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())
        binding.eggBottomRightFifth.layoutParams = ConstraintLayout.LayoutParams((eggSize.width*screenUnit).toInt(), (eggSize.height*screenUnit).toInt())


        digitSize.width = 0.3
        digitSize.height = digitSize.width*2

        binding.digitEmpty.layoutParams = ConstraintLayout.LayoutParams(1,(digitSize.height*screenUnit).toInt())
        binding.digitOne.layoutParams = ConstraintLayout.LayoutParams((digitSize.width*screenUnit).toInt(),(digitSize.height*screenUnit).toInt())
        binding.digitTen.layoutParams = ConstraintLayout.LayoutParams((digitSize.width*screenUnit).toInt(),(digitSize.height*screenUnit).toInt())
        binding.digitHundred.layoutParams = ConstraintLayout.LayoutParams((digitSize.width*screenUnit).toInt(),(digitSize.height*screenUnit).toInt())
        binding.digitThousand.layoutParams = ConstraintLayout.LayoutParams((digitSize.width*screenUnit).toInt(),(digitSize.height*screenUnit).toInt())


        arrowSize.width= 2.0
        arrowSize.height=arrowSize.width*2/3

        binding.buttonBottomLeft.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width*screenUnit).toInt(), (arrowSize.height*screenUnit).toInt())
        binding.buttonTopLeft.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width*screenUnit).toInt(), (arrowSize.height*screenUnit).toInt())
        binding.buttonBottomRight.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width*screenUnit).toInt(), (arrowSize.height*screenUnit).toInt())
        binding.buttonTopRight.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width*screenUnit).toInt(), (arrowSize.height*screenUnit).toInt())

        startButtonSize.width = arrowSize.height
        startButtonSize.height = arrowSize.height

        binding.startA.layoutParams = ConstraintLayout.LayoutParams((startButtonSize.width*screenUnit).toInt(), (startButtonSize.height*screenUnit).toInt())
        binding.startB.layoutParams = ConstraintLayout.LayoutParams((startButtonSize.width*screenUnit).toInt(), (startButtonSize.height*screenUnit).toInt())

        binding.letterA.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        binding.letterB.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())


        binding.closeApp.layoutParams = ConstraintLayout.LayoutParams((startButtonSize.width*screenUnit).toInt(), (startButtonSize.height*screenUnit).toInt())
        binding.exit.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        userIdSize.height = arrowSize.height
        userIdSize.width = screenSize.width-3*userIdSize.height

        binding.account.layoutParams = ConstraintLayout.LayoutParams((userIdSize.height*screenUnit).toInt(), (userIdSize.height*screenUnit).toInt())
        binding.userID.layoutParams = ConstraintLayout.LayoutParams((userIdSize.width*screenUnit).toInt(), (userIdSize.height*screenUnit).toInt())
        binding.userID.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        rabbitSize.width = screenUnit*1.4
        rabbitSize.height = screenUnit*1.4

        binding.rabbit.layoutParams = ConstraintLayout.LayoutParams(rabbitSize.width.toInt(),rabbitSize.height.toInt())

        faultSize.width = (screenUnit*0.6)
        faultSize.height = faultSize.width
        binding.middleFault.layoutParams = ConstraintLayout.LayoutParams(faultSize.width.toInt(),faultSize.height.toInt())
        binding.leftFault.layoutParams = ConstraintLayout.LayoutParams(faultSize.width.toInt(),faultSize.height.toInt())
        binding.rightFault.layoutParams = ConstraintLayout.LayoutParams(faultSize.width.toInt(),faultSize.height.toInt())

        bottomFaultSizeSmall.width = (screenUnit/2).toDouble()
        bottomFaultSizeSmall.height=bottomFaultSizeSmall.width*1.5

        bottomFaultSizeSmallDifferent.height = bottomFaultSizeSmall.height
        bottomFaultSizeSmallDifferent.width = bottomFaultSizeSmallDifferent.height/1.2



        binding.faultLeftSecond.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmallDifferent.width.toInt(),bottomFaultSizeSmallDifferent.height.toInt())
        binding.faultLeftThird.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmall.width.toInt(),bottomFaultSizeSmall.height.toInt())
        binding.faultLeftFourth.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmall.width.toInt(),bottomFaultSizeSmall.height.toInt())
        binding.faultRightSecond.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmallDifferent.width.toInt(),bottomFaultSizeSmallDifferent.height.toInt())
        binding.faultRightThird.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmall.width.toInt(),bottomFaultSizeSmall.height.toInt())
        binding.faultRightFourth.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeSmall.width.toInt(),bottomFaultSizeSmall.height.toInt())

        bottomFaultSizeFirst.width = (screenUnit*2).toDouble()
        bottomFaultSizeFirst.height = (screenUnit*1.5)
        binding.faultLeftFirst.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeFirst.width.toInt(),bottomFaultSizeFirst.height.toInt())
        binding.faultRightFirst.layoutParams = ConstraintLayout.LayoutParams(bottomFaultSizeFirst.width.toInt(),bottomFaultSizeFirst.height.toInt())

        wolfSize.width = (screenUnit*3.5)
        wolfSize.height = wolfSize.width

        binding.leftWolf.layoutParams = ConstraintLayout.LayoutParams(wolfSize.width.toInt(),wolfSize.height.toInt())
        binding.rightWolf.layoutParams = ConstraintLayout.LayoutParams(wolfSize.width.toInt(),wolfSize.height.toInt())

    }

    private fun setDrawable() {

        view?.let {
        binding.screenBack.setImageDrawable(MainScreenDrawableBack(requireContext(),screenUnit,screenSize.width,screenSize.height))
        binding.screenFront.setImageDrawable(MainScreenDrawableFront(requireContext(),screenUnit,screenSize.width,screenSize.height))

        binding.buttonBottomLeft.setImageDrawable(
            ArrowBottomLeft(requireContext(),screenUnit*arrowSize.width,
                screenUnit*arrowSize.height
            )
        )

        binding.buttonTopLeft.setImageDrawable(
            ArrowTopLeft(requireContext(),screenUnit*arrowSize.width,
                screenUnit*arrowSize.height
            )
        )

        binding.buttonTopRight.setImageDrawable(
            ArrowTopRight(requireContext(),screenUnit*arrowSize.width,
                screenUnit*arrowSize.height
            )
        )

        binding.buttonBottomRight.setImageDrawable(
            ArrowBottomRight(requireContext(),screenUnit*arrowSize.width,
                screenUnit*arrowSize.height
            )
        )

        binding.startA.setImageDrawable(
            StartButton(requireContext(),screenUnit*startButtonSize.width,
            screenUnit*startButtonSize.height
        )
        )

        binding.startB.setImageDrawable(
            StartButton(requireContext(),screenUnit*startButtonSize.width,
            screenUnit*startButtonSize.height
        )
        )

        binding.closeApp.setImageDrawable(
            StartButton(requireContext(),screenUnit*startButtonSize.width,
            screenUnit*startButtonSize.height
        )
        )

        binding.account.setImageDrawable(
            StartButton(requireContext(),screenUnit*userIdSize.height,
            screenUnit*userIdSize.height
        )
        )

        binding.userID.background = TextViewDrawable(requireContext(),userIdSize.width*screenUnit,userIdSize.height*screenUnit)


        val dbRef = Firebase.database.getReference("GooglePlayApps")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val numberOfApps = snapshot.getValue(GooglePlayApps::class.java)
                val apps = Functions.readNumberOfAppsFromSharedPreferences(requireContext())
                numberOfApps?.let {
                    apps.setAppsInGooglePlayInt(numberOfApps)
                    val newApp = apps.isNewApp()
                    Functions.saveNumberOfAppsToSharedPreferences(requireContext(),apps)
                    if(newApp){
                        binding.account.setImageDrawable(StartButtonGreen(requireContext(),screenUnit*userIdSize.height,screenUnit*userIdSize.height))
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //do nothing
            }
        })
        }
    }
}