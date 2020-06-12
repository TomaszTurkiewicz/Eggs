package com.tt.eggs.recyclerView

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tt.eggs.R
import com.tt.eggs.classes.User
import kotlinx.android.synthetic.main.recyclerview_layout.view.*

class RankingRecyclerViewAdapter(private val users: List<User>, private val userID:String) : RecyclerView.Adapter<RankingRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_layout,parent,false)
        )
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.position.text=(position+1).toString()
        holder.username.text= users[position].userName
        if(users[position].gameA.counterA==0) {
            holder.highA.text = users[position].gameA.highScoreA.toString()
        }
        else{
            holder.highA.text = users[position].gameA.highScoreA.toString() +" ("+users[position].gameA.counterA+")"
        }
        if(users[position].gameB.counterB==0) {
            holder.highB.text = users[position].gameB.highScoreB.toString()
        }
        else{
            holder.highB.text = users[position].gameB.highScoreB.toString() +" ("+users[position].gameB.counterB+")"
        }


        holder.total.text= (users[position].gameA.totalScoreA+users[position].gameB.totalScoreB).toString()
        if(users[position].id.equals(userID)){
            holder.layout.setBackgroundColor(Color.GREEN)
        }else{
            holder.layout.setBackgroundColor(Color.TRANSPARENT)
        }

    }

    override fun getItemCount(): Int {
        return users.size
    }

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layout:LinearLayout = itemView.recyclerView_layout
    val position:TextView = itemView.position
    val username:TextView = itemView.username
    val highA:TextView = itemView.highA
    val highB:TextView = itemView.highB
    val total:TextView = itemView.total



}




}