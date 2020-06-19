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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.eggs.classes.Dimension
import com.tt.eggs.classes.User
import com.tt.eggs.drawable.ArrowDown
import com.tt.eggs.drawable.ArrowUp
import com.tt.eggs.drawable.StartButton
import com.tt.eggs.drawable.TextViewDrawableWithBorder
import kotlinx.android.synthetic.main.activity_ranking.*

class Ranking : AppCompatActivity() {

    private lateinit var userList: MutableList<User>
    private lateinit var userid: String
    private var index: Int = 0

    private var screenHeight = 0
    private var screenWidth = 0
    private var screenUnit = 0
    private val headerSize = Dimension()
    private val positionSize = Dimension()
    private val userNameSize = Dimension()
    private val highScoreASize = Dimension()
    private val highScoreBSize = Dimension()
    private val totalPointsSize = Dimension()

    private val positionLayoutSize = Dimension()
    private val arrowSize = Dimension()
    private val backToGameButtonSize = Dimension()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_ranking)

        makeUI()

        backToGameRanking.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



//        recyclerView.visibility = View.GONE
//        ranking_error.visibility = View.GONE

        val currentUser = Firebase.auth.currentUser
        userid = currentUser?.uid ?: ""
        userList = mutableListOf()
        createUserListFromFirebase()




        ranking_up.setOnClickListener {
            if(index>0){
                index -= 1
                displayFiveUsersWithIndex(userid)
            }
        }

        ranking_down.setOnClickListener {
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
        set.clone(ranking_layout)

        set.connect(header.id,ConstraintSet.LEFT,ranking_layout.id,ConstraintSet.LEFT,0)
        set.connect(header.id,ConstraintSet.TOP,ranking_layout.id,ConstraintSet.TOP,0)

        set.connect(position1.id,ConstraintSet.TOP,header.id,ConstraintSet.BOTTOM,0)
        set.connect(position1.id,ConstraintSet.LEFT,ranking_layout.id,ConstraintSet.LEFT,0)

        set.connect(position2.id,ConstraintSet.TOP,position1.id,ConstraintSet.BOTTOM,0)
        set.connect(position2.id,ConstraintSet.LEFT,ranking_layout.id,ConstraintSet.LEFT,0)

        set.connect(position3.id,ConstraintSet.TOP,position2.id,ConstraintSet.BOTTOM,0)
        set.connect(position3.id,ConstraintSet.LEFT,ranking_layout.id,ConstraintSet.LEFT,0)

        set.connect(position4.id,ConstraintSet.TOP,position3.id,ConstraintSet.BOTTOM,0)
        set.connect(position4.id,ConstraintSet.LEFT,ranking_layout.id,ConstraintSet.LEFT,0)

        set.connect(position5.id,ConstraintSet.TOP,position4.id,ConstraintSet.BOTTOM,0)
        set.connect(position5.id,ConstraintSet.LEFT,ranking_layout.id,ConstraintSet.LEFT,0)

        set.connect(ranking_up.id,ConstraintSet.TOP,position1.id,ConstraintSet.TOP,0)
        set.connect(ranking_up.id,ConstraintSet.LEFT,position1.id,ConstraintSet.RIGHT,0)
        set.connect(ranking_up.id,ConstraintSet.RIGHT,ranking_layout.id,ConstraintSet.RIGHT,0)

        set.connect(ranking_down.id,ConstraintSet.BOTTOM,position5.id,ConstraintSet.BOTTOM,0)
        set.connect(ranking_down.id,ConstraintSet.LEFT,position5.id,ConstraintSet.RIGHT,0)
        set.connect(ranking_down.id,ConstraintSet.RIGHT,ranking_layout.id,ConstraintSet.RIGHT,0)

        set.connect(back_to_game_linearLayout_ranking.id,ConstraintSet.LEFT,position5.id,ConstraintSet.LEFT,0)
        set.connect(back_to_game_linearLayout_ranking.id,ConstraintSet.RIGHT,position5.id,ConstraintSet.RIGHT,0)
        set.connect(back_to_game_linearLayout_ranking.id,ConstraintSet.BOTTOM,ranking_layout.id,ConstraintSet.BOTTOM,0)
        set.connect(back_to_game_linearLayout_ranking.id,ConstraintSet.TOP,position5.id,ConstraintSet.BOTTOM,0)

        set.applyTo(ranking_layout)

    }

    private fun setViewSizes() {

        val username =3
        val highscore = 2
        val total = 2

        headerSize.height = (screenUnit).toDouble()


        val unit = screenWidth/10

//        positionSize.width = (unit*2).toDouble()
//        positionSize.height = headerSize.height
        userNameSize.width = (unit*username).toDouble()
        userNameSize.height = headerSize.height
        highScoreASize.width = (unit*highscore).toDouble()
        highScoreASize.height = headerSize.height
        highScoreBSize.width = (unit*highscore).toDouble()
        highScoreBSize.height = headerSize.height
        totalPointsSize.width = (unit*total).toDouble()
        totalPointsSize.height = headerSize.height
        headerSize.width=userNameSize.width+
                highScoreASize.width+
                highScoreBSize.width+
                totalPointsSize.width

        header.layoutParams=ConstraintLayout.LayoutParams((headerSize.width).toInt(),(headerSize.height).toInt())
//        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        ranking_user_name.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        ranking_high_score_A.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        ranking_high_score_B.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        ranking_total_score.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_user_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_A.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_B.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_total_score.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())

        userNameSize.width = (unit*username).toDouble()
        userNameSize.height = (screenUnit*1.5).toDouble()
        highScoreASize.width = (unit*highscore).toDouble()
        highScoreASize.height = (screenUnit*1.5).toDouble()
        highScoreBSize.width = (unit*highscore).toDouble()
        highScoreBSize.height = (screenUnit*1.5).toDouble()
        totalPointsSize.width = (unit*total).toDouble()
        totalPointsSize.height = (screenUnit*1.5).toDouble()
        positionLayoutSize.width=userNameSize.width+
                highScoreASize.width+
                highScoreBSize.width+
                totalPointsSize.width

        positionLayoutSize.width=positionLayoutSize.width
        positionLayoutSize.height= (screenUnit*1.5).toDouble()
        position1.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        position2.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        position3.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        position4.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        position5.layoutParams=ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())


        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        ranking_user_name1.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        ranking_high_score_A1.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        ranking_high_score_B1.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        ranking_total_score1.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_user_name1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_A1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_B1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_total_score1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())

        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        ranking_user_name2.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        ranking_high_score_A2.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        ranking_high_score_B2.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        ranking_total_score2.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_user_name2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_A2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_B2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_total_score2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())

        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        ranking_user_name3.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        ranking_high_score_A3.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        ranking_high_score_B3.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        ranking_total_score3.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_user_name3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_A3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_B3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_total_score3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())


        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        ranking_user_name4.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        ranking_high_score_A4.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        ranking_high_score_B4.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        ranking_total_score4.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_user_name4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_A4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_B4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_total_score4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())

        //        ranking_position.layoutParams = LinearLayout.LayoutParams((positionSize.width).toInt(),(positionSize.height).toInt())
        ranking_user_name5.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        ranking_high_score_A5.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        ranking_high_score_B5.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        ranking_total_score5.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())

//        ranking_position.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_user_name5.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_A5.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_high_score_B5.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        ranking_total_score5.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())

        arrowSize.width= (screenUnit*4/3).toDouble()
        arrowSize.height = arrowSize.width*2

        ranking_up.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width).toInt(),(arrowSize.height).toInt())
        ranking_down.layoutParams = ConstraintLayout.LayoutParams((arrowSize.width).toInt(),(arrowSize.height).toInt())

        backToGameButtonSize.width= (screenUnit*4/3).toDouble()
        backToGameButtonSize.height = backToGameButtonSize.width

        backToGameRanking.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        back_to_game_ranking.layoutParams = LinearLayout.LayoutParams((4*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        back_to_game_ranking.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

    }

    private fun setDrawable() {
        position1.background = TextViewDrawableWithBorder(this,positionLayoutSize.width,positionLayoutSize.height)
        position2.background = TextViewDrawableWithBorder(this,positionLayoutSize.width,positionLayoutSize.height)
        position3.background = TextViewDrawableWithBorder(this,positionLayoutSize.width,positionLayoutSize.height)
        position4.background = TextViewDrawableWithBorder(this,positionLayoutSize.width,positionLayoutSize.height)
        position5.background = TextViewDrawableWithBorder(this,positionLayoutSize.width,positionLayoutSize.height)
        ranking_up.setImageDrawable(ArrowUp(this,arrowSize.width,arrowSize.height))
        ranking_down.setImageDrawable(ArrowDown(this,arrowSize.width,arrowSize.height))
        backToGameRanking.setImageDrawable(StartButton(this,backToGameButtonSize.width,backToGameButtonSize.height))

    }

    private fun getScreenHeightAndWidth() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
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
//                progress_bar.visibility = View.GONE
//                ranking_error.text = "DATABASE ERROR"
//                ranking_error.visibility = View.VISIBLE
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
//                    progress_bar.visibility = View.GONE
//                    ranking_error.text = "DATABASE EMPTY"
//                    ranking_error.visibility = View.VISIBLE
                }

            }

        })

    }

    private fun sortAndDisplay() {

        if(userList.size>1){
            sort()
        }


        var userPosition:Int =-1
        if(!userid.equals("")){
            for(i in 0 until userList.size-1){
                if(userList[i].id.equals(userid)){
                    userPosition=i
                }
            }
        }


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
            displaySingleUser(index,ranking_user_name1,ranking_high_score_A1, ranking_high_score_B1, ranking_total_score1, userId)
        }
        else{
            displaySingleUser(-1,ranking_user_name1,ranking_high_score_A1, ranking_high_score_B1, ranking_total_score1, userId)
        }


        //second position
        if(index+1<userList.size){
            displaySingleUser(index+1,ranking_user_name2,ranking_high_score_A2, ranking_high_score_B2, ranking_total_score2, userId)
        }
        else
        {
            displaySingleUser(-1,ranking_user_name2,ranking_high_score_A2, ranking_high_score_B2, ranking_total_score2, userId)
        }


        // third position
        if(index+2<userList.size){
            displaySingleUser(index+2,ranking_user_name3,ranking_high_score_A3, ranking_high_score_B3, ranking_total_score3, userId)
        }
        else
        {
            displaySingleUser(-1,ranking_user_name3,ranking_high_score_A3, ranking_high_score_B3, ranking_total_score3, userId)
        }

        // fourth position
        if(index+3<userList.size){
            displaySingleUser(index+3,ranking_user_name4,ranking_high_score_A4, ranking_high_score_B4, ranking_total_score4, userId)
        }
        else
        {
            displaySingleUser(-1,ranking_user_name4,ranking_high_score_A4, ranking_high_score_B4, ranking_total_score4, userId)
        }

        // fifth position
        if(index+4<userList.size){
            displaySingleUser(index+4,ranking_user_name5,ranking_high_score_A5, ranking_high_score_B5, ranking_total_score5, userId)
        }
        else
        {
            displaySingleUser(-1,ranking_user_name5,ranking_high_score_A5, ranking_high_score_B5, ranking_total_score5, userId)
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
            rankingUserName1!!.text = "" + position + " " + userList[index].userName
            if (userList[index].gameA.counterA == 0) {
                rankingHighScoreA1!!.text = userList[index].gameA.highScoreA.toString()
            } else {
                rankingHighScoreA1!!.text =
                    "" + userList[index].gameA.highScoreA + "(" + userList[index].gameA.counterA + ")"
            }
            if (userList[index].gameB.counterB == 0) {
                rankingHighScoreB1!!.text = userList[index].gameB.highScoreB.toString()
            } else {
                rankingHighScoreB1!!.text =
                    "" + userList[index].gameB.highScoreB + "(" + userList[index].gameB.counterB + ")"
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

// TODO progress bar
// TODO show database error
