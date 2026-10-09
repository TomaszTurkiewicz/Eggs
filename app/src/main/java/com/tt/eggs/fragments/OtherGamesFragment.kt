package com.tt.eggs.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.tt.eggs.MainActivity
import com.tt.eggs.R
import com.tt.eggs.classes.*
import com.tt.eggs.databinding.FragmentOtherGamesBinding
import com.tt.eggs.drawable.RoundedFrameDrawable
import com.tt.eggs.drawable.StartButton
import com.tt.eggs.drawable.StartButtonGreen

class OtherGamesFragment : Fragment() {

    private var screenUnit = 0
    private val buttonSize = Dimension()
    private val wholeScreenSize = Dimension()
    private var _binding: FragmentOtherGamesBinding? = null
    private val binding get() = _binding!!
//    private lateinit var apps : NewApps
//    private val newAppsHandler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        apps.appsSavedINMemory = Functions.readNumberOfAppsFromSharedPreferences(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherGamesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeUI()
        setOnClickListeners()
//        checkNewApps().run()
    }



    private fun setOnClickListeners() {

        binding.backToGameOtherGamesButton.setOnClickListener {
            findNavController().navigateUp()
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
            Functions.saveNewAppAvailable(requireContext(),false)
            binding.otherGamesButton.setImageDrawable(StartButton(requireContext()))
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
        set.clone(binding.otherGamesFragment)

        set.connect(binding.otherGamesActivityContainer.id,
            ConstraintSet.TOP,binding.otherGamesFragment.id,
            ConstraintSet.TOP,0)
        set.connect(binding.otherGamesActivityContainer.id,
            ConstraintSet.BOTTOM,binding.otherGamesFragment.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.otherGamesActivityContainer.id,
            ConstraintSet.RIGHT,binding.otherGamesFragment.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.otherGamesActivityContainer.id,
            ConstraintSet.LEFT,binding.otherGamesFragment.id,
            ConstraintSet.LEFT,0)

        set.connect(binding.backToGameLinearLayoutOtherGames.id,
            ConstraintSet.BOTTOM,binding.otherGamesActivityContainer.id,
            ConstraintSet.BOTTOM,screenUnit)
        set.connect(binding.backToGameLinearLayoutOtherGames.id,
            ConstraintSet.LEFT,binding.otherGamesActivityContainer.id,
            ConstraintSet.LEFT,screenUnit)

        set.connect(binding.sendGameLinearlayout.id,
            ConstraintSet.TOP,binding.otherGamesActivityContainer.id,
            ConstraintSet.TOP,screenUnit)
        set.connect(binding.sendGameLinearlayout.id,
            ConstraintSet.LEFT,binding.otherGamesActivityContainer.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.sendGameLinearlayout.id,
            ConstraintSet.RIGHT,binding.otherGamesActivityContainer.id,
            ConstraintSet.RIGHT,0)

        set.connect(binding.otherGamesLinearlayout.id,
            ConstraintSet.TOP,binding.sendGameLinearlayout.id,
            ConstraintSet.BOTTOM,screenUnit)
        set.connect(binding.otherGamesLinearlayout.id,
            ConstraintSet.LEFT,binding.otherGamesActivityContainer.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.otherGamesLinearlayout.id,
            ConstraintSet.RIGHT,binding.otherGamesActivityContainer.id,
            ConstraintSet.RIGHT,0)

        set.applyTo(binding.otherGamesFragment)
    }

    private fun setDrawable() {

        binding.sendGameText.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))
        binding.otherGamesText.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))
        binding.backToGameTextViewOtherGames.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))

        binding.otherGamesActivityContainer.setBackgroundColor(ContextCompat.getColor(requireContext(),
            Theme(requireContext()).getBackgroundColor()))
        binding.backToGameOtherGamesButton.setImageDrawable(StartButton(requireContext()))
        binding.backToGameLinearLayoutOtherGames.background = RoundedFrameDrawable(requireContext(),buttonSize.height/20,buttonSize.height/2)
        binding.sendGameButton.setImageDrawable(StartButton(requireContext()))
        binding.sendGameLinearlayout.background = RoundedFrameDrawable(requireContext(),buttonSize.height/20,buttonSize.height/2)
        val newApp = Functions.readNewAppAvailable(requireContext())
        binding.otherGamesButton.setImageDrawable(if(newApp) StartButtonGreen(requireContext()) else StartButton(requireContext()))
        binding.otherGamesLinearlayout.background = RoundedFrameDrawable(requireContext(),buttonSize.height/20,buttonSize.height/2)



    }

//    private fun checkNewApps():Runnable = Runnable {
//        val activity = activity as MainActivity
//        if(activity.appsChecked()){
//            newAppsHandler.removeCallbacksAndMessages(null)
//            val apps = activity.getApps()
//            apps.appsSavedINMemory = Functions.readNumberOfAppsFromSharedPreferences(requireContext())
//            val newApp = apps.isNewApp()
//            Functions.saveNumberOfAppsToSharedPreferences(requireContext(),apps.appsSavedINMemory)
//            if(newApp){
//                view?.let {
//                    binding.otherGamesButton.setImageDrawable(StartButtonGreen(requireContext()))
//                }
//            }
//        }else{
//            newAppsHandler.postDelayed(checkNewApps(),1000)
//        }
//    }

    private fun getScreenHighAndWidth() {
        screenUnit = ScreenMetricsCompat.getScreenSize(requireContext())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}