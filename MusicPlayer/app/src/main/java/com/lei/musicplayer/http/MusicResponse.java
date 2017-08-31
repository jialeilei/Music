package com.lei.musicplayer.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by lei on 2017/8/19.
 */
public abstract class MusicResponse<T> {

    public abstract void success(Call call, T data);

    public abstract void OnFail(Call call,IOException e);

}
