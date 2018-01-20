package com.lei.musicplayer.bean;

import com.lei.musicplayer.constant.MusicType;

/**
 * Created by lei on 2017/8/2.
 */
public class Music {
    long id;//songId
    String title;//music name
    String artist;
    long duration;
    long size;
    String url;//music url
    String lrcLink;
    String album;
    String albumKey;
    String albumArt="";//pic
    int musicType;

    public int getMusicType() {
        return musicType;
    }

    public String getAlbum() {
        return album;
    }

    public void setMusicType(@MusicType int musicType) {
        this.musicType = musicType;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLrcLink() {
        return lrcLink;
    }

    public void setLrcLink(String lrcLink) {
        this.lrcLink = lrcLink;
    }

    public String getAlbumKey() {
        return albumKey;
    }

    public void setAlbumKey(String albumKey) {
        this.albumKey = albumKey;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", lrcLink='" + lrcLink + '\'' +
                ", album='" + album + '\'' +
                ", albumKey='" + albumKey + '\'' +
                ", albumArt='" + albumArt + '\'' +
                ", musicType=" + musicType +
                '}';
    }
}
