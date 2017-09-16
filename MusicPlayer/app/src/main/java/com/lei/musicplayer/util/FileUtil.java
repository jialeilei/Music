package com.lei.musicplayer.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lei on 2017/9/13.
 */
public class FileUtil {

    public static void writeToDir(InputStream is){

        FileOutputStream outputStream = null;
        BufferedInputStream bis = null;
        File file = new File("path","fileName");
        try {
            outputStream = new FileOutputStream(file);
            bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) == -1){
                outputStream.write(buffer,0,len);
                outputStream.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bis.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
