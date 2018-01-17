package com.lei.musicplayer.http;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lei on 2018/1/17.
 */

public class HttpInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Request request = builder.addHeader("User-Agent","Firefox/25.0").build();
        return chain.proceed(request);
    }

}
