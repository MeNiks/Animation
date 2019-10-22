package app.niks.base.retrofit

import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("User-Agent", System.getProperty("http.agent") ?: "Android")
            .build()
        return chain.proceed(request)
    }
}
