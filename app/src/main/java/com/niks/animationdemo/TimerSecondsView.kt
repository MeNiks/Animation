package com.niks.animationdemo

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import app.niks.base.extension.convertDpToPixel
import app.niks.base.extension.handleThrowable
import app.niks.base.extension.logInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.seconds_view.view.*
import java.util.concurrent.TimeUnit

class TimerSecondsView : ConstraintLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.seconds_view, this, true)
        printViews()
    }

    private var compositeDisposable: CompositeDisposable? = null

    private var gotoPosition = 0
    private var remainingSeconds = 0L


    fun setRemainingTime(time: Long) {
        this.remainingSeconds = time
        firstView.text = "${time - 1}"
        secondView.text = "$time"
    }

    fun startTime() {
        logInfo(TAG, "startTime")
        compositeDisposable = CompositeDisposable()
        compositeDisposable?.addAll(
            Observable
                .interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                // (remainingSeconds + 1) so that it will srart with the exact seconds
                // Ex - For 59 sec interval will start from 1 to seconds will be (59 +1) - 1 = 59
                .map { interval -> (remainingSeconds + 1) - interval }
                .filter { seconds -> seconds >= 0 }
                .subscribe({ seconds ->
                    logInfo(TAG, "time : $seconds")
                    incrementSeconds(seconds)
                }, ::handleThrowable)
        )
    }

    fun stopTime() {
        compositeDisposable?.clear()
    }

    private fun incrementSeconds(seconds: Long) {
        gotoPosition++
        gotoPosition %= NUMBER_OF_VIEWS

        printCoordinates(firstView)
        if (getFinalViewPosition(gotoPosition, -1) == -1)
            firstView.text = "${seconds}"
        startAnimation(firstView, gotoPosition, 0)


        printCoordinates(secondView)
        if (getFinalViewPosition(gotoPosition, 0) == -1)
            secondView.text = "${seconds}"
        startAnimation(secondView, gotoPosition, 1)


        printCoordinates(thirdView)
        if (getFinalViewPosition(gotoPosition, 1) == -1)
            thirdView.text = "${seconds}"
        startAnimation(thirdView, gotoPosition, 2)


        printCoordinates(fourthView)
        if (getFinalViewPosition(gotoPosition, 2) == -1)
            fourthView.text = "${seconds}"
        startAnimation(fourthView, gotoPosition, 3)


        printCoordinates(fifthView)
        if (getFinalViewPosition(gotoPosition, 3) == -1)
            fifthView.text = "${seconds}"
        startAnimation(fifthView, gotoPosition, 4)

        logInfo(TAG, "=================================================================")

    }

    private fun getYDelta(goToPosition: Int, position: Int): Float {
        val currentViewNextPosition = goToPosition + position
        //To move the view to 0th position
        var modValue = currentViewNextPosition % NUMBER_OF_VIEWS
        logInfo(TAG, "Mod Value : $modValue")
        //to get negative values for postion>0 and position values for position 0
        modValue -= position
        modValue -= 1
        //To get coordinates above 0th element
        val deltaY = modValue * context.convertDpToPixel(50F)
        logInfo(TAG, "currentViewNextPosition : $currentViewNextPosition  Mod Value : $modValue DeltaY : $deltaY")
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

        if (isViewAtEnd) {
            view.visibility = View.INVISIBLE
            objectAnimator.doOnEnd {
                view.visibility = View.VISIBLE
            }
        }
    }

    private fun getFinalViewPosition(gotoPosition: Int, viewPosition: Int): Int {
        var finalViewPosition = viewPosition + gotoPosition
        if (finalViewPosition == 4) {
            finalViewPosition = -1
        }
        return finalViewPosition
    }

    private fun printCoordinates(view: View) {
        logInfo(TAG, "X = ${view.x} Y = ${view.y}")
    }

    private fun printViews() {
        Handler(Looper.getMainLooper()).postDelayed({
            printCoordinates(firstView)
            printCoordinates(secondView)
            printCoordinates(thirdView)
            printCoordinates(fourthView)
            logInfo(TAG, "=============")
        }, 1000)
    }

    companion object {
        private const val TAG = "yep"
        private const val NUMBER_OF_VIEWS = 5
    }

}