package com.tt.eggs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import com.tt.eggs.classes.Game
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var game = Game()

    var basket = 3

    val mHandler = Handler()
    var gameLoop: Runnable = object : Runnable{
        override fun run(){
            game.moveDown()
            displayState()
            mHandler.postDelayed(this,1000)
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

        // set button listeners
        buttonsOnClickListeners()


        gameLoop.run()




    }

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

    private fun displayState(){
        displayEgg(eggTopLeftFirst,1,1)
        displayEgg(eggTopLeftSecond,2,1)
        displayEgg(eggTopLeftThird,3,1)
        displayEgg(eggTopLeftFourth,4,1)

        displayEgg(eggBottomLeftFirst,1,0)
        displayEgg(eggBottomLeftSecond,2,0)
        displayEgg(eggBottomLeftThird,3,0)
        displayEgg(eggBottomLeftFourth,4,0)

        displayEgg(eggBottomRightFirst,1,3)
        displayEgg(eggBottomRightSecond,2,3)
        displayEgg(eggBottomRightThird,3,3)
        displayEgg(eggBottomRightFourth,4,3)

        displayEgg(eggTopRightFirst,1,2)
        displayEgg(eggTopRightSecond,2,2)
        displayEgg(eggTopRightThird,3,2)
        displayEgg(eggTopRightFourth,4,2)

    }

    private fun displayEgg( imageView: ImageView, x:Int, y:Int){
        if(game.displayCell(x,y)){
            imageView.setImageDrawable(getDrawable(R.drawable.button))
        }
        else{
            imageView.setImageDrawable(null)
        }
    }

    private fun displayBasket(){
        game.setBasket(basket)
        if(game.position[1]){
            basketTopLeft.setImageDrawable(getDrawable(R.drawable.basket))
        }else{
            basketTopLeft.setImageDrawable(null)
        }
        if(game.position[0]){
            basketBottomLeft.setImageDrawable(getDrawable(R.drawable.basket))
        }else{
            basketBottomLeft.setImageDrawable(null)
        }
        if(game.position[3]){
            basketBottomRight.setImageDrawable(getDrawable(R.drawable.basket))
        }else{
            basketBottomRight.setImageDrawable(null)
        }
        if(game.position[2]){
            basketTopRight.setImageDrawable(getDrawable(R.drawable.basket))
        }else{
            basketTopRight.setImageDrawable(null)
        }
    }

    private fun updateArray(){
        game.clear()
        displayState()

    }

    private fun buttonsOnClickListeners(){
        buttonTopLeft.setOnClickListener {
            basket=1
            displayBasket()

        }
        buttonBottomLeft.setOnClickListener {
            basket=0
            displayBasket()
        }
        buttonBottomRight.setOnClickListener {
            basket=3
            displayBasket()
        }
        buttonTopRight.setOnClickListener {
            basket=2
            displayBasket()
        }
    }



    }


