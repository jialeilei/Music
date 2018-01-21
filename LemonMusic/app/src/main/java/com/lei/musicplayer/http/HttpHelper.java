package com.lei.musicplayer.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lei on 2018/1/21.
 */
public abstract class HttpHelper {

    static Retrofit retrofit;
    static OkHttpClient httpClient;

    private static Retrofit initRetrofit(){
        if (retrofit == null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient = new OkHttpClient.Builder()
                    //.addInterceptor(logging)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request.Builder builder = chain.request().newBuilder();
                            builder.addHeader("User-Agent","Firefox/25.0");
                            return chain.proceed(builder.build());
                        }
                    })
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.BASE_URL) // 设置网络请求的Url地址
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                    // .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                    .build();
        }
        return retrofit;
    }

    protected static ApiService getApiService(){
        return initRetrofit().create(ApiService.class);
    }
}
