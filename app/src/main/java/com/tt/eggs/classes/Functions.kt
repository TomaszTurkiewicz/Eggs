package com.tt.eggs.classes

import android.content.Context

class Functions {
    companion object{

        fun readGameAFromSharedPreferences(context: Context?, userId:String):GameA{
            var tPoints = 0
            var tHigh = 0
            var tCounter = 0
            if(context!=null){
            val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
            tPoints = sharedPreferences.getInt("TOTAL_A",0)
            tHigh =  sharedPreferences.getInt("HIGH_A",0)
            tCounter =  sharedPreferences.getInt("COUNTER_A",0)
        }
            return GameA(tPoints,tHigh,tCounter)

        }

        fun readGameBFromSharedPreferences(context: Context?, userId:String):GameB{
            var tPoints = 0
            var tHigh = 0
            var tCounter = 0
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
                tPoints = sharedPreferences.getInt("TOTAL_B",0)
                tHigh =  sharedPreferences.getInt("HIGH_B",0)
                tCounter =  sharedPreferences.getInt("COUNTER_B",0)
            }
            return GameB(tPoints,tHigh,tCounter)

        }

        fun savePointsLoseAToSharedPreferences(context: Context?, userId: String, score:Int):Boolean{
            var newHighScore = false
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
                var tPoints = sharedPreferences.getInt("TOTAL_A", 0)
                var tHigh = sharedPreferences.getInt("HIGH_A", 0)

                tPoints += score

                if(tHigh<score){
                    tHigh=score
                    newHighScore=true
                }
                val editor = sharedPreferences.edit()
                editor.putInt("TOTAL_A",tPoints)
                editor.putInt("HIGH_A",tHigh)
                editor.apply()
            }
            return newHighScore
        }

        fun savePointsLoseBToSharedPreferences(context: Context?, userId: String, score:Int):Boolean{
            var newHighScore = false
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
                var tPoints = sharedPreferences.getInt("TOTAL_B", 0)
                var tHigh = sharedPreferences.getInt("HIGH_B", 0)

                tPoints += score

                if(tHigh<score){
                    tHigh=score
                    newHighScore=true
                }
                val editor = sharedPreferences.edit()
                editor.putInt("TOTAL_B",tPoints)
                editor.putInt("HIGH_B",tHigh)
                editor.apply()
            }
            return newHighScore
        }

        fun saveStatisticAToSharedPreferences(context: Context?, userId: String, gameA: GameA){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("TOTAL_A",gameA.totalScoreA)
                editor.putInt("HIGH_A",gameA.highScoreA)
                editor.putInt("COUNTER_A",gameA.counterA)
                editor.apply()
            }
        }

        fun saveStatisticBToSharedPreferences(context: Context?, userId: String, gameB: GameB){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("TOTAL_B",gameB.totalScoreB)
                editor.putInt("HIGH_B",gameB.highScoreB)
                editor.putInt("COUNTER_B",gameB.counterB)
                editor.apply()
            }
        }

        fun savePointsWinAToSharedPreferences(context: Context?, userId: String, score:Int){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
                var tPoints = sharedPreferences.getInt("TOTAL_A", 0)
                var tHigh = sharedPreferences.getInt("HIGH_A", 0)
                var tCounter = sharedPreferences.getInt("COUNTER_A", 0)
                tPoints += score

                if(tHigh!=1000){
                    tHigh=1000
                }
                tCounter+=1

                val editor = sharedPreferences.edit()
                editor.putInt("TOTAL_A",tPoints)
                editor.putInt("HIGH_A",tHigh)
                editor.putInt("COUNTER_A",tCounter)

                editor.apply()
            }
        }

        fun savePointsWinBToSharedPreferences(context: Context?, userId: String, score:Int){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
                var tPoints = sharedPreferences.getInt("TOTAL_B", 0)
                var tHigh = sharedPreferences.getInt("HIGH_B", 0)
                var tCounter = sharedPreferences.getInt("COUNTER_B", 0)
                tPoints += score

                if(tHigh!=1000){
                    tHigh=1000
                }
                tCounter+=1

                val editor = sharedPreferences.edit()
                editor.putInt("TOTAL_B",tPoints)
                editor.putInt("HIGH_B",tHigh)
                editor.putInt("COUNTER_B",tCounter)

                editor.apply()
            }
        }

        fun clearSavedUserGameInSharedPreferences(context: Context?, userId: String){
            if(context!=null){
                 val sharedPreferences = context.getSharedPreferences(userId, Context.MODE_PRIVATE)
                 val editor = sharedPreferences.edit()
                 editor.putInt("POINTS",0)
                 editor.putInt("FAULTS",0)
                 editor.putBoolean("GAME_MODE",false)
                 editor.apply()
             }

            }

        fun clearSavedGameInSharedPreferences(context: Context?){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("GAME_STATE", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("POINTS",0)
                editor.putInt("FAULTS",0)
                editor.putBoolean("GAME_MODE",false)
                editor.apply()
            }

        }

        fun saveUserGameStateToSharedPreferences(context: Context?,userId: String, gameState:GameState){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("POINTS",gameState.score)
                editor.putInt("FAULTS",gameState.fault)
                editor.putBoolean("GAME_MODE",gameState.gameMode)
                editor.apply()
            }
        }

        fun saveGameStateToSharedPreferences(context: Context?, gameState:GameState){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("GAME_STATE", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("POINTS",gameState.score)
                editor.putInt("FAULTS",gameState.fault)
                editor.putBoolean("GAME_MODE",gameState.gameMode)
                editor.apply()
            }
        }

        fun checkUserGameStateFromSharedPreferences(context: Context?,userId: String):GameState{
            val gameState=GameState()
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId, Context.MODE_PRIVATE)
                gameState.score = sharedPreferences.getInt("POINTS", 0)
                gameState.fault = sharedPreferences.getInt("FAULTS", 0)
                gameState.gameMode = sharedPreferences.getBoolean("GAME_MODE", false)
            }
            return gameState
        }

        fun checkGameStateFromSharedPreferences(context: Context?):GameState{
            val gameState=GameState()
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("GAME_STATE", Context.MODE_PRIVATE)
                gameState.score = sharedPreferences.getInt("POINTS", 0)
                gameState.fault = sharedPreferences.getInt("FAULTS", 0)
                gameState.gameMode = sharedPreferences.getBoolean("GAME_MODE", false)
            }
            return gameState
        }

        fun saveUserNameToSharedPreferences(context: Context?, userId: String, userName: String){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId,Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("USER_NAME",userName)
                editor.apply()
            }
        }

        fun checkUserNameFromSharedPreferences(context: Context?,userId: String):String{
            var userName = ""
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences(userId, Context.MODE_PRIVATE)
                userName = sharedPreferences.getString("USER_NAME", "ANONYMOUS").toString()
            }
            return userName
        }

        fun saveLoggedStateToSharedPreferences(context: Context?, loggedIn:Boolean, userid: String){
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("LOGGED_IN",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("LOGGED_IN", loggedIn)
                editor.putString("USER_ID",userid)
                editor.apply()
            }
        }
        fun readLoggedInStatusFromSharedPreferences(context: Context?):LoggedInStatus{
            val loggedInStatus = LoggedInStatus()
            if(context!=null){
                val sharedPreferences = context.getSharedPreferences("LOGGED_IN", Context.MODE_PRIVATE)
                loggedInStatus.loggedIn = sharedPreferences.getBoolean("LOGGED_IN",false)
                loggedInStatus.userid = sharedPreferences.getString("USER_ID","").toString()
            }
            return loggedInStatus
        }


    }

}
