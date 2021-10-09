package com.tt.eggs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.eggs.classes.Dimension
import com.tt.eggs.classes.User
import com.tt.eggs.databinding.ActivityRankingBinding
import com.tt.eggs.drawable.*

class Ranking : AppCompatActivity() {

    private lateinit var userList: MutableList<User>
    private lateinit var userid: String
    private var index: Int = 0

    private var screenHeight = 0
    private var screenWidth = 0
    private var screenUnit = 0
    private val headerSize = Dimension()
    private val userNameSize = Dimension()
    private val highScoreASize = Dimension()
    private val highScoreBSize = Dimension()
    private val totalPointsSize = Dimension()

    private val positionLayoutSize = Dimension()
    private val arrowSize = Dimension()
    private val backToGameButtonSize = Dimension()

    private lateinit var binding: ActivityRankingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        fullScreen(view)

        makeUI()

        binding.backToGameRankingImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.progressBar1.visibility = View.VISIBLE
        binding.progressBar2.visibility = View.VISIBLE
        binding.progressBar3.visibility = View.VISIBLE
        binding.progressBar4.visibility = View.VISIBLE
        binding.progressBar5.visibility = View.VISIBLE

//        recyclerView.visibility = View.GONE
//        ranking_error.visibility = View.GONE

        val currentUser = Firebase.auth.currentUser
        userid = currentUser?.uid ?: ""
        userList = mutableListOf()
        createUserListFromFirebase()




        binding.rankingUp.setOnClickListener {
            if(index>0){
                index -= 1
                displayFiveUsersWithIndex(userid)
            }
        }

        binding.rankingDown.setOnClickListener {
            if(index<userList.size-4){
                index +=1
                displayFiveUsersWithIndex(userid)
            }
        }





    }

    private fun makeUI() {
        getScreenHeightAndWidth()
        setViewSizes()
        makeViewConnections()
        setDrawable()


    }



    private fun makeViewConnections() {
        val set = ConstraintSet()
        set.clone(binding.rankingActivity)

        set.connect(binding.header.id,ConstraintSet.LEFT,binding.rankingActivity.id,ConstraintSet.LEFT,screenUnit/2)
        set.connect(binding.header.id,ConstraintSet.TOP,binding.rankingActivity.id,ConstraintSet.TOP,0)

        set.connect(binding.position1.id,ConstraintSet.TOP,binding.header.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.position1.id,ConstraintSet.LEFT,binding.rankingActivity.id,ConstraintSet.LEFT,screenUnit/2)

        set.connect(binding.position2.id,ConstraintSet.TOP,binding.position1.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.position2.id,ConstraintSet.LEFT,binding.rankingActivity.id,ConstraintSet.LEFT,screenUnit/2)

        set.connect(binding.position3.id,ConstraintSet.TOP,binding.position2.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.position3.id,ConstraintSet.LEFT,binding.rankingActivity.id,ConstraintSet.LEFT,screenUnit/2)

        set.connect(binding.position4.id,ConstraintSet.TOP,binding.position3.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.position4.id,ConstraintSet.LEFT,binding.rankingActivity.id,ConstraintSet.LEFT,screenUnit/2)

        set.connect(binding.position5.id,ConstraintSet.TOP,binding.position4.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.position5.id,ConstraintSet.LEFT,binding.rankingActivity.id,ConstraintSet.LEFT,screenUnit/2)

        set.connect(binding.rankingUp.id,ConstraintSet.TOP,binding.position1.id,ConstraintSet.TOP,0)
        set.connect(binding.rankingUp.id,ConstraintSet.LEFT,binding.position1.id,ConstraintSet.RIGHT,0)
        set.connect(binding.rankingUp.id,ConstraintSet.RIGHT,binding.rankingActivity.id,ConstraintSet.RIGHT,0)

        set.connect(binding.rankingDown.id,ConstraintSet.BOTTOM,binding.position5.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.rankingDown.id,ConstraintSet.LEFT,binding.position5.id,ConstraintSet.RIGHT,0)
        set.connect(binding.rankingDown.id,ConstraintSet.RIGHT,binding.rankingActivity.id,ConstraintSet.RIGHT,0)

        set.connect(binding.backToGameLinearLayoutRanking.id,ConstraintSet.LEFT,binding.position5.id,ConstraintSet.LEFT,0)
        set.connect(binding.backToGameLinearLayoutRanking.id,ConstraintSet.BOTTOM,binding.rankingActivity.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.backToGameLinearLayoutRanking.id,ConstraintSet.TOP,binding.position5.id,ConstraintSet.BOTTOM,0)

        set.connect(binding.progressBar1.id,ConstraintSet.TOP,binding.position1.id,ConstraintSet.TOP,0)
        set.connect(binding.progressBar1.id,ConstraintSet.BOTTOM,binding.position1.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.progressBar1.id,ConstraintSet.LEFT,binding.position1.id,ConstraintSet.LEFT,0)
        set.connect(binding.progressBar1.id,ConstraintSet.RIGHT,binding.position1.id,ConstraintSet.RIGHT,0)

        set.connect(binding.progressBar2.id,ConstraintSet.TOP,binding.position2.id,ConstraintSet.TOP,0)
        set.connect(binding.progressBar2.id,ConstraintSet.BOTTOM,binding.position2.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.progressBar2.id,ConstraintSet.LEFT,binding.position2.id,ConstraintSet.LEFT,0)
        set.connect(binding.progressBar2.id,ConstraintSet.RIGHT,binding.position2.id,ConstraintSet.RIGHT,0)

        set.connect(binding.progressBar3.id,ConstraintSet.TOP,binding.position3.id,ConstraintSet.TOP,0)
        set.connect(binding.progressBar3.id,ConstraintSet.BOTTOM,binding.position3.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.progressBar3.id,ConstraintSet.LEFT,binding.position3.id,ConstraintSet.LEFT,0)
        set.connect(binding.progressBar3.id,ConstraintSet.RIGHT,binding.position3.id,ConstraintSet.RIGHT,0)

        set.connect(binding.progressBar4.id,ConstraintSet.TOP,binding.position4.id,ConstraintSet.TOP,0)
        set.connect(binding.progressBar4.id,ConstraintSet.BOTTOM,binding.position4.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.progressBar4.id,ConstraintSet.LEFT,binding.position4.id,ConstraintSet.LEFT,0)
        set.connect(binding.progressBar4.id,ConstraintSet.RIGHT,binding.position4.id,ConstraintSet.RIGHT,0)

        set.connect(binding.progressBar5.id,ConstraintSet.TOP,binding.position5.id,ConstraintSet.TOP,0)
        set.connect(binding.progressBar5.id,ConstraintSet.BOTTOM,binding.position5.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.progressBar5.id,ConstraintSet.LEFT,binding.position5.id,ConstraintSet.LEFT,0)
        set.connect(binding.progressBar5.id,ConstraintSet.RIGHT,binding.position5.id,ConstraintSet.RIGHT,0)

        set.applyTo(binding.rankingActivity)

    }

    private fun setViewSizes() {

        val username =4
        val highScore = 1.5
        val total = 2

        headerSize.height = (screenUnit).toDouble()


        val unit = screenWidth/10

//        positionSize.width = (unit*2).toDouble()
//        positionSize.height = headerSize.height
        userNameSize.width = (unit*username).toDouble()
        userNameSize.height = headerSize.height
        highScoreASize.width = (unit*highScore)
        highScoreASize.height = headerSize.height
        highScoreBSize.width = (unit*highScore)
        highScoreBSize.height = headerSize.height
        totalPointsSize.width = (unit*total).toDouble()
        totalPointsSize.height = headerSize.height
        headerSize.width=userNameSize.width+
                highScoreASize.width+
                highScoreBSize.width+
                totalPointsSize.width

        binding.header.layoutParams=ConstraintLayout.LayoutParams((headerSize.width).toInt(),(headerSize.height).toInt())
//        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        binding.rankingUserName.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        binding.rankingHighScoreA.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        binding.rankingHighScoreB.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        binding.rankingTotalScore.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreA.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreB.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore.setPadding(0,0, (screenUnit*0.5).toInt(),0)

        userNameSize.width = (unit*username).toDouble()
        userNameSize.height = (screenUnit*1.5)
        highScoreASize.width = (unit*highScore)
        highScoreASize.height = (screenUnit*1.5)
        highScoreBSize.width = (unit*highScore)
        highScoreBSize.height = (screenUnit*1.5)
        totalPointsSize.width = (unit*total).toDouble()
        totalPointsSize.height = (screenUnit*1.5)
        positionLayoutSize.width=userNameSize.width+
                highScoreASize.width+
                highScoreBSize.width+
                totalPointsSize.width

        positionLayoutSize.width=positionLayoutSize.width
        positionLayoutSize.height= (screenUnit*1.5)
        binding.position1.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        binding.position2.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        binding.position3.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        binding.position4.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        binding.position5.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        binding.progressBar1.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width*0.9).toInt(),(positionLayoutSize.height).toInt())
        binding.progressBar2.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width*0.9).toInt(),(positionLayoutSize.height).toInt())
        binding.progressBar3.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width*0.9).toInt(),(positionLayoutSize.height).toInt())
        binding.progressBar4.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width*0.9).toInt(),(positionLayoutSize.height).toInt())
        binding.progressBar5.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width*0.9).toInt(),(positionLayoutSize.height).toInt())


        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        binding.rankingUserName1.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        binding.rankingHighScoreA1.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        binding.rankingHighScoreB1.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        binding.rankingTotalScore1.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())
        binding.rankingTotalScore1.setPadding(0,0, (screenUnit*0.5).toInt(),0)

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName1.setPadding(screenUnit/2,0,0,0)
        binding.rankingHighScoreA1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreB1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())

        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        binding.rankingUserName2.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        binding.rankingUserName2.setPadding(screenUnit/2,0,0,0)
        binding.rankingHighScoreA2.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        binding.rankingHighScoreB2.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        binding.rankingTotalScore2.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())
        binding.rankingTotalScore2.setPadding(0,0, (screenUnit*0.5).toInt(),0)

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreA2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreB2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())

        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        binding.rankingUserName3.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        binding.rankingUserName3.setPadding(screenUnit/2,0,0,0)
        binding.rankingHighScoreA3.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        binding.rankingHighScoreB3.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        binding.rankingTotalScore3.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())
        binding.rankingTotalScore3.setPadding(0,0, (screenUnit*0.5).toInt(),0)

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreA3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreB3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())


        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        binding.rankingUserName4.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        binding.rankingHighScoreA4.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        binding.rankingHighScoreB4.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        binding.rankingTotalScore4.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingUserName4.setPadding(screenUnit/2,0,0,0)
        binding.rankingHighScoreA4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingHighScoreB4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.rankingTotalScore4.setPadding(0,0, (screenUnit*0.5).toInt(),0)

        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        binding.rankingUserName5.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        binding.rankingHighScoreA5.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        binding.rankingHighScoreB5.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        binding.rankingTotalScore5.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())

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
        binding.position1.background = TextViewDrawableWithBorder(this,positionLayoutSize.width,positionLayoutSize.height)
        binding.position2.background = TextViewDrawableWithBorder(this,positionLayoutSize.width,positionLayoutSize.height)
        binding.position3.background = TextViewDrawableWithBorder(this,positionLayoutSize.width,positionLayoutSize.height)
        binding.position4.background = TextViewDrawableWithBorder(this,positionLayoutSize.width,positionLayoutSize.height)
        binding.position5.background = TextViewDrawableWithBorder(this,positionLayoutSize.width,positionLayoutSize.height)
        binding.rankingUp.setImageDrawable(ArrowUp(this,arrowSize.width,arrowSize.height))
        binding.rankingDown.setImageDrawable(ArrowDown(this,arrowSize.width,arrowSize.height))
        binding.backToGameRankingImageView.setImageDrawable(StartButton(this,backToGameButtonSize.width,backToGameButtonSize.height))
        binding.backToGameLinearLayoutRanking.background = RoundedFrameDrawable(this,5.5*backToGameButtonSize.width,backToGameButtonSize.height,
            backToGameButtonSize.height/20,
            backToGameButtonSize.height/2
        )
    }

    private fun getScreenHeightAndWidth() {
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

    private fun createUserListFromFirebase() {
        val dbRef = Firebase.database.getReference("user")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                binding.progressBar1.visibility = View.GONE
                binding.progressBar2.visibility = View.GONE
                binding.progressBar3.visibility = View.GONE
                binding.progressBar4.visibility = View.GONE
                binding.progressBar5.visibility = View.GONE
                binding.rankingUserName1.text = getString(R.string.database_error)
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(user in p0.children){
                        val tUser = user.getValue(User::class.java)
                        userList.add(tUser!!)
                    }
                    sortAndDisplay()
                }
                else{
                    binding.progressBar1.visibility = View.GONE
                    binding.progressBar2.visibility = View.GONE
                    binding.progressBar3.visibility = View.GONE
                    binding.progressBar4.visibility = View.GONE
                    binding.progressBar5.visibility = View.GONE
                    binding.rankingUserName1.text = getString(R.string.database_empty)

                }

            }

        })

    }

    private fun sortAndDisplay() {

        if(userList.size>1){
            sort()
        }


        var userPosition:Int =-1
        if(userid != ""){
            for(i in 0 until userList.size-1){
                if(userList[i].id.equals(userid)){
                    userPosition=i
                }
            }
        }

        binding.progressBar1.visibility = View.GONE
        binding.progressBar2.visibility = View.GONE
        binding.progressBar3.visibility = View.GONE
        binding.progressBar4.visibility = View.GONE
        binding.progressBar5.visibility = View.GONE

        displayFiveUsers(userid,userPosition)

    }

    private fun displayFiveUsers( userId:String, userPosition: Int) {

        // list shorter than 6
        if(userList.size<=5){
            displayFiveUsersWithIndex(userId)
        }

        // list longer than 5

        else{
            index = if(userPosition-3>0) userPosition-3 else 0
            displayFiveUsersWithIndex(userId)

        }

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
                rankingUserName1.setTextColor(getColor(R.color.red))
                rankingHighScoreA1.setTextColor(getColor(R.color.red))
                rankingHighScoreB1.setTextColor(getColor(R.color.red))
                rankingTotalScore1.setTextColor(getColor(R.color.red))
            } else {
                rankingUserName1.setTextColor(getColor(R.color.black))
                rankingHighScoreA1.setTextColor(getColor(R.color.black))
                rankingHighScoreB1.setTextColor(getColor(R.color.black))
                rankingTotalScore1.setTextColor(getColor(R.color.black))
            }
        }
        else{
                rankingUserName1!!.text = ""
                rankingHighScoreA1!!.text = ""
                rankingHighScoreB1!!.text = ""
                rankingTotalScore1!!.text = ""
        }

    }

    private fun sort(){
        var boolean=false
        for(i in userList.size-1 downTo 1){

            // score is different
            if(userList[i].score()>userList[i-1].score()){
                val tUser = userList[i]
                userList[i]=userList[i-1]
                userList[i-1]=tUser
                boolean=true
            }

            // score is the same
            else if(userList[i].score()==userList[i-1].score()){

                // high score B
                if(userList[i].gameB.highScoreB>userList[i-1].gameB.highScoreB){
                    val tUser = userList[i]
                    userList[i]=userList[i-1]
                    userList[i-1]=tUser
                    boolean=true
                }

                // high scoreB is the same
                else if (userList[i].gameB.highScoreB==userList[i-1].gameB.highScoreB){

                    // counterB different
                    if(userList[i].gameB.counterB>userList[i-1].gameB.counterB){
                        val tUser = userList[i]
                        userList[i]=userList[i-1]
                        userList[i-1]=tUser
                        boolean=true
                    }

                    else if(userList[i].gameB.counterB==userList[i-1].gameB.counterB){

                        // check high scoreA
                        if(userList[i].gameA.highScoreA>userList[i-1].gameA.highScoreA){
                            val tUser = userList[i]
                            userList[i]=userList[i-1]
                            userList[i-1]=tUser
                            boolean=true
                        }

                        // the same high scoreA
                        else if(userList[i].gameA.highScoreA==userList[i-1].gameA.highScoreA){


                            if(userList[i].gameA.counterA>userList[i-1].gameA.counterA){
                                val tUser = userList[i]
                                userList[i]=userList[i-1]
                                userList[i-1]=tUser
                                boolean=true
                            }
                        }
                    }
                }
            }
        }
        if(boolean){
            sort()
        }
    }

    private fun fullScreen(mainActivityLayout:View) {
        WindowCompat.setDecorFitsSystemWindows(window,false)
        WindowInsetsControllerCompat(window, mainActivityLayout).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }



}

