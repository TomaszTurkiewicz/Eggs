package com.tt.eggs

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.ktx.Firebase
import com.tt.eggs.classes.*
import com.tt.eggs.drawable.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var loggedInStatus = LoggedInStatus()
    private var screenHeight = 0
    private var screenWidth = 0
    private var screenUnit = 0
    private val userNameSize = Dimension()
    private val changeNameButtonSize = Dimension()
    private val scoreSize = Dimension()
    private val scoreUserSize = Dimension()
    private val backToGameButtonSize = Dimension()
    private val rankingButtonSize = Dimension()
    private val loginButtonSize = Dimension()
    private val otherGamesButtonSize = Dimension()
    private val updateButtonSize = Dimension()

    // for update loop
    private var updateState = Static.ON
    private val mHandlerUpdate = Handler()

    private var updateObject=Update()

    /**------------------ activity life cycle -------------------------------------**/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // display full screen
        fullScreen()
        setContentView(R.layout.activity_login)

        makeUI()

        // check if user is logged in and make UI

        checkUser()


        // buttons on click listeners
        setButtonsActions()

        // for signing with google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        updateObject=Functions.readUpdateFromSharedPreferences(this)

        if(updateObject.isUpdate){
            update.setOnClickListener {
                redirectToStore(updateObject.url)
            }

            update().run()
        }

    }

    private fun redirectToStore(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    private fun makeUI() {
        getScreenHeightAndWidth()
        setViewSizes()
        setDrawable()
        connectViews()

    }

    private fun connectViews() {
        val set = ConstraintSet()
        set.clone(login_activity)

        set.connect(user_name_tv.id,ConstraintSet.TOP,login_activity.id,ConstraintSet.TOP,
            (screenUnit*0.5).toInt()
        )
        set.connect(user_name_tv.id,ConstraintSet.LEFT,login_activity.id,ConstraintSet.LEFT,screenUnit)

        set.connect(change_name_linearLayout.id,ConstraintSet.TOP,user_name_tv.id,ConstraintSet.TOP,0)
        set.connect(change_name_linearLayout.id,ConstraintSet.LEFT,user_name_tv.id,ConstraintSet.RIGHT,screenUnit)

        set.connect(user_name_et.id,ConstraintSet.TOP,login_activity.id,ConstraintSet.TOP,
            (screenUnit*0.5).toInt()
        )
        set.connect(user_name_et.id,ConstraintSet.LEFT,login_activity.id,ConstraintSet.LEFT,screenUnit)

        set.connect(change_name_linearLayout_et.id,ConstraintSet.TOP,user_name_et.id,ConstraintSet.TOP,0)
        set.connect(change_name_linearLayout_et.id,ConstraintSet.LEFT,user_name_et.id,ConstraintSet.RIGHT,screenUnit)

        set.connect(highScoreA.id,ConstraintSet.TOP,login_activity.id,ConstraintSet.TOP,
            (screenUnit*2.5).toInt()
        )
        set.connect(highScoreA.id,ConstraintSet.LEFT,login_activity.id,ConstraintSet.LEFT,screenUnit)

        set.connect(highScoreB.id,ConstraintSet.TOP,login_activity.id,ConstraintSet.TOP,
            (screenUnit*4.5).toInt()
        )
        set.connect(highScoreB.id,ConstraintSet.LEFT,login_activity.id,ConstraintSet.LEFT,screenUnit)

        set.connect(totalScore.id,ConstraintSet.TOP,login_activity.id,ConstraintSet.TOP,
            (screenUnit*6.5).toInt()
        )
        set.connect(totalScore.id,ConstraintSet.LEFT,login_activity.id,ConstraintSet.LEFT,screenUnit)

        set.connect(highScoreAUser.id,ConstraintSet.TOP,highScoreA.id,ConstraintSet.TOP, 0)
        set.connect(highScoreAUser.id,ConstraintSet.LEFT,highScoreA.id,ConstraintSet.RIGHT,0)

        set.connect(highScoreBUser.id,ConstraintSet.TOP,highScoreB.id,ConstraintSet.TOP, 0)
        set.connect(highScoreBUser.id,ConstraintSet.LEFT,highScoreB.id,ConstraintSet.RIGHT,0)

        set.connect(totalScoreUser.id,ConstraintSet.TOP,totalScore.id,ConstraintSet.TOP, 0)
        set.connect(totalScoreUser.id,ConstraintSet.LEFT,totalScore.id,ConstraintSet.RIGHT,0)

        set.connect(back_to_game_linearLayout_et.id,ConstraintSet.TOP,login_activity.id,ConstraintSet.TOP, (screenUnit*8.5).toInt())
        set.connect(back_to_game_linearLayout_et.id,ConstraintSet.LEFT,login_activity.id,ConstraintSet.LEFT,screenUnit)

        set.connect(ranking_linearLayout.id,ConstraintSet.TOP,login_activity.id,ConstraintSet.TOP, (screenUnit*2.5).toInt())
        set.connect(ranking_linearLayout.id,ConstraintSet.RIGHT,login_activity.id,ConstraintSet.RIGHT, screenUnit)

        set.connect(other_games_linearLayout.id,ConstraintSet.TOP,login_activity.id,ConstraintSet.TOP, (screenUnit*4.5).toInt())
        set.connect(other_games_linearLayout.id,ConstraintSet.RIGHT,login_activity.id,ConstraintSet.RIGHT, screenUnit)

        set.connect(update_linearLayout.id,ConstraintSet.TOP,login_activity.id,ConstraintSet.TOP, (screenUnit*6.5).toInt())
        set.connect(update_linearLayout.id,ConstraintSet.RIGHT,login_activity.id,ConstraintSet.RIGHT, screenUnit)

        set.connect(login_linearLayout.id,ConstraintSet.TOP,login_activity.id,ConstraintSet.TOP, (screenUnit*8.5).toInt())
        set.connect(login_linearLayout.id,ConstraintSet.RIGHT,login_activity.id,ConstraintSet.RIGHT, screenUnit)



        set.applyTo(login_activity)

    }

    private fun setDrawable() {
            user_name_tv.background = TextViewDrawable(this,userNameSize.width,userNameSize.height)
            user_name_et.background = TextViewDrawable(this,userNameSize.width,userNameSize.height)
        change_name_button.setImageDrawable(StartButton(this,changeNameButtonSize.width,changeNameButtonSize.height))
        change_name_ok_button.setImageDrawable(StartButton(this,changeNameButtonSize.width,changeNameButtonSize.height))
        highScoreAUser.background = TextViewDrawable(this,scoreUserSize.width,scoreUserSize.height)
        highScoreBUser.background = TextViewDrawable(this,scoreUserSize.width,scoreUserSize.height)
        totalScoreUser.background = TextViewDrawable(this,scoreUserSize.width,scoreUserSize.height)
        backToGame.setImageDrawable(StartButton(this,backToGameButtonSize.width,backToGameButtonSize.height))
        ranking.setImageDrawable(StartButton(this,rankingButtonSize.width,rankingButtonSize.height))
        googleSignIn.setImageDrawable(StartButton(this,loginButtonSize.width,loginButtonSize.height))
        other_games_button.setImageDrawable(StartButton(this,otherGamesButtonSize.width,otherGamesButtonSize.height))
        back_to_game_linearLayout_et.background = RoundedFrameDrawable(this,5.5*backToGameButtonSize.width,backToGameButtonSize.height,backToGameButtonSize.height/20,backToGameButtonSize.height/2)
        login_linearLayout.background = RoundedFrameDrawable(this,5.5*loginButtonSize.width,loginButtonSize.height,loginButtonSize.height/20,loginButtonSize.height/2)
        other_games_linearLayout.background = RoundedFrameDrawable(this,5.5*otherGamesButtonSize.width,otherGamesButtonSize.height,otherGamesButtonSize.height/20,otherGamesButtonSize.height/2)
        ranking_linearLayout.background = RoundedFrameDrawable(this,5.5*rankingButtonSize.width,rankingButtonSize.height,rankingButtonSize.height/20,rankingButtonSize.height/2)
        update_linearLayout.background = RoundedFrameDrawable(this,5.5*updateButtonSize.width,updateButtonSize.height,updateButtonSize.height/20,updateButtonSize.height/2)
        update.setImageDrawable(StartButtonGray(this,updateButtonSize.width,updateButtonSize.height))
    }

    private fun setViewSizes() {

        userNameSize.height= (screenUnit*4/3).toDouble()
        userNameSize.width= (screenUnit*10).toDouble()

        user_name_tv.layoutParams = ConstraintLayout.LayoutParams((userNameSize.width).toInt(), (userNameSize.height).toInt())
        user_name_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        user_name_et.layoutParams = ConstraintLayout.LayoutParams((userNameSize.width).toInt(), (userNameSize.height).toInt())
        user_name_et.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        changeNameButtonSize.width= (screenUnit*4/3).toDouble()
        changeNameButtonSize.height = changeNameButtonSize.width

        change_name_button.layoutParams = LinearLayout.LayoutParams((changeNameButtonSize.width).toInt(),(changeNameButtonSize.height).toInt())
        change_name_textView.layoutParams = LinearLayout.LayoutParams((2*changeNameButtonSize.width).toInt(),(changeNameButtonSize.height).toInt())
        change_name_textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        change_name_ok_button.layoutParams = LinearLayout.LayoutParams((changeNameButtonSize.width).toInt(),(changeNameButtonSize.height).toInt())
        change_name_ok_.layoutParams = LinearLayout.LayoutParams((2*changeNameButtonSize.width).toInt(),(changeNameButtonSize.height).toInt())
        change_name_ok_.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        scoreSize.width = (screenUnit*5).toDouble()
        scoreSize.height = (screenUnit*4/3).toDouble()

        scoreUserSize.width = (screenUnit*5).toDouble()
        scoreUserSize.height = (screenUnit*4/3).toDouble()

        highScoreA.layoutParams = ConstraintLayout.LayoutParams((scoreSize.width).toInt(),(scoreSize.height).toInt())
        highScoreB.layoutParams = ConstraintLayout.LayoutParams((scoreSize.width).toInt(),(scoreSize.height).toInt())
        totalScore.layoutParams = ConstraintLayout.LayoutParams((scoreSize.width).toInt(),(scoreSize.height).toInt())

        highScoreA.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        highScoreB.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        totalScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        highScoreAUser.layoutParams = ConstraintLayout.LayoutParams((scoreUserSize.width).toInt(),(scoreUserSize.height).toInt())
        highScoreBUser.layoutParams = ConstraintLayout.LayoutParams((scoreUserSize.width).toInt(),(scoreUserSize.height).toInt())
        totalScoreUser.layoutParams = ConstraintLayout.LayoutParams((scoreUserSize.width).toInt(),(scoreUserSize.height).toInt())

        highScoreAUser.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        highScoreBUser.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        totalScoreUser.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        backToGameButtonSize.width= (screenUnit*4/3).toDouble()
        backToGameButtonSize.height = backToGameButtonSize.width

        backToGame.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        back_to_game_textView.layoutParams = LinearLayout.LayoutParams((4*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        back_to_game_textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        back_to_game_textView_blank.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width/2).toInt(),(backToGameButtonSize.height).toInt())

        rankingButtonSize.width= (screenUnit*4/3).toDouble()
        rankingButtonSize.height = rankingButtonSize.width

        ranking.layoutParams = LinearLayout.LayoutParams((rankingButtonSize.width).toInt(),(rankingButtonSize.height).toInt())
        ranking_tv.layoutParams = LinearLayout.LayoutParams((4*rankingButtonSize.width).toInt(),(rankingButtonSize.height).toInt())
        ranking_tv_blank.layoutParams = LinearLayout.LayoutParams((0.5*rankingButtonSize.width).toInt(),(rankingButtonSize.height).toInt())
        ranking_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        loginButtonSize.width= (screenUnit*4/3).toDouble()
        loginButtonSize.height = loginButtonSize.width

        googleSignIn.layoutParams = LinearLayout.LayoutParams((loginButtonSize.width).toInt(),(loginButtonSize.height).toInt())
        login_tv.layoutParams = LinearLayout.LayoutParams((4*loginButtonSize.width).toInt(),(loginButtonSize.height).toInt())
        login_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        login_tv_blank.layoutParams = LinearLayout.LayoutParams((0.5*loginButtonSize.width).toInt(),(loginButtonSize.height).toInt())

        otherGamesButtonSize.width= (screenUnit*4/3).toDouble()
        otherGamesButtonSize.height = otherGamesButtonSize.width

        other_games_button.layoutParams = LinearLayout.LayoutParams((otherGamesButtonSize.width).toInt(),(otherGamesButtonSize.height).toInt())
        other_games_tv.layoutParams = LinearLayout.LayoutParams((4*otherGamesButtonSize.width).toInt(),(otherGamesButtonSize.height).toInt())
        other_games_tv_blank.layoutParams = LinearLayout.LayoutParams((0.5*otherGamesButtonSize.width).toInt(),(otherGamesButtonSize.height).toInt())
        other_games_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        updateButtonSize.width= (screenUnit*4/3).toDouble()
        updateButtonSize.height = updateButtonSize.width

        update.layoutParams = LinearLayout.LayoutParams((updateButtonSize.width).toInt(),(updateButtonSize.height).toInt())
        update_tv.layoutParams = LinearLayout.LayoutParams((4*updateButtonSize.width).toInt(),(updateButtonSize.height).toInt())
        update_tv_blank.layoutParams = LinearLayout.LayoutParams((0.5*updateButtonSize.width).toInt(),(updateButtonSize.height).toInt())
        update_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())


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

    private fun checkUser() {

        updateUI()

    }


    /**-------------------------- displaying functions -------------------------------**/

    // full screen
    private fun fullScreen(){
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

    // update ui and create/save user to database
    private fun updateUI(){
        loggedInStatus = Functions.readLoggedInStatusFromSharedPreferences(this)


        displayUI(loggedInStatus)

    }

    private fun displayUI(loggedInStatus: LoggedInStatus) {

        auth=Firebase.auth
        login_tv.text=if(auth.currentUser!=null) "LOG OUT" else "LOG IN"

        if(loggedInStatus.loggedIn){
            display(loggedInStatus.userid)
        }
        else{
            displayNotLoggedIn()
        }
    }

    // display nothing
    private fun displayNotLoggedIn() {
        user_name_tv.text="-"
        highScoreAUser.text="-"
        highScoreBUser.text="-"
        totalScoreUser.text="-"
        change_name_linearLayout.visibility=View.GONE
        user_name_et.visibility=View.GONE
        change_name_linearLayout_et.visibility=View.GONE
    }

    //display user statistics
    private fun display(userID:String) {
        val tUser = User(userID,
        Functions.checkUserNameFromSharedPreferences(this,userID),
        Functions.readGameAFromSharedPreferences(this,userID),
        Functions.readGameBFromSharedPreferences(this,userID))

        user_name_tv.text=tUser.userName

        if(tUser.gameA.counterA>0){
            highScoreAUser.text=""+tUser.gameA.highScoreA + "("+tUser.gameA.counterA+")"
        }
        else{
            highScoreAUser.text=tUser.gameA.highScoreA.toString()
        }

        if(tUser.gameB.counterB>0){
            highScoreBUser.text=""+tUser.gameB.highScoreB + "("+tUser.gameB.counterB+")"
        }
        else{
            highScoreBUser.text=tUser.gameB.highScoreB.toString()
        }



        totalScoreUser.text=(tUser.gameA.totalScoreA+tUser.gameB.totalScoreB).toString()

        change_name_linearLayout.visibility=View.VISIBLE
        user_name_et.visibility = View.GONE
        change_name_linearLayout_et.visibility=View.GONE
    }


    /** --------------------- buttons listeners -------------------------------**/

    private fun setButtonsActions() {
        // back to main screen
        backToGame.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        other_games_button.setOnClickListener {
            val intent = Intent(this,OtherGamesActivity::class.java)
            startActivity(intent)
            finish()
        }




        googleSignIn.setOnClickListener {
            if (auth.currentUser!=null){
                signOut()
            }else{
                signIn()
            }
        }

        change_name_button.setOnClickListener {
            user_name_et.visibility=View.VISIBLE
            user_name_et.requestFocus()
            change_name_linearLayout_et.visibility=View.VISIBLE
            change_name_linearLayout.visibility=View.GONE
            updateUserName(loggedInStatus.userid)



        }

        ranking.setOnClickListener {
            val intent = Intent(this,Ranking::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateUserName(userID: String) {
        val tUser = User(userID,
            Functions.checkUserNameFromSharedPreferences(this,userID),
            Functions.readGameAFromSharedPreferences(this,userID),
            Functions.readGameBFromSharedPreferences(this,userID))

        user_name_et.setText(tUser.userName)
        change_name_ok_button.setOnClickListener {
            tUser.userName=user_name_et.text.toString()
            Functions.saveUserNameToSharedPreferences(this,userID,tUser.userName)
            val currentUser = auth.currentUser
            if(currentUser!=null) {
                if (currentUser.uid.equals(userID)) {
                    val dbReference = Firebase.database.getReference("user").child(userID)
                    dbReference.setValue(tUser)

                }
            }
            change_name_linearLayout.visibility = View.VISIBLE
            updateUI()
        }

    }

    /** ----------------------- sign in methods ---------------------------------**/

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            }
            catch (e:ApiException){
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken:String){
        val credentials = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credentials)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    val user = Firebase.auth.currentUser
                        if(user!=null){
                            // create user if not exists or compare if exists
                            checkUserInDatabase(user)

                        }
                }else{
                    Functions.saveLoggedStateToSharedPreferences(this,false,"")
                    updateUI()
                }
            }
    }

    // create user in database if not exists
    private fun checkUserInDatabase(user: FirebaseUser?) {
        if(user!=null){

            // reference to user database
            val dbRef = Firebase.database.getReference("user").child(user.uid)

            // check if database exists (if not create)
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    // do nothing
                }

                override fun onDataChange(p0: DataSnapshot) {
                    // user doesn't exists
                    if(!p0.exists()){

                        // create user
                        val userDB = User(id = user.uid)
                        dbRef.setValue(userDB)
                        checkUserInDatabase(user)

                    }

                    // user exists
                    else{
                        // check if sharedpreferences and firebase database are the same if not make them the same
                        val tUser = p0.getValue(User::class.java)
                        val userName = Functions.checkUserNameFromSharedPreferences(this@LoginActivity,user.uid)
                        val gameA = Functions.readGameAFromSharedPreferences(this@LoginActivity,user.uid)
                        val gameB = Functions.readGameBFromSharedPreferences(this@LoginActivity,user.uid)
                        if(tUser!=null) {
                            if(!userName.equals(tUser.userName)){
                                Functions.saveUserNameToSharedPreferences(this@LoginActivity,user.uid,tUser.userName)
                            }

                            if (tUser.gameA.totalScoreA < gameA.totalScoreA) {
                                tUser.gameA = gameA
                            }
                            else{
                                Functions.saveStatisticAToSharedPreferences(this@LoginActivity,user.uid,tUser.gameA)
                            }
                            if (tUser.gameB.totalScoreB < gameB.totalScoreB) {
                                tUser.gameB = gameB
                            }
                            else{
                                Functions.saveStatisticBToSharedPreferences(this@LoginActivity,user.uid,tUser.gameB)
                            }
                            dbRef.setValue(tUser)

                            Functions.saveLoggedStateToSharedPreferences(this@LoginActivity,true, user.uid)

                            updateUI()

                        }
                    }
                }
            })
        }

    }

    private fun signOut(){
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this){
            Functions.saveLoggedStateToSharedPreferences(this,false,"")
            updateUI()
        }
    }
    /** ------------------------ companion objects ----------------------------------**/


    companion object{
        private const val RC_SIGN_IN = 9001
    }


    private fun update():Runnable = Runnable {
        if(updateState==Static.ON){
            update.setImageDrawable(StartButtonGreen(this,updateButtonSize.width,updateButtonSize.height))
        }else{
            update.setImageDrawable(StartButton(this,updateButtonSize.width,updateButtonSize.height))
        }
        updateState=!updateState
        mHandlerUpdate.postDelayed(update(),500)
    }

}



