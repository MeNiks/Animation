package com.niks.animationdemo

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import app.niks.base.extension.convertDpToPixel
import app.niks.base.extension.handleThrowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.seconds_view.view.*
import java.util.concurrent.TimeUnit

class TimerSecondsView : ConstraintLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.seconds_view, this, true)
    }

    private var compositeDisposable: CompositeDisposable? = null

    private var gotoPosition = 0
    private val NUMBER_OF_VIEWS = 5

    fun startTime() {
        compositeDisposable = CompositeDisposable()
        compositeDisposable?.addAll(
            Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribe({
                    incrementSeconds()
                }, ::handleThrowable)

        )

    }

    private fun incrementSeconds() {
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

    }

    private fun getYDelta(goToPosition: Int, position: Int): Float {
        val currentViewNextPosition = goToPosition + position
        //To move the view to 0th position
        var modValue = currentViewNextPosition % NUMBER_OF_VIEWS
        logInfo("Mod Value : $modValue")
        //to get negative values for postion>0 and position values for position 0
        modValue -= position
        modValue -= 1
        val deltaY = modValue * context.convertDpToPixel(50F)
        logInfo("currentViewNextPosition : $currentViewNextPosition  Mod Value : $modValue DeltaY : $deltaY")
        return deltaY
    }


    private fun startAnimation(view: View, gotoPosition: Int, viewPosition: Int) {
        var finalViewPosition = viewPosition + gotoPosition
        finalViewPosition %= NUMBER_OF_VIEWS
        val isViewAtEnd = finalViewPosition == 0

        val yTranslation = getYDelta(gotoPosition, viewPosition)
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationY", yTranslation)
        objectAnimator.interpolator = AccelerateDecelerateInterpolator()
        objectAnimator.duration = 1000
        objectAnimator.start()
//        objectAnimator.doOnStart {
//        }


        if (isViewAtEnd) {
            view.visibility = View.INVISIBLE
            objectAnimator.doOnEnd {
                view.visibility = View.VISIBLE
            }
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