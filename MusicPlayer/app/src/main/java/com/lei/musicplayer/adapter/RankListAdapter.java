package com.lei.musicplayer.adapter;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.lei.musicplayer.R;
import com.lei.musicplayer.bean.OnlineMusic;
import com.lei.musicplayer.util.LogTool;

import java.util.List;

/**
 * Created by lei on 2017/9/11.
 */
public class RankListAdapter extends CommonAdapter<OnlineMusic> {
    private static final String TAG = "RankListAdapter";
    Context mContext;

    public RankListAdapter(Context context, List<OnlineMusic> data, int mItemLayout) {
        super(context, data, mItemLayout);
        mContext = context;
    }

    @Override
    public void convert(ViewHolder viewHolder, OnlineMusic item, int position) {

        LogTool.i(TAG,"title: "+item.getTitle());
        viewHolder.setText(R.id.tv_title,item.getTitle()+"");
        viewHolder.setText(R.id.tv_artist, item.getArtist_name());

        Glide.with(mContext).load(item.getPic_small())
                .placeholder(R.mipmap.default_music)
                .into((ImageView) viewHolder.getView(R.id.img_music));

    }
}
