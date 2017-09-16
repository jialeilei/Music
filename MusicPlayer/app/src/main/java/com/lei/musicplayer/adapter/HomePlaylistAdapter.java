package com.lei.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import com.lei.musicplayer.R;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.util.LogTool;
import com.lei.musicplayer.util.Util;
import java.util.List;

/**
 * Created by lei on 2017/9/16.
 */
public class HomePlaylistAdapter extends CommonAdapter<Music> {

    private static final String TAG = "PlaylistAdapter";

    public HomePlaylistAdapter(Context context, List<Music> data, int mItemLayout) {
        super(context, data, mItemLayout);
        if(AppCache.getMusicPlaylist() == null){
            LogTool.i(TAG,"musicPlaylist is null");
        }else {
            LogTool.i(TAG,"musicPlaylist size is " + AppCache.getMusicPlaylist().size());
        }
    }

    @Override
    public void convert(ViewHolder viewHolder, Music item, int position) {
        viewHolder.setText(R.id.tv_title, item.getTitle());
        viewHolder.setText(R.id.tv_artist, item.getArtist());
        viewHolder.setText(R.id.tv_duration, String.valueOf(Util.formatTime(item.getDuration())));
        if (AppCache.getMusicPlaylist().get(position).getAlbumArt().length() > 0){
            Bitmap bp = BitmapFactory.decodeFile(
                    AppCache.getMusicPlaylist().get(position).getAlbumArt());
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


}
