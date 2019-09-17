package com.lei.musicplayer.application;

import android.app.Application;
import android.content.Context;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.service.PlayerService;
import com.lei.musicplayer.util.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/8/29.
 */
public class AppCache {

    private PlayerService mPlayerService;
    private static AppCache mAppCache;
    private static Context mContext;
    private List<Music> localMusicList = new ArrayList<Music>();
    public static List<Music> musicPlayList = new ArrayList<Music>();
    private static int mSystemStatusHeight;
    private static Music mPlayingMusic;
    private static boolean isPlaying = false;


    private AppCache(){}

    private static AppCache getInstance(){
        if (mAppCache == null){
            mAppCache = new AppCache();
        }
        return mAppCache;
    }

    public static void init(Application application){
        getInstance().initialize(application);
    }

    /*
    * 初始化操作
    * */
    private void initialize(Application application){
        mContext = application.getApplicationContext();
    }

    public static PlayerService getPlayService(){
        return getInstance().mPlayerService;
    }

    public static void setPlayService(PlayerService service){
        getInstance().mPlayerService = service;
    }

    public static void setLocalMusicList(List<Music> list){
        getInstance().localMusicList = list;
    }

    public static List<Music> getLocalMusicList(){
        return getInstance().localMusicList;
    }

    public static void setMusicPlaylist(List<Music> list){
        getInstance().musicPlayList = list;
    }

    public static List<Music> getMusicPlaylist(){
        return getInstance().localMusicList;
    }

    public static int getSystemStatusHeight(){
        if (mSystemStatusHeight <= 0){
            mSystemStatusHeight = Util.getStatusBarHeight(mContext);
        }
        return  mSystemStatusHeight;
    }

    public static void setPlayingMusic(Music info){
        mPlayingMusic = info;
    }

    public static Music getPlayingMusic(){
        if (mPlayingMusic == null && getInstance().localMusicList.size() > 0){
            mPlayingMusic = getInstance().localMusicList.get(0);
        }
        return mPlayingMusic;
    }

    public static void setIsPlaying(boolean state){
        isPlaying = state;
    }

    public static boolean isPlaying(){
        return isPlaying;
    }

    /*
    * 清理 activity
    * */
    public static void clearStack() {

    }

}
