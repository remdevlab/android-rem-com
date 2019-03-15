package org.remdev.android.kotlinapp.rest

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.remdev.timlog.LogFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

val DEFAULT_TIMEOUT = 30000L;

val GSON = createGson();

val log = LogFactory.create(WeatherAPI::class.java);

fun <T> buildRestApi(endpoint : String, cls : Class<T>) : T {
    val clientBuilder = getUnsafeOkHttpClientBuilder()

    clientBuilder.addInterceptor(getHttpLoggingInterceptor())
    val client = clientBuilder
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()

    val retrofit = Retrofit.Builder()
            .baseUrl(endpoint)
            .addConverterFactory(getGsonFactory())
            .client(client)
            .build()

    return retrofit.create(cls)
}

private fun getUnsafeOkHttpClientBuilder(): OkHttpClient.Builder {
    try {
        val builder = OkHttpClient.Builder()
        return builder
    } catch (e: Exception) {
        log.e(e, "An error occurred when building 'trust-everything' client")
        throw RuntimeException(e)
    }
}

private fun getGsonFactory(): GsonConverterFactory {
    return GsonConverterFactory.create(createGson())
}

private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> log.d(message) })
    interceptor.level = HttpLoggingInterceptor.Level.BASIC
    return interceptor
}

fun createGson() : Gson {
    val gsonBuilder = GsonBuilder()
    return gsonBuilder.setLenient().create();
}

fun <T> fromJson(source: String, target: Class<T>): T {
    return GSON.fromJson(source, target)
}

inline fun <reified T> fromJsonToList(source: String?): List<T> {
    val items = GSON.fromJson(source, T::class.java)
    return items?.let { listOf(it) } ?: emptyList<T>()
}

fun <T> toJson(source: T): String {
    return GSON.toJson(source)
}

inline fun <reified T> View.findView(@IdRes viewId : Int) : T {
    return findViewById<View>(viewId) as T
}

inline fun <reified T> Activity.findView(@IdRes viewId : Int) : T {
    return findViewById<View>(viewId) as T
}

fun Context.toast(@StringRes mgsId : Int) {
    Toast.makeText(this, mgsId, Toast.LENGTH_SHORT).show()
}

fun Context.toast(mgsId : String) {
    Toast.makeText(this, mgsId, Toast.LENGTH_SHORT).show()
}

fun Context.startService(cls : Class<out Service>) {
    startService(Intent(this, cls))
}