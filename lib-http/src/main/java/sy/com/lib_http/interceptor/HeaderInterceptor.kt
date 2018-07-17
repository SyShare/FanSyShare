package com.pince.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor(): Interceptor {
    var mHeadParams: Map<String, String> = HashMap<String, String>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = if (null == mHeadParams || mHeadParams.size == 0) chain.request() else addHeader(chain.request(), mHeadParams)
        return chain.proceed(request)
    }

    private fun addHeader(original: Request, headerParams: Map<String, String>): Request {
        val builder = original.newBuilder()
        val items = headerParams.entries
        val iterator = items.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next() as java.util.Map.Entry<String, String>
            builder.header(item.key, item.value)
        }
        return builder.build()
    }
}