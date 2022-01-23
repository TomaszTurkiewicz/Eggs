package com.tt.eggs


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.eggs.classes.*
import com.tt.eggs.databinding.ActivityLoginBinding
import com.tt.eggs.drawable.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var loggedInStatus = LoggedInStatus()
    private var screenUnit = 0
    private val userNameSize = Dimension()
    private val changeNameButtonSize = Dimension()
    private val scoreSize = Dimension()
    private val scoreUserSize = Dimension()
    private val backToGameButtonSize = Dimension()
    private val rankingButtonSize = Dimension()
    private val loginButtonSize = Dimension()
    private val otherGamesButtonSize = Dimension()
    private val wholeScreenSize = Dimension()


    private lateinit var binding: ActivityLoginBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    /**------------------ activity life cycle -------------------------------------**/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // display full screen

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        fullScreen(view)

        makeUI()

        // check if user is logged in and make UI

        checkUser()


        // buttons on click listeners
        setButtonsActions()

        // for signing with google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("348971081913-hi14av9f0sq2iier39kthd36k7pe6ao5.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data: Intent? = result.data
                doSomething(data)
            }
        }


    }

    private fun makeUI() {
        getScreenHeightAndWidth()
        setViewSizes()
        setDrawable()
        connectViews()

    }

    private fun connectViews() {
        val set = ConstraintSet()
        set.clone(binding.loginActivity)

        set.connect(binding.loginActivityContainer.id,ConstraintSet.TOP,binding.loginActivity.id,ConstraintSet.TOP,0)
        set.connect(binding.loginActivityContainer.id,ConstraintSet.BOTTOM,binding.loginActivity.id,ConstraintSet.BOTTOM,0)
        set.connect(binding.loginActivityContainer.id,ConstraintSet.RIGHT,binding.loginActivity.id,ConstraintSet.RIGHT,0)
        set.connect(binding.loginActivityContainer.id,ConstraintSet.LEFT,binding.loginActivity.id,ConstraintSet.LEFT,0)

        set.connect(binding.userNameTv.id,ConstraintSet.TOP,binding.loginActivityContainer.id,ConstraintSet.TOP,
            (screenUnit*0.5).toInt()
        )
        set.connect(binding.userNameTv.id,ConstraintSet.LEFT,binding.loginActivityContainer.id,ConstraintSet.LEFT,screenUnit)

        set.connect(binding.changeNameLinearLayout.id,ConstraintSet.TOP,binding.userNameTv.id,ConstraintSet.TOP,0)
        set.connect(binding.changeNameLinearLayout.id,ConstraintSet.LEFT,binding.userNameTv.id,ConstraintSet.RIGHT,screenUnit)

        set.connect(binding.userNameEt.id,ConstraintSet.TOP,binding.loginActivityContainer.id,ConstraintSet.TOP,
            (screenUnit*0.5).toInt()
        )
        set.connect(binding.userNameEt.id,ConstraintSet.LEFT,binding.loginActivityContainer.id,ConstraintSet.LEFT,screenUnit)

        set.connect(binding.changeNameLinearLayoutEt.id,ConstraintSet.TOP,binding.userNameEt.id,ConstraintSet.TOP,0)
        set.connect(binding.changeNameLinearLayoutEt.id,ConstraintSet.LEFT,binding.userNameEt.id,ConstraintSet.RIGHT,screenUnit)

        set.connect(binding.highScoreA.id,ConstraintSet.TOP,binding.loginActivityContainer.id,ConstraintSet.TOP,
            (screenUnit*2.5).toInt()
        )
        set.connect(binding.highScoreA.id,ConstraintSet.LEFT,binding.loginActivityContainer.id,ConstraintSet.LEFT,screenUnit)

        set.connect(binding.highScoreB.id,ConstraintSet.TOP,binding.loginActivityContainer.id,ConstraintSet.TOP,
            (screenUnit*4.5).toInt()
        )
        set.connect(binding.highScoreB.id,ConstraintSet.LEFT,binding.loginActivityContainer.id,ConstraintSet.LEFT,screenUnit)

        set.connect(binding.totalScore.id,ConstraintSet.TOP,binding.loginActivityContainer.id,ConstraintSet.TOP,
            (screenUnit*6.5).toInt()
        )
        set.connect(binding.totalScore.id,ConstraintSet.LEFT,binding.loginActivityContainer.id,ConstraintSet.LEFT,screenUnit)

        set.connect(binding.highScoreAUser.id,ConstraintSet.TOP,binding.highScoreA.id,ConstraintSet.TOP, 0)
        set.connect(binding.highScoreAUser.id,ConstraintSet.LEFT,binding.highScoreA.id,ConstraintSet.RIGHT,0)

        set.connect(binding.highScoreBUser.id,ConstraintSet.TOP,binding.highScoreB.id,ConstraintSet.TOP, 0)
        set.connect(binding.highScoreBUser.id,ConstraintSet.LEFT,binding.highScoreB.id,ConstraintSet.RIGHT,0)

        set.connect(binding.totalScoreUser.id,ConstraintSet.TOP,binding.totalScore.id,ConstraintSet.TOP, 0)
        set.connect(binding.totalScoreUser.id,ConstraintSet.LEFT,binding.totalScore.id,ConstraintSet.RIGHT,0)

        set.connect(binding.backToGameLinearLayoutEt.id,ConstraintSet.TOP,binding.loginActivityContainer.id,ConstraintSet.TOP, (screenUnit*8.5).toInt())
        set.connect(binding.backToGameLinearLayoutEt.id,ConstraintSet.LEFT,binding.loginActivityContainer.id,ConstraintSet.LEFT,screenUnit)

        set.connect(binding.rankingLinearLayout.id,ConstraintSet.TOP,binding.loginActivityContainer.id,ConstraintSet.TOP, (screenUnit*2.5).toInt())
        set.connect(binding.rankingLinearLayout.id,ConstraintSet.RIGHT,binding.loginActivityContainer.id,ConstraintSet.RIGHT, screenUnit)

        set.connect(binding.otherGamesLinearLayout.id,ConstraintSet.TOP,binding.loginActivityContainer.id,ConstraintSet.TOP, (screenUnit*4.5).toInt())
        set.connect(binding.otherGamesLinearLayout.id,ConstraintSet.RIGHT,binding.loginActivityContainer.id,ConstraintSet.RIGHT, screenUnit)

        set.connect(binding.loginLinearLayout.id,ConstraintSet.TOP,binding.loginActivityContainer.id,ConstraintSet.TOP, (screenUnit*6.5).toInt())
        set.connect(binding.loginLinearLayout.id,ConstraintSet.RIGHT,binding.loginActivityContainer.id,ConstraintSet.RIGHT, screenUnit)



        set.applyTo(binding.loginActivity)

    }

    private fun setDrawable() {
        binding.userNameTv.background = TextViewDrawable(this,userNameSize.width,userNameSize.height)
        binding.userNameEt.background = TextViewDrawable(this,userNameSize.width,userNameSize.height)
        binding.changeNameButton.setImageDrawable(StartButton(this,changeNameButtonSize.width,changeNameButtonSize.height))
        binding.changeNameOkButton.setImageDrawable(StartButton(this,changeNameButtonSize.width,changeNameButtonSize.height))
        binding.highScoreAUser.background = TextViewDrawable(this,scoreUserSize.width,scoreUserSize.height)
        binding.highScoreBUser.background = TextViewDrawable(this,scoreUserSize.width,scoreUserSize.height)
        binding.totalScoreUser.background = TextViewDrawable(this,scoreUserSize.width,scoreUserSize.height)
        binding.backToGame.setImageDrawable(StartButton(this,backToGameButtonSize.width,backToGameButtonSize.height))
        binding.ranking.setImageDrawable(StartButton(this,rankingButtonSize.width,rankingButtonSize.height))
        binding.googleSignIn.setImageDrawable(StartButton(this,loginButtonSize.width,loginButtonSize.height))
        binding.otherGamesButton.setImageDrawable(StartButton(this,otherGamesButtonSize.width,otherGamesButtonSize.height))
        binding.backToGameLinearLayoutEt.background = RoundedFrameDrawable(this,5.5*backToGameButtonSize.width,backToGameButtonSize.height,backToGameButtonSize.height/20,backToGameButtonSize.height/2)
        binding.loginLinearLayout.background = RoundedFrameDrawable(this,5.5*loginButtonSize.width,loginButtonSize.height,loginButtonSize.height/20,loginButtonSize.height/2)
        binding.otherGamesLinearLayout.background = RoundedFrameDrawable(this,5.5*otherGamesButtonSize.width,otherGamesButtonSize.height,otherGamesButtonSize.height/20,otherGamesButtonSize.height/2)
        binding.rankingLinearLayout.background = RoundedFrameDrawable(this,5.5*rankingButtonSize.width,rankingButtonSize.height,rankingButtonSize.height/20,rankingButtonSize.height/2)
    }

    private fun setViewSizes() {

        userNameSize.height= (screenUnit*4/3).toDouble()
        userNameSize.width= (screenUnit*10).toDouble()

        wholeScreenSize.width = 20.0 * screenUnit
        wholeScreenSize.height = 10.0 * screenUnit

        binding.loginActivityContainer.layoutParams = ConstraintLayout.LayoutParams((wholeScreenSize.width).toInt(),(wholeScreenSize.height).toInt())

        binding.userNameTv.layoutParams = ConstraintLayout.LayoutParams((userNameSize.width).toInt(), (userNameSize.height).toInt())
        binding.userNameTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        binding.userNameEt.layoutParams = ConstraintLayout.LayoutParams((userNameSize.width).toInt(), (userNameSize.height).toInt())
        binding.userNameEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        changeNameButtonSize.width= (screenUnit*4/3).toDouble()
        changeNameButtonSize.height = changeNameButtonSize.width

        binding.changeNameButton.layoutParams = LinearLayout.LayoutParams((changeNameButtonSize.width).toInt(),(changeNameButtonSize.height).toInt())
        binding.changeNameTextView.layoutParams = LinearLayout.LayoutParams((2*changeNameButtonSize.width).toInt(),(changeNameButtonSize.height).toInt())
        binding.changeNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        binding.changeNameOkButton.layoutParams = LinearLayout.LayoutParams((changeNameButtonSize.width).toInt(),(changeNameButtonSize.height).toInt())
        binding.changeNameOk.layoutParams = LinearLayout.LayoutParams((2*changeNameButtonSize.width).toInt(),(changeNameButtonSize.height).toInt())
        binding.changeNameOk.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        scoreSize.width = (screenUnit*5).toDouble()
        scoreSize.height = (screenUnit*4/3).toDouble()

        scoreUserSize.width = (screenUnit*5).toDouble()
        scoreUserSize.height = (screenUnit*4/3).toDouble()

        binding.highScoreA.layoutParams = ConstraintLayout.LayoutParams((scoreSize.width).toInt(),(scoreSize.height).toInt())
        binding.highScoreB.layoutParams = ConstraintLayout.LayoutParams((scoreSize.width).toInt(),(scoreSize.height).toInt())
        binding.totalScore.layoutParams = ConstraintLayout.LayoutParams((scoreSize.width).toInt(),(scoreSize.height).toInt())

        binding.highScoreA.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        binding.highScoreB.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        binding.totalScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        binding.highScoreAUser.layoutParams = ConstraintLayout.LayoutParams((scoreUserSize.width).toInt(),(scoreUserSize.height).toInt())
        binding.highScoreBUser.layoutParams = ConstraintLayout.LayoutParams((scoreUserSize.width).toInt(),(scoreUserSize.height).toInt())
        binding.totalScoreUser.layoutParams = ConstraintLayout.LayoutParams((scoreUserSize.width).toInt(),(scoreUserSize.height).toInt())

        binding.highScoreAUser.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        binding.highScoreBUser.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        binding.totalScoreUser.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        backToGameButtonSize.width= (screenUnit*4/3).toDouble()
        backToGameButtonSize.height = backToGameButtonSize.width

        binding.backToGame.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameTextView.layoutParams = LinearLayout.LayoutParams((4*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        binding.backToGameTextViewBlank.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width/2).toInt(),(backToGameButtonSize.height).toInt())

        rankingButtonSize.width= (screenUnit*4/3).toDouble()
        rankingButtonSize.height = rankingButtonSize.width

        binding.ranking.layoutParams = LinearLayout.LayoutParams((rankingButtonSize.width).toInt(),(rankingButtonSize.height).toInt())
        binding.rankingTv.layoutParams = LinearLayout.LayoutParams((4*rankingButtonSize.width).toInt(),(rankingButtonSize.height).toInt())
        binding.rankingTvBlank.layoutParams = LinearLayout.LayoutParams((0.5*rankingButtonSize.width).toInt(),(rankingButtonSize.height).toInt())
        binding.rankingTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        loginButtonSize.width= (screenUnit*4/3).toDouble()
        loginButtonSize.height = loginButtonSize.width

        binding.googleSignIn.layoutParams = LinearLayout.LayoutParams((loginButtonSize.width).toInt(),(loginButtonSize.height).toInt())
        binding.loginTv.layoutParams = LinearLayout.LayoutParams((4*loginButtonSize.width).toInt(),(loginButtonSize.height).toInt())
        binding.loginTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        binding.loginTvBlank.layoutParams = LinearLayout.LayoutParams((0.5*loginButtonSize.width).toInt(),(loginButtonSize.height).toInt())

        otherGamesButtonSize.width= (screenUnit*4/3).toDouble()
        otherGamesButtonSize.height = otherGamesButtonSize.width

        binding.otherGamesButton.layoutParams = LinearLayout.LayoutParams((otherGamesButtonSize.width).toInt(),(otherGamesButtonSize.height).toInt())
        binding.otherGamesTv.layoutParams = LinearLayout.LayoutParams((4*otherGamesButtonSize.width).toInt(),(otherGamesButtonSize.height).toInt())
        binding.otherGamesTvBlank.layoutParams = LinearLayout.LayoutParams((0.5*otherGamesButtonSize.width).toInt(),(otherGamesButtonSize.height).toInt())
        binding.otherGamesTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

    }

    private fun getScreenHeightAndWidth() {

        screenUnit=Functions.readScreenUnitFromSharedPreferences(this)
    }

    private fun checkUser() {

        updateUI()

    }


    /**-------------------------- displaying functions -------------------------------**/

    // full screen
    private fun fullScreen(mainActivityLayout:View){

        WindowCompat.setDecorFitsSystemWindows(window,false)
        WindowInsetsControllerCompat(window, mainActivityLayout).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    // update ui and create/save user to database
    private fun updateUI(){
        loggedInStatus = Functions.readLoggedInStatusFromSharedPreferences(this)


        displayUI(loggedInStatus)

    }

    private fun displayUI(loggedInStatus: LoggedInStatus) {

        auth=Firebase.auth
        binding.loginTv.text=if(auth.currentUser!=null) "LOG OUT" else "LOG IN"

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
        binding.highScoreAUser.text="-"
        binding.highScoreBUser.text="-"
        binding.totalScoreUser.text="-"
        binding.changeNameLinearLayout.visibility=View.GONE
        binding.userNameEt.visibility=View.GONE
        binding.changeNameLinearLayoutEt.visibility=View.GONE
    }

    //display user statistics
    private fun display(userID:String) {
        val tUser = User(userID,
        Functions.checkUserNameFromSharedPreferences(this,userID),
        Functions.readGameAFromSharedPreferences(this,userID),
        Functions.readGameBFromSharedPreferences(this,userID))

        binding.userNameTv.text=tUser.userName

        if(tUser.gameA.counterA>0){
  //          binding.highScoreAUser.text=""+tUser.gameA.highScoreA + "("+tUser.gameA.counterA+")"
            binding.highScoreAUser.text=getString(R.string.high_score,tUser.gameA.highScoreA,tUser.gameA.counterA)
        }
        else{
            binding.highScoreAUser.text=tUser.gameA.highScoreA.toString()
        }

        if(tUser.gameB.counterB>0){
//            binding.highScoreBUser.text=""+tUser.gameB.highScoreB + "("+tUser.gameB.counterB+")"
            binding.highScoreAUser.text=getString(R.string.high_score,tUser.gameB.highScoreB,tUser.gameB.counterB)
        }
        else{
            binding.highScoreBUser.text=tUser.gameB.highScoreB.toString()
        }



        binding.totalScoreUser.text=(tUser.gameA.totalScoreA+tUser.gameB.totalScoreB).toString()

        binding.changeNameLinearLayout.visibility=View.VISIBLE
        binding.userNameEt.visibility = View.GONE
        binding.changeNameLinearLayoutEt.visibility=View.GONE
    }


    /** --------------------- buttons listeners -------------------------------**/

    private fun setButtonsActions() {
        // back to main screen
        binding.backToGame.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.otherGamesButton.setOnClickListener {
            val intent = Intent(this,OtherGamesActivity::class.java)
            startActivity(intent)
            finish()
        }




        binding.googleSignIn.setOnClickListener {
            if (auth.currentUser!=null){
                signOut()
            }else{
                signIn()
            }
        }

        binding.changeNameButton.setOnClickListener {
            binding.userNameEt.visibility=View.VISIBLE
            binding.userNameEt.requestFocus()
            binding.changeNameLinearLayoutEt.visibility=View.VISIBLE
            binding.changeNameLinearLayout.visibility=View.GONE
            updateUserName(loggedInStatus.userid)



        }

        binding.ranking.setOnClickListener {
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

        binding.userNameEt.setText(tUser.userName)
        binding.changeNameOkButton.setOnClickListener {
            tUser.userName=binding.userNameEt.text.toString()
            Functions.saveUserNameToSharedPreferences(this,userID,tUser.userName)
            val currentUser = auth.currentUser
            if(currentUser!=null) {
                if (currentUser.uid == userID) {
                    val dbReference = Firebase.database.getReference("user").child(userID)
                    dbReference.setValue(tUser)

                }
            }
            binding.changeNameLinearLayout.visibility = View.VISIBLE
            updateUI()
        }

    }

    /** ----------------------- sign in methods ---------------------------------**/

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
//        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun doSomething(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken)
        }catch (e:ApiException){
            Log.w("TAG","Google sign in failed", e)
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == RC_SIGN_IN){
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)!!
//                firebaseAuthWithGoogle(account.idToken!!)
//            }
//            catch (e:ApiException){
//                Log.w("TAG", "Google sign in failed", e)
//            }
//        }
//    }

    private fun firebaseAuthWithGoogle(idToken:String?){
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
                            if(userName != tUser.userName){
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


}



