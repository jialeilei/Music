package com.lei.musicplayer.bean;

/**
 * Created by lei on 2017/8/18.
 */
public class SongLinkResponse {
    int errorCode;
    SongData data ;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public SongData getData() {
        return data;
    }

    public void setData(SongData data) {
        this.data = data;
    }
}
