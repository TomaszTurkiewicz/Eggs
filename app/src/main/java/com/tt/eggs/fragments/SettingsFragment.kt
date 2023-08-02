package com.tt.eggs.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tt.eggs.MainActivity
import com.tt.eggs.R
import com.tt.eggs.classes.*
import com.tt.eggs.databinding.FragmentSettingsBinding
import com.tt.eggs.drawable.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var screenUnit = 0
    private val userNameSize = Dimension()
    private val backToGameButtonSize = Dimension()
    private val rankingButtonSize = Dimension()
    private val darkModeButtonSize = Dimension()
    private val accountButtonSize = Dimension()
    private val otherGamesButtonSize = Dimension()
    private val darkModeTextViewSize = Dimension()
    private val wholeScreenSize = Dimension()
    private var loggedInStatus = LoggedInStatus()
    private val mHandler = Handler(Looper.getMainLooper())
    private var rankingReady = false
    private var darkMode = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeUI()

        checkUser()

        darkMode = Functions.readDarkModeFromSharedPreferences(requireContext())

        displayDarkModeTextView()

        // buttons on click listeners
        setButtonsActions()

        rankingChecking().run()
    }

    private fun displayDarkModeTextView() {
        binding.darkModeTv.text = when(darkMode){
            Static.DARK_MODE_AUTO -> getString(R.string.auto)
            Static.DARK_MODE_ON -> getString(R.string.on)
            else -> getString(R.string.off)
        }

    }

    private fun rankingChecking(): Runnable = Runnable {
        val activity = activity as MainActivity
        rankingReady = activity.getDownloaded()
        if(rankingReady){
            view?.let {
                binding.ranking.setImageDrawable(StartButton(requireContext()))
            }
            mHandler.removeCallbacksAndMessages(null)
        }else{
            mHandler.postDelayed(rankingChecking(),1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }


    private fun setButtonsActions() {
        // back to main screen
        binding.backToGame.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.otherGamesButton.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragment2ToOtherGamesFragment()
            findNavController().navigate(action)
        }


        binding.ranking.setOnClickListener {
            if(rankingReady) {
                val action = SettingsFragmentDirections.actionSettingsFragment2ToRankingFragment()
                findNavController().navigate(action)
            }else{
                Toast.makeText(requireContext(),"RANKING NOT READY YET", Toast.LENGTH_SHORT).show()
            }
        }

        binding.accountButton.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragment2ToLoginFragment()
            findNavController().navigate(action)
        }

        binding.darkModeButton.setOnClickListener {
            darkMode = (darkMode+1)%3
            Functions.saveDarkMOdeToSharedPreferences(requireContext(),darkMode)
            displayDarkModeTextView()
            setDrawable()
        }

    }

    private fun checkUser() {

        updateUI()

    }

    private fun updateUI(){
        loggedInStatus = Functions.readLoggedInStatusFromSharedPreferences(requireContext())


        displayUI(loggedInStatus)

    }

    private fun displayUI(loggedInStatus: LoggedInStatus) {

        if(loggedInStatus.loggedIn){
            display(loggedInStatus.userid)
        }
        else{
            displayNotLoggedIn()
        }
    }

    // display nothing
    private fun displayNotLoggedIn() {
        binding.userNameTv.text="-"
    }

    //display user statistics
    private fun display(userID:String) {
        val tUser = User(userID,
            Functions.checkUserNameFromSharedPreferences(requireContext(),userID),
            Functions.readGameAFromSharedPreferences(requireContext(),userID),
            Functions.readGameBFromSharedPreferences(requireContext(),userID))

        binding.userNameTv.text=tUser.userName
    }

    private fun makeUI() {
        getScreenHeightAndWidth()
        setViewSizes()
        setDrawable()
        connectViews()

    }

    private fun connectViews() {
        val set = ConstraintSet()
        set.clone(binding.settingsFragment)

        set.connect(binding.settingsActivityContainer.id,
            ConstraintSet.TOP,binding.settingsFragment.id,
            ConstraintSet.TOP,0)
        set.connect(binding.settingsActivityContainer.id,
            ConstraintSet.BOTTOM,binding.settingsFragment.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.settingsActivityContainer.id,
            ConstraintSet.RIGHT,binding.settingsFragment.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.settingsActivityContainer.id,
            ConstraintSet.LEFT,binding.settingsFragment.id,
            ConstraintSet.LEFT,0)

        set.connect(binding.userNameTv.id,
            ConstraintSet.TOP,binding.settingsActivityContainer.id,
            ConstraintSet.TOP,
            (screenUnit*0.5).toInt()
        )
        set.connect(binding.userNameTv.id,
            ConstraintSet.LEFT,binding.settingsActivityContainer.id,
            ConstraintSet.LEFT,screenUnit)

        set.connect(binding.backToGameLinearLayoutEt.id,
            ConstraintSet.TOP,binding.settingsActivityContainer.id,
            ConstraintSet.TOP, (screenUnit*8.5).toInt())
        set.connect(binding.backToGameLinearLayoutEt.id,
            ConstraintSet.LEFT,binding.settingsActivityContainer.id,
            ConstraintSet.LEFT,screenUnit)

        set.connect(binding.accountLinearLayout.id,
            ConstraintSet.TOP,binding.settingsActivityContainer.id,
            ConstraintSet.TOP, (screenUnit*0.5).toInt())
        set.connect(binding.accountLinearLayout.id,
            ConstraintSet.RIGHT,binding.settingsActivityContainer.id,
            ConstraintSet.RIGHT, screenUnit)

        set.connect(binding.rankingLinearLayout.id,
            ConstraintSet.TOP,binding.settingsActivityContainer.id,
            ConstraintSet.TOP, (screenUnit*2.5).toInt())
        set.connect(binding.rankingLinearLayout.id,
            ConstraintSet.RIGHT,binding.settingsActivityContainer.id,
            ConstraintSet.RIGHT, 0)
        set.connect(binding.rankingLinearLayout.id,
            ConstraintSet.LEFT,binding.settingsActivityContainer.id,
            ConstraintSet.LEFT, 0)

        set.connect(binding.otherGamesLinearLayout.id,
            ConstraintSet.TOP,binding.settingsActivityContainer.id,
            ConstraintSet.TOP, (screenUnit*4.5).toInt())
        set.connect(binding.otherGamesLinearLayout.id,
            ConstraintSet.RIGHT,binding.settingsActivityContainer.id,
            ConstraintSet.RIGHT, 0)
        set.connect(binding.otherGamesLinearLayout.id,
            ConstraintSet.LEFT,binding.settingsActivityContainer.id,
            ConstraintSet.LEFT, 0)

        set.connect(binding.darkModeLinearLayout.id,
            ConstraintSet.TOP,binding.settingsActivityContainer.id,
            ConstraintSet.TOP, (screenUnit*6.5).toInt())
        set.connect(binding.darkModeLinearLayout.id,
            ConstraintSet.RIGHT,binding.otherGamesLinearLayout.id,
            ConstraintSet.RIGHT, 0
        )

        set.connect(binding.darkModeTv.id,
            ConstraintSet.TOP,binding.settingsActivityContainer.id,
            ConstraintSet.TOP, (screenUnit*6.5).toInt())
        set.connect(binding.darkModeTv.id,
            ConstraintSet.LEFT,binding.otherGamesLinearLayout.id,
            ConstraintSet.LEFT, 0
        )

        set.applyTo(binding.settingsFragment)

    }

    private fun setDrawable() {

        binding.backToGameTextView.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))
        binding.accountTv.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))
        binding.otherGamesTv.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))
        binding.rankingTv.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))
        binding.darkModeTextView.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))

        binding.settingsActivityContainer.setBackgroundColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getBackgroundColor()))
        binding.userNameTv.background = TextViewDrawable(requireContext())
        binding.darkModeTv.background = TextViewDrawable(requireContext())
        binding.backToGame.setImageDrawable(StartButton(requireContext()))
        if(rankingReady){
            binding.ranking.setImageDrawable(StartButton(requireContext()))
        }else{
            binding.ranking.setImageDrawable(StartButtonGray(requireContext()))
        }


        binding.darkModeButton.setImageDrawable(StartButton(requireContext()))
        val newApp = Functions.readNewAppAvailable(requireContext())
        binding.otherGamesButton.setImageDrawable(if(newApp) StartButtonGreen(requireContext())else StartButton(requireContext()))
        binding.accountButton.setImageDrawable(StartButton(requireContext()))
        binding.backToGameLinearLayoutEt.background = RoundedFrameDrawable(requireContext(),backToGameButtonSize.height/20,backToGameButtonSize.height/2)
        binding.otherGamesLinearLayout.background = RoundedFrameDrawable(requireContext(),otherGamesButtonSize.height/20,otherGamesButtonSize.height/2)
        binding.rankingLinearLayout.background = RoundedFrameDrawable(requireContext(),rankingButtonSize.height/20,rankingButtonSize.height/2)
        binding.darkModeLinearLayout.background = RoundedFrameDrawable(requireContext(),darkModeButtonSize.height/20,darkModeButtonSize.height/2)
        binding.accountLinearLayout.background = RoundedFrameDrawable(requireContext(),rankingButtonSize.height/20,rankingButtonSize.height/2)

    }


    private fun getScreenHeightAndWidth() {
        screenUnit = ScreenMetricsCompat.getScreenSize(requireContext())
    }

    private fun setViewSizes() {

        userNameSize.height= (screenUnit*4/3).toDouble()
        userNameSize.width= (screenUnit*10).toDouble()

        wholeScreenSize.width = 20.0 * screenUnit
        wholeScreenSize.height = 10.0 * screenUnit

        binding.settingsActivityContainer.layoutParams = ConstraintLayout.LayoutParams((wholeScreenSize.width).toInt(),(wholeScreenSize.height).toInt())

        binding.userNameTv.layoutParams = ConstraintLayout.LayoutParams((userNameSize.width).toInt(), (userNameSize.height).toInt())
        binding.userNameTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        backToGameButtonSize.width= (screenUnit*4/3).toDouble()
        backToGameButtonSize.height = backToGameButtonSize.width

        binding.backToGame.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameTextView.layoutParams = LinearLayout.LayoutParams((4*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        binding.backToGameTextViewBlank.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width/2).toInt(),(backToGameButtonSize.height).toInt())

        rankingButtonSize.width= (screenUnit*4/3).toDouble()
        rankingButtonSize.height = rankingButtonSize.width

        binding.ranking.layoutParams = LinearLayout.LayoutParams((rankingButtonSize.width).toInt(),(rankingButtonSize.height).toInt())
        binding.rankingTv.layoutParams = LinearLayout.LayoutParams((8.5*rankingButtonSize.width).toInt(),(rankingButtonSize.height).toInt())
        binding.rankingTvBlank.layoutParams = LinearLayout.LayoutParams((0.5*rankingButtonSize.width).toInt(),(rankingButtonSize.height).toInt())
        binding.rankingTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        darkModeButtonSize.width= (screenUnit*4/3).toDouble()
        darkModeButtonSize.height = rankingButtonSize.width

        binding.darkModeButton.layoutParams = LinearLayout.LayoutParams((darkModeButtonSize.width).toInt(),(darkModeButtonSize.height).toInt())
        binding.darkModeTextView.layoutParams = LinearLayout.LayoutParams((4.0*darkModeButtonSize.width).toInt(),(darkModeButtonSize.height).toInt())
        binding.darkModeTextViewBlank.layoutParams = LinearLayout.LayoutParams((0.5*darkModeButtonSize.width).toInt(),(darkModeButtonSize.height).toInt())
        binding.darkModeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        darkModeTextViewSize.width = (screenUnit*5).toDouble()
        darkModeTextViewSize.height = (screenUnit*4/3).toDouble()
        binding.darkModeTv.layoutParams = ConstraintLayout.LayoutParams(darkModeTextViewSize.width.toInt(),darkModeTextViewSize.height.toInt())
        binding.darkModeTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        accountButtonSize.width= (screenUnit*4/3).toDouble()
        accountButtonSize.height = accountButtonSize.width
        binding.accountButton.layoutParams = LinearLayout.LayoutParams((accountButtonSize.width).toInt(),(accountButtonSize.height).toInt())
        binding.accountTv.layoutParams = LinearLayout.LayoutParams((4*accountButtonSize.width).toInt(),(accountButtonSize.height).toInt())
        binding.accountTvBlank.layoutParams = LinearLayout.LayoutParams((0.5*accountButtonSize.width).toInt(),(accountButtonSize.height).toInt())
        binding.accountTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        otherGamesButtonSize.width= (screenUnit*4/3).toDouble()
        otherGamesButtonSize.height = otherGamesButtonSize.width

        binding.otherGamesButton.layoutParams = LinearLayout.LayoutParams((otherGamesButtonSize.width).toInt(),(otherGamesButtonSize.height).toInt())
        binding.otherGamesTv.layoutParams = LinearLayout.LayoutParams((8.5*otherGamesButtonSize.width).toInt(),(otherGamesButtonSize.height).toInt())
        binding.otherGamesTvBlank.layoutParams = LinearLayout.LayoutParams((0.5*otherGamesButtonSize.width).toInt(),(otherGamesButtonSize.height).toInt())
        binding.otherGamesTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

    }

}