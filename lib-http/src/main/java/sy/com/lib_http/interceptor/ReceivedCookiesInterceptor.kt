package com.pince.network.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import android.content.SharedPreferences
import com.pince.network.SP_COOKIE_FILE_NAME
import android.R.id.edit
import com.pince.network.SP_COOKIE_KEY


class ReceivedCookiesInterceptor(context: Context): Interceptor {

    val mContext: Context
    var sp: SharedPreferences

    init {
        mContext = context;
        sp = mContext.getSharedPreferences(SP_COOKIE_FILE_NAME, Context.MODE_PRIVATE)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        if (!originalResponse.headers("set-cookie").isEmpty()) {
            val cookieBuffer = StringBuffer()
            cookieBuffer.append(originalResponse.headers("set-cookie").get(0)).append(";");
            val editor = sp.edit()
            editor.putString(SP_COOKIE_KEY, cookieBuffer.toString())
            editor.commit();
        }
        return originalResponse
    }
}