package com.tt.eggs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tt.eggs.drawable.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test_egg.*

class TestEgg : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_egg)

        val size = 800
        eggImage.layoutParams = ConstraintLayout.LayoutParams(size, (size*1.2).toInt())

        val set = ConstraintSet()
        set.clone(eggActivityLayout)

        set.connect(eggImage.id, ConstraintSet.LEFT,eggActivityLayout.id, ConstraintSet.LEFT,0)
        set.connect(eggImage.id, ConstraintSet.RIGHT,eggActivityLayout.id, ConstraintSet.RIGHT,0)
        set.connect(eggImage.id, ConstraintSet.TOP,eggActivityLayout.id, ConstraintSet.TOP,0)
        set.connect(eggImage.id, ConstraintSet.BOTTOM,eggActivityLayout.id, ConstraintSet.BOTTOM,0)

        set.applyTo(eggActivityLayout)




            eggImage.setImageDrawable(RunningChickenLeftFirst(this, size.toDouble()))








    }


}
