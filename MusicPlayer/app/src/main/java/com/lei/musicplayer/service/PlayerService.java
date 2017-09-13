package com.lei.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.animation.AnimationUtils;
import com.lei.musicplayer.constant.AppConstant;
import com.lei.musicplayer.R;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.LrcContent;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.constant.MusicType;
import com.lei.musicplayer.fragment.PlayFragment;
import com.lei.musicplayer.util.LogTool;
import com.lei.musicplayer.util.LrcProcess;
import com.lei.musicplayer.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/8/3.
 * play music
 */
public class PlayerService extends Service {
    private static final String TAG = "PlayerService";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //private String path;
    private int play_progress = 0;//the current progress of one music
    public int play_position = 0;//the current position of music in list
    private List<Music> mLocalMusicList = new ArrayList<Music>();
    private int mPlayType = AppConstant.CIRCLE_ALL;
    private OnPlayerServiceListener mPlayerServiceListener;

    //歌词
    LrcProcess lrcProcess = new LrcProcess();
    List<LrcContent> lrcList = new ArrayList<LrcContent>();
    int index = 0;//歌词索引值

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1://更新当前音乐播放位置
                    if(mediaPlayer != null) {
                        play_progress = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
                        mPlayerServiceListener.onMusicCurrentPosition(play_progress);
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
                case 2:
                    break;
            }
        };
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayerBinder() ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogTool.i(TAG, " onStartCommand ");
        if (intent != null && intent.getAction() != null){
            switch (intent.getAction()){
                case AppConstant.ACTION_PLAY_STOP :
                    playOrStop(intent.getIntExtra(AppConstant.MSG_PLAY_POSITION,-1));
                    break;
                case AppConstant.ACTION__CONTROL_PROGRESS://拖动进度条
                    seekBarPlay(intent.getIntExtra(AppConstant.MSG_PROGRESS,0));
                    break;
                case AppConstant.ACTION__NEXT :
                    playNext();
                    break;
                case AppConstant.ACTION__PREVIOUS :
                    playPrevious();
                    break;
                default:
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /*
    * 初始化歌词类
    * */
    private void initLrc(){

        lrcProcess = new LrcProcess();
        //读取歌词文件
        lrcProcess.readLRC(Environment.getExternalStorageDirectory()
                +"/"+mLocalMusicList.get(play_position).getTitle());
        LogTool.i(TAG,"directory: "+ Environment.getExternalStorageDirectory()
                +"/"+mLocalMusicList.get(play_position).getTitle()
                +" uri: " + mLocalMusicList.get(play_position).getUrl());
        //传回处理后的歌词文件
        lrcList = lrcProcess.getLrcList();
        if (lrcList == null || lrcList.size() == 0){return;}
        LogTool.i(TAG,"lrcList.size: "+lrcList.size());
        PlayFragment.lrcView.setmLrcList(lrcList);
        //切换带动画显示歌词
        PlayFragment.lrcView.setAnimation(AnimationUtils.loadAnimation(PlayerService.this, R.anim.lrc_slide_up));
        handler.post(mRunnable);

    }

    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            PlayFragment.lrcView.setIndex(lrcIndex());
            PlayFragment.lrcView.invalidate();
            handler.postDelayed(mRunnable, 100);
        }
    };

    /**
     * 根据时间获取歌词显示的索引值
     * @return
     */
    public int lrcIndex() {
        int currentTime = play_position;
        int duration = mediaPlayer.getDuration();

        if(mediaPlayer.isPlaying()) {
            currentTime = mediaPlayer.getCurrentPosition();
            duration = mediaPlayer.getDuration();
        }
        if(currentTime < duration) {
            for (int i = 0; i < lrcList.size(); i++) {
                if (i < lrcList.size() - 1) {
                    if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
                        index = i;
                    }
                    if (currentTime > lrcList.get(i).getLrcTime()
                            && currentTime < lrcList.get(i + 1).getLrcTime()) {
                        index = i;
                    }
                }
                if (i == lrcList.size() - 1
                        && currentTime > lrcList.get(i).getLrcTime()) {
                    index = i;
                }
            }
        }
        return index;
    }

    private void playOrStop(int getPlayPosition) {
        if (getPlayPosition > -1){//listView列表点击歌曲，直接0进度开始播放歌曲
            play_progress = 0;
            play_position = getPlayPosition;
            play();
        }else{//暂停、开始按钮点击歌曲,继续上次进度播放
            if (mediaPlayer.isPlaying()){//暂停
                stop();
            }else {//继续上次位置播放
                play();
            }
        }
    }

    private void seekBarPlay(int progress) {
        play_progress = progress;
        play();
    }

    public void play(){
        play(mLocalMusicList.get(play_position));
    }
    /*
    * start music from zero
    * */
    public void playStartMusic(Music music){
        play_progress = 0;
        play(music);
    }

    public void play(Music music) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(music.getUrl());
            mediaPlayer.prepare();
            //注册一个监听器
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    if (play_progress > 0) {
                        mediaPlayer.seekTo(play_progress);
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    complete();
                }
            });

            AppCache.setPlayingMusic(music);
            mPlayerServiceListener.onMusicPlay();
            handler.sendEmptyMessage(1);
            initLrc();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void playPrevious() {
        play_progress = 0;
        if (play_position == 0){
            play_position = mLocalMusicList.size() - 1;
        }else {
            play_position --;
        }
        play();
    }

    public void playNext() {
        play_progress = 0;
        if (play_position == mLocalMusicList.size() - 1){
            play_position = 0;
        }else {
            play_position ++;
        }
        play();
    }

    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    private void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mPlayerServiceListener.onMusicStop();
            stopHandlerLoop();
        }
    }

    private void stopHandlerLoop() {
        handler.removeMessages(1);
    }

    private void complete(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mPlayerServiceListener.onMusicComplete();
            stopHandlerLoop();
        }
    }

    public void onDestroy(){
        if (mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        AppCache.setPlayService(null);
        super.onDestroy();
    }


    /*
    * 扫描本地音乐
    * */
    public void scanLocalMusic(ScanCallBack callBack){
        mLocalMusicList = Util.getLocalMusic();
        if (mLocalMusicList != null ){
            AppCache.setLocalMusicList(mLocalMusicList);
            callBack.onSuccess();
        }else {
            callBack.onFail("localMusicList is null");
        }
    }


    public void setOnPlayerListener(OnPlayerServiceListener listener){
        mPlayerServiceListener = listener;
    }

    public class PlayerBinder extends android.os.Binder{
        public PlayerService getService(){
            return PlayerService.this;
        }
    }
    
}
