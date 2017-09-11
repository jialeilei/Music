package com.lei.musicplayer.http;

import com.lei.musicplayer.bean.OnLineMusicList;
import com.lei.musicplayer.bean.SongLinkResponse;

import java.lang.annotation.Target;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lei on 2017/8/23.
 */
public interface ApiService {

    @GET("data/music/links?")
    Call<SongLinkResponse> getSongData(@Query("songIds") int songIds);

    @GET("data/music/links?")
    Call<String> getSongDataString(@Query("songIds") int songIds);

    @GET("v1/restserver/ting")
    @Headers("User-Agent:Firefox/25.0")
    Call<OnLineMusicList> getOnLineMusicList(@Query("type")String type,
                                              @Query("size") String size,
                                              @Query("offset") String offset,
                                              @Query("method") String method);



}
