package com.lei.musicplayer.util;


import android.util.Log;
import com.lei.musicplayer.BuildConfig;

/**
 * Created by lei on 2017/8/12.
 */
public class LogTool {


    private static String className;
    private static String methodName;
    private static String lineNumber;


    private static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    private LogTool(){
        throw new UnsupportedOperationException(getClass().getSimpleName() + " can't be instantiated ");
    }

    private static void getInfo(StackTraceElement[] elements){
        className = getClassName(elements[1].getClassName());
        methodName = elements[1].getMethodName();
        lineNumber = String.valueOf(elements[1].getLineNumber());
    }

    private static String getClassName(String longName) {
        String[] strs = longName.split("\\.");
        if (strs.length < 1)return null;
        return strs[strs.length - 1];
    }

    private static String createLog(String msg){
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(msg);
        return buffer.toString();
    }

    public static void i(String Tag,String msg){
        if (!isDebug())return;
        getInfo(new Throwable().getStackTrace());
        Log.i(Tag,msg);
    }

    public static void d(String Tag,String msg){
        if (!isDebug())return;
        getInfo(new Throwable().getStackTrace());
        Log.d(Tag,msg);
    }

    public static void w(String Tag,String msg){
        if (!isDebug())return;
        getInfo(new Throwable().getStackTrace());
        Log.w(Tag,msg);
    }

    public static void e(String Tag,String msg){
        if (!isDebug())return;
        getInfo(new Throwable().getStackTrace());
        Log.e(Tag,msg);
    }




    public static void i(String msg){
        if (!isDebug())return;
        getInfo(new Throwable().getStackTrace());
        Log.i(className,createLog(msg));
    }

    public static void d(String msg){
        if (!isDebug())return;
        getInfo(new Throwable().getStackTrace());
        Log.d(className,msg);
    }

    public static void w(String msg){
        if (!isDebug())return;
        getInfo(new Throwable().getStackTrace());
        Log.w(className,msg);
    }

    public static void e(String msg){
        if (!isDebug())return;
        getInfo(new Throwable().getStackTrace());
        Log.e(className,msg);
    }


}
