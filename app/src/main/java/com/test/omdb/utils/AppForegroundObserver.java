package com.test.omdb.utils;

import androidx.annotation.AnyThread;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * A wrapper around {@link ProcessLifecycleOwner} that allows for safely adding/removing observers
 * on multiple threads.
 */
public final class AppForegroundObserver {

    private final Set<Listener> listeners = new CopyOnWriteArraySet<>();

    private volatile Boolean isForegrounded = null;
    private volatile Boolean isDestroyed = null;

    @MainThread
    public void begin() {
        ThreadUtil.assertMainThread();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onStart(@NonNull LifecycleOwner owner) {
                onForeground();
            }

            @Override
            public void onStop(@NonNull LifecycleOwner owner) {
                onBackground();
            }

            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                AppForegroundObserver.this.onDestroy();
            }
        });
    }

    /**
     * Adds a listener to be notified of when the app moves between the background and the foreground.
     * To mimic the behavior of subscribing to {@link ProcessLifecycleOwner}, this listener will be
     * immediately notified of the foreground state if we've experienced a foreground/background event
     * already.
     */
    @AnyThread
    public void addListener(@NonNull Listener listener) {
        listeners.add(listener);

        if (isForegrounded != null) {
            if (isForegrounded) {
                listener.onForeground();
            } else if (isDestroyed){
                listener.onDestroy();
            } else {
                listener.onBackground();
            }
        }
    }

    @AnyThread
    public void removeListener(@NonNull Listener listener) {
        listeners.remove(listener);
    }

    public boolean isForegrounded() {
        return isForegrounded != null && isForegrounded;
    }

    @MainThread
    private void onForeground() {
        isDestroyed = false;
        isForegrounded = true;

        for (Listener listener : listeners) {
            listener.onForeground();
        }
    }

    @MainThread
    private void onBackground() {
        isDestroyed = false;
        isForegrounded = false;

        for (Listener listener : listeners) {
            listener.onBackground();
        }
    }

    @MainThread
    private void onDestroy(){
        isDestroyed = true;
        isForegrounded = false;

        for (Listener listener : listeners){
            listener.onDestroy();
        }

    }

    public interface Listener {
        void onForeground();

        void onBackground();

        void onDestroy();

    }
}
