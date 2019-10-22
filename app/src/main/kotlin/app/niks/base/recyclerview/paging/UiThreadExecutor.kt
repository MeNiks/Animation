package app.niks.base.recyclerview.paging

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

class UiThreadExecutor : Executor {
    private val mHandler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable?) {
        mHandler.post(command)
    }
}
