package com.lei.musicplayer.util;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by lei on 2017/10/22.
 */
public class ToastTool {

    private static Context mContent;

    public static void init(Context context){
        mContent = context;
    }

    public static void ToastLong(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void ToastShort(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void ToastLong(String msg){
        Toast.makeText(mContent, msg, Toast.LENGTH_LONG).show();
    }

    public static void ToastShort(String msg){
        Toast.makeText(mContent,msg,Toast.LENGTH_SHORT).show();
    }

}
