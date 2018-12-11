package sy.com.initproject.root.interceptor

import com.afander.logger.LoggerUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Author by Administrator , Date on 2018/12/5.
 * PS: Not easy to write code, please indicate.
 */
class LoggingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val t1 = System.nanoTime()
        LoggerUtil.d(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()))

        val response = chain.proceed(request)

        val t2 = System.nanoTime()
        LoggerUtil.d(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6, response.headers()))


        LoggerUtil.d(response.networkResponse())
        System.out.println()
        LoggerUtil.d(response.cacheResponse())

        return response
    }
}