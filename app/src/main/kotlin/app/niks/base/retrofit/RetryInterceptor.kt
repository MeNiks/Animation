package app.niks.base.retrofit

import app.niks.base.userdata.Central
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

class RetryInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // try the request
        var response = doRequest(chain, request)
        var tryCount = 0
        while (response == null && tryCount <= RETRY_COUNT) {
            var url = request.url().toString()
            Timber.tag(Central.LOG_TAG).i("Retry %s", url)
            val newRequest = request.newBuilder().url(url).build()
            tryCount++
            // retry the request
            response = doRequest(chain, newRequest)
        }
        if (response == null) { // important ,should throw an exception here
            throw IOException()
        }
        return response
    }

    private fun doRequest(chain: Interceptor.Chain, request: Request): Response? {
        var response: Response? = null
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
        }

        return response
    }

    companion object {
        const val RETRY_COUNT = 3
    }
}
