package com.lei.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.lei.musicplayer.AppConstant;
import com.lei.musicplayer.util.Util;
import java.io.IOException;

/**
 * Created by lei on 2017/8/3.
 * play music
 */
public class PlayerService extends Service {
    private static final String TAG = "PlayerService";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String path;
    private boolean isPause;
    private int play_progress = 0;

    enum PlayMode { ALL_CIRCLE ,SINGLE_CIRCLE, COMMON }
    private PlayMode mPlayMode = PlayMode.COMMON;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if(mediaPlayer != null) {
                    play_progress = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
                    Intent intent = new Intent(AppConstant.ACTION_PROGRESS);
                    intent.putExtra(AppConstant.PlayerMsg.MSG_PROGRESS, play_progress);
                    sendBroadcast(intent);
                    Log.i(TAG," send over ");
                    handler.sendEmptyMessageDelayed(1, 1000);
                    Log.i(TAG," handler " + play_progress);
                }

            }
        };
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer.isPlaying()){
            stop();
            
        }
        path = intent.getStringExtra("url");
        int msg = intent.getIntExtra("MSG",0);
        if (msg == AppConstant.PlayerState.STATE_PLAY){
            play_progress = intent.getIntExtra(AppConstant.PlayerMsg.MSG_PROGRESS, 0);
            play(play_progress);
            Log.i(TAG, "play_progress: " + play_progress);
        } else if (msg == AppConstant.PlayerState.STATE_PAUSE) {
            pause();
        }

        Log.i("PlayerService", "服务已经启动 msg: " + msg);
        return super.onStartCommand(intent, flags, startId);
    }


    private void play(final int progress) {

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            //注册一个监听器
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    if (progress > 0) {
                        mediaPlayer.seekTo(progress);
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    if (mPlayMode == PlayMode.SINGLE_CIRCLE){
                        Log.i(TAG, "complete single" + PlayMode.SINGLE_CIRCLE);
                    }else if (mPlayMode == PlayMode.ALL_CIRCLE){
                        Log.i(TAG, "complete all" + PlayMode.ALL_CIRCLE);
                    }else {
                        complete();
                    }

                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    Log.i(TAG, "currentPosition: " + mp.getCurrentPosition()+" duration: "+mp.getDuration());
                }
            });

            Intent intent = new Intent(AppConstant.ACTION_STATE);
            intent.putExtra("state", AppConstant.PlayerState.STATE_PLAY);
            sendBroadcast(intent);
            handler.sendEmptyMessage(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPause = true;
        }
    }

    private void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            Intent intent = new Intent(AppConstant.ACTION_STATE);
            intent.putExtra("state", AppConstant.PlayerState.STATE_STOP);
            intent.putExtra(AppConstant.PlayerMsg.MSG_PROGRESS, mediaPlayer.getCurrentPosition());
            sendBroadcast(intent);
            Log.i(TAG, "stop: " + Util.formatTime(mediaPlayer.getCurrentPosition())
                    + " duration: " + Util.formatTime(mediaPlayer.getDuration()));
            stopHandlerLoop();
//            try {
//                mediaPlayer.prepare();//在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void stopHandlerLoop() {
        handler.removeMessages(1);
    }

    private void complete(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            Intent intent = new Intent(AppConstant.ACTION_STATE);
            intent.putExtra("state", AppConstant.PlayerState.STATE_STOP);
            intent.putExtra(AppConstant.PlayerMsg.MSG_PROGRESS, 0);
            sendBroadcast(intent);
            stopHandlerLoop();
        }
    }


    public void onDestroy(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
    
}
