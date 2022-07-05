package com.tt.eggs

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.tt.eggs.classes.Dimension
import com.tt.eggs.classes.Functions
import com.tt.eggs.classes.GooglePlayApps
import com.tt.eggs.classes.NewApps
import com.tt.eggs.databinding.ActivityOtherGamesBinding
import com.tt.eggs.drawable.RoundedFrameDrawable
import com.tt.eggs.drawable.StartButton
import com.tt.eggs.drawable.StartButtonGreen


class OtherGamesActivity : AppCompatActivity() {

    private var screenUnit = 0
    private val buttonSize = Dimension()
    private val wholeScreenSize = Dimension()
    private lateinit var binding: ActivityOtherGamesBinding
    private lateinit var apps : NewApps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherGamesBinding.inflate(layoutInflater)
        val view = binding.root
        apps = Functions.readNumberOfAppsFromSharedPreferences(this)
        setContentView(view)
        fullScreen(view)
        makeUI()
        setOnClickListeners()
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

        binding.otherGamesButton.setOnClickListener {
            apps.saveNewNumberOfApps()
            Functions.saveNumberOfAppsToSharedPreferences(this,apps)
            binding.otherGamesButton.setImageDrawable(StartButton(this,buttonSize.width,buttonSize.height))

            val link = getString(R.string.other_games_link)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
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

        set.connect(binding.otherGamesActivityContainer.id,ConstraintSet.TOP,binding.otherGames.id,ConstraintSet.TOP,0)
        set.connect(binding.otherGamesActivityContainer.id,ConstraintSet.BOTTOM,binding.otherGames.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.otherGamesActivityContainer.id,ConstraintSet.RIGHT,binding.otherGames.id,ConstraintSet.RIGHT,0)
        set.connect(binding.otherGamesActivityContainer.id,ConstraintSet.LEFT,binding.otherGames.id,ConstraintSet.LEFT,0)

        set.connect(binding.backToGameLinearLayoutOtherGames.id,ConstraintSet.BOTTOM,binding.otherGamesActivityContainer.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(binding.backToGameLinearLayoutOtherGames.id,ConstraintSet.LEFT,binding.otherGamesActivityContainer.id,ConstraintSet.LEFT,screenUnit)

        set.connect(binding.sendGameLinearlayout.id,ConstraintSet.TOP,binding.otherGamesActivityContainer.id,ConstraintSet.TOP,screenUnit)
        set.connect(binding.sendGameLinearlayout.id,ConstraintSet.LEFT,binding.otherGamesActivityContainer.id,ConstraintSet.LEFT,0)
        set.connect(binding.sendGameLinearlayout.id,ConstraintSet.RIGHT,binding.otherGamesActivityContainer.id,ConstraintSet.RIGHT,0)

        set.connect(binding.otherGamesLinearlayout.id,ConstraintSet.TOP,binding.sendGameLinearlayout.id,ConstraintSet.BOTTOM,screenUnit)
        set.connect(binding.otherGamesLinearlayout.id,ConstraintSet.LEFT,binding.otherGamesActivityContainer.id,ConstraintSet.LEFT,0)
        set.connect(binding.otherGamesLinearlayout.id,ConstraintSet.RIGHT,binding.otherGamesActivityContainer.id,ConstraintSet.RIGHT,0)

        set.applyTo(binding.otherGames)
    }

    private fun setDrawable() {
        binding.backToGameOtherGamesButton.setImageDrawable(StartButton(this,buttonSize.width,buttonSize.height))
        binding.backToGameLinearLayoutOtherGames.background = RoundedFrameDrawable(this,5.5*buttonSize.width,buttonSize.height,buttonSize.height/20,buttonSize.height/2)
        binding.sendGameButton.setImageDrawable(StartButton(this,buttonSize.width,buttonSize.height))
        binding.sendGameLinearlayout.background = RoundedFrameDrawable(this,10*buttonSize.width,buttonSize.height, buttonSize.height/20,buttonSize.height/2)
        binding.otherGamesButton.setImageDrawable(StartButton(this,buttonSize.width,buttonSize.height))
        binding.otherGamesLinearlayout.background = RoundedFrameDrawable(this,10*buttonSize.width,buttonSize.height, buttonSize.height/20,buttonSize.height/2)


        if(apps.isNewApp()){
            binding.otherGamesButton.setImageDrawable(StartButtonGreen(this,buttonSize.width,buttonSize.height))
        }
    }

    private fun setViewSizes() {

        wholeScreenSize.width = (20*screenUnit).toDouble()
        wholeScreenSize.height = (10*screenUnit).toDouble()
        binding.otherGamesActivityContainer.layoutParams = ConstraintLayout.LayoutParams((wholeScreenSize.width).toInt(),(wholeScreenSize.height).toInt())

        buttonSize.width= (screenUnit*4/3).toDouble()
        buttonSize.height = buttonSize.width
        binding.backToGameTvBlank.layoutParams = LinearLayout.LayoutParams((buttonSize.width/2).toInt(),(buttonSize.height).toInt())
        binding.backToGameOtherGamesButton.layoutParams = LinearLayout.LayoutParams((buttonSize.width).toInt(),(buttonSize.height).toInt())
        binding.backToGameTextViewOtherGames.layoutParams = LinearLayout.LayoutParams((4*buttonSize.width).toInt(),(buttonSize.height).toInt())
        binding.backToGameTextViewOtherGames.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        buttonSize.width = (screenUnit*4/3).toDouble()
        buttonSize.height = buttonSize.width
        binding.sendGameText.layoutParams = LinearLayout.LayoutParams((8.5*buttonSize.width).toInt(),(buttonSize.height).toInt())
        binding.sendGameButton.layoutParams = LinearLayout.LayoutParams((buttonSize.width).toInt(),(buttonSize.height).toInt())
        binding.sendGameTvBlank.layoutParams = LinearLayout.LayoutParams((buttonSize.width/2).toInt(),(buttonSize.height).toInt())
        binding.sendGameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        buttonSize.width = (screenUnit*4/3).toDouble()
        buttonSize.height = buttonSize.width
        binding.otherGamesText.layoutParams = LinearLayout.LayoutParams((8.5*buttonSize.width).toInt(),(buttonSize.height).toInt())
        binding.otherGamesButton.layoutParams = LinearLayout.LayoutParams((buttonSize.width).toInt(),(buttonSize.height).toInt())
        binding.otherGamesTvBlank.layoutParams = LinearLayout.LayoutParams((buttonSize.width/2).toInt(),(buttonSize.height).toInt())
        binding.otherGamesText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
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
