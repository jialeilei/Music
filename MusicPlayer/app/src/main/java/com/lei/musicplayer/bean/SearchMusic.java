package com.lei.musicplayer.bean;

import java.util.List;

/**
 * Created by lei on 2017/10/21.
 */
    public class SearchMusic {

    private List<Song> song;

    public List<Song> getSong() {
        return song;
    }

    public void setSong(List<Song> song) {
        this.song = song;
    }

    public class Song {
        private String songname;
        private String artistname;
        private String songid;

        public String getSongname() {
            return songname;
        }

        public void setSongname(String songname) {
            this.songname = songname;
        }

        public String getArtistname() {
            return artistname;
        }

        public void setArtistname(String artistname) {
            this.artistname = artistname;
        }

        public String getSongid() {
            return songid;
        }

        public void setSongid(String songid) {
            this.songid = songid;
        }
    }
}
