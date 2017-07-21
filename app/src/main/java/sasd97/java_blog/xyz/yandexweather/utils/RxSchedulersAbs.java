package sasd97.java_blog.xyz.yandexweather.utils;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.SingleTransformer;

/**
 * Created by alexander on 07/07/2017.
 */

public abstract class RxSchedulersAbs {

    abstract public Scheduler getMainThreadScheduler();
    abstract public Scheduler getIOScheduler();
    abstract public Scheduler getComputationScheduler();

    public <T> ObservableTransformer<T, T> getIOToMainTransformer()  {
        return objectObservable -> objectObservable
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler());
    }

    public <T> SingleTransformer<T, T> getIOToMainTransformerSingle()  {
        return objectObservable -> objectObservable
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler());
    }

    public <T> ObservableTransformer<T, T> getComputationToMainTransformer()  {
        return objectObservable -> objectObservable
                .subscribeOn(getComputationScheduler())
                .observeOn(getMainThreadScheduler());
    }

    public <T> SingleTransformer<T, T> getComputationToMainTransformerSingle()  {
        return objectObservable -> objectObservable
                .subscribeOn(getComputationScheduler())
                .observeOn(getMainThreadScheduler());
    }

}