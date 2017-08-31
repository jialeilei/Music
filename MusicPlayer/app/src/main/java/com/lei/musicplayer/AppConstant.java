package com.lei.musicplayer;

/**
 * Created by lei on 2017/8/3.
 */
public interface AppConstant {

    //short time
    int MUSIC_DURATION = 10000;// 10 seconds

    //action
    String ACTION_STATE = "action_state";
    String ACTION_DURATION = "action_duration";
    String ACTION_PROGRESS = "action_progress";
    String ACTION_PLAY_STOP = "action_play_stop"; //暂停、播放
    //public static int ACTION__PAUSE = 1;
    //public static int ACTION__STOP = 2;
    String ACTION__NEXT = "action_next";
    String ACTION__PREVIOUS = "action_previous";


    int CIRCLE_ALL = 6;
    int CIRCLE_SINGLE = 7;
    //state
    String STATE_PAUSE = "state_pause";
    String STATE_PLAYING = "state_playing";


    //value
    String MSG_PROGRESS = "msg_progress";
    String MSG_STATE = "state";


}
