package com.lei.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import com.lei.musicplayer.AppConstant;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.Mp3Info;
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
    private boolean isPause;
    private int play_progress = 0;//播放中乐曲的进度
    private int play_position = 0;//列表中音乐位置
    private List<Mp3Info> mLocalMusicList = new ArrayList<Mp3Info>();
    private int mPlayType = AppConstant.CIRCLE_ALL;
    PlayerServiceListener mPlayerServiceListener;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if(mediaPlayer != null) {
                    play_progress = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
                    Intent intent = new Intent(AppConstant.ACTION_PROGRESS);
                    intent.putExtra(AppConstant.MSG_PROGRESS, play_progress);
                    sendBroadcast(intent);
                    handler.sendEmptyMessageDelayed(1, 1000);
                    Log.i(TAG," handler " + play_progress);
                }

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
        Log.i("PlayerService", " 服务 ");
        if (intent != null && intent.getAction() != null){
            switch (intent.getAction()){
                case AppConstant.ACTION_PLAY_STOP :
                    int shortTimeProgress = intent.getIntExtra(AppConstant.MSG_PROGRESS,0);
                    if (shortTimeProgress > 0){
                        play_progress = shortTimeProgress;
                    }
                    if (mediaPlayer.isPlaying()){
                        stop();
                    }else {
                        play(play_progress,play_position);
                    }

                    break;
                case AppConstant.ACTION__NEXT :
                    stop();
                    playNext();
                    break;
                case AppConstant.ACTION__PREVIOUS :
                    stop();
                    playPrevious();
                    break;
                default:
                    break;
            }

            /*path = intent.getStringExtra("url");
            int msg = intent.getIntExtra("MSG",0);
            if (msg == AppConstant.PlayerState.STATE_PLAY){
                play_progress = intent.getIntExtra(AppConstant.PlayerMsg.MSG_PROGRESS, 0);
                play(play_progress);
                Log.i(TAG, "play_progress: " + play_progress);
            } else if (msg == AppConstant.PlayerState.STATE_PAUSE) {
                pause();
            }
            Log.i("PlayerService", "服务已经启动 msg: " + msg);*/
        }


        return super.onStartCommand(intent, flags, startId);
    }

    private void play(final int progress,final int playPosition) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(mLocalMusicList.get(playPosition).getUrl());
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

                    if (mPlayType == AppConstant.CIRCLE_SINGLE) {
                        Log.i(TAG, "complete single");
                    } else if (mPlayType == AppConstant.CIRCLE_ALL) {
                        Log.i(TAG, "complete all");
                        //stop();
                    }
                    complete();
                    //play(0);
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    Log.i(TAG, "currentPosition: " + mp.getCurrentPosition() + " duration: " + mp.getDuration());
                }
            });

            Intent intent = new Intent(AppConstant.ACTION_STATE);
            intent.putExtra(AppConstant.MSG_STATE, AppConstant.STATE_PLAYING);
            sendBroadcast(intent);
            handler.sendEmptyMessage(1);
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
        play(play_progress, play_position);
    }

    public void playNext() {
        play_progress = 0;
        if (play_position == mLocalMusicList.size() - 1){
            play_position = 0;
        }else {
            play_position ++;
        }
        play(play_progress, play_position);
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
            intent.putExtra(AppConstant.MSG_STATE, AppConstant.STATE_PAUSE);
            intent.putExtra(AppConstant.MSG_PROGRESS, mediaPlayer.getCurrentPosition());
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
            intent.putExtra(AppConstant.MSG_STATE, AppConstant.STATE_PAUSE);
            intent.putExtra(AppConstant.MSG_PROGRESS, 0);
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


    /*
    * 扫描本地音乐
    * */
    public void scanLocalMusic(ScanCallBack callBack){
        mLocalMusicList = getMp3Infos();
        if (mLocalMusicList != null ){
            AppCache.setLocalMusicList(mLocalMusicList);
            callBack.onSuccess();
        }else {
            callBack.onFail("localMusicList is null");
        }

    }

    /*
   * 获取本地音乐
   * */
    public List<Mp3Info> getMp3Infos(){

        List<Mp3Info> infos = new ArrayList<Mp3Info>();
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            //对歌曲长度进行过滤
            if (duration < AppConstant.MUSIC_DURATION){
                continue;
            }
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            //the path of the music
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));

            Mp3Info info = new Mp3Info();
            if (isMusic != 0){
                info.setId(id);
                info.setTitle(title);
                info.setArtist(artist);
                info.setDuration(duration);
                info.setSize(size);
                info.setUrl(url);
                infos.add(info);
            }

        }
        return infos;
    }

    public void setOnPlayerListener(PlayerServiceListener listener){
        mPlayerServiceListener = listener;
    }

    private PlayerServiceListener getOnPlayerListener(){
        return mPlayerServiceListener;
    }

    public class PlayerBinder extends android.os.Binder{
        public PlayerService getService(){
            return PlayerService.this;
        }
    }
    
}
