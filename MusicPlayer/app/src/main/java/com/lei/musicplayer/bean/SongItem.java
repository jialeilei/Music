package com.lei.musicplayer.bean;

/**
 * Created by lei on 2017/8/18.
 */
public class SongItem {
    String sid = "";
    String author = "";
    String sname = "";
    String pay_type = "";

    public SongItem(String sid, String author, String sname, String pay_type) {
        this.sid = sid;
        this.author = author;
        this.sname = sname;
        this.pay_type = pay_type;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }
}
