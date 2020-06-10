package com.tt.eggs.classes

data class User(var id:String? = "",
                var gameA: GameA = GameA(),
                var gameB: GameB = GameB())