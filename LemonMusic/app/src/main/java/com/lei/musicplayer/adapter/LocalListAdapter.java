package com.lei.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import com.lei.musicplayer.R;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.util.Util;
import java.util.List;

/**
 * Created by lei on 2017/8/3.
 */
public class LocalListAdapter extends CommonAdapter<Music> {

    private static final String TAG = "LocalListAdapter";

    //private int playing_position;

    public LocalListAdapter(Context context, List<Music> data, int mItemLayout) {
        super(context, data, mItemLayout);
    }

    public void refreshData(){
        mData = AppCache.getLocalMusicList();
        this.notifyDataSetChanged();
    }


    @Override
    public void convert(ViewHolder viewHolder, Music item, int position) {

        viewHolder.setText(R.id.tv_title, item.getTitle());
        viewHolder.setText(R.id.tv_artist, item.getArtist());
        viewHolder.setText(R.id.tv_duration, String.valueOf(Util.formatTime(item.getDuration())));
        if (AppCache.getLocalMusicList().get(position).getAlbumArt().length() > 0){
            Bitmap bp = BitmapFactory.decodeFile(
                    AppCache.getLocalMusicList().get(position).getAlbumArt());
            viewHolder.setImageBitmap(R.id.img_music,bp);
        }else {
            viewHolder.setImageResource(R.id.img_music,R.mipmap.default_music);
        }

        if (AppCache.getPlayingMusic().getId() == item.getId()){
            viewHolder.getView(R.id.tv_playing).setVisibility(View.VISIBLE);
        }else {
            viewHolder.getView(R.id.tv_playing).setVisibility(View.INVISIBLE);
        }

    }

//    public void setPlayingPosition(int position){
//        playing_position = position;
//    }

}
