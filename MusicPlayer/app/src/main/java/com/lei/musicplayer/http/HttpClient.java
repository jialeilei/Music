package com.lei.musicplayer.http;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lei on 2017/9/10.
 */

public class HttpClient {

    public static final String SPLASH_URL = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
    public static final String BASE_URL = "http://tingapi.ting.baidu.com/";
    public static final String BASE_URL_BEHIND = "v1/restserver/ting";
    public static final String METHOD_GET_MUSIC_LIST = "baidu.ting.billboard.billList";
    public static final String METHOD_DOWNLOAD_MUSIC = "baidu.ting.song.play";
    public static final String METHOD_ARTIST_INFO = "baidu.ting.artist.getInfo";
    public static final String METHOD_SEARCH_MUSIC = "baidu.ting.search.catalogSug";
    public static final String METHOD_LRC = "baidu.ting.song.lry";
    public static final String PARAM_METHOD = "method";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_OFFSET = "offset";
    public static final String PARAM_SONG_ID = "songid";
    public static final String PARAM_TING_UID = "tinguid";
    public static final String PARAM_QUERY = "query";

    static Retrofit retrofit;
    static OkHttpClient httpClient = new OkHttpClient();

    public static Retrofit initRetrofit(){
        if (retrofit == null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // 设置网络请求的Url地址
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                    // .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                    .build();
        }

        return retrofit;
    }

    public static ApiService getApiService(){
        return initRetrofit().create(ApiService.class);
    }


}
