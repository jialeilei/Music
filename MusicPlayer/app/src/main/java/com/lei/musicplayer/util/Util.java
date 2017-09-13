package com.lei.musicplayer.util;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.constant.AppConstant;
import com.lei.musicplayer.constant.MusicType;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/8/2.
 */
public class Util {

    static Context mContext;

    public static void init(Context context){
        mContext = context;
    }

    /**
     * 格式化时间，将毫秒转换为分:秒格式
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }


    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

    public static List<Music> getLocalMusic(){
        List<Music> infos = new ArrayList<Music>();
        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            //对歌曲长度进行过滤
            if (duration < AppConstant.MUSIC_DURATION){
                continue;
            }
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            //the path of the music
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

            //getCoverImage(Integer.parseInt(albumId));
            //String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            String albumKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY));
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            //LogTool.i(TAG,"albumArt: "+albumArt);

            Music info = new Music();
            if (isMusic != 0){
                info.setId(id);
                info.setTitle(title);
                info.setArtist(artist);
                info.setDuration(duration);
                info.setSize(size);
                info.setUrl(url);
                info.setAlbumKey(albumKey);
                info.setMusicType(MusicType.local);
                infos.add(info);
            }
        }
        return getAlbumArt(infos);
    }

    public static List<Music> getAlbumArt(List<Music> infos){
        String[] mediaColumns1 = new String[] {
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_KEY};
        Cursor cursor1 = mContext.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                mediaColumns1, null, null,
                null);
        if (cursor1 != null) {
            cursor1.moveToFirst();
            do {
                String album_art =  cursor1.getString(0);
                String album =  cursor1.getString(1);
                String albumKey = cursor1.getString(2);
                //LogTool.i(TAG, "ALBUM_ART 0 " + album_art + "ALBUM_ART 1 " + album + " ALBUM_ART 2 " + albumKey);
                if (album_art != null && album != null){
                    for (Music info : infos) {
                        if (info.getAlbumKey().equals(albumKey)){
                            info.setAlbumArt(album_art);
                            break;
                        }
                    }
                }
            } while (cursor1.moveToNext());
            cursor1.close();
        }
        return infos;
    }

    public static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    //filename是我们的文件全名，包括后缀
    public static void updateMedia(String filename){

        MediaScannerConnection.scanFile(mContext,
                new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        LogTool.i("ExternalStorage", "Scanned " + path + ":");
                        LogTool.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    public static void ToastLong(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
    }
    public static void ToastShort(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

}
