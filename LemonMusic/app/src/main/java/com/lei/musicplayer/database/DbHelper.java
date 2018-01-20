package com.lei.musicplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lei on 2017/9/23.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String CREATE_PLAY_LIST = "create table playlist ("
            + "id integer primary key autoincrement, "
            + "songId text, " + "title text, " + "artist text, "
            + "duration text, " + "type int, "
            + "url text, " + "lrcLink text, " + "albumArt text)";

    public static final String CREATE_COLLECT_LIST = "create table collectlist ("
            + "id integer primary key autoincrement, "
            + "songId text, " + "title text, " + "artist text, "
            + "duration text, " + "type int, "
            + "url text, " + "lrcLink text, " + "albumArt text)";

    private Context mContext;

    public DbHelper(Context context) {
        super(context, "music.db", null, 1);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PLAY_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists playlist" );
        onCreate(db);
    }

}
