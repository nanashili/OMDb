package com.test.omdb.data.dependencies;

import android.app.Application;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import com.test.omdb.data.network.NetworkServiceManager;
import com.test.omdb.utils.AppForegroundObserver;

/**
 * We will use this class to check if the NetworkServiceManager has been initialised from the provider
 * if not we re-init it till it's officially initialised!
 */

public class OmdbDependencies {

    private static final Object LOCK = new Object();

    private static Application application;
    private static Provider provider;
    private static AppForegroundObserver appForegroundObserver;

    private static volatile NetworkServiceManager networkServiceManager;

    @MainThread
    public static void init(@NonNull Application application, @NonNull Provider provider) {
        synchronized (LOCK) {
            if (OmdbDependencies.application != null || OmdbDependencies.provider != null) {
                throw new IllegalStateException("Already initialized!");
            }

            OmdbDependencies.application = application;
            OmdbDependencies.provider = provider;
            OmdbDependencies.appForegroundObserver = provider.provideAppForegroundObserver();

            OmdbDependencies.appForegroundObserver.begin();
        }
    }

    public static @NonNull
    Application getApplication() {
        return application;
    }

    public static @NonNull NetworkServiceManager getNetworkServiceManager() {
        NetworkServiceManager local = networkServiceManager;

        if (local != null) {
            return local;
        }

        synchronized (LOCK) {
            if (networkServiceManager == null) {
                networkServiceManager = provider.networkServiceManager();
            }
            return networkServiceManager;
        }
    }

    public static @NonNull
    AppForegroundObserver getAppForegroundObserver() {
        return appForegroundObserver;
    }

    public interface Provider {
        @NonNull
        NetworkServiceManager networkServiceManager();

        @NonNull
        AppForegroundObserver provideAppForegroundObserver();
    }
}
