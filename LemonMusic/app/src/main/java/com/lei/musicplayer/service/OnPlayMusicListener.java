package com.lei.musicplayer.service;

/**
 * Created by lei on 2017/8/31.
 */
public interface OnPlayMusicListener {

    void onMusicPlay();

    void onMusicCurrentPosition(int currentPosition);

    void onMusicStop();

    void onMusicComplete();

}
