package com.lei.musicplayer.service;

/**
 * Created by lei on 2017/8/31.
 */
public interface PlayerServiceListener {

    void onPlayerServiceProgress(int progress);

    void onPlayerServiceStop();

    void onPlayerServicePlay();

}
