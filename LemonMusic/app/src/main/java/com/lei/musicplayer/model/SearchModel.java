package com.lei.musicplayer.model;


import com.lei.musicplayer.bean.SearchMusic;
import com.lei.musicplayer.http.GetCallBack;
import com.lei.musicplayer.http.HttpClient;
import java.util.List;


/**
 * Created by Lei on 2018/4/25.
 */

public class SearchModel {


    private Callback mCallback;

    public SearchModel(Callback callback){
        mCallback = callback;
    }


    public void searchMusic(String keyword) {
        HttpClient.getSearchMusic(keyword, new GetCallBack<SearchMusic>() {
            @Override
            public void onSuccess(SearchMusic response) {
                if (response.getSong() != null && response.getSong().size() > 0) {
                    mCallback.searchSuccess(response.getSong());
                } else {
                    mCallback.searchFail("没有搜索到歌曲");
                }
            }

            @Override
            public void onFail(Throwable t) {
                mCallback.searchFail("搜索失败，请检查网络");
            }
        });
    }


    public interface Callback{

        void searchSuccess(List<SearchMusic.Song> songList);

        void searchFail(String str);
    }

}
