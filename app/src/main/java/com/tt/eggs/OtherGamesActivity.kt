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
import com.tt.eggs.classes.Dimension
import com.tt.eggs.drawable.RoundedFrameDrawable
import com.tt.eggs.drawable.StartButton
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_other_games.*

class OtherGamesActivity : AppCompatActivity() {

    private var screenHeight = 0
    private var screenWidth = 0
    private var screenUnit = 0

    private val backToGameButtonSize = Dimension()
    private val sendGameButtonSize = Dimension()
    private val battleShipsGameButtonSize = Dimension()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_other_games)

        makeUI()

        setOnClickListeners()


    }

    private fun setOnClickListeners() {

        backToGameOtherGames.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        /*
        send_game_button.setOnClickListener {
            try{
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT,"Eggs Game")
                // TODO link to this game in google store
                val message = ""
                intent.putExtra(Intent.EXTRA_TEXT,message)
                startActivity(Intent.createChooser(intent,"CHOOSE"))
            } catch (e:Exception){

            }

        }
        */

        battle_ships_game_button.setOnClickListener {
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
        set.clone(other_games_layout)

        set.connect(back_to_game_linearLayout_other_games.id,ConstraintSet.BOTTOM,other_games_layout.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(back_to_game_linearLayout_other_games.id,ConstraintSet.LEFT,other_games_layout.id,ConstraintSet.LEFT,screenUnit)

        set.connect(send_game_linear_layout.id,ConstraintSet.TOP,other_games_layout.id,ConstraintSet.TOP,screenUnit)
        set.connect(send_game_linear_layout.id,ConstraintSet.LEFT,other_games_layout.id,ConstraintSet.LEFT,0)
        set.connect(send_game_linear_layout.id,ConstraintSet.RIGHT,other_games_layout.id,ConstraintSet.RIGHT,0)

        set.connect(battle_ships_game_linear_layout.id,ConstraintSet.TOP,send_game_linear_layout.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(battle_ships_game_linear_layout.id,ConstraintSet.LEFT,other_games_layout.id,ConstraintSet.LEFT,0)
        set.connect(battle_ships_game_linear_layout.id,ConstraintSet.RIGHT,other_games_layout.id,ConstraintSet.RIGHT,0)



        set.applyTo(other_games_layout)

    }

    private fun setDrawable() {
        backToGameOtherGames.setImageDrawable(StartButton(this,backToGameButtonSize.width,backToGameButtonSize.height))
        back_to_game_linearLayout_other_games.background = RoundedFrameDrawable(this,5.5*backToGameButtonSize.width,backToGameButtonSize.height,backToGameButtonSize.height/20,backToGameButtonSize.height/2)
        send_game_button.setImageDrawable(StartButton(this,sendGameButtonSize.width,sendGameButtonSize.height))
        send_game_linear_layout.background = RoundedFrameDrawable(this,12*sendGameButtonSize.width,sendGameButtonSize.height, sendGameButtonSize.height/20,sendGameButtonSize.height/2)
        battle_ships_game_image_view.setImageResource(R.drawable.ship_icon)
        battle_ships_game_linear_layout.background = RoundedFrameDrawable(this,12*battleShipsGameButtonSize.width,3*battleShipsGameButtonSize.height,battleShipsGameButtonSize.height/20,battleShipsGameButtonSize.height/2)
        battle_ships_game_button.setImageDrawable(StartButton(this,battleShipsGameButtonSize.width,battleShipsGameButtonSize.height))
    }

    private fun setViewSizes() {
        backToGameButtonSize.width= (screenUnit*4/3).toDouble()
        backToGameButtonSize.height = backToGameButtonSize.width
        back_toGame_tv_blank.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width/2).toInt(),(backToGameButtonSize.height).toInt())
        backToGameOtherGames.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        back_to_game_textView_other_games.layoutParams = LinearLayout.LayoutParams((4*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        back_to_game_textView_other_games.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        sendGameButtonSize.width = (screenUnit*4/3).toDouble()
        sendGameButtonSize.height = sendGameButtonSize.width
        send_game_text.layoutParams = LinearLayout.LayoutParams((10.5*sendGameButtonSize.width).toInt(),(sendGameButtonSize.height).toInt())
        send_game_button.layoutParams = LinearLayout.LayoutParams((sendGameButtonSize.width).toInt(),(sendGameButtonSize.height).toInt())
        send_game_tv_blank.layoutParams = LinearLayout.LayoutParams((sendGameButtonSize.width/2).toInt(),(sendGameButtonSize.height).toInt())
        send_game_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        battleShipsGameButtonSize.width = (screenUnit*4/3).toDouble()
        battleShipsGameButtonSize.height = battleShipsGameButtonSize.width
        battle_ships_game_image_view.layoutParams = LinearLayout.LayoutParams((2*battleShipsGameButtonSize.width).toInt(),(2*battleShipsGameButtonSize.height).toInt())
        battle_ships_game_text.layoutParams = LinearLayout.LayoutParams((8*battleShipsGameButtonSize.width).toInt(),(2*battleShipsGameButtonSize.height).toInt())
        battle_ships_game_button.layoutParams = LinearLayout.LayoutParams((battleShipsGameButtonSize.width).toInt(),(battleShipsGameButtonSize.height).toInt())
        battle_ships_game_tv_blank.layoutParams = LinearLayout.LayoutParams((battleShipsGameButtonSize.width/2).toInt(),(battleShipsGameButtonSize.height).toInt())
        battle_ships_game_tv_blank_front.layoutParams = LinearLayout.LayoutParams((battleShipsGameButtonSize.width/2).toInt(),(battleShipsGameButtonSize.height).toInt())
        battle_ships_game_linear_layout.layoutParams = ConstraintLayout.LayoutParams((12*battleShipsGameButtonSize.width).toInt(),(3*battleShipsGameButtonSize.height).toInt())
        battle_ships_game_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

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

    private fun fullScreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        val decorView: View = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if(visibility and View.SYSTEM_UI_FLAG_FULLSCREEN==0){
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            }
        }

    }
}
