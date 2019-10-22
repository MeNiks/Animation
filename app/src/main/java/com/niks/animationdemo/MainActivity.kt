package com.niks.animationdemo

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var clickedCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        startAnimation.setOnClickListener {
            //printCoordinates(fir)
            // when (clickedCount) {
            //     0 -> {
            //
            //     }
            //
            //     1 -> {
            //
            //     }
            //     2 -> {
            //
            //     }
            //
            //     3 -> {
            //
            //     }
            // }

            startAnimation(firstView, getYDelta(clickedCount, 0, firstView))
            startAnimation(secondView, (getYDelta(clickedCount + 1, 1, secondView)))
            printCoordinates(secondView)
            startAnimation(thirdView, (getYDelta(clickedCount + 2, 2, thirdView)))
            startAnimation(fourthView, (getYDelta(clickedCount + 3, 3, fourthView)))

            clickedCount++

        }
    }

    private fun getYDelta(clickedCount: Int, position: Int, view: View): Float {
        return if (clickedCount % 4 == position) {
            0F
        } else {
            view.y + dpToPx(60).toFloat()
        }
    }

    fun dpToPx(dp: Int): Int {
        val displayMetrics = resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun startAnimation(view: View, yTranslation: Float) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationY", yTranslation)
        objectAnimator.interpolator = AccelerateDecelerateInterpolator()
        objectAnimator.duration = 1000
        objectAnimator.start()
    }

    private fun printCoordinates(view: View) {
        Log.d("yep", "X = ${view.x} Y = ${view.y}")
    }
}
