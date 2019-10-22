package app.niks.base.retrofit

import app.niks.base.extension.connectivityManager
import app.niks.base.userdata.Creator
import okhttp3.Interceptor
import okhttp3.Response

class NetworkCheckInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (checkInternetConnection()) {
            chain.proceed(chain.request())
        } else {
            throw NoInternetException()
        }
    }

    private fun checkInternetConnection(): Boolean {
        val cs = Creator.context.connectivityManager
        return cs.activeNetworkInfo != null
    }
}

class NoInternetException : RuntimeException()
