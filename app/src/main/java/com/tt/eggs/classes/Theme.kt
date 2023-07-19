package com.tt.eggs.classes

import android.content.Context
import android.content.res.Configuration
import com.tt.eggs.R

class Theme(val context: Context) {

    private val darkMode = Functions.readDarkModeFromSharedPreferences(context)

    private val darkModeBoolean = checkDarkMode(darkMode)

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


    fun getBackgroundColor() = theme.getBackgroundColor()
    fun getTextColor() = theme.getTextColor()
    fun getAccentLightColor() = theme.getAccentLightColor()
    fun getAccentMiddleColor() = theme.getAccentMiddleColor()
    fun getAccentDarkColor() = theme.getAccentDarkColor()
    fun getRoundedFrameWhiteColor() = theme.getRoundedFrameWhiteColor()
    fun getLCDColor() = theme.getLCDColor()
    fun getRedColor() = theme.getRedColor()
    fun getWhiteColor() = theme.getWhiteColor()
    fun getGreenColor() = theme.getGreenColor()
    fun getShadowColor() = theme.getShadowColor()
    fun getRedDarkColor() = theme.getRedDarkColor()
    fun getRedLightColor() = theme.getRedLightColor()
    fun getGreenDarkColor() = theme.getGreenDarkColor()
    fun getGreenLightColor() = theme.getGreenLightColor()

    private open class LightMode {
        open val background = R.color.gray
        open val text = R.color.black
        open val accentLight = R.color.gray_light
        open val accentMiddle = R.color.gray_middle
        open val accentDark = R.color.gray_dark
        open val roundedFrameWhite = R.color.white
        open val lcd = R.color.gray_LCD
        open val red = R.color.red
        open val white = R.color.white
        open val green = R.color.green
        open val shadow = R.color.shadow
        open val redDark = R.color.red_dark
        open val redLight = R.color.red_light
        open val greenDark = R.color.green_dark
        open val greenLight = R.color.green_light

        open fun getGreenDarkColor():Int{
            return this.greenDark
        }

        open fun getGreenLightColor():Int{
            return this.greenLight
        }

        open fun getRedLightColor():Int{
            return this.redLight
        }

        open fun getRedDarkColor():Int{
            return this.redDark
        }

        open fun getShadowColor():Int{
            return this.shadow
        }

        open fun getGreenColor():Int{
            return this.green
        }

        open fun getWhiteColor():Int{
            return this.white
        }

        open fun getRedColor():Int{
            return this.red
        }

        open fun getLCDColor():Int{
            return this.lcd
        }

        open fun getRoundedFrameWhiteColor():Int {
            return this.roundedFrameWhite
        }

        open fun getBackgroundColor(): Int {
            return this.background
        }

        open fun getTextColor():Int {
            return this.text
        }

        open fun getAccentLightColor():Int{
            return this.accentLight
        }

        open fun getAccentMiddleColor():Int{
            return this.accentMiddle
        }

        open fun getAccentDarkColor():Int{
            return this.accentDark
        }
    }

    private class DarkMode : LightMode() {
        override val background = R.color.black
        override val text = R.color.gray_middle
        override val accentLight = R.color.black_light
        override val accentMiddle = R.color.black_middle
        override val accentDark = R.color.black_dark
        override val roundedFrameWhite = R.color.gray_middle
        override val lcd = R.color.gray_LCD_dark_mode
        override val red = R.color.red_dark_mode
        override val white = R.color.white_dark_mode
        override val green = R.color.green_dark_mode
        override val shadow = R.color.shadow_dark_mode
        override val redDark = R.color.red_dark_dark_mode
        override val redLight = R.color.red_light_dark_mode
        override val greenDark = R.color.green_dark_dark_mode
        override val greenLight = R.color.green_light_dark_mode

        override fun getGreenDarkColor():Int{
            return this.greenDark
        }

        override fun getGreenLightColor():Int{
            return this.greenLight
        }
        override fun getRedLightColor():Int{
            return this.redLight
        }

        override fun getRedDarkColor():Int{
            return this.redDark
        }

        override fun getShadowColor():Int{
            return this.shadow
        }

        override fun getGreenColor():Int{
            return this.green
        }

        override fun getWhiteColor():Int{
            return this.white
        }

        override fun getRedColor():Int{
            return this.red
        }

        override fun getLCDColor():Int{
            return this.lcd
        }

        override fun getRoundedFrameWhiteColor():Int {
            return this.roundedFrameWhite
        }

        override fun getBackgroundColor(): Int {
            return this.background
        }

        override fun getTextColor(): Int {
            return this.text
        }

        override fun getAccentLightColor(): Int {
            return this.accentLight
        }

        override fun getAccentMiddleColor(): Int {
            return this.accentMiddle
        }

        override fun getAccentDarkColor(): Int {
            return this.accentDark
        }
    }
}