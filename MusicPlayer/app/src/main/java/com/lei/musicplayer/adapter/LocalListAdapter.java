package com.lei.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import com.lei.musicplayer.R;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.Mp3Info;
import com.lei.musicplayer.util.LogTool;
import com.lei.musicplayer.util.Util;
import java.util.List;

/**
 * Created by lei on 2017/8/3.
 */
public class LocalListAdapter extends CommonAdapter<Mp3Info> {

    private int playing_position;
    public LocalListAdapter(Context context, List<Mp3Info> data, int mItemLayout) {
        super(context, data, mItemLayout);
    }

    @Override
    public void convert(ViewHolder viewHolder, Mp3Info item, int position) {

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

        if (playing_position == position){
            viewHolder.getView(R.id.tv_playing).setVisibility(View.VISIBLE);
        }else {
            viewHolder.getView(R.id.tv_playing).setVisibility(View.INVISIBLE);
        }

    }

    private Bitmap setViewBitmap(int viewId,Mp3Info info){

        return null;
    }

    public void setPlayingPosition(int position){
        playing_position = position;
    }
}
