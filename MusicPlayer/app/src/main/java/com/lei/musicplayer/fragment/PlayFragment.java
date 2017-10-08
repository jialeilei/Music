package com.lei.musicplayer.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.LrcContent;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.util.LogTool;
import com.lei.musicplayer.util.Util;
import com.lei.musicplayer.view.LrcView;

import java.util.List;


public class PlayFragment extends BaseFragment implements View.OnClickListener,View.OnTouchListener{

    private static final String TAG = "PlayFragment";
    RelativeLayout rlTop;
    ImageView imgDown;
    static TextView tvName,tvAuthor;
    public LrcView lrcView;
    List<LrcContent> lrcList;

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
        tvName = (TextView) view.findViewById(R.id.tv_music_name);
        tvAuthor = (TextView) view.findViewById(R.id.tv_music_author);
        lrcView = (LrcView) view.findViewById(R.id.lrc_view);
    }

    public void updateProgress(int position){
        lrcView.setIndex(Util.lrcIndex(position,AppCache.getPlayingMusic().getDuration(),lrcList));
    }


    public void setLrc(){
        lrcList = Util.initLrc(AppCache.getPlayingMusic());
        lrcView.setLrcList(lrcList);
        lrcView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.lrc_slide_up));
        LogTool.i(TAG, "setLrc list.size: " + lrcList.size());
    }

    public static void updateInfo(){
        Music info = AppCache.getPlayingMusic();
        tvAuthor.setText(info.getArtist());
        tvName.setText(info.getTitle());
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
        LogTool.i(TAG,"PlayFragment onResume");
        updateInfo();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_down:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //intercept event
        return true;
    }
}
