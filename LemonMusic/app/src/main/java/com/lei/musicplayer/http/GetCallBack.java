package com.lei.musicplayer.http;

/**
 * Created by lei on 2017/9/12.
 */
public interface GetCallBack<T> {

    void onSuccess(T response);

    void onFail(Throwable t);

}
