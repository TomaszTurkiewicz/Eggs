package com.tt.eggs

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.tt.eggs.classes.Dimension
import com.tt.eggs.databinding.ActivityOtherGamesBinding
import com.tt.eggs.drawable.RoundedFrameDrawable
import com.tt.eggs.drawable.StartButton


class OtherGamesActivity : AppCompatActivity() {

    private var screenHeight = 0
    private var screenWidth = 0
    private var screenUnit = 0

    private val backToGameButtonSize = Dimension()
    private val sendGameButtonSize = Dimension()
    private val battleShipsGameButtonSize = Dimension()

    private lateinit var binding: ActivityOtherGamesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtherGamesBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        fullScreen(view)

        makeUI()

        setOnClickListeners()


    }

    private fun setOnClickListeners() {

        binding.backToGameOtherGames.setOnClickListener {
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


        binding.battleShipsGameButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.tt.battleshipsgame"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
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

        set.connect(binding.backToGameLinearLayoutOtherGames.id,ConstraintSet.BOTTOM,binding.otherGames.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(binding.backToGameLinearLayoutOtherGames.id,ConstraintSet.LEFT,binding.otherGames.id,ConstraintSet.LEFT,screenUnit)

        set.connect(binding.sendGameLinearlayout.id,ConstraintSet.TOP,binding.otherGames.id,ConstraintSet.TOP,screenUnit)
        set.connect(binding.sendGameLinearlayout.id,ConstraintSet.LEFT,binding.otherGames.id,ConstraintSet.LEFT,0)
        set.connect(binding.sendGameLinearlayout.id,ConstraintSet.RIGHT,binding.otherGames.id,ConstraintSet.RIGHT,0)

        set.connect(binding.battleShipsGameLinearlayout.id,ConstraintSet.TOP,binding.sendGameLinearlayout.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(binding.battleShipsGameLinearlayout.id,ConstraintSet.LEFT,binding.otherGames.id,ConstraintSet.LEFT,0)
        set.connect(binding.battleShipsGameLinearlayout.id,ConstraintSet.RIGHT,binding.otherGames.id,ConstraintSet.RIGHT,0)



        set.applyTo(binding.otherGames)

    }

    private fun setDrawable() {
        binding.backToGameOtherGames.setImageDrawable(StartButton(this,backToGameButtonSize.width,backToGameButtonSize.height))
        binding.backToGameLinearLayoutOtherGames.background = RoundedFrameDrawable(this,5.5*backToGameButtonSize.width,backToGameButtonSize.height,backToGameButtonSize.height/20,backToGameButtonSize.height/2)
        binding.sendGameButton.setImageDrawable(StartButton(this,sendGameButtonSize.width,sendGameButtonSize.height))
        binding.sendGameLinearlayout.background = RoundedFrameDrawable(this,12*sendGameButtonSize.width,sendGameButtonSize.height, sendGameButtonSize.height/20,sendGameButtonSize.height/2)
        binding.battleShipsGameImageView.setImageResource(R.drawable.ship_icon)
        binding.battleShipsGameLinearlayout.background = RoundedFrameDrawable(this,12*battleShipsGameButtonSize.width,3*battleShipsGameButtonSize.height,battleShipsGameButtonSize.height/20,battleShipsGameButtonSize.height/2)
        binding.battleShipsGameButton.setImageDrawable(StartButton(this,battleShipsGameButtonSize.width,battleShipsGameButtonSize.height))
    }

    private fun setViewSizes() {
        backToGameButtonSize.width= (screenUnit*4/3).toDouble()
        backToGameButtonSize.height = backToGameButtonSize.width
        binding.backToGameTvBlank.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width/2).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameOtherGames.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameTextViewOtherGames.layoutParams = LinearLayout.LayoutParams((4*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameTextViewOtherGames.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        sendGameButtonSize.width = (screenUnit*4/3).toDouble()
        sendGameButtonSize.height = sendGameButtonSize.width
        binding.sendGameText.layoutParams = LinearLayout.LayoutParams((10.5*sendGameButtonSize.width).toInt(),(sendGameButtonSize.height).toInt())
        binding.sendGameButton.layoutParams = LinearLayout.LayoutParams((sendGameButtonSize.width).toInt(),(sendGameButtonSize.height).toInt())
        binding.sendGameTvBlank.layoutParams = LinearLayout.LayoutParams((sendGameButtonSize.width/2).toInt(),(sendGameButtonSize.height).toInt())
        binding.sendGameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        battleShipsGameButtonSize.width = (screenUnit*4/3).toDouble()
        battleShipsGameButtonSize.height = battleShipsGameButtonSize.width
        binding.battleShipsGameImageView.layoutParams = LinearLayout.LayoutParams((2*battleShipsGameButtonSize.width).toInt(),(2*battleShipsGameButtonSize.height).toInt())
        binding.battleShipsGameText.layoutParams = LinearLayout.LayoutParams((8*battleShipsGameButtonSize.width).toInt(),(2*battleShipsGameButtonSize.height).toInt())
        binding.battleShipsGameButton.layoutParams = LinearLayout.LayoutParams((battleShipsGameButtonSize.width).toInt(),(battleShipsGameButtonSize.height).toInt())
        binding.battleShipsGameTvBlank.layoutParams = LinearLayout.LayoutParams((battleShipsGameButtonSize.width/2).toInt(),(battleShipsGameButtonSize.height).toInt())
        binding.battleShipsGameTvBlankFront.layoutParams = LinearLayout.LayoutParams((battleShipsGameButtonSize.width/2).toInt(),(battleShipsGameButtonSize.height).toInt())
        binding.battleShipsGameLinearlayout.layoutParams = ConstraintLayout.LayoutParams((12*battleShipsGameButtonSize.width).toInt(),(3*battleShipsGameButtonSize.height).toInt())
        binding.battleShipsGameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

    }

    private fun getScreenHighAndWidth() {
        val displayMetrics = DisplayMetrics()
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
            val display = this.display
            display?.getRealMetrics(displayMetrics)
        }
        else{
            @Suppress("DEPRECATION")
            val display = this.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(displayMetrics)
        }
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels
        val unitWidth = screenWidth/20
        val unitHeight = screenHeight/10
        screenUnit=if(unitWidth>unitHeight)unitHeight else unitWidth

    }

    private fun fullScreen(mainActivityLayout:View) {
        WindowCompat.setDecorFitsSystemWindows(window,false)
        WindowInsetsControllerCompat(window, mainActivityLayout).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

    }
}
