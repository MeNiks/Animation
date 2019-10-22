package app.niks.base.userdata

import android.annotation.SuppressLint
import android.content.Context
import app.niks.base.image.ImageLoader
import app.niks.base.retrofit.HeaderInterceptor
import app.niks.base.retrofit.NetworkCheckInterceptor
import app.niks.base.retrofit.RetryInterceptor
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.niks.animationdemo.BuildConfig
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("StaticFieldLeak")
object Creator {
    lateinit var context: Context

    val gson: Gson by lazy {
        val gsonBuilder = GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
        gsonBuilder.create()
    }

    val retrofit: Retrofit by lazy {

        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(NetworkCheckInterceptor())
        okHttpClientBuilder.addInterceptor(HeaderInterceptor())
        okHttpClientBuilder.addInterceptor(RetryInterceptor())

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        okHttpClientBuilder.connectTimeout(15, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)

        Retrofit.Builder()
            .baseUrl(Central.BASE_URL)
            .callFactory(okHttpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    val glideRequestManager: RequestManager by lazy {
        Glide.with(context)
    }

    val imageLoader: ImageLoader by lazy {
        ImageLoader()
    }
    // User Defined
}
