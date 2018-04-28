package com.lei.musicplayer.model;


import android.content.Context;
import com.lei.musicplayer.R;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.bean.MusicLink;
import com.lei.musicplayer.bean.SearchMusic;
import com.lei.musicplayer.constant.MusicType;
import com.lei.musicplayer.http.DownloadCallBack;
import com.lei.musicplayer.http.GetCallBack;
import com.lei.musicplayer.http.HttpClient;
import java.util.List;
import static com.lei.musicplayer.application.AppCache.getPlayService;


/**
 * Created by Lei on 2018/4/25.
 */

public class SearchModel {


    private Callback mCallback;
    private Context mContext;


    public SearchModel(Context context, Callback callback){
        mContext = context;
        mCallback = callback;
    }


    public void searchMusic(String keyword) {
        HttpClient.getSearchMusic(keyword, new GetCallBack<SearchMusic>() {
            @Override
            public void onSuccess(SearchMusic response) {
                if (response.getSong() != null && response.getSong().size() > 0) {
                    mCallback.onSearchMusicSuccess(response.getSong());
                } else {
                    mCallback.onSearchMusicFail("没有搜索到歌曲");
                }
            }

            @Override
            public void onFail(Throwable t) {
                mCallback.onSearchMusicFail("搜索失败，请检查网络");
            }
        });
    }

    public void playMusic(final SearchMusic.Song song) {
        HttpClient.getMusicLink(song.getSongid(), new GetCallBack<MusicLink>() {
            @Override
            public void onSuccess(MusicLink response) {
                getPlayService().playStartMusic(song2Music(song,response));
                if (mCallback != null){
                    mCallback.onPlayMusicSuccess(mContext.getResources().getString(R.string.playing_music));
                }
            }

            @Override
            public void onFail(Throwable t) {
                if (mCallback != null){
                    mCallback.onPlayMusicFail("无法获取音乐");
                }
            }
        });
    }

    private Music song2Music(SearchMusic.Song song, MusicLink response) {
        Music music = new Music();
        music.setMusicType(MusicType.online);
        music.setId(Long.parseLong(song.getSongid()));
        music.setUrl(response.getBitrate().getFile_link());
        music.setDuration(response.getBitrate().getFile_duration() * 1000);
        music.setTitle(song.getSongname());
        music.setArtist(song.getArtistname());
        return music;
    }

    public void downloadMusic(final SearchMusic.Song song) {
        HttpClient.getMusicLink(song.getSongid(), new GetCallBack<MusicLink>() {
            @Override
            public void onSuccess(MusicLink response) {
                downloadMusic(song2Music(song,response));
                downloadLrc(song2Music(song,response));
                downloadAlbum();
                if (mCallback != null){
                    mCallback.onDownloadMusicState("正在下载");
                }
            }

            @Override
            public void onFail(Throwable t) {
                if (mCallback != null){
                    mCallback.onDownloadMusicState("无法获取音乐");
                }
            }
        });

    }

    private void downloadLrc(Music music) {
        HttpClient.downloadLrcString(music);
    }

    private void downloadAlbum() {

    }

    private void downloadMusic(Music music) {
        HttpClient.downloadMusic(music, new DownloadCallBack() {
            @Override
            public void onMusicSuccess() {
                if (mCallback != null){
                    mCallback.onDownloadMusicSuccess("下载完成");
                }
            }

            @Override
            public void onMusicFail() {
                if (mCallback != null){
                    mCallback.onDownloadMusicFail("歌曲下载失败");
                }
            }

        });

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
