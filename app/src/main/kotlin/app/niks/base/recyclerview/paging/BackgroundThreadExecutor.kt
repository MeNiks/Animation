package app.niks.base.recyclerview.paging

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class BackgroundThreadExecutor : Executor {
    private val mHandler = Executors.newFixedThreadPool(3)
    override fun execute(command: Runnable?) {
        mHandler.execute(command)
    }
}
