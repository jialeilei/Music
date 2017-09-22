package com.lei.musicplayer.http;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.bean.MusicLink;
import com.lei.musicplayer.bean.OnLineMusicList;
import com.lei.musicplayer.bean.OnlineMusic;
import com.lei.musicplayer.util.LogTool;
import com.lei.musicplayer.util.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lei on 2017/9/10.
 * 网络请求类
 */
public class HttpClient {

    public static final String SPLASH_URL = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
    public static final String BASE_URL_BEFORE = "http://tingapi.ting.baidu.com/";
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
    private static final String TAG = "HttpClient";
    //成都 http://yinyueshiting.baidu.com/data2/music/239908cd71a27d737fef95b17d18b97c/540728460/540728460.mp3?xcode=77de0da9c76a222085429371225e2601


    static Retrofit retrofit;
    static OkHttpClient httpClient = new OkHttpClient();

    private static Retrofit initRetrofit(){
        init();
        return retrofit;
    }

    public static void init() {
        if (retrofit == null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_BEFORE) // 设置网络请求的Url地址
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                    // .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                    .build();
        }
    }

    public static ApiService getApiService(){
        return initRetrofit().create(ApiService.class);
    }

    /*
    * 排行音乐
    * */
    public static void getOnlineMusicList(String type,int size,int offset, final MusicCallBack<OnLineMusicList> callBack){
        getApiService().getOnLineMusicList(type, String.valueOf(size), String.valueOf(offset), METHOD_GET_MUSIC_LIST)
                .enqueue(new Callback<OnLineMusicList>() {
                    @Override
                    public void onResponse(Call<OnLineMusicList> call, Response<OnLineMusicList> response) {
                        if (response.isSuccessful()) {
                            callBack.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<OnLineMusicList> call, Throwable t) {
                        callBack.onFail(t);
                    }
                });
    }

    /*
    * file_duration
    * file_link
    * Transfer-Encoding: chunked
    * */
    public static void getMusicLink(String songId,final MusicCallBack<MusicLink> callBack){
        getApiService().getMusicLink(songId, METHOD_DOWNLOAD_MUSIC).enqueue(new Callback<MusicLink>() {
            @Override
            public void onResponse(Call<MusicLink> call, Response<MusicLink> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<MusicLink> call, Throwable t) {
                callBack.onFail(t);
            }
        });
    }

    public static void download(final Music music, final MusicCallBack callBack){
        Util.ToastShort("正在下载");
        getApiService().download(music.getUrl()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Util.writeMusicToDir(music,response.body().byteStream(),callBack);
                    //callBack.onSuccess("length: " + response.body().contentLength());
                    Util.ToastShort("下载完成");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBack.onFail(t);
            }
        });
    }



}
