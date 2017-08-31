package com.lei.musicplayer.application;

import android.app.Application;

/**
 * Created by lei on 2017/8/31.
 */
public class MusicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppCache.init(this);
    }
}
