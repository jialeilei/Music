package com.lei.musicplayer.service;

/**
 * Created by lei on 2017/8/31.
 */
public interface OnPlayerServiceListener {

    void onMusicCurrentPosition(int currentPosition);

    void onMusicStop();

    void onMusicPlay();

    void onMusicComplete();

}
