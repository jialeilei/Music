package com.lei.musicplayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lei.musicplayer.AppConstant;
import com.lei.musicplayer.adapter.ListViewAdapter;
import com.lei.musicplayer.bean.Mp3Info;
import com.lei.musicplayer.R;
import com.lei.musicplayer.service.PlayerService;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    ListView mListView;
    ListViewAdapter mAdapter;
    List<Mp3Info> listMp3;
    private int play_position = 0;
    private int play_progress = 0;
    private boolean isStop = false;
    //view
    ImageButton img_previous, img_next, img_play;
    TextView tvMusicName,tvMusicAuthor,tvMusicDuration;
    ProgressBar mProgressBarCurrent;
    SeekBar mSeekBarCurrent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        listMp3 = getMp3Infos();
        mListView = (ListView) findViewById(R.id.lvMusic);
        mAdapter = new ListViewAdapter(this,listMp3,R.layout.music_list_item);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new MusicListItemClickListener());

        img_previous = (ImageButton) findViewById(R.id.img_previous);
        img_previous.setOnClickListener(this);
        img_play = (ImageButton) findViewById(R.id.img_play);
        img_play.setOnClickListener(this);
        img_next = (ImageButton) findViewById(R.id.img_next);
        img_next.setOnClickListener(this);
        tvMusicName = (TextView) findViewById(R.id.tv_music_name);
        tvMusicAuthor = (TextView) findViewById(R.id.tv_music_author);
        tvMusicDuration = (TextView) findViewById(R.id.tv_music_duration);
        mProgressBarCurrent = (ProgressBar) findViewById(R.id.progress_bar_current);
        mSeekBarCurrent = (SeekBar) findViewById(R.id.seek_bar_current);
        mSeekBarCurrent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                play_progress = progress * (int) listMp3.get(play_position).getDuration() /100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                sendPlayInfo(AppConstant.PlayerState.STATE_PLAY);
                // int progress = play_progress * 100 / (int) listMp3.get(play_position).getDuration()

            }
        });

        mainReceiver = new MainReceiver();
        // 创建IntentFilter
        IntentFilter filter = new IntentFilter();
        // 指定BroadcastReceiver监听的Action
        filter.addAction(AppConstant.ACTION_STATE);
        filter.addAction(AppConstant.ACTION_PROGRESS);
        // 注册BroadcastReceiver
        registerReceiver(mainReceiver, filter);
    }

    private MainReceiver mainReceiver;


    public class MainReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(AppConstant.ACTION_STATE)){

                setMusicInfo();
                int state = intent.getIntExtra("state",0);
                if (state == AppConstant.PlayerState.STATE_PLAY){
                    img_play.setImageResource(R.mipmap.player_playing);
                    isStop = false;
                }else {
                    img_play.setImageResource(R.mipmap.player_stop);
                    play_progress = intent.getIntExtra(AppConstant.PlayerMsg.MSG_PROGRESS,0);
                    isStop = true;
                }
            }else if (action.equals(AppConstant.ACTION_PROGRESS)){

                play_progress = intent.getIntExtra(AppConstant.PlayerMsg.MSG_PROGRESS,0);
                int progress = play_progress * 100 / (int) listMp3.get(play_position).getDuration();
                mProgressBarCurrent.setProgress(progress);
                mSeekBarCurrent.setProgress(progress);
                Log.i(TAG,"progress " + progress +
                        " duration: "+ ((int) listMp3.get(play_position).getDuration())
                        +" play_progress: "+play_progress);
            }

        }
    }

    private class MusicListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            play_position = position;
            play_progress = 0;
            sendPlayInfo(AppConstant.PlayerState.STATE_PLAY);
        }
    }
    /*
    * 启动服务播放音乐
    * */
    private void sendPlayInfo(int msg){
        if (listMp3 != null ){
            Mp3Info info = listMp3.get(play_position);
            Intent intent = new Intent();
            intent.putExtra("url",info.getUrl());
            intent.putExtra("MSG", msg);
            intent.putExtra(AppConstant.PlayerMsg.MSG_PROGRESS, play_progress);
            intent.setClass(MainActivity.this, PlayerService.class);
            startService(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_next:
                if (listMp3.size() - 1  > play_position){
                    play_position ++;
                }else if (listMp3.size() - 1  == play_position){
                    play_position = 0;
                }
                play_progress = 0;
                sendPlayInfo(AppConstant.PlayerState.STATE_PLAY);

                break;
            case R.id.img_play:

                Log.i(TAG,"stop: " + isStop);
                if (isStop){
                    sendPlayInfo(AppConstant.PlayerState.STATE_PLAY);
                }else {
                    sendPlayInfo(AppConstant.PlayerState.STATE_STOP);
                }
                break;
            case R.id.img_previous:
                if (play_position > 0){
                    play_position --;
                }else{
                    play_position = listMp3.size() - 1;
                }
                play_progress = 0;
                sendPlayInfo(AppConstant.PlayerState.STATE_PLAY);
                break;
            default:
                break;
        }
    }

    private void setMusicInfo(){
        tvMusicName.setText(listMp3.get(play_position).getTitle());
        tvMusicAuthor.setText(listMp3.get(play_position).getArtist());
    }

    public List<Mp3Info> getMp3Infos(){

        List<Mp3Info> infos = new ArrayList<Mp3Info>();
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        for (int i = 0; i < cursor.getCount(); i++) {
            Mp3Info info = new Mp3Info();
            cursor.moveToNext();
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            //the path of the music
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
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
}
