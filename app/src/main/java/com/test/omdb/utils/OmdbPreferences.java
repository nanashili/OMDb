package com.test.omdb.utils;

import android.content.SharedPreferences;
import com.test.omdb.data.dependencies.OmdbDependencies;

public class OmdbPreferences {

    /**
     * Since SavedInstanceState gets destroyed when the app is closed, back pressed or if the system shuts down
     * we will use SharedPreferences to save the state of the application.
     */
    public static void saveState(String activity, String type, String id, String title){
        SharedPreferences mPrefs = OmdbDependencies.getApplication().getSharedPreferences("OMDBApplication", 0);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("lastOpenedActivity", activity);
        editor.putString("search_type", type);
        editor.putString("search_id", id);
        editor.putString("search_title", title);
        editor.apply();
    }

}
