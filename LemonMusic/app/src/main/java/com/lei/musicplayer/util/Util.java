package com.lei.musicplayer.util;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.lei.musicplayer.bean.LrcContent;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.bean.OnlineMusic;
import com.lei.musicplayer.constant.AppConstant;
import com.lei.musicplayer.constant.MusicType;
import com.lei.musicplayer.http.DownloadCallBack;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/8/2.
 * 工具类
 */
public class Util {

    private static Context mContext;
    //储存路径
    private static final String BASIC_PATH = Environment.getExternalStorageDirectory() + "/MusicPlayer/";
    private static final String MUSIC = "music/";
    private static final String LRC = "lrc/";
    private static final String ALBUM = "album/";
    static String PARENT_PATH = "";
    static String CHILD_PATH = "";

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

    private static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    public static void writeLrcToDir(Music music, String stream, DownloadCallBack callBack) {
        PARENT_PATH = BASIC_PATH + LRC;
        writeToDir(getFileName(music) + ".lrc", stream, callBack);
    }

    public static void writeLrcToDir(Music music, InputStream stream, DownloadCallBack callBack) {
        PARENT_PATH = BASIC_PATH + LRC;
        writeToDir(getFileName(music) + ".lrc", stream, callBack);
    }

    public static void writeMusicToDir(final Music music ,final InputStream stream,DownloadCallBack callBack){
        PARENT_PATH = BASIC_PATH + MUSIC;
        writeToDir(getFileName(music) + ".mp3", stream, callBack);
    }

    private static String getFileName(Music music) {
        return CHILD_PATH = music.getTitle() + "-" + music.getArtist() ;
    }

    /*
    * 将数据储存到SD卡
    * */
    private static void writeToDir(final String fileName ,final InputStream is, final DownloadCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mkdirs(PARENT_PATH);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    File file = new File(PARENT_PATH ,fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();
                    updateMedia(PARENT_PATH, callBack);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private static void writeToDir(final String fileName ,final String is, final DownloadCallBack callBack) {
        if (is == null){
            callBack.onLrcFail();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mkdirs(PARENT_PATH);
                    BufferedWriter bw = new BufferedWriter(new FileWriter(PARENT_PATH + fileName));
                    bw.write(is);
                    bw.flush();
                    bw.close();
                    updateMedia(PARENT_PATH, callBack);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //filename是我们的文件全名，包括后缀
    public static void updateMedia(String filename, final DownloadCallBack callBack){
        MediaScannerConnection.scanFile(mContext,
                new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        callBack.onMusicSuccess();
                    }
                });
    }

    public static Music onLineMusic2Music(OnlineMusic mOnlineMusic,String file_link,int duration) {
        Music info = onLineMusicToMusic(mOnlineMusic);
        info.setDuration(duration);
        info.setUrl(file_link);
        return info;
    }

    public static Music onLineMusicToMusic(OnlineMusic mOnlineMusic) {
        Music info = new Music();
        info.setId(Long.parseLong(mOnlineMusic.getSong_id()));
        info.setTitle(mOnlineMusic.getTitle());
        info.setArtist(mOnlineMusic.getArtist_name());
        info.setLrcLink(mOnlineMusic.getLrclink());
        info.setAlbum(mOnlineMusic.getAlbum_title());
        info.setAlbumArt(mOnlineMusic.getPic_small());
        info.setMusicType(MusicType.online);
        return info;
    }

    /*
   * 初始化歌词类
   * */
    public static List<LrcContent> initLrc(Music music){

        List<LrcContent> lrcList = new ArrayList<>();
        LrcProcess lrcProcess = new LrcProcess();
        //读取歌词文件
        lrcProcess.readLRC(Environment.getExternalStorageDirectory() + "/MusicPlayer/lrc/"
                + music.getTitle() + "-"
                + music.getArtist() + ".lrc");
        //传回处理后的歌词文件
        lrcList = lrcProcess.getLrcList();
        if (lrcList == null || lrcList.size() == 0){return null;}

//        lrcView.setmLrcList(lrcList);
//        lrcView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.lrc_slide_up));
        //handler.post(mRunnable);

        return lrcList;
    }

    /**
     * 根据时间获取歌词显示的索引值
     * @return
     */
    public static int lrcIndex(int currentTime,long duration,List<LrcContent> lrcList) {
        int index = 0;
        if(currentTime < duration) {
            for (int i = 0; i < lrcList.size(); i++) {
                if (i < lrcList.size() - 1) {
                    if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
                        index = i;
                    }
                    if (currentTime > lrcList.get(i).getLrcTime()
                            && currentTime < lrcList.get(i + 1).getLrcTime()) {
                        index = i;
                    }
                }
                if (i == lrcList.size() - 1
                        && currentTime > lrcList.get(i).getLrcTime()) {
                    index = i;
                }
            }
        }
        return index;
    }


}
