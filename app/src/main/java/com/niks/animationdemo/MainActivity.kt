package com.niks.animationdemo

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import app.niks.base.extension.convertDpToPixel


class MainActivity : AppCompatActivity() {
    //First position is 0
    var gotoPosition = 0
    val NUMBER_OF_VIEWS = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        printViews()

        startAnimation.setOnClickListener {

            gotoPosition++
            gotoPosition %= NUMBER_OF_VIEWS

            printCoordinates(firstView)
            startAnimation(firstView, gotoPosition, 0)

            printCoordinates(secondView)
            startAnimation(secondView, gotoPosition, 1)

            printCoordinates(thirdView)
            startAnimation(thirdView, gotoPosition, 2)

            printCoordinates(fourthView)
            startAnimation(fourthView, gotoPosition, 3)

            printCoordinates(fifthView)
            startAnimation(fifthView, gotoPosition, 4)

            Log.d("yep", "=================================================================")

            //printViews()

        }
    }

    private fun getYDelta(goToPosition: Int, position: Int): Float {
        val currentViewNextPosition = goToPosition + position
        //To move the view to 0th position
        var modValue = currentViewNextPosition % NUMBER_OF_VIEWS
        logInfo("Mod Value : $modValue")
        //to get negative values for postion>0 and position values for position 0
        modValue -= position
        val deltaY = modValue * convertDpToPixel(50F)
        logInfo("currentViewNextPosition : $currentViewNextPosition  Mod Value : $modValue DeltaY : $deltaY")
        return deltaY
    }


    private fun startAnimation(view: View, gotoPosition: Int, viewPosition: Int) {
        var finalViewPosition = viewPosition + gotoPosition
        finalViewPosition %= NUMBER_OF_VIEWS

        if (finalViewPosition == 0) {
            logInfo("viewPosition:$viewPosition is INVISIBLE")
            view.visibility = View.INVISIBLE
        } else {
            logInfo("viewPosition:$viewPosition is VISIBLE")
            view.visibility = View.VISIBLE
        }
//        val isViewAtEnd = isViewAtEnd(gotoPosition,viewPosition)
        val yTranslation = getYDelta(gotoPosition, viewPosition)
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationY", yTranslation)
        objectAnimator.interpolator = AccelerateDecelerateInterpolator()
        objectAnimator.duration = 1000
        objectAnimator.start()
        objectAnimator.doOnStart {

        }
        objectAnimator.doOnEnd {

        }
    }

//    private fun isViewAtEnd(gotoPosition: Int, viewPosition: Int): Boolean {
//        //Todo
//    }

    private fun printCoordinates(view: View) {
        Log.d("yep", "X = ${view.x} Y = ${view.y}")
    }

    fun logInfo(msg: String) {
        Log.d("yep", msg)

    }

    private fun printViews() {
        Handler(Looper.getMainLooper()).postDelayed({
            printCoordinates(firstView)
            printCoordinates(secondView)
            printCoordinates(thirdView)
            printCoordinates(fourthView)
            Log.d("yep", "=================================================================")
        }, 2000)
    }


}