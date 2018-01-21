package com.lei.musicplayer.http;

import com.lei.musicplayer.bean.Lrc;
import com.lei.musicplayer.bean.MusicLink;
import com.lei.musicplayer.bean.OnLineMusicList;
import com.lei.musicplayer.bean.SearchMusic;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by lei on 2017/8/23.
 */
public interface ApiService {

    String BASE_URL = "http://tingapi.ting.baidu.com/";
    String BASE_URL_BEHIND = "v1/restserver/ting";

    @GET(BASE_URL_BEHIND)
    Call<OnLineMusicList> getOnLineMusicList(@Query("type")String type, @Query("size") String size,
                                              @Query("offset") String offset, @Query("method") String method);

    @GET(BASE_URL_BEHIND)
    Call<MusicLink> getMusicLink(@Query("songid")String songId, @Query("method") String method);

    @GET
    Call<ResponseBody> download(@Url String url);

    @GET(BASE_URL_BEHIND)
    Call<SearchMusic> getSearchMusic(@Query("method") String method, @Query("query")String keyword);

    @GET(BASE_URL_BEHIND)
    Call<Lrc> getLrc(@Query("method") String method, @Query("songid")String id);

}
