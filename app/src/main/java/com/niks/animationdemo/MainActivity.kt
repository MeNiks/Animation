package com.niks.animationdemo

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var clickedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        printViews()


        startAnimation.setOnClickListener {
            startAnimation(firstView, getYDelta(clickedCount, 0, firstView))
            startAnimation(secondView, (getYDelta(clickedCount + 1, 1, secondView)))
            startAnimation(thirdView, (getYDelta(clickedCount + 2, 2, thirdView)))
            startAnimation(fourthView, (getYDelta(clickedCount + 3, 3, fourthView)))
            printViews()
            clickedCount++
        }
    }

    private fun printViews() {
        Handler(Looper.getMainLooper()).postDelayed({
            printCoordinates(firstView)
            printCoordinates(secondView)
            printCoordinates(thirdView)
            printCoordinates(fourthView)
            Log.d("yep", "=============")
        }, 2000)
    }

    private fun getYDelta(clickedCount: Int, position: Int, view: View): Float {
        if (clickedCount % 4 == 0) {
            return when (clickedCount % 4) {
                0 -> {
                    100F
                }
                1 -> {
                    200F
                }
                2 -> {
                    300F
                }
                3 -> {
                    0F
                }
                else -> {
                    0F
                }
            }
        } else {
            return if (view.y == 300F) {
                return -300F
            } else {
                100F
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    private fun startAnimation(view: View, yTranslation: Float) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationY", yTranslation)
        objectAnimator.interpolator = AccelerateDecelerateInterpolator()
        objectAnimator.duration = 1000
        objectAnimator.start()
    }

    private fun printCoordinates(view: View) {
        Log.d("yep", "X = ${view.x} Y = ${view.y} $view")
    }
}
