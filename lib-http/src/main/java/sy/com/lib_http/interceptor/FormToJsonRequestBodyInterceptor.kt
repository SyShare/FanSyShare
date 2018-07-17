package com.pince.network.interceptor

import okhttp3.*
import okio.Buffer
import java.nio.charset.Charset


class FormToJsonRequestBodyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //如下可以用来读取body中的内容，并重新设置body，可以用来将项目中所有需要json类型Content-Type的Post请求进行统一处理
        //虽然调用处是@FormUrlEncode的表单类型，这里统一修改为json类型，可以用来替代每个接口都需要单独修改的麻烦
//        builder.put()

        val request: Request = chain.request()

        //重新构建request
        val requestBuilder = request.newBuilder();
        val oldFormBody = request.body();
        if (oldFormBody is FormBody) {
            val newFormBody = FormBody.Builder();
            for (i in 0..oldFormBody.size() - 1) {
                newFormBody.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
            }
            newFormBody.add("sign", "******");
            requestBuilder.method(request.method(), newFormBody.build());
        }
        val newRequest = requestBuilder.build()

        return chain.proceed(newRequest)

    }

    /**
     *  解析出body内容
     */
    fun getRequestBody(request: Request): String {
        val requestBody = request.body()
        val buffer = Buffer()
        requestBody?.writeTo(buffer)
        var charset = Charset.forName("UTF-8")
        val contentType: MediaType = requestBody?.contentType()!!
        if (contentType != null) {
            charset = contentType.charset(charset)
        }
        val paramsStr = buffer.readString(charset)
        return paramsStr
    }
}