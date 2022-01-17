package com.test.omdb.utils;

import androidx.annotation.Nullable;

public final class AsynchronousCallback {

    /**
     * Use to call back from a asynchronous repository call, e.g. a load operation.
     * <p>
     * Using the original thread used for operation to invoke the callback methods.
     * <p>
     * The contract is that exactly one method on the callback will be called, exactly once.
     *
     * @param <R> Result type
     * @param <E> Error type
     */
    public interface WorkerThread<R, E> {

        @androidx.annotation.WorkerThread
        void onComplete(@Nullable R result);

        @androidx.annotation.WorkerThread
        void onError(@Nullable E error);
    }
}

