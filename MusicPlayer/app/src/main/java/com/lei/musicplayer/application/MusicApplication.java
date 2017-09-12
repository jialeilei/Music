package com.lei.musicplayer.application;

import android.app.Application;
import com.lei.musicplayer.http.HttpClient;
import com.lei.musicplayer.util.LogTool;

/**
 * Created by lei on 2017/8/31.
 */
public class MusicApplication extends Application {

    private static final String TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();

        AppCache.init(this);
        LogTool.i(TAG, "before " + System.currentTimeMillis());
        HttpClient.init();
        LogTool.i(TAG, "after " + System.currentTimeMillis());

    }
}
