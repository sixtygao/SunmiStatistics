package com.sunmi.sunmistatisticslib.utils;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class RetryWhenDelay implements Function<Flowable<Throwable>, Publisher<?>> {

    private long retryDelay;
    private int maxRetryCount;
    private int retryCount;

    public RetryWhenDelay(long retryDelay, int retryCount) {
        this.retryDelay = retryDelay;
        this.maxRetryCount = retryCount;
    }

    @Override
    public Publisher<?> apply(Flowable<Throwable> throwableFlowable) throws Exception {
        return throwableFlowable.flatMap(new Function<Throwable, Publisher<?>>() {
            @Override
            public Publisher<?> apply(Throwable throwable) throws Exception {
                if (retryCount < maxRetryCount) {
                    retryCount++;
                    return Flowable.timer(retryDelay, TimeUnit.SECONDS);
                }
                return Flowable.error(throwable);
            }
        });
    }
}
