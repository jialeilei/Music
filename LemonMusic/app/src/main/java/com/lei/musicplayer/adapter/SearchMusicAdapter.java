package com.lei.musicplayer.adapter;

import android.content.Context;
import com.lei.musicplayer.R;
import com.lei.musicplayer.bean.SearchMusic;
import java.util.List;

/**
 * Created by lei on 2017/9/11.
 */
public class SearchMusicAdapter extends CommonAdapter<SearchMusic.Song> {

    private static final String TAG = "SearchMusicAdapter";

    public SearchMusicAdapter(Context context, List<SearchMusic.Song> data, int mItemLayout) {
        super(context, data, mItemLayout);
    }

    @Override
    public void convert(ViewHolder viewHolder, SearchMusic.Song item, int position) {
        viewHolder.setText(R.id.tv_title,item.getSongname());
        viewHolder.setText(R.id.tv_artist, item.getArtistname());
    }
}
