package com.pince.network.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import android.content.SharedPreferences
import com.pince.network.SP_COOKIE_FILE_NAME
import com.pince.network.SP_COOKIE_KEY
import io.reactivex.Observable
import okio.Buffer
import okio.BufferedSink
import org.reactivestreams.Subscriber


class AddCookiesInterceptor(context: Context) : Interceptor {
    val mContext: Context

    init {
        mContext = context
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val sp = mContext.getSharedPreferences(SP_COOKIE_FILE_NAME, Context.MODE_PRIVATE)
        builder.addHeader("cookie", sp.getString(SP_COOKIE_KEY, ""));
        return chain.proceed(builder.build());
    }
}