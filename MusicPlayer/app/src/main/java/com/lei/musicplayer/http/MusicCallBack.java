package com.lei.musicplayer.http;

import android.support.annotation.Nullable;

/**
 * Created by lei on 2017/9/12.
 */
public interface MusicCallBack<T> {

    void onSuccess(T response);

    void onFail(@Nullable Throwable t);

}
