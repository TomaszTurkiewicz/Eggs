package com.tt.eggs.classes

class Static {
    companion object{

        // position left, right, bottom, top
        const val LEFT_BOTTOM = 0
        const val LEFT_TOP = 1
        const val RIGHT_TOP = 2
        const val RIGHT_BOTTOM = 3

        // faults catching eggs
        const val FAULT_NO_FAULT = 0
        const val FAULT_HALF = 1
        const val FAULT_ONE = 2
        const val FAULT_ONE_AND_HALF = 3
        const val FAULT_TWO = 4
        const val FAULT_TWO_AND_HALF = 5

        // game mode (A or B)
        const val GAME_A = false
        const val GAME_B = true

        // egg
        const val EGG = true
        const val NO_EGG = false

        // basket
        const val BASKET = true
        const val NO_BASKET = false

        // switch flashing
        const val OFF = false
        const val ON = true

        // max points in the game
        const val MAX_POINTS = 1000

        // game state
        const val DEMO = 0
        const val PLAY_A = 1
        const val PAUSE_A = 2
        const val PLAY_B = 3
        const val PAUSE_B = 4
        const val LOSE_A = 5
        const val LOSE_B = 6
        const val WIN_A = 7
        const val WIN_B = 8

        const val GAME_SIZE = 6

        // wolf direction
        const val WOLF_SHADOW = 0
        const val WOLF_UP = 1
        const val WOLF_DOWN = 2

        // dark modes
        const val DARK_MODE_AUTO = 0
        const val DARK_MODE_ON = 1
        const val DARK_MODE_OFF = 2

    }
}