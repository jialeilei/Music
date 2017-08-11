package com.lei.musicplayer.adapter;

import android.content.Context;

import com.lei.musicplayer.R;
import com.lei.musicplayer.bean.Mp3Info;
import com.lei.musicplayer.util.Util;

import java.util.List;

/**
 * Created by lei on 2017/8/3.
 */
public class ListViewAdapter extends CommonAdapter<Mp3Info> {

    public ListViewAdapter(Context context, List<Mp3Info> data, int mItemLayout) {
        super(context, data, mItemLayout);
    }

    @Override
    public void convert(ViewHolder viewHolder, Mp3Info item, int position) {

        viewHolder.setText(R.id.tv_title,item.getTitle());
        viewHolder.setText(R.id.tv_artist,item.getArtist());
        viewHolder.setText(R.id.tv_duration, String.valueOf(Util.formatTime(item.getDuration())));
    }
}
