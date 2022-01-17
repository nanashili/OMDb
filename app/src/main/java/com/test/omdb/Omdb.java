package com.test.omdb;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.test.omdb.data.dependencies.OmdbDependencies;
import com.test.omdb.data.dependencies.OmdbProvider;
import com.test.omdb.ui.detail.DetailActivity;
import com.test.omdb.utils.AppForegroundObserver;
import com.test.omdb.utils.AppStartup;

public class Omdb extends Application implements AppForegroundObserver.Listener {

    private static final String TAG = Omdb.class.getName();

    Omdb mInstance;
    Context context;

    @Override
    public void onCreate() {
        AppStartup.getInstance().onApplicationCreate();
        super.onCreate();

        mInstance = this;
        context = this;

        AppStartup.getInstance()
                .addBlocking("logging", () -> {
                    Log.i(TAG, "onCreate()");
                })
                .addBlocking("app-dependencies", this::initializeAppDependencies)
                .addBlocking("lifecycle-observer", () -> OmdbDependencies.getAppForegroundObserver().addListener(this))
                .execute();

        handleRouting();
    }

    private void initializeAppDependencies() {
        OmdbDependencies.init(this, new OmdbProvider(this));
    }

    @Override
    public void onForeground() {
        long startTime = System.currentTimeMillis();
        Log.e(TAG, "App is now visible.");
        Log.e(TAG, "onStart() took " + (System.currentTimeMillis() - startTime) + " ms");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "App has been destroyed.");
    }

    @Override
    public void onBackground() {
        Log.i(TAG, "App is no longer visible.");
    }

    private void handleRouting(){
        SharedPreferences mPrefs = getSharedPreferences("OMDBApplication", 0);

        String lastActivity = mPrefs.getString("lastOpenedActivity", "");
        String searchType = mPrefs.getString("search_type", "");
        String searchId = mPrefs.getString("search_id", "");
        String searchTitle = mPrefs.getString("search_title", "");

        if (lastActivity.equals("DetailActivity")){
            Intent intent = new Intent(this, DetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("search_type", searchType);
            intent.putExtra("search_id", searchId);
            intent.putExtra("search_title", searchTitle);
            startActivity(intent);
        }
    }

}
