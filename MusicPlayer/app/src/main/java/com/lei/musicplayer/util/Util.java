package com.lei.musicplayer.util;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by lei on 2017/8/2.
 */
public class Util {

    Context mContext;

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

//
//    private void getHotRank() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Document doc = null;
//                LogTool.i(TAG,"start to get doc "+ SystemClock.currentThreadTimeMillis());
//                try {
//                    doc = Jsoup.connect(TOP_RANK_URL).get();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                LogTool.i(TAG,"get doc finished "+SystemClock.currentThreadTimeMillis());
//                Elements elements = doc.getElementsByClass("normal-song-list");
//                Elements lead_top = doc.getElementsByClass("song-item-hook");
//                String str="";
//
//                for (Element element : elements) {
//                    LogTool.i(TAG,element.text()+"");
//                }
//                LogTool.i(TAG," item size: " + lead_top.size());
//                for (Element element : lead_top) {
//                    SongList mSongList = new SongList();
//                    mSongList = gson.fromJson(element.attr("data-songitem"),
//                            SongList.class);
//                    songLists.add(mSongList);
//                    LogTool.i(TAG," item:"+element.attr("data-songitem")
//                                    +" title: "+element.getElementsByClass("song-title").attr("title")
//                    );
//                }
//                LogTool.i(TAG, "SongList.size: " + songLists.size());
//                for (SongList songList : songLists) {
//                    LogTool.i(TAG,songList.getSongItem().getSname()+" id:"+songList.getSongItem().getSid());
//                }
//
//            }
//        }).start();
//    }
//
//    private void getDownloadLink() {
//        HttpHelper helper = retrofit.create(HttpHelper.class);
//        helper.getSongData(276867440).enqueue(new retrofit2.Callback<SongLinkResponse>() {
//            @Override
//            public void onResponse(retrofit2.Call<SongLinkResponse> call, retrofit2.Response<SongLinkResponse> response) {
//
//                if (response.isSuccessful()){
//                    LogTool.i(TAG,response.body().getData().getSongList().toString());
//                }else {
//                    LogTool.i(TAG,"onResponse fail");
//                }
//            }
//
//
//            @Override
//            public void onFailure(retrofit2.Call<SongLinkResponse> call, Throwable t) {
//                LogTool.i(TAG,"onFail");
//            }
//        });
//    }
}
