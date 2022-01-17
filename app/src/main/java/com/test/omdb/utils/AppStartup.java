package com.test.omdb.utils;

import android.os.Handler;
import android.os.Looper;

import android.util.Log;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import com.test.omdb.utils.concurrent.ThreadExecutors;

import java.util.LinkedList;
import java.util.List;

/**
 * Manages our app startup flow.
 */
public final class AppStartup {

    private static final String TAG = AppStartup.class.getName();
    private static final AppStartup INSTANCE = new AppStartup();

    /**
     * The time to wait after Application#onCreate() to see if any UI rendering starts
     */
    private final long UI_WAIT_TIME = 500;

    /**
     * The maximum amount of time we'll wait for critical rendering events to finish.
     */
    private final List<Task> blocking;
    private final List<Task> nonBlocking;
    private final List<Task> postRender;
    private final Handler postRenderHandler;

    private long applicationStartTime;

    private AppStartup() {
        this.blocking = new LinkedList<>();
        this.nonBlocking = new LinkedList<>();
        this.postRender = new LinkedList<>();
        this.postRenderHandler = new Handler(Looper.getMainLooper());
    }

    public static @NonNull
    AppStartup getInstance() {
        return INSTANCE;
    }

    public void onApplicationCreate() {
        this.applicationStartTime = System.currentTimeMillis();
    }

    /**
     * Schedules a task that must happen during app startup in a blocking fashion.
     */
    @MainThread
    public @NonNull
    AppStartup addBlocking(@NonNull String name, @NonNull Runnable task) {
        blocking.add(new Task(name, task));
        return this;
    }

    /**
     * Begins all pending task execution.
     */
    @MainThread
    public void execute() {
        Stopwatch stopwatch = new Stopwatch("init");

        for (Task task : blocking) {
            task.getRunnable().run();
            stopwatch.split(task.getName());
        }
        blocking.clear();

        for (Task task : nonBlocking) {
            ThreadExecutors.BOUNDED.execute(task.getRunnable());
        }
        nonBlocking.clear();

        stopwatch.split("schedule-non-blocking");
        stopwatch.stop(TAG);

        postRenderHandler.postDelayed(() -> {
            Log.i(TAG, "Assuming the application has started in the background. Running post-render tasks.");
            executePostRender();
        }, UI_WAIT_TIME);
    }

    private void executePostRender() {
        for (Task task : postRender) {
            ThreadExecutors.BOUNDED.execute(task.getRunnable());
        }
        postRender.clear();
    }

    private class Task {
        private final String name;
        private final Runnable runnable;

        protected Task(@NonNull String name, @NonNull Runnable runnable) {
            this.name = name;
            this.runnable = runnable;
        }

        @NonNull
        String getName() {
            return name;
        }

        public @NonNull
        Runnable getRunnable() {
            return runnable;
        }
    }
}
