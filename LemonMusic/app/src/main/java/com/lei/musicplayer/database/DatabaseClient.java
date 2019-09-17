package com.lei.musicplayer.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.constant.MusicType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/9/23.
 */
public class DatabaseClient {

    static Context mContext;
    static DbHelper database;
    static SQLiteDatabase db;

    final static String TABLE_PLAY_LIST = "playlist";
    final static String MUSIC_ID = "songId";
    final static String MUSIC_TITLE = "title";
    final static String MUSIC_ARTIST = "artist";
    final static String MUSIC_DURATION = "duration";
    final static String MUSIC_TYPE = "type";
    final static String MUSIC_URL = "url";
    final static String MUSIC_LRC_LINK = "lrcLink";
    final static String MUSIC_ALBUM_ART = "albumArt";//img

    public static void init(Context context){
        mContext = context;
        database = new DbHelper(mContext);
    }

//    + "songId text, " + "title text, " + "artist text, "
//            + "duration text, " + "type int, "
//            + "url text, " + "lrcLink text, " + "albumArt text)";

    public static void addMusic(Music music) {
        List<Music> checkList = getMusic();
        int size = checkList.size();
        for (int i = 0; i < size; i++) {
            if (checkList.get(i).getId() == music.getId() ||
                    checkList.get(i).getTitle().equals(music.getTitle())){
                return;
            }
        }
        // 如果当我们二次调用这个数据库方法,他们调用的是同一个数据库对象,在这里的方法创建的数据调用对象是用的同一个对象
        db = database.getWritableDatabase();
        db.execSQL("insert into " + TABLE_PLAY_LIST + " (" + MUSIC_ID + ","
                + MUSIC_TITLE + ","
                + MUSIC_ARTIST + ","
                + MUSIC_DURATION + ","
                + MUSIC_TYPE + ","
                + MUSIC_URL + ","
                + MUSIC_LRC_LINK + ","
                + MUSIC_ALBUM_ART + ") values(?,?,?,?,?,?,?,?)", new Object[]
                {music.getId(), music.getTitle(), music.getArtist(),music.getDuration(),music.getMusicType(),
                music.getUrl(),music.getLrcLink(),music.getAlbumArt()});
    }

    public static void deleteMusic(Music music){
        List<Music> checkList = getMusic();
        int size = checkList.size();
        for (int i = 0; i < size; i++) {
            if (checkList.get(i).getId() == music.getId() ||
                    checkList.get(i).getTitle().equals(music.getTitle())){
                db = database.getWritableDatabase();
                db.execSQL("delete from " + TABLE_PLAY_LIST + " where "
                        + MUSIC_ID + "=? or "
                        + MUSIC_TITLE + "=?", new Object[]
                        { music.getId(),music.getTitle() });
            }
        }

    }

    public static List<Music> getMusic(){
        List<Music> list = new ArrayList<>();
        if (database == null)return list;
        db = database.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select * from playlist ",null);
        Cursor cursor = db.query(TABLE_PLAY_LIST, null, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Music music = new Music();
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MUSIC_ID)));
            String title = cursor.getString(cursor.getColumnIndex(MUSIC_TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MUSIC_ARTIST));
            long duration = Long.parseLong(cursor.getString(cursor.getColumnIndex(MUSIC_DURATION)));
            int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MUSIC_TYPE)));
            String url = cursor.getString(cursor.getColumnIndex(MUSIC_URL));
            String lrclink = cursor.getString(cursor.getColumnIndex(MUSIC_LRC_LINK));
            String albumart = cursor.getString(cursor.getColumnIndex(MUSIC_ALBUM_ART));
            music.setId(id);
            music.setTitle(title);
            music.setArtist(artist);
            music.setDuration(duration);
            if (type == MusicType.local){
                music.setMusicType(MusicType.local);
            }else {
                music.setMusicType(MusicType.online);
            }
            music.setUrl(url);
            music.setLrcLink(lrclink);
            music.setAlbumArt(albumart);
            list.add(music);
        }

        return list;
    }

    public static void addCollect(Music music){

    }

    public static boolean checkCollect(Music music){
        boolean result = false;


        return result;
    }

    public static void deleteCollect(Music music){

    }

    public static List<Music> getCollect(){
        List<Music> list = new ArrayList<>();



        return list;
    }

    public void deletePerson(String id) {
        db = database.getWritableDatabase();
        db.execSQL("delete from " + TABLE_PLAY_LIST + " where " + MUSIC_ID + "=? ", new Object[]
                { id });
    }
}
