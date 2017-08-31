package com.lei.musicplayer.application;

import android.app.Application;
import android.content.Context;
import com.lei.musicplayer.bean.Mp3Info;
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
    private List<Mp3Info> localMusicList = new ArrayList<Mp3Info>();
    private static int mSystemStatusHeight;
    private static int mMusicPlayPosition;


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

    public static void setLocalMusicList(List<Mp3Info> list){
        getInstance().localMusicList = list;
    }

    public static List<Mp3Info> getLocalMusicList(){
        return getInstance().localMusicList;
    }

    public static int getSystemStatusHeight(){
        if (mSystemStatusHeight <= 0){
            mSystemStatusHeight = Util.getStatusBarHeight(mContext);
        }
        return  mSystemStatusHeight;
    }

    public static void setPlayingPosition(int position){
        mMusicPlayPosition = position;
    }

    public static Mp3Info getPlayingMp3Info(){
        return getInstance().localMusicList.get(mMusicPlayPosition);
    }


}
