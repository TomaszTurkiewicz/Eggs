package com.tt.eggs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.eggs.classes.User
import com.tt.eggs.recyclerView.RankingRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_ranking.*

class Ranking : AppCompatActivity() {

    private lateinit var rankingAdapter: RankingRecyclerViewAdapter
    private lateinit var userList: MutableList<User>
    private lateinit var userid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_ranking)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView.visibility = View.GONE
        ranking_error.visibility = View.GONE

        val currentUser = Firebase.auth.currentUser
        userid = currentUser?.uid ?: ""
        userList = mutableListOf()
        createUserListFromFirebase()





    }

    private fun createUserListFromFirebase() {
        val dbRef = Firebase.database.getReference("user")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                progress_bar.visibility = View.GONE
                ranking_error.text = "DATABASE ERROR"
                ranking_error.visibility = View.VISIBLE
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
                    progress_bar.visibility = View.GONE
                    ranking_error.text = "DATABASE EMPTY"
                    ranking_error.visibility = View.VISIBLE
                }

            }

        })

    }

    private fun sortAndDisplay() {

        if(userList.size>1){
            sort()
        }


        initRecyclerView()
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

    private fun initRecyclerView(){
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@Ranking)
            rankingAdapter = RankingRecyclerViewAdapter(users = userList,userID = userid)
            adapter = rankingAdapter
        }

        recyclerView.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE

    }

}
