package com.tt.eggs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    /**------------------ activity life cycle -------------------------------------**/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // display full screen
        fullScreen()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        updateUI(currentUser)

        setButtonsActions()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

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

    private fun updateUI(user:FirebaseUser?){
        login_status.text = if(user!=null)"LOGGED IN" else "NOT LOGGED IN"

    }


    /** --------------------- buttons listeners -------------------------------**/

    private fun setButtonsActions() {
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
                    val user = auth.currentUser
                    updateUI(user)
                }else{
                    updateUI(null)
                }
            }
    }

    private fun signOut(){
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this){
            updateUI(null)
        }
    }
    /** ------------------------ companion objects ----------------------------------**/


    companion object{
        private const val RC_SIGN_IN = 9001
    }

}
