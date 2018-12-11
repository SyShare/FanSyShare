package com.youlu.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Author by Administrator , Date on 2018/12/5.
 * PS: Not easy to write code, please indicate.
 */
class CacheNetworkInterceptor : Interceptor {

    //Pragma与Cache-Control一样,是兼容HTTP1.0的头部
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
                .newBuilder()
                .removeHeader("Pragma")
                .addHeader("Cache-Control", "max-age=60")
                .build()
    }
}