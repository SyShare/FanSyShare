package sy.com.initproject.root.basemodel;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.baseproject.architecture.BaseViewModel;
import com.pince.ut.NetUtil;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import sy.com.initproject.root.AppContext;

/**
 * description
 *
 * @author SyShare
 */
public abstract class ApiObserver<T> extends DisposableObserver<T> {

    private boolean toastError;

    public ApiObserver() {
    }

    public ApiObserver(boolean toastError) {
        this.toastError = toastError;
    }

    public ApiObserver(@NonNull BaseViewModel viewModel) {
        this(false, viewModel);
    }

    public ApiObserver(boolean toastError, @NonNull BaseViewModel viewModel) {
        this(toastError);
        viewModel.add(this);
    }

    @Override
    public void onStart() {
        if (!NetUtil.isNetworkAvailable(AppContext.getContext())) {
            dispose();
            onError(new NetworkErrorException());
        }
        if (!isDisposed()) {
            onBefore();
        }
    }

    @Override
    public void onNext(T response) {
        try {
            assertSuccessful(response);
        } catch (Exception e) {
            onError(e);
        }
    }

    @Deprecated
    @Override
    public void onError(Throwable throwable) {
        Log.wtf("ApiObserver", "It is not a crash, just print StackTrace \n" + Log.getStackTraceString(throwable));
        try {
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

    private void assertSuccessful(T response) {
        if (response == null) {
            throw new IllegalArgumentException("response is null");
        }
        onSuccess(response);
    }

    public void onBefore() {
    }

    public abstract void onSuccess(@NonNull T resp);

    public void onFail(Throwable throwable) {
    }
}
