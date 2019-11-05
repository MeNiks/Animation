package com.niks.animationdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.niks.base.timber.CrashReportingTree
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashReportingTree())

        Fabric.with(this, Crashlytics())

        timerSecondsView.setRemainingTime(30)

        startAnimation.setOnClickListener {
            timerSecondsView.startTime()
        }
    }

    override fun onStop() {
        super.onStop()
        timerSecondsView.stopTime()
    }
}