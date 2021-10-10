package com.tt.eggs

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tt.eggs.classes.Dimension
import com.tt.eggs.classes.Functions
import com.tt.eggs.classes.OtherGamesObject
import com.tt.eggs.databinding.ActivityOtherGamesBinding
import com.tt.eggs.drawable.ArrowDown
import com.tt.eggs.drawable.ArrowUp
import com.tt.eggs.drawable.RoundedFrameDrawable
import com.tt.eggs.drawable.StartButton




class OtherGamesActivity : AppCompatActivity() {


    private var screenUnit = 0

    private val backToGameButtonSize = Dimension()
    private val sendGameButtonSize = Dimension()
    private val otherGamesButtonSize = Dimension()
    private val wholeScreenSize = Dimension()
    private val arrowSize = Dimension()
    private lateinit var gameList: MutableList<OtherGamesObject>
    private var gameListReady = false
    private var itemCounter = 0
    private val storage = Firebase.storage
    private val mHandlerDisplayFirstItems = Handler(Looper.getMainLooper())

    private lateinit var binding: ActivityOtherGamesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtherGamesBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        fullScreen(view)

        makeUI()

        setOnClickListeners()

        gameList = mutableListOf()
        val dbRef = Firebase.database.getReference("OtherGames")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(game in snapshot.children){
                        val tGame = game.getValue(OtherGamesObject::class.java)
                        gameList.add(tGame!!)
                    }
                    gameListReady = true
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        displayGames().run()
    }

    private fun displayGames():Runnable = Runnable {
        if(gameListReady){
            mHandlerDisplayFirstItems.removeCallbacksAndMessages(null)
            displayGame(itemCounter)
        }
        else{
            mHandlerDisplayFirstItems.postDelayed(displayGames(),100)
        }
    }

    private fun displayGame(counter:Int){
        binding.otherGamesImageView.setImageBitmap(null)
        val storageRef = storage.reference.child("OtherGames").child(gameList[counter].picture)
        val size: Long = 1024*1024
        storageRef.getBytes(size).addOnSuccessListener {
            val bm = BitmapFactory.decodeByteArray(it,0,it.size)
            binding.otherGamesImageView.setImageBitmap(bm)
        }

        val pageCounter = counter+1
        val totalCounter = gameList.size
        binding.otherGamesPageCounter.text = getString(R.string.page_counter,pageCounter,totalCounter)

        binding.otherGamesTextViewName.text = gameList[counter].gameName

        binding.otherGamesUp.setOnClickListener {
            if(pageCounter>1){
                itemCounter -= 1
                displayGame(itemCounter)
            }
        }
        binding.otherGamesDown.setOnClickListener {
            if(pageCounter<totalCounter){
                itemCounter += 1
                displayGame(itemCounter)
            }
        }

        binding.otherGamesButton.setOnClickListener {
            val link = gameList[counter].uri
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    private fun setOnClickListeners() {

        binding.backToGameOtherGamesButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.sendGameButton.setOnClickListener {
            try{
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT,"Eggs Game")
                val message = "https://play.google.com/store/apps/details?id=com.tt.eggs"
                intent.putExtra(Intent.EXTRA_TEXT,message)
                startActivity(Intent.createChooser(intent,"CHOOSE"))
            } catch (e:Exception){

            }

        }

    }

    private fun makeUI() {
        getScreenHighAndWidth()

        setViewSizes()

        setDrawable()

        connectViews()

    }

    private fun connectViews() {
        val set = ConstraintSet()
        set.clone(binding.otherGames)

        set.connect(binding.otherGamesActivityContainer.id,ConstraintSet.TOP,binding.otherGames.id,ConstraintSet.TOP,0)
        set.connect(binding.otherGamesActivityContainer.id,ConstraintSet.BOTTOM,binding.otherGames.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.otherGamesActivityContainer.id,ConstraintSet.RIGHT,binding.otherGames.id,ConstraintSet.RIGHT,0)
        set.connect(binding.otherGamesActivityContainer.id,ConstraintSet.LEFT,binding.otherGames.id,ConstraintSet.LEFT,0)

        set.connect(binding.backToGameLinearLayoutOtherGames.id,ConstraintSet.BOTTOM,binding.otherGamesActivityContainer.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(binding.backToGameLinearLayoutOtherGames.id,ConstraintSet.LEFT,binding.otherGamesActivityContainer.id,ConstraintSet.LEFT,screenUnit)

        set.connect(binding.sendGameLinearlayout.id,ConstraintSet.TOP,binding.otherGamesActivityContainer.id,ConstraintSet.TOP,screenUnit)
        set.connect(binding.sendGameLinearlayout.id,ConstraintSet.LEFT,binding.otherGamesActivityContainer.id,ConstraintSet.LEFT,screenUnit)

        set.connect(binding.otherGamesLinearLayout.id,ConstraintSet.TOP,binding.sendGameLinearlayout.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(binding.otherGamesLinearLayout.id,ConstraintSet.LEFT,binding.sendGameLinearlayout.id,ConstraintSet.LEFT,0)

        set.connect(binding.otherGamesUp.id,ConstraintSet.TOP,binding.otherGamesActivityContainer.id,ConstraintSet.TOP,screenUnit)
        set.connect(binding.otherGamesUp.id,ConstraintSet.LEFT,binding.otherGamesLinearLayout.id,ConstraintSet.RIGHT,0)
        set.connect(binding.otherGamesUp.id,ConstraintSet.RIGHT,binding.otherGamesActivityContainer.id,ConstraintSet.RIGHT,0)

        set.connect(binding.otherGamesDown.id,ConstraintSet.BOTTOM,binding.otherGamesActivityContainer.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(binding.otherGamesDown.id,ConstraintSet.LEFT,binding.otherGamesLinearLayout.id,ConstraintSet.RIGHT,0)
        set.connect(binding.otherGamesDown.id,ConstraintSet.RIGHT,binding.otherGamesActivityContainer.id,ConstraintSet.RIGHT,0)

        set.connect(binding.otherGamesPageCounter.id,ConstraintSet.BOTTOM,binding.otherGamesActivityContainer.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(binding.otherGamesPageCounter.id,ConstraintSet.RIGHT,binding.otherGamesLinearLayout.id,ConstraintSet.RIGHT,0)


        set.applyTo(binding.otherGames)

    }

    private fun setDrawable() {
        binding.backToGameOtherGamesButton.setImageDrawable(StartButton(this,backToGameButtonSize.width,backToGameButtonSize.height))
        binding.backToGameLinearLayoutOtherGames.background = RoundedFrameDrawable(this,5.5*backToGameButtonSize.width,backToGameButtonSize.height,backToGameButtonSize.height/20,backToGameButtonSize.height/2)
        binding.sendGameButton.setImageDrawable(StartButton(this,sendGameButtonSize.width,sendGameButtonSize.height))
        binding.sendGameLinearlayout.background = RoundedFrameDrawable(this,10*sendGameButtonSize.width,sendGameButtonSize.height, sendGameButtonSize.height/20,sendGameButtonSize.height/2)
        binding.otherGamesLinearLayout.background = RoundedFrameDrawable(this,10*otherGamesButtonSize.width,3*otherGamesButtonSize.height,otherGamesButtonSize.height/20,otherGamesButtonSize.height/2)
        binding.otherGamesButton.setImageDrawable(StartButton(this,otherGamesButtonSize.width,otherGamesButtonSize.height))
        binding.otherGamesUp.setImageDrawable(ArrowUp(this,arrowSize.width,arrowSize.height))
        binding.otherGamesDown.setImageDrawable(ArrowDown(this,arrowSize.width,arrowSize.height))
    }

    private fun setViewSizes() {

        wholeScreenSize.width = (20*screenUnit).toDouble()
        wholeScreenSize.height = (10*screenUnit).toDouble()
        binding.otherGamesActivityContainer.layoutParams = ConstraintLayout.LayoutParams((wholeScreenSize.width).toInt(),(wholeScreenSize.height).toInt())

        backToGameButtonSize.width= (screenUnit*4/3).toDouble()
        backToGameButtonSize.height = backToGameButtonSize.width
        binding.backToGameTvBlank.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width/2).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameOtherGamesButton.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameTextViewOtherGames.layoutParams = LinearLayout.LayoutParams((4*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameTextViewOtherGames.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        sendGameButtonSize.width = (screenUnit*4/3).toDouble()
        sendGameButtonSize.height = sendGameButtonSize.width
        binding.sendGameText.layoutParams = LinearLayout.LayoutParams((8.5*sendGameButtonSize.width).toInt(),(sendGameButtonSize.height).toInt())
        binding.sendGameButton.layoutParams = LinearLayout.LayoutParams((sendGameButtonSize.width).toInt(),(sendGameButtonSize.height).toInt())
        binding.sendGameTvBlank.layoutParams = LinearLayout.LayoutParams((sendGameButtonSize.width/2).toInt(),(sendGameButtonSize.height).toInt())
        binding.sendGameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        otherGamesButtonSize.width = (screenUnit*4/3).toDouble()
        otherGamesButtonSize.height = otherGamesButtonSize.width
        binding.otherGamesImageView.layoutParams = LinearLayout.LayoutParams((2*otherGamesButtonSize.width).toInt(),(2*otherGamesButtonSize.height).toInt())
        binding.otherGamesTextViewName.layoutParams = LinearLayout.LayoutParams((6*otherGamesButtonSize.width).toInt(),(2*otherGamesButtonSize.height).toInt())
        binding.otherGamesButton.layoutParams = LinearLayout.LayoutParams((otherGamesButtonSize.width).toInt(),(otherGamesButtonSize.height).toInt())
        binding.otherGamesTvBackBlank.layoutParams = LinearLayout.LayoutParams((otherGamesButtonSize.width/2).toInt(),(otherGamesButtonSize.height).toInt())
        binding.otherGamesTvFrontBlank.layoutParams = LinearLayout.LayoutParams((otherGamesButtonSize.width/2).toInt(),(otherGamesButtonSize.height).toInt())
        binding.otherGamesLinearLayout.layoutParams = ConstraintLayout.LayoutParams((10*otherGamesButtonSize.width).toInt(),(3*otherGamesButtonSize.height).toInt())
        binding.otherGamesTextViewName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        arrowSize.width= (screenUnit*4/3).toDouble()
        arrowSize.height = arrowSize.width*2
        binding.otherGamesUp.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width).toInt(),(arrowSize.height).toInt())
        binding.otherGamesDown.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width).toInt(),(arrowSize.height).toInt())
        binding.otherGamesPageCounter.layoutParams = ConstraintLayout.LayoutParams((4*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.otherGamesPageCounter.setTextSize(TypedValue.COMPLEX_UNIT_PX,(screenUnit*0.6).toFloat())

    }

    private fun getScreenHighAndWidth() {
        screenUnit= Functions.readScreenUnitFromSharedPreferences(this)

    }

    private fun fullScreen(mainActivityLayout:View) {
        WindowCompat.setDecorFitsSystemWindows(window,false)
        WindowInsetsControllerCompat(window, mainActivityLayout).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

    }
}
