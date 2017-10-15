package com.lei.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.LrcContent;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.util.LogTool;
import com.lei.musicplayer.util.Util;
import com.lei.musicplayer.view.LrcView;
import java.util.List;

public class PlayFragment extends BaseFragment implements View.OnClickListener,
        View.OnTouchListener,SeekBar.OnSeekBarChangeListener{

    private static final String TAG = "PlayFragment";
    private RelativeLayout rlTop;
    private ImageView imgDown, imgPlay, imgPrev, imgNext;
    private TextView tvDuration,tvCurrent;
    private SeekBar sbProgress;
    private TextView tvName,tvAuthor;
    private LrcView lrcView;
    List<LrcContent> lrcList;
    private int seekBarProgress = 0;//to control the progress of  music
    int progress = 0;//show the progress of music
    private boolean haveLrc = false;
    int duration = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play,container,false);
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    protected void initView(View view) {

        rlTop = (RelativeLayout) view.findViewById(R.id.rl_top);
        rlTop.setPadding(0, AppCache.getSystemStatusHeight(),0,0);
        imgDown = (ImageView) view.findViewById(R.id.img_down);
        imgDown.setOnClickListener(this);
        imgPlay = (ImageView) view.findViewById(R.id.fg_bottom_play);
        imgPlay.setOnClickListener(this);
        imgPrev = (ImageView) view.findViewById(R.id.fg_bottom_prev);
        imgPrev.setOnClickListener(this);
        imgNext = (ImageView) view.findViewById(R.id.fg_bottom_next);
        imgNext.setOnClickListener(this);
        tvName = (TextView) view.findViewById(R.id.tv_music_name);
        tvAuthor = (TextView) view.findViewById(R.id.tv_music_author);
        lrcView = (LrcView) view.findViewById(R.id.lrc_view);
        sbProgress = (SeekBar) view.findViewById(R.id.fg_bottom_seek_bar);
        sbProgress.setOnSeekBarChangeListener(this);
        tvDuration = (TextView) view.findViewById(R.id.tv_time_duration);
        tvCurrent = (TextView) view.findViewById(R.id.tv_time_current);
    }



    public void updateProgress(int position){
        if (haveLrc){
            lrcView.setIndex(Util.lrcIndex(position,AppCache.getPlayingMusic().getDuration(),lrcList));
        }
        progress = position * 100  / (int)AppCache.getPlayingMusic().getDuration();
        sbProgress.setProgress(progress);
        tvCurrent.setText("" + Util.formatTime(position));

        if (!isImagePlaying){
            imgPlay.setImageResource(R.mipmap.ic_play_btn_play);
            isImagePlaying = true;
        }
    }


    private void setLrc(){
        haveLrc = false;
        lrcList = Util.initLrc(AppCache.getPlayingMusic());
        duration = (int) AppCache.getPlayingMusic().getDuration();
        lrcView.setLrcList(lrcList);
        tvDuration.setText(Util.formatTime(duration));
        if (lrcList != null){
            haveLrc = true;
        }
    }

    boolean isImagePlaying = false;
    public void updateInfo(){
        Music info = AppCache.getPlayingMusic();
        tvAuthor.setText(info.getArtist() + " ");
        tvName.setText(info.getTitle() + " ");
        setPlayImage();
        setLrc();
    }

    private void setPlayImage() {
        if (AppCache.isPlaying()){
            if (!isImagePlaying){
                imgPlay.setImageResource(R.mipmap.ic_play_btn_play);
                isImagePlaying = true;
            }
        }else {
            if (isImagePlaying){
                imgPlay.setImageResource(R.mipmap.ic_play_btn_pause);
                isImagePlaying = false;
            }
        }
    }

    public void onMusicPlay(){
        updateInfo();
    }

    public void onMusicStop(){
        if (isImagePlaying){
            imgPlay.setImageResource(R.mipmap.ic_play_btn_pause);
            isImagePlaying = false;
        }

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBarProgress = progress * (int) AppCache.getPlayingMusic().getDuration() / 100;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        getPlayerService().seekBarPlay(seekBarProgress);
    }

    @Override
    protected void refreshListView() {

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_down:
                getActivity().onBackPressed();
                break;
            case R.id.fg_bottom_play:
                getPlayerService().playOrStop();
                break;
            case R.id.fg_bottom_next:
                getPlayerService().playNext();
                break;
            case R.id.fg_bottom_prev:
                getPlayerService().playPrev();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //intercept event
        return true;
    }
}
