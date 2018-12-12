package sy.com.lib_http.arch;

import android.accounts.NetworkErrorException;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pince.ut.NetUtil;
import com.pince.ut.constans.AbAppData;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import sy.com.lib_http.ApiException;
import sy.com.lib_http.OkHttpWrapper;
import sy.com.lib_http.bean.BaseResponse;
import sy.com.lib_http.util.ApiCodeUtil;

/**
 * description
 *
 * @author SyShare
 */
public abstract class ApiObserver<T> extends DisposableObserver<T> implements LifecycleObserver {

    private boolean toastError;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Lifecycle mLifecycle;

    public ApiObserver() {
    }


    public ApiObserver(boolean toastError) {
        this.toastError = toastError;
    }

    /**
     * used to be MVP
     * @param mLifecycle life
     */
    public ApiObserver(@NonNull Lifecycle mLifecycle) {
        this(false, mLifecycle);
    }

    /**
     *used to br ViewModel
     * @param viewModel AndroidViewModel
     */
    public ApiObserver(@NonNull BaseViewModel viewModel) {
        this(false, viewModel);
    }

    public ApiObserver(boolean toastError, @NonNull BaseViewModel viewModel) {
        this(toastError);
        viewModel.add(this);
    }

    public ApiObserver(boolean toastError, @NonNull Lifecycle mLifecycle) {
        this(toastError);
        this.mLifecycle = mLifecycle;
        mLifecycle.addObserver(this);
        addDisposable();
    }

    @Override
    public void onStart() {
        if (!NetUtil.isNetworkAvailable(OkHttpWrapper.getContext())
                && !ApiCodeUtil.INSTANCE.getEnableCache()) {
            dispose();
            onError(new NetworkErrorException());
        }
        if (!isDisposed()) {
            onBefore();
        }
    }

    private void addDisposable() {
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        disposables.add(this);
    }

    @Override
    public void onNext(T response) {
        try {
            assertSuccessful(response);
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        Log.wtf("ApiObserver", "It is not a crash, just print StackTrace \n" + Log.getStackTraceString(throwable));
        try {
            if (toastError) {
                OkHttpWrapper.toastError(OkHttpWrapper.getContext(), throwable);
            }
            RxJavaPlugins.getErrorHandler().accept(throwable);
        } catch (Exception e) {
            Log.wtf("ApiObserver", "It is not a crash, just print StackTrace \n" + Log.getStackTraceString(e));
        } finally {
            onFail(throwable);
        }
    }

    @Override
    public void onComplete() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDisPose() {
        if (AbAppData.DEBUG) {
            Log.i("HttpCallback", "onDisPose ");
        }

        if (this.mLifecycle != null) {
            this.mLifecycle.removeObserver(this);
        }

        if (disposables != null) {
            disposables.clear();
        }

    }

    private void assertSuccessful(T response) {
        if (response == null) {
            throw new ApiException("response is null");
        }
        if (response instanceof BaseResponse) {
            BaseResponse resp = (BaseResponse) response;

            if (!resp.isSuccessful()) {
                throw new ApiException(resp.getMsg(), resp.getCode());
            }
        }
        onSuccess(response);
    }

    public void onBefore() {
    }

    public abstract void onSuccess(@NonNull T resp);

    public void onFail(Throwable throwable) {
    }
}
