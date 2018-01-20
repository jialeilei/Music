package com.lei.musicplayer.application;


import android.app.Application;
import com.lei.musicplayer.database.DatabaseClient;
import com.lei.musicplayer.http.HttpClient;
import com.lei.musicplayer.util.ToastTool;
import com.lei.musicplayer.util.Util;

/**
 * Created by lei on 2017/8/31.
 */
public class MusicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppCache.init(this);
        Util.init(this);
        HttpClient.init();
        DatabaseClient.init(this);
        ToastTool.init(this);

    }
}
