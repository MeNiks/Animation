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

    private var yDelta = 0
    private var remainingSeconds = 0L
    private val START_VIEW_POSITION = -1


    fun setRemainingTime(time: Long) {
        this.remainingSeconds = time
        firstView.text = "${time - 1}"
        secondView.text = "$time"
    }

    fun startTime() {
        logInfo(TAG, "startTime")
        if (compositeDisposable?.size() ?: 0 > 0) {
            return
        }
        compositeDisposable = CompositeDisposable()
        compositeDisposable?.addAll(
            Observable
                .interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map { interval -> remainingSeconds - interval }
                .filter { seconds -> seconds >= 0 }
                .subscribe({ seconds ->
                    logInfo(TAG, "seconds : $seconds")
                    incrementSeconds(seconds)
                }, ::handleThrowable)
        )
    }

    fun stopTime() {
        compositeDisposable?.clear()
    }

    private fun incrementSeconds(seconds: Long) {
        yDelta++
        yDelta %= NUMBER_OF_VIEWS

        printCoordinates(firstView)
        if (getGoingToPosition(START_VIEW_POSITION) == START_VIEW_POSITION)
            firstView.text = "${seconds}"
        startAnimation(firstView, START_VIEW_POSITION)


        printCoordinates(secondView)
        if (getGoingToPosition(START_VIEW_POSITION + 1) == START_VIEW_POSITION)
            secondView.text = "${seconds}"
        startAnimation(secondView, START_VIEW_POSITION + 1)


        printCoordinates(thirdView)
        if (getGoingToPosition(START_VIEW_POSITION + 2) == START_VIEW_POSITION)
            thirdView.text = "${seconds}"
        startAnimation(thirdView, START_VIEW_POSITION + 2)


        printCoordinates(fourthView)
        if (getGoingToPosition(START_VIEW_POSITION + 3) == START_VIEW_POSITION)
            fourthView.text = "${seconds}"
        startAnimation(fourthView, START_VIEW_POSITION + 3)


        printCoordinates(fifthView)
        if (getGoingToPosition(START_VIEW_POSITION + 4) == START_VIEW_POSITION)
            fifthView.text = "${seconds}"
        startAnimation(fifthView, START_VIEW_POSITION + 4)

        logInfo(TAG, "=================================================================")

    }

    private fun getYDelta(goingToPosition: Int, viewPosition: Int): Float {
        return (goingToPosition - viewPosition) * context.convertDpToPixel(50F)
    }


    private fun startAnimation(view: View, viewPosition: Int) {
        val goingToPosition = getGoingToPosition(viewPosition)


        val goingToStartPosition = goingToPosition == START_VIEW_POSITION

        val yTranslation = getYDelta(goingToPosition, viewPosition)

        logInfo(TAG, "viewPosition : $viewPosition yDelta : $yDelta goingToPosition : $goingToPosition yTranslation : $yTranslation                              ")

        val objectAnimator = ObjectAnimator.ofFloat(view, "translationY", yTranslation)
        objectAnimator.interpolator = AccelerateDecelerateInterpolator()
        objectAnimator.duration = 1000
        objectAnimator.start()

        if (goingToStartPosition) {
            view.visibility = View.INVISIBLE
            objectAnimator.doOnEnd {
                view.visibility = View.VISIBLE
            }
        }
    }

    private fun getGoingToPosition(viewPosition: Int): Int {
        var goingToPosition = viewPosition + yDelta
        if (goingToPosition >= (NUMBER_OF_VIEWS + START_VIEW_POSITION)) {
            goingToPosition %= (NUMBER_OF_VIEWS + START_VIEW_POSITION)
            goingToPosition += START_VIEW_POSITION
        }
        return goingToPosition
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
            printCoordinates(fifthView)
            logInfo(TAG, "=============")
        }, 1000)
    }

    companion object {
        private const val TAG = "yep"
        private const val NUMBER_OF_VIEWS = 5
    }

}