package com.tt.eggs.fragments

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import com.tt.eggs.MainActivity
import com.tt.eggs.R
import com.tt.eggs.classes.Dimension
import com.tt.eggs.classes.Functions
import com.tt.eggs.classes.ScreenMetricsCompat
import com.tt.eggs.classes.User
import com.tt.eggs.databinding.FragmentRankingBinding
import com.tt.eggs.drawable.*


class RankingFragment : Fragment() {

    private lateinit var userList: MutableList<User>
    private lateinit var userid: String
    private var index: Int = 0


    private var screenUnit = 0
    private val headerSize = Dimension()
    private val userNameSize = Dimension()
    private val highScoreASize = Dimension()
    private val highScoreBSize = Dimension()
    private val totalPointsSize = Dimension()

    private val positionLayoutSize = Dimension()
    private val arrowSize = Dimension()
    private val backToGameButtonSize = Dimension()
    private val wholeScreenSize = Dimension()

    private var _binding:FragmentRankingBinding? = null
    private val binding get() = _binding!!

    private var rankingReady = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = activity as MainActivity
        userList = activity.getList()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeUI()
        prepareRanking()

        binding.backToGameRankingImageView.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.rankingUp.setOnClickListener {
            if(rankingReady){
            if(index>0){
                index -= 1
                displayFiveUsersWithIndex(userid)
            }
            }
        }

        binding.rankingDown.setOnClickListener {
            if(rankingReady){
            if(index<userList.size-4){
                index +=1
                displayFiveUsersWithIndex(userid)
            }
            }
        }
    }


    private fun prepareRanking(){
        //todo finish first!!!
    }

    private fun displayFiveUsersWithIndex(userId: String) {

        // first position
        if(index<userList.size){
            displaySingleUser(index,binding.rankingUserName1,binding.rankingHighScoreA1, binding.rankingHighScoreB1, binding.rankingTotalScore1, userId)
        }
        else{
            displaySingleUser(-1,binding.rankingUserName1,binding.rankingHighScoreA1, binding.rankingHighScoreB1, binding.rankingTotalScore1, userId)
        }


        //second position
        if(index+1<userList.size){
            displaySingleUser(index+1,binding.rankingUserName2,binding.rankingHighScoreA2, binding.rankingHighScoreB2, binding.rankingTotalScore2, userId)
        }
        else
        {
            displaySingleUser(-1,binding.rankingUserName2,binding.rankingHighScoreA2, binding.rankingHighScoreB2, binding.rankingTotalScore2, userId)
        }


        // third position
        if(index+2<userList.size){
            displaySingleUser(index+2,binding.rankingUserName3,binding.rankingHighScoreA3, binding.rankingHighScoreB3, binding.rankingTotalScore3, userId)
        }
        else
        {
            displaySingleUser(-1,binding.rankingUserName3,binding.rankingHighScoreA3, binding.rankingHighScoreB3, binding.rankingTotalScore3, userId)
        }

        // fourth position
        if(index+3<userList.size){
            displaySingleUser(index+3,binding.rankingUserName4,binding.rankingHighScoreA4, binding.rankingHighScoreB4, binding.rankingTotalScore4, userId)
        }
        else
        {
            displaySingleUser(-1,binding.rankingUserName4,binding.rankingHighScoreA4, binding.rankingHighScoreB4, binding.rankingTotalScore4, userId)
        }

        // fifth position
        if(index+4<userList.size){
            displaySingleUser(index+4,binding.rankingUserName5,binding.rankingHighScoreA5, binding.rankingHighScoreB5, binding.rankingTotalScore5, userId)
        }
        else
        {
            displaySingleUser(-1,binding.rankingUserName5,binding.rankingHighScoreA5, binding.rankingHighScoreB5, binding.rankingTotalScore5, userId)
        }
    }

    private fun displaySingleUser(
        index: Int,
        rankingUserName1: TextView?,
        rankingHighScoreA1: TextView?,
        rankingHighScoreB1: TextView?,
        rankingTotalScore1: TextView?,
        userId: String
    ) {
        if(index>=0) {
            val position = index + 1
            rankingUserName1!!.text = getString(R.string.ranking_position,position,userList[index].userName)
            if (userList[index].gameA.counterA == 0) {
                rankingHighScoreA1!!.text = userList[index].gameA.highScoreA.toString()
            } else {
                rankingHighScoreA1!!.text = getString(R.string.high_score,userList[index].gameA.highScoreA,userList[index].gameA.counterA)
            }
            if (userList[index].gameB.counterB == 0) {
                rankingHighScoreB1!!.text = userList[index].gameB.highScoreB.toString()
            } else {
                rankingHighScoreB1!!.text = getString(R.string.high_score,userList[index].gameB.highScoreB,userList[index].gameB.counterB)
            }

            rankingTotalScore1!!.text = userList[index].score().toString()

            if (userList[index].id.equals(userId)) {
                rankingUserName1.setTextColor(getColor(requireContext(),R.color.red))
                rankingHighScoreA1.setTextColor(getColor(requireContext(),R.color.red))
                rankingHighScoreB1.setTextColor(getColor(requireContext(),R.color.red))
                rankingTotalScore1.setTextColor(getColor(requireContext(),R.color.red))
            } else {
                rankingUserName1.setTextColor(getColor(requireContext(),R.color.black))
                rankingHighScoreA1.setTextColor(getColor(requireContext(),R.color.black))
                rankingHighScoreB1.setTextColor(getColor(requireContext(),R.color.black))
                rankingTotalScore1.setTextColor(getColor(requireContext(),R.color.black))
            }
        }
        else{
            rankingUserName1!!.text = ""
            rankingHighScoreA1!!.text = ""
            rankingHighScoreB1!!.text = ""
            rankingTotalScore1!!.text = ""
        }

    }

    private fun makeUI() {
        getScreenHeightAndWidth()
        setViewSizes()
        setDrawable()
        makeViewConnections()
    }

    private fun getScreenHeightAndWidth() {
        screenUnit= ScreenMetricsCompat.getScreenSize(requireContext())

    }

    private fun setViewSizes() {

        val username =7
        val highScore = 3
        val total = 4

        headerSize.height = (screenUnit).toDouble()



        userNameSize.width = (screenUnit*username).toDouble()
        userNameSize.height = headerSize.height
        highScoreASize.width = (screenUnit*highScore).toDouble()
        highScoreASize.height = headerSize.height
        highScoreBSize.width = (screenUnit*highScore).toDouble()
        highScoreBSize.height = headerSize.height
        totalPointsSize.width = (screenUnit*total).toDouble()
        totalPointsSize.height = headerSize.height
        headerSize.width=userNameSize.width+
                highScoreASize.width+
                highScoreBSize.width+
                totalPointsSize.width


        positionLayoutSize.width=headerSize.width

        wholeScreenSize.width = 20.0*screenUnit
        wholeScreenSize.height = 10.0*screenUnit

        binding.rankingActivityContainer.layoutParams = ConstraintLayout.LayoutParams((wholeScreenSize.width).toInt(),(wholeScreenSize.height).toInt())
        binding.header.layoutParams= ConstraintLayout.LayoutParams((headerSize.width).toInt(),(headerSize.height).toInt())
        binding.rankingUserName.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        binding.rankingHighScoreA.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        binding.rankingHighScoreB.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        binding.rankingTotalScore.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())


        binding.rankingUserName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreA.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreB.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore.setPadding(0,0, (screenUnit*0.5).toInt(),0)



        positionLayoutSize.width=positionLayoutSize.width
        positionLayoutSize.height= (screenUnit*1.5)
        binding.position1.layoutParams=
            ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        binding.position2.layoutParams=
            ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        binding.position3.layoutParams=
            ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        binding.position4.layoutParams=
            ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        binding.position5.layoutParams=
            ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        binding.progressBar1.layoutParams=
            ConstraintLayout.LayoutParams((positionLayoutSize.width*0.9).toInt(),(positionLayoutSize.height).toInt())
        binding.progressBar2.layoutParams=
            ConstraintLayout.LayoutParams((positionLayoutSize.width*0.9).toInt(),(positionLayoutSize.height).toInt())
        binding.progressBar3.layoutParams=
            ConstraintLayout.LayoutParams((positionLayoutSize.width*0.9).toInt(),(positionLayoutSize.height).toInt())
        binding.progressBar4.layoutParams=
            ConstraintLayout.LayoutParams((positionLayoutSize.width*0.9).toInt(),(positionLayoutSize.height).toInt())
        binding.progressBar5.layoutParams=
            ConstraintLayout.LayoutParams((positionLayoutSize.width*0.9).toInt(),(positionLayoutSize.height).toInt())


        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        binding.rankingUserName1.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height*1.5).toInt())
        binding.rankingHighScoreA1.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height*1.5).toInt())
        binding.rankingHighScoreB1.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height*1.5).toInt())
        binding.rankingTotalScore1.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height*1.5).toInt())
        binding.rankingTotalScore1.setPadding(0,0, (screenUnit*0.5).toInt(),0)

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName1.setPadding(screenUnit/2,0,0,0)
        binding.rankingHighScoreA1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreB1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())

        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        binding.rankingUserName2.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height*1.5).toInt())
        binding.rankingUserName2.setPadding(screenUnit/2,0,0,0)
        binding.rankingHighScoreA2.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height*1.5).toInt())
        binding.rankingHighScoreB2.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height*1.5).toInt())
        binding.rankingTotalScore2.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height*1.5).toInt())
        binding.rankingTotalScore2.setPadding(0,0, (screenUnit*0.5).toInt(),0)

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreA2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreB2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())

        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        binding.rankingUserName3.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height*1.5).toInt())
        binding.rankingUserName3.setPadding(screenUnit/2,0,0,0)
        binding.rankingHighScoreA3.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height*1.5).toInt())
        binding.rankingHighScoreB3.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height*1.5).toInt())
        binding.rankingTotalScore3.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height*1.5).toInt())
        binding.rankingTotalScore3.setPadding(0,0, (screenUnit*0.5).toInt(),0)

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreA3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreB3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())


        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        binding.rankingUserName4.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height*1.5).toInt())
        binding.rankingHighScoreA4.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height*1.5).toInt())
        binding.rankingHighScoreB4.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height*1.5).toInt())
        binding.rankingTotalScore4.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height*1.5).toInt())

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName4.setPadding(screenUnit/2,0,0,0)
        binding.rankingHighScoreA4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreB4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore4.setPadding(0,0, (screenUnit*0.5).toInt(),0)

        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        binding.rankingUserName5.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height*1.5).toInt())
        binding.rankingHighScoreA5.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height*1.5).toInt())
        binding.rankingHighScoreB5.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height*1.5).toInt())
        binding.rankingTotalScore5.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height*1.5).toInt())

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName5.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName5.setPadding(screenUnit/2,0,0,0)
        binding.rankingHighScoreA5.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreB5.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore5.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore5.setPadding(0,0, (screenUnit*0.5).toInt(),0)

        arrowSize.width= (screenUnit*4/3).toDouble()
        arrowSize.height = arrowSize.width*2

        binding.rankingUp.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width).toInt(),(arrowSize.height).toInt())
        binding.rankingDown.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width).toInt(),(arrowSize.height).toInt())

        backToGameButtonSize.width= (screenUnit*4/3).toDouble()
        backToGameButtonSize.height = backToGameButtonSize.width

        binding.backToGameRankingImageView.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGmeRanking.layoutParams = LinearLayout.LayoutParams((4*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameBlankRanking.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width/2).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGmeRanking.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

    }

    private fun setDrawable() {
        binding.position1.background = TextViewDrawableWithBorder(requireContext(),positionLayoutSize.width,positionLayoutSize.height)
        binding.position2.background = TextViewDrawableWithBorder(requireContext(),positionLayoutSize.width,positionLayoutSize.height)
        binding.position3.background = TextViewDrawableWithBorder(requireContext(),positionLayoutSize.width,positionLayoutSize.height)
        binding.position4.background = TextViewDrawableWithBorder(requireContext(),positionLayoutSize.width,positionLayoutSize.height)
        binding.position5.background = TextViewDrawableWithBorder(requireContext(),positionLayoutSize.width,positionLayoutSize.height)
        binding.rankingUp.setImageDrawable(ArrowUp(requireContext(),arrowSize.width,arrowSize.height))
        binding.rankingDown.setImageDrawable(ArrowDown(requireContext(),arrowSize.width,arrowSize.height))
        binding.backToGameRankingImageView.setImageDrawable(StartButton(requireContext(),backToGameButtonSize.width,backToGameButtonSize.height))
        binding.backToGameLinearLayoutRanking.background = RoundedFrameDrawable(requireContext(),5.5*backToGameButtonSize.width,backToGameButtonSize.height,
            backToGameButtonSize.height/20,
            backToGameButtonSize.height/2
        )
    }

    private fun makeViewConnections() {
        val set = ConstraintSet()
        set.clone(binding.rankingFragment)

        set.connect(binding.rankingActivityContainer.id,
            ConstraintSet.TOP,binding.rankingFragment.id,
            ConstraintSet.TOP,0)
        set.connect(binding.rankingActivityContainer.id,
            ConstraintSet.BOTTOM,binding.rankingFragment.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.rankingActivityContainer.id,
            ConstraintSet.RIGHT,binding.rankingFragment.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.rankingActivityContainer.id,
            ConstraintSet.LEFT,binding.rankingFragment.id,
            ConstraintSet.LEFT,0)

        set.connect(binding.header.id,
            ConstraintSet.LEFT,binding.rankingActivityContainer.id,
            ConstraintSet.LEFT,screenUnit/2)
        set.connect(binding.header.id,
            ConstraintSet.TOP,binding.rankingActivityContainer.id,
            ConstraintSet.TOP,0)

        set.connect(binding.position1.id,
            ConstraintSet.TOP,binding.header.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.position1.id,
            ConstraintSet.LEFT,binding.rankingActivityContainer.id,
            ConstraintSet.LEFT,screenUnit/2)

        set.connect(binding.position2.id,
            ConstraintSet.TOP,binding.position1.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.position2.id,
            ConstraintSet.LEFT,binding.rankingActivityContainer.id,
            ConstraintSet.LEFT,screenUnit/2)

        set.connect(binding.position3.id,
            ConstraintSet.TOP,binding.position2.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.position3.id,
            ConstraintSet.LEFT,binding.rankingActivityContainer.id,
            ConstraintSet.LEFT,screenUnit/2)

        set.connect(binding.position4.id,
            ConstraintSet.TOP,binding.position3.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.position4.id,
            ConstraintSet.LEFT,binding.rankingActivityContainer.id,
            ConstraintSet.LEFT,screenUnit/2)

        set.connect(binding.position5.id,
            ConstraintSet.TOP,binding.position4.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.position5.id,
            ConstraintSet.LEFT,binding.rankingActivityContainer.id,
            ConstraintSet.LEFT,screenUnit/2)

        set.connect(binding.rankingUp.id,
            ConstraintSet.TOP,binding.position1.id,
            ConstraintSet.TOP,0)
        set.connect(binding.rankingUp.id,
            ConstraintSet.LEFT,binding.position1.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.rankingUp.id,
            ConstraintSet.RIGHT,binding.rankingActivityContainer.id,
            ConstraintSet.RIGHT,0)

        set.connect(binding.rankingDown.id,
            ConstraintSet.BOTTOM,binding.position5.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.rankingDown.id,
            ConstraintSet.LEFT,binding.position5.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.rankingDown.id,
            ConstraintSet.RIGHT,binding.rankingActivityContainer.id,
            ConstraintSet.RIGHT,0)

        set.connect(binding.backToGameLinearLayoutRanking.id,
            ConstraintSet.LEFT,binding.position5.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.backToGameLinearLayoutRanking.id,
            ConstraintSet.BOTTOM,binding.rankingActivityContainer.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.backToGameLinearLayoutRanking.id,
            ConstraintSet.TOP,binding.position5.id,
            ConstraintSet.BOTTOM,0)

        set.connect(binding.progressBar1.id,
            ConstraintSet.TOP,binding.position1.id,
            ConstraintSet.TOP,0)
        set.connect(binding.progressBar1.id,
            ConstraintSet.BOTTOM,binding.position1.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.progressBar1.id,
            ConstraintSet.LEFT,binding.position1.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.progressBar1.id,
            ConstraintSet.RIGHT,binding.position1.id,
            ConstraintSet.RIGHT,0)

        set.connect(binding.progressBar2.id,
            ConstraintSet.TOP,binding.position2.id,
            ConstraintSet.TOP,0)
        set.connect(binding.progressBar2.id,
            ConstraintSet.BOTTOM,binding.position2.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.progressBar2.id,
            ConstraintSet.LEFT,binding.position2.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.progressBar2.id,
            ConstraintSet.RIGHT,binding.position2.id,
            ConstraintSet.RIGHT,0)

        set.connect(binding.progressBar3.id,
            ConstraintSet.TOP,binding.position3.id,
            ConstraintSet.TOP,0)
        set.connect(binding.progressBar3.id,
            ConstraintSet.BOTTOM,binding.position3.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.progressBar3.id,
            ConstraintSet.LEFT,binding.position3.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.progressBar3.id,
            ConstraintSet.RIGHT,binding.position3.id,
            ConstraintSet.RIGHT,0)

        set.connect(binding.progressBar4.id,
            ConstraintSet.TOP,binding.position4.id,
            ConstraintSet.TOP,0)
        set.connect(binding.progressBar4.id,
            ConstraintSet.BOTTOM,binding.position4.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.progressBar4.id,
            ConstraintSet.LEFT,binding.position4.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.progressBar4.id,
            ConstraintSet.RIGHT,binding.position4.id,
            ConstraintSet.RIGHT,0)

        set.connect(binding.progressBar5.id,
            ConstraintSet.TOP,binding.position5.id,
            ConstraintSet.TOP,0)
        set.connect(binding.progressBar5.id,
            ConstraintSet.BOTTOM,binding.position5.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.progressBar5.id,
            ConstraintSet.LEFT,binding.position5.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.progressBar5.id,
            ConstraintSet.RIGHT,binding.position5.id,
            ConstraintSet.RIGHT,0)

        set.applyTo(binding.rankingFragment)

    }


}

//TODO