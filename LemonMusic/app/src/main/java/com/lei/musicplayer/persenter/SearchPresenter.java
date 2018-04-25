package com.lei.musicplayer.persenter;

import android.content.Context;
import com.lei.musicplayer.bean.SearchMusic;
import com.lei.musicplayer.model.SearchModel;
import java.util.List;

/**
 * Created by Lei on 2018/4/25.
 */

public class SearchPresenter implements SearchModel.Callback{

    private SearchModel mSearchModel;
    private Context mContext;
    private Callback mCallback;


    public SearchPresenter(Context context,Callback callback){

        mContext = context;
        mCallback = callback;
        mSearchModel = new SearchModel(this);
    }

    public void searchMusic(String keyword) {
        mSearchModel.searchMusic(keyword);
    }

    @Override
    public void searchSuccess(List<SearchMusic.Song> songList) {
        if (mCallback != null){
            mCallback.searchMusicSuccess(songList);
        }
    }


    @Override
    public void searchFail(String str) {
        if (mCallback != null){
            mCallback.searchMusicFail(str);
        }
    }



    public interface Callback{

        void searchMusicSuccess(List<SearchMusic.Song> songList);

        void searchMusicFail(String str);

    }


}
