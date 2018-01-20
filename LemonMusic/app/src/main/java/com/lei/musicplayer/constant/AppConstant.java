package com.lei.musicplayer.constant;

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
    //control the playing progress of music by seekBar
    String ACTION__CONTROL_PROGRESS = "action__control_progress";
    String ACTION__NEXT = "action_next";
    String ACTION__PREVIOUS = "action_previous";
    //String ACTION_STOP = "action_stop";
    //String ACTION_PLAY = "action_play";

    int CIRCLE_ALL = 6;
    int CIRCLE_SINGLE = 7;
    //state
    String STATE_PAUSE = "state_pause";
    String STATE_PLAYING = "state_playing";

    //value
    String MSG_PROGRESS = "msg_progress";
    String MSG_STATE = "state";
    String MSG_PLAY_POSITION = "msg_play_position";


}
