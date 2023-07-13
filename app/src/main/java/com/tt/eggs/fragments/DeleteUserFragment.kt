package com.tt.eggs.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.eggs.R
import com.tt.eggs.classes.*
import com.tt.eggs.databinding.FragmentDeleteUserBinding
import com.tt.eggs.drawable.RoundedFrameDrawable
import com.tt.eggs.drawable.StartButton
import com.tt.eggs.drawable.TextViewDrawableWithBorder
import java.lang.Exception


class DeleteUserFragment : Fragment() {

    private var _binding : FragmentDeleteUserBinding? = null
    private val binding get() = _binding!!

    private var screenUnit = 0
    private val wholeScreenSize = Dimension()

    private val userNameSize = Dimension()
    private val highScoreASize = Dimension()
    private val highScoreBSize = Dimension()
    private val totalPointsSize = Dimension()
    private val positionLayoutSize = Dimension()
    private val headerSize = Dimension()
    private val backToGameButtonSize = Dimension()

    private var loggedInStatus = LoggedInStatus()

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        loggedInStatus = Functions.readLoggedInStatusFromSharedPreferences(requireContext())

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("348971081913-hi14av9f0sq2iier39kthd36k7pe6ao5.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(),gso)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data: Intent? = result.data
                doSomething(data)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteUserBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeUI()

    }

    private fun makeUI() {
        getScreenHeightAndWidth()
        setViewSizes()
        setDrawable()
        connectViews()
        displayUI(loggedInStatus)
        clicks()
    }

    private fun clicks() {
        binding.backToGameImageView.setOnClickListener {
            goFragmentUp()
        }

        binding.deleteUserImage.setOnClickListener {
            auth.signOut()

            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }

    }

    private fun displayUI(loggedInStatus: LoggedInStatus) {
        if(loggedInStatus.loggedIn){
            val user = User(loggedInStatus.userid,
                Functions.checkUserNameFromSharedPreferences(requireContext(),loggedInStatus.userid),
                Functions.readGameAFromSharedPreferences(requireContext(),loggedInStatus.userid),
                Functions.readGameBFromSharedPreferences(requireContext(),loggedInStatus.userid))

            binding.userName.text = user.userName
            if(user.gameA.counterA>0){
                binding.highScoreA.text = getString(R.string.high_score,user.gameA.highScoreA,user.gameA.counterA)
            }
            else{
                binding.highScoreA.text=user.gameA.highScoreA.toString()
            }

            if(user.gameB.counterB>0){

                binding.highScoreB.text=getString(R.string.high_score,user.gameB.highScoreB,user.gameB.counterB)
            }
            else{
                binding.highScoreB.text=user.gameB.highScoreB.toString()
            }
            binding.totalScore.text=(user.gameA.totalScoreA+user.gameB.totalScoreB).toString()

        }else{
            goFragmentUp()
        }

    }

    private fun connectViews() {
        val set = ConstraintSet()
        set.clone(binding.deleteUserFragment)

        set.connect(binding.deleteUserActivityContainer.id,
            ConstraintSet.TOP,binding.deleteUserFragment.id,
            ConstraintSet.TOP,0)
        set.connect(binding.deleteUserActivityContainer.id,
            ConstraintSet.BOTTOM,binding.deleteUserFragment.id,
            ConstraintSet.BOTTOM,0)
        set.connect(binding.deleteUserActivityContainer.id,
            ConstraintSet.RIGHT,binding.deleteUserFragment.id,
            ConstraintSet.RIGHT,0)
        set.connect(binding.deleteUserActivityContainer.id,
            ConstraintSet.LEFT,binding.deleteUserFragment.id,
            ConstraintSet.LEFT,0)

        set.connect(binding.warningTitle.id,
            ConstraintSet.TOP,binding.deleteUserActivityContainer.id,
            ConstraintSet.TOP,(screenUnit*0.5).toInt())
        set.connect(binding.warningTitle.id,
            ConstraintSet.LEFT,binding.deleteUserActivityContainer.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.warningTitle.id,
            ConstraintSet.RIGHT,binding.deleteUserActivityContainer.id,
            ConstraintSet.RIGHT,0)

        set.connect(binding.warningMessage.id,
            ConstraintSet.TOP,binding.deleteUserActivityContainer.id,
            ConstraintSet.TOP,(screenUnit*2.5).toInt())
        set.connect(binding.warningMessage.id,
            ConstraintSet.LEFT,binding.deleteUserActivityContainer.id,
            ConstraintSet.LEFT,0)
        set.connect(binding.warningMessage.id,
            ConstraintSet.RIGHT,binding.deleteUserActivityContainer.id,
            ConstraintSet.RIGHT,0)

        set.connect(binding.header.id,
            ConstraintSet.TOP,binding.deleteUserActivityContainer.id,
            ConstraintSet.TOP, (screenUnit*3.5).toInt())
        set.connect(binding.header.id,
            ConstraintSet.LEFT,binding.deleteUserActivityContainer.id,
            ConstraintSet.LEFT, 0)
        set.connect(binding.header.id,
            ConstraintSet.RIGHT,binding.deleteUserActivityContainer.id,
            ConstraintSet.RIGHT, 0)

        set.connect(binding.frame.id, ConstraintSet.TOP,binding.header.id, ConstraintSet.BOTTOM, 0)
        set.connect(binding.frame.id,
            ConstraintSet.LEFT,binding.deleteUserActivityContainer.id,
            ConstraintSet.LEFT, 0)
        set.connect(binding.frame.id,
            ConstraintSet.RIGHT,binding.deleteUserActivityContainer.id,
            ConstraintSet.RIGHT, 0)

        set.connect(binding.backToGameLinearLayout.id,
            ConstraintSet.TOP,binding.deleteUserActivityContainer.id,
            ConstraintSet.TOP, (screenUnit*8.5).toInt())
        set.connect(binding.backToGameLinearLayout.id,
            ConstraintSet.LEFT,binding.deleteUserActivityContainer.id,
            ConstraintSet.LEFT,screenUnit)

        set.connect(binding.deleteUserLinearLayout.id,
            ConstraintSet.TOP,binding.deleteUserActivityContainer.id,
            ConstraintSet.TOP, (screenUnit*6.5).toInt())
        set.connect(binding.deleteUserLinearLayout.id,
            ConstraintSet.RIGHT,binding.deleteUserActivityContainer.id,
            ConstraintSet.RIGHT, screenUnit)

        set.applyTo(binding.deleteUserFragment)

    }

    private fun setDrawable() {

        binding.warningMessage.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))
        binding.headerUserName.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))
        binding.headerHighScoreA.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))
        binding.headerHighScoreB.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))
        binding.headerTotalScore.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))
        binding.backToGame.setTextColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getTextColor()))

        binding.deleteUserActivityContainer.setBackgroundColor(ContextCompat.getColor(requireContext(),Theme(requireContext()).getBackgroundColor()))
        binding.frame.background = TextViewDrawableWithBorder(requireContext())

        binding.backToGameImageView.setImageDrawable(StartButton(requireContext()))
        binding.backToGameLinearLayout.background = RoundedFrameDrawable(requireContext(),
            backToGameButtonSize.height/20,
            backToGameButtonSize.height/2
        )

        binding.deleteUserImage.setImageDrawable(StartButton(requireContext()))
        binding.deleteUserLinearLayout.background = RoundedFrameDrawable(requireContext(),backToGameButtonSize.height/20,backToGameButtonSize.height/2)
    }

    private fun getScreenHeightAndWidth() {

        screenUnit= ScreenMetricsCompat.getScreenSize(requireContext())
    }

    private fun setViewSizes() {
        wholeScreenSize.width = 20.0 * screenUnit
        wholeScreenSize.height = 10.0 * screenUnit
        binding.deleteUserActivityContainer.layoutParams = ConstraintLayout.LayoutParams((wholeScreenSize.width).toInt(),(wholeScreenSize.height).toInt())

        binding.warningTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,(screenUnit).toFloat())
        binding.warningMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX,(screenUnit*0.6).toFloat())

        val username =7
        val highScore = 3
        val total = 4
        headerSize.height = (screenUnit).toDouble()

        userNameSize.width = (screenUnit*username).toDouble()
        userNameSize.height = (screenUnit).toDouble()
        highScoreASize.width = (screenUnit*highScore).toDouble()
        highScoreASize.height = (screenUnit).toDouble()
        highScoreBSize.width = (screenUnit*highScore).toDouble()
        highScoreBSize.height = (screenUnit).toDouble()
        totalPointsSize.width = (screenUnit*total).toDouble()
        totalPointsSize.height = (screenUnit).toDouble()
        positionLayoutSize.width=userNameSize.width+
                highScoreASize.width+
                highScoreBSize.width+
                totalPointsSize.width
        positionLayoutSize.height= (screenUnit*1.5)

        headerSize.width=userNameSize.width+
                highScoreASize.width+
                highScoreBSize.width+
                totalPointsSize.width

        binding.header.layoutParams= ConstraintLayout.LayoutParams((headerSize.width).toInt(),(headerSize.height).toInt())
        binding.headerUserName.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height).toInt())
        binding.headerHighScoreA.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height).toInt())
        binding.headerHighScoreB.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height).toInt())
        binding.headerTotalScore.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height).toInt())
        binding.headerUserName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.headerHighScoreA.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.headerHighScoreB.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.headerTotalScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.headerTotalScore.setPadding(0,0, (screenUnit*0.5).toInt(),0)

        binding.userName.layoutParams = LinearLayout.LayoutParams((userNameSize.width).toInt(),(userNameSize.height*1.5).toInt())
        binding.highScoreA.layoutParams = LinearLayout.LayoutParams((highScoreASize.width).toInt(),(highScoreASize.height*1.5).toInt())
        binding.highScoreB.layoutParams = LinearLayout.LayoutParams((highScoreBSize.width).toInt(),(highScoreBSize.height*1.5).toInt())
        binding.totalScore.layoutParams = LinearLayout.LayoutParams((totalPointsSize.width).toInt(),(totalPointsSize.height*1.5).toInt())
        binding.frame.layoutParams=
            ConstraintLayout.LayoutParams((positionLayoutSize.width).toInt(),(positionLayoutSize.height).toInt())
        binding.totalScore.setPadding(0,0, (screenUnit*0.5).toInt(),0)

        binding.userName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.userName.setPadding(screenUnit/2,0,0,0)
        binding.highScoreA.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.highScoreB.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())
        binding.totalScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.5).toFloat())

        backToGameButtonSize.width= (screenUnit*4/3).toDouble()
        backToGameButtonSize.height = backToGameButtonSize.width

        binding.backToGameImageView.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGame.layoutParams = LinearLayout.LayoutParams((4*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGameBlank.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width/2).toInt(),(backToGameButtonSize.height).toInt())
        binding.backToGame.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())

        binding.deleteUserImage.layoutParams = LinearLayout.LayoutParams((backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.deleteUserTv.layoutParams = LinearLayout.LayoutParams((4*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())
        binding.deleteUserTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (screenUnit*0.6).toFloat())
        binding.deleteUserTvBlank.layoutParams = LinearLayout.LayoutParams((0.5*backToGameButtonSize.width).toInt(),(backToGameButtonSize.height).toInt())

    }

    private fun doSomething(data:Intent?){
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try{
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken)
        }catch (e: Exception){
            Log.w("TAG","Google sign in failed",e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken:String?){
        val credentials = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credentials)
            .addOnCompleteListener(requireActivity()){task ->
                if(task.isSuccessful){
                    val user = Firebase.auth.currentUser
                    user?.let {
                        val dbRef = Firebase.database.getReference("user").child(user.uid)

                        dbRef.removeValue().addOnCompleteListener {
                            user.delete().addOnCompleteListener{
                                Toast.makeText(requireContext(),"USER DELETED", Toast.LENGTH_LONG).show()
                                Functions.saveLoggedStateToSharedPreferences(requireContext(),false,"")
                                goFragmentUp()
                            }.addOnFailureListener {
                                Toast.makeText(requireContext(),"SOMETHING WENT WRONG TRY ONCE AGAIN LATER",
                                    Toast.LENGTH_LONG).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(),"SOMETHING WENT WRONG TRY ONCE AGAIN LATER", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else{
                    Toast.makeText(requireContext(),"SOMETHING WENT WRONG TRY ONCE AGAIN LATER", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun goFragmentUp(){
        findNavController().navigateUp()
    }

}

