package com.tt.eggs.classes

import android.content.Context
import android.content.res.Configuration
import com.tt.eggs.R

class Theme(val context: Context) {

    private val darkMode = Functions.readDarkModeFromSharedPreferences(context)

    val darkModeBoolean = checkDarkMode(darkMode)

    private fun checkDarkMode(darkMode: Int): Boolean {
        return when(darkMode){
            Static.DARK_MODE_ON -> true
            Static.DARK_MODE_OFF -> false
            else -> checkSystemDarkMode()
        }
    }

    private fun checkSystemDarkMode(): Boolean {
        val boolean = when(context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)){
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }
        return boolean
    }

    private val theme = if(darkModeBoolean) DarkMode() else LightMode()
//    private val theme = LightMode()

    fun getBackgroundColor() = theme.getBackgroundColor()
    fun getTextColor() = theme.getTextColor()


    private open class LightMode {
        open val background = R.color.gray
        open val text = R.color.black

        open fun getBackgroundColor(): Int {
            return this.background
        }

        open fun getTextColor():Int {
            return this.text
        }
    }

    private class DarkMode : LightMode() {
        override val background = R.color.black
        override val text = R.color.gray_middle

        override fun getBackgroundColor(): Int {
            return this.background
        }

        override fun getTextColor(): Int {
            return this.text
        }
    }
}