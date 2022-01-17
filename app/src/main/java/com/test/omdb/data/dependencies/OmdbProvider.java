package com.test.omdb.data.dependencies;

import android.app.Application;
import androidx.annotation.NonNull;
import com.test.omdb.data.network.NetworkServiceManager;
import com.test.omdb.utils.AppForegroundObserver;


/**
 * The OmdbProvider is responsible for initialising the NetworkServiceManager or any other class
 * for that matter which needs to be initialised globally
 */
public class OmdbProvider implements OmdbDependencies.Provider {

    private final Application context;

    public OmdbProvider(@NonNull Application context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NetworkServiceManager networkServiceManager() {
        return new NetworkServiceManager(true, context);
    }

    @NonNull
    @Override
    public AppForegroundObserver provideAppForegroundObserver() {
        return new AppForegroundObserver();
    }
}
