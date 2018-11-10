package com.tjohnn.baking;

import android.app.Application;

import com.tjohnn.baking.util.TimberTree;

import timber.log.Timber;

public class App extends Application {


    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        if (BuildConfig.DEBUG)
            Timber.plant(new TimberTree());
    }

    public static App getInstance(){
        return app;
    }
}
