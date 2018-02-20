package com.lei.musicplayer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.lei.musicplayer.R;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.database.DatabaseClient;
import com.lei.musicplayer.util.Util;
import java.util.List;

/**
 * Created by lei on 2017/9/16.
 */
public class HomeListAdapter extends CommonAdapter<Music> {

    private static final String TAG = HomeListAdapter.class.getSimpleName();

    public HomeListAdapter(Context context, List<Music> data, int mItemLayout) {
        super(context, data, mItemLayout);
    }

    public void refreshData(){
        mData = DatabaseClient.getMusic();
        this.notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder viewHolder, final Music item, final int position) {

        viewHolder.setText(R.id.tv_title, item.getTitle());
        viewHolder.setText(R.id.tv_artist, item.getArtist());
        viewHolder.setText(R.id.tv_duration, String.valueOf(Util.formatTime(item.getDuration())));
        Glide.with(mContext)
                .load(item.getAlbumArt())
                .placeholder(R.mipmap.default_music)
                .into((ImageView) viewHolder.getView(R.id.img_music));

        if (AppCache.getPlayingMusic().getId() == item.getId()){
            viewHolder.getView(R.id.tv_playing).setVisibility(View.VISIBLE);
        }else {
            viewHolder.getView(R.id.tv_playing).setVisibility(View.INVISIBLE);
        }

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCache.getPlayService().play(mData,position);
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog(position);
                return false;
            }
        });

    }

    private void showDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("删除")
                .setMessage("是否删除列表中的 " + mData.get(position).getTitle()+" ?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseClient.deleteMusic(mData.get(position));
                        refreshData();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        builder.show();

    }


}
