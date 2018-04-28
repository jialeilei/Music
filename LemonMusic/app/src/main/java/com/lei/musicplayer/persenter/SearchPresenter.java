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
        mSearchModel = new SearchModel(mContext,this);
    }

    public void searchMusic(String keyword) {
        mSearchModel.searchMusic(keyword);
    }

    public void playMusic(SearchMusic.Song song){
        mSearchModel.playMusic(song);
    }

    public void downloadMusic(SearchMusic.Song song) {
        mSearchModel.downloadMusic(song);
    }


    @Override
    public void onSearchMusicSuccess(List<SearchMusic.Song> songList) {
        if (mCallback != null){
            mCallback.onSearchMusicSuccess(songList);
        }
    }

    @Override
    public void onSearchMusicFail(String str) {
        if (mCallback != null){
            mCallback.onSearchMusicFail(str);
        }
    }

    @Override
    public void onPlayMusicSuccess(String msg) {
        if (mCallback != null){
            mCallback.onPlayMusicSuccess(msg);
        }
    }

    @Override
    public void onPlayMusicFail(String msg) {
        if (mCallback != null){
            mCallback.onPlayMusicFail(msg);
        }
    }

    @Override
    public void onDownloadMusicState(String msg) {
        if (mCallback != null){
            mCallback.onDownloadMusicState(msg);
        }
    }

    @Override
    public void onDownloadMusicSuccess(String msg) {
        if (mCallback != null){
            mCallback.onDownloadMusicSuccess(msg);
        }
    }

    @Override
    public void onDownloadMusicFail(String msg) {
        if (mCallback != null){
            mCallback.onDownloadMusicFail(msg);
        }
    }

    public void unbind() {

        if (mCallback != null){
            mCallback = null;
        }

        if (mSearchModel != null){
            mSearchModel = null;
        }

        if (mContext != null){
            mContext = null;
        }

    }

    public interface Callback{

        void onSearchMusicSuccess(List<SearchMusic.Song> songList);

        void onSearchMusicFail(String str);

        void onPlayMusicSuccess(String msg);

        void onPlayMusicFail(String msg);

        void onDownloadMusicState(String msg);

        void onDownloadMusicSuccess(String msg);

        void onDownloadMusicFail(String msg);

    }


}
