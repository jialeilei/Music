package com.lei.musicplayer.util;

import okhttp3.internal.Platform;

/**
 * Created by lei on 2018/1/17.
 */

public interface Logger {

    void log(String msg);

    Logger DEFAULT = new Logger() {
        @Override
        public void log(String msg) {
            Platform.get().log(msg);
        }
    };

}
