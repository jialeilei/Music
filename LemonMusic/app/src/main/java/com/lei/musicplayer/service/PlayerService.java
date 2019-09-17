package com.lei.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.lei.musicplayer.constant.AppConstant;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.database.DatabaseClient;
import com.lei.musicplayer.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/8/3.
 * play music
 */
public class PlayerService extends Service implements IPlayerService{
    private static final String TAG = "PlayerService";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //private String path;
    private int play_progress = 0;//the current progress of one music
    public int play_position = 0;//the current position of music in list
    private List<Music> musicList = new ArrayList<Music>();
    private OnPlayMusicListener mPlayerServiceListener;


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1://更新当前音乐播放位置
                    if(mediaPlayer != null) {
                        play_progress = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
                        mPlayerServiceListener.onMusicCurrentPosition(play_progress);
                        handler.sendEmptyMessageDelayed(1, 500);
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
        if (intent != null && intent.getAction() != null){
            switch (intent.getAction()){
                case AppConstant.ACTION_PLAY_STOP :
                    playOrStop();
                    break;
                case AppConstant.ACTION__CONTROL_PROGRESS://拖动进度条
                    seekBarPlay(intent.getIntExtra(AppConstant.MSG_PROGRESS,0));
                    break;
                case AppConstant.ACTION__NEXT :
                    onPlayNext();
                    break;
                case AppConstant.ACTION__PREVIOUS :
                    onPlayPrev();
                    break;
                default:
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    public void playOrStop() {
        if (mediaPlayer.isPlaying()){//暂停
            stop();
        }else {//继续上次位置播放
            onPlay();
        }
    }

    public void seekBarPlay(int progress) {//调节进度播放
        stop();
        play_progress = progress;
        onPlay();
    }

    /*
    * 列表点击进入
    * */
    public void playStartMusic(Music music){
        play_progress = 0;
        play(music);
    }
    /*
    * 播放列表点击进入，方便循环播放
    * */
    public void play(List<Music> list,int position){
        play_position = position;
        play_progress = 0;
        musicList = list;
        play(musicList.get(position));
    }

    private void play(Music music) {

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

            AppCache.setIsPlaying(true);
            AppCache.setPlayingMusic(music);
            mPlayerServiceListener.onMusicPlay();
            DatabaseClient.addMusic(music);
            handler.sendEmptyMessage(1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void stopHandlerLoop() {
        handler.removeMessages(1);
    }


    @Override
    public void onPlay() {
        play(musicList.get(play_position));
    }

    @Override
    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            AppCache.setIsPlaying(false);
            mPlayerServiceListener.onMusicStop();
            stopHandlerLoop();
        }
    }

    @Override
    public void onPlayNext() {
        play_progress = 0;
        if (musicList == null){
            musicList = Util.getLocalMusic();
        }
        if (play_position == musicList.size() - 1){
            play_position = 0;
        }else {
            play_position ++;
        }
        onPlay();
    }

    @Override
    public void onPlayPrev() {

        play_progress = 0;
        if (play_position == 0){
            play_position = musicList.size() - 1;
        }else {
            play_position --;
        }
        onPlay();
    }

    private void complete(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mPlayerServiceListener.onMusicComplete();
            stopHandlerLoop();
            onPlayNext();
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
        musicList = Util.getLocalMusic();
        if (musicList != null ){
            AppCache.setLocalMusicList(musicList);
            callBack.onFinish();
        }else {
            callBack.onFail("localMusicList is null");
        }
    }

    public void setOnPlayerListener(OnPlayMusicListener listener){
        mPlayerServiceListener = listener;
    }

    public class PlayerBinder extends android.os.Binder{
        public IPlayerService getService(){
            return PlayerService.this;
        }
    }
    
}
