package com.test.omdb.utils.concurrent;

import androidx.annotation.NonNull;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadExecutors {

    public static final ExecutorService BOUNDED = Executors.newFixedThreadPool(getIdealThreadCount(), new NumberedThreadFactory("thread-bounded"));

    private ThreadExecutors() {
    }

    /**
     * Returns an 'ideal' thread count based on the number of available processors.
     */
    public static int getIdealThreadCount() {
        return Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4));
    }

    private static class NumberedThreadFactory implements ThreadFactory {

        private final String baseName;
        private final AtomicInteger counter;

        NumberedThreadFactory(@NonNull String baseName) {
            this.baseName = baseName;
            this.counter = new AtomicInteger();
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, baseName + "-" + counter.getAndIncrement());
        }
    }
}
