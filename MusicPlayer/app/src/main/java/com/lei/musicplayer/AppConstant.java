package com.lei.musicplayer;

/**
 * Created by lei on 2017/8/3.
 */
public class AppConstant {

    //音乐目前的状态
    public static String ACTION_STATE = "action_state";
    public static String ACTION_DURATION = "action_duration";
    //progress
    public static String ACTION_PROGRESS = "action_progress";

    public static class PlayerState {
        public static int STATE_PLAY = 0;//暂停、播放
        public static int STATE_PAUSE = 1;
        public static int STATE_STOP = 2;
    }

    public static class PlayerType{
        public static int CIRCLE_ALL = 6;
        public static int CIRCLE_SINGLE = 7;
        public static int CIRCLE_DEFAULT = 8;
    }

    public static class PlayerMsg{
        public static String MSG_PROGRESS = "msg_progress";
    }




}
