package viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import com.pince.ut.NetUtil
import io.reactivex.observers.DisposableObserver
import io.reactivex.plugins.RxJavaPlugins
import sy.com.initproject.root.AppContext

abstract class BaseObserver<T>(toastError: Boolean) : DisposableObserver<T>() {

    private var toastError: Boolean = false


    init {
        this.toastError = toastError
    }

    public override fun onStart() {
        if (!NetUtil.isNetworkAvailable(AppContext.getContext())) {
            dispose();
            onError(NetworkErrorException())
        }
        if (!isDisposed) {
            onBefore()
        }
    }

    override fun onNext(response: T) {
        try {
            assertSuccessful(response)
        } catch (e: Exception) {
            onError(e)
        }

    }

    override fun onError(throwable: Throwable) {
        Log.wtf("ApiObserver", "It is not a crash, just print StackTrace \n" + Log.getStackTraceString(throwable))
        try {
            RxJavaPlugins.getErrorHandler()?.accept(throwable)
        } catch (e: Exception) {
            Log.wtf("ApiObserver", "It is not a crash, just print StackTrace \n" + Log.getStackTraceString(e))
        } finally {
            onFail(throwable)
        }
    }

    override fun onComplete() {}

    private fun assertSuccessful(response: T?) {
        if (response == null) {
            throw IllegalArgumentException("response is null")
        }
        onSuccess(response)
    }

    fun onBefore() {}

    abstract fun onSuccess(resp: T)

    fun onFail(throwable: Throwable) {}
}