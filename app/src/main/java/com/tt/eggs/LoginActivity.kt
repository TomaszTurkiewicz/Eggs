package com.tt.eggs

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.tt.eggs.classes.Functions
import com.tt.eggs.classes.LoggedInStatus
import com.tt.eggs.classes.User

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var loggedInStatus = LoggedInStatus()


    /**------------------ activity life cycle -------------------------------------**/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // display full screen
        fullScreen()
        setContentView(R.layout.activity_login)

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
        googleSignIn.text=if(auth.currentUser!=null) "LOG OUT" else "LOG IN"

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
        totalScoreAUser.text="-"
        highScoreBUser.text="-"
        totalScoreBUser.text="-"
        totalScoreUser.text="-"
        highScoreACounter.text=""
        highScoreBCounter.text=""
        change_name_button.visibility=View.GONE
        user_name_et.visibility=View.GONE
        update_name_button.visibility=View.GONE
    }

    //display user statistics
    private fun display(userID:String) {
        val tUser = User(userID,
        Functions.checkUserNameFromSharedPreferences(this,userID),
        Functions.readGameAFromSharedPreferences(this,userID),
        Functions.readGameBFromSharedPreferences(this,userID))

        user_name_tv.text=tUser.userName
        highScoreAUser.text=tUser.gameA.highScoreA.toString()
        totalScoreAUser.text=tUser.gameA.totalScoreA.toString()
        highScoreBUser.text=tUser.gameB.highScoreB.toString()
        totalScoreBUser.text=tUser.gameB.totalScoreB.toString()
        totalScoreUser.text=(tUser.gameA.totalScoreA+tUser.gameB.totalScoreB).toString()
        if(tUser.gameA.counterA>0){
        highScoreACounter.text="("+tUser.gameA.counterA+")"
        }
        else{
            highScoreACounter.text=""
        }
        if(tUser.gameB.counterB>0){
            highScoreBCounter.text="("+tUser.gameB.counterB+")"
        }
        else{
            highScoreBCounter.text=""
        }
        change_name_button.visibility=View.VISIBLE
        user_name_et.visibility = View.GONE
        update_name_button.visibility=View.GONE
    }


    /** --------------------- buttons listeners -------------------------------**/

    private fun setButtonsActions() {
        // back to main screen
        backToGame.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
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
            update_name_button.visibility=View.VISIBLE
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
        update_name_button.setOnClickListener {
            tUser.userName=user_name_et.text.toString()
            Functions.saveUserNameToSharedPreferences(this,userID,tUser.userName)
            val currentUser = auth.currentUser
            if(currentUser!=null) {
                if (currentUser.uid.equals(userID)) {
                    val dbReference = Firebase.database.getReference("user").child(userID)
                    dbReference.setValue(tUser)
                    updateUI()
                }
            }
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
                            Functions.saveLoggedStateToSharedPreferences(this,true, user.uid)

                            updateUI()
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
                        val gameA = Functions.readGameAFromSharedPreferences(this@LoginActivity,user.uid)
                        val gameB = Functions.readGameBFromSharedPreferences(this@LoginActivity,user.uid)
                        if(tUser!=null) {
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

}

// TODO send game to a friend
// TODO ranking activity
// TODO update button (invisible when there is no update needed)

