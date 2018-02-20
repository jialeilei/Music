package com.lei.musicplayer.util;

import android.util.Log;
import com.lei.musicplayer.BuildConfig;

/**
 * Created by lei on 2017/8/12.
 */
public class L {

    private static String className;

    private static boolean isDebug(){
        return BuildConfig.DEBUG;
    }

    public static void i(String msg){
        if (!isDebug()){
            return;
        }
        getClassInfo(new Throwable().getStackTrace());
        Log.i(className,msg);
    }

    public static void i(String tag,String msg){
        if (!isDebug()){
            return;
        }
        getClassInfo(new Throwable().getStackTrace());
        Log.i(tag,msg);
    }

    private static void getClassInfo(StackTraceElement[] elements) {
        className = elements[1].getClassName();
    }


}
