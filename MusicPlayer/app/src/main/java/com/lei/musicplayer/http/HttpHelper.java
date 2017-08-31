package com.lei.musicplayer.http;

import com.lei.musicplayer.bean.SongLinkResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lei on 2017/8/23.
 */
public interface HttpHelper {

    @GET("data/music/links?")
    Call<SongLinkResponse> getSongData(@Query("songIds") int songIds);

    @GET("data/music/links?")
    Call<String> getSongDataString(@Query("songIds") int songIds);



}
