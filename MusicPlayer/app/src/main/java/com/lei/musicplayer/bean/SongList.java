package com.lei.musicplayer.bean;

/**
 * Created by lei on 2017/8/18.
 */
public class SongList {
    int yyr_song_id = 0;
    SongItem songItem;



    //{"errorCode":22000,
    // "data":{"xcode":"08ce14a45b4464a2e6a359392b8aaf35",
    // "songList":[{"queryId":"276867440",
    // "songId":276867440,
    // "songName":"\u521a\u597d\u9047\u89c1\u4f60",
    // "artistId":"1078",
    // "artistName":"\u674e\u7389\u521a",
    String queryId;
    int songId;
    String songName;
    String artistId;
    String artistName;
    int albumId;
    int albumName;
    String songPicSmall;
    String songPicBig;
    String songPicRadio;
    String lrcLink;
    // "albumId":276867491,
    // "albumName":"\u521a\u597d\u9047\u89c1\u4f60",
    // "songPicSmall":"http:\/\/musicdata.baidu.com\/data2\/pic\/d59cab8d47b4ae5cd500cbb67de9cc5c\/276867491\/276867491.jpg@s_1,w_90,h_90",
    // "songPicBig":"http:\/\/musicdata.baidu.com\/data2\/pic\/d59cab8d47b4ae5cd500cbb67de9cc5c\/276867491\/276867491.jpg@s_1,w_150,h_150",
    // "songPicRadio":"http:\/\/musicdata.baidu.com\/data2\/pic\/d59cab8d47b4ae5cd500cbb67de9cc5c\/276867491\/276867491.jpg@s_1,w_300,h_300",
    // "lrcLink":"http:\/\/musicdata.baidu.com\/data2\/lrc\/b794e0a41a7806a92746d5ac3652dd8c\/543756270\/543756270.lrc",
    String version = "";
    int copyType = 0;
    int time = 200;
    int linkCode;
    String songLink = "";
    String showLink = "";
    String format = "";
    int rate;
    // "version":"",
    // "copyType":0,
    // "time":200,
    // "linkCode":22000,
    // "songLink":"http:\/\/yinyueshiting.baidu.com\/data2\/music\/efc490a7ce506659395762fd79575036\/540719533\/2768674401503010861128.mp3?xcode=08ce14a45b4464a243e6e1d8249fc792",
    // "showLink":"http:\/\/yinyueshiting.baidu.com\/data2\/music\/efc490a7ce506659395762fd79575036\/540719533\/2768674401503010861128.mp3?xcode=08ce14a45b4464a243e6e1d8249fc792",
    // "format":"mp3",
    // "rate":128,
    // "size":3202016,
    // "relateStatus":"0",
    // "resourceType":"0",
    // "source":"web"}]}}
    int size;
    String relateStatus = "0";
    String resourceType = "0";
    String source = "web";

    public SongList() {}

    public SongList(int yyr_song_id, SongItem songItem) {
        this.yyr_song_id = yyr_song_id;
        this.songItem = songItem;
    }

    public int getYyr_song_id() {
        return yyr_song_id;
    }

    public void setYyr_song_id(int yyr_song_id) {
        this.yyr_song_id = yyr_song_id;
    }

    public SongItem getSongItem() {
        return songItem;
    }

    public void setSongItem(SongItem songItem) {
        this.songItem = songItem;
    }

    //baidu music response
    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getAlbumName() {
        return albumName;
    }

    public void setAlbumName(int albumName) {
        this.albumName = albumName;
    }

    public String getSongPicSmall() {
        return songPicSmall;
    }

    public void setSongPicSmall(String songPicSmall) {
        this.songPicSmall = songPicSmall;
    }

    public String getSongPicBig() {
        return songPicBig;
    }

    public void setSongPicBig(String songPicBig) {
        this.songPicBig = songPicBig;
    }

    public String getSongPicRadio() {
        return songPicRadio;
    }

    public void setSongPicRadio(String songPicRadio) {
        this.songPicRadio = songPicRadio;
    }

    public String getLrcLink() {
        return lrcLink;
    }

    public void setLrcLink(String lrcLink) {
        this.lrcLink = lrcLink;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCopyType() {
        return copyType;
    }

    public void setCopyType(int copyType) {
        this.copyType = copyType;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(int linkCode) {
        this.linkCode = linkCode;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getShowLink() {
        return showLink;
    }

    public void setShowLink(String showLink) {
        this.showLink = showLink;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getRelateStatus() {
        return relateStatus;
    }

    public void setRelateStatus(String relateStatus) {
        this.relateStatus = relateStatus;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "SongList{" +
                "yyr_song_id=" + yyr_song_id +
                ", songItem=" + songItem +
                ", queryId='" + queryId + '\'' +
                ", songId=" + songId +
                ", songName='" + songName + '\'' +
                ", artistId='" + artistId + '\'' +
                ", artistName='" + artistName + '\'' +
                ", albumId=" + albumId +
                ", albumName=" + albumName +
                ", songPicSmall='" + songPicSmall + '\'' +
                ", songPicBig='" + songPicBig + '\'' +
                ", songPicRadio='" + songPicRadio + '\'' +
                ", lrcLink='" + lrcLink + '\'' +
                ", version='" + version + '\'' +
                ", copyType=" + copyType +
                ", time=" + time +
                ", linkCode=" + linkCode +
                ", songLink='" + songLink + '\'' +
                ", showLink='" + showLink + '\'' +
                ", format='" + format + '\'' +
                ", rate=" + rate +
                ", size=" + size +
                ", relateStatus='" + relateStatus + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
