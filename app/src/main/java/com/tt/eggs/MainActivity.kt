package com.tt.eggs


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.eggs.classes.User
import com.tt.eggs.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var userList: MutableList<User>
    private var listSorted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // make full screen
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        fullScreen(view)
        MobileAds.initialize(this)

        userList = mutableListOf()
        createUserListFromFirebase()

    }


    private fun fullScreen(view:View){
        WindowCompat.setDecorFitsSystemWindows(window,false)
        WindowInsetsControllerCompat(window,view).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun createUserListFromFirebase() {
        val dbRef = Firebase.database.getReference("user")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(user in p0.children){
                        val tUser = user.getValue(User::class.java)
                        userList.add(tUser!!)
                    }
                    sortAndDisplay()
                }
            }

        })

    }

    private fun sortAndDisplay() {

        if(userList.size>1){
            sort()
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
        }else{
            listSorted = true
            binding.imageViewRanking.setBackgroundColor(ContextCompat.getColor(this,R.color.green))
        }
    }

    fun getSorted():Boolean{
        return this.listSorted
    }

    fun getList(): MutableList<User> {
        return this.userList
    }
}

//TODO DO NOT SORT HERE!!!








