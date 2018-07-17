package sy.com.lib_http.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RxThreadCallAdapter extends CallAdapter.Factory {

    RxJava2CallAdapterFactory rxFactory = RxJava2CallAdapterFactory.create();
    private Scheduler subscribeScheduler;
    private Scheduler observerScheduler;

    public RxThreadCallAdapter(Scheduler subscribeScheduler, Scheduler observerScheduler) {
        this.subscribeScheduler = subscribeScheduler;
        this.observerScheduler = observerScheduler;
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        CallAdapter callAdapter = rxFactory.get(returnType, annotations, retrofit);
        return callAdapter != null ? new ThreadCallAdapter(callAdapter) : null;
    }

    final class ThreadCallAdapter<R> implements CallAdapter<R, Observable<?>> {
        CallAdapter<R, Observable<?>> delegateAdapter;

        ThreadCallAdapter(CallAdapter<R, Observable<?>> delegateAdapter) {
            this.delegateAdapter = delegateAdapter;
        }

        @Override public Type responseType() {
            return delegateAdapter.responseType();
        }

        @Override
        public Observable<?> adapt(Call<R> call) {
            return delegateAdapter.adapt(call).subscribeOn(subscribeScheduler)
                    .observeOn(observerScheduler);
        }

    }
}
