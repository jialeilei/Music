package com.lei.musicplayer.constant;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lei on 2017/9/10.
 */
@IntDef({
        MusicType.local,
        MusicType.online,
        MusicType.unknow
})
@Retention(RetentionPolicy.SOURCE)
public @interface MusicType {

    int local = 1;

    int online = 2;

    int unknow = 3;

}
