package com.lei.musicplayer.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.lei.musicplayer.R;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.bean.MusicLink;
import com.lei.musicplayer.bean.OnLineMusicList;
import com.lei.musicplayer.bean.OnlineMusic;
import com.lei.musicplayer.http.HttpClient;
import com.lei.musicplayer.http.MusicCallBack;
import com.lei.musicplayer.util.LogTool;
import com.lei.musicplayer.util.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/9/20.
 */
public class RankAdapter extends BaseAdapter {

    private static final String TAG = "RankAdapter";
    Context mContext;
    List<OnlineMusic> list = new ArrayList<>();//初始数据为1
    String[] types;
    int typePosition;

    public RankAdapter(Context context, int position){
        mContext = context;
        typePosition = position;
        types = mContext.getResources().getStringArray(R.array.online_music_list_type);
        OnlineMusic onlineMusic = new OnlineMusic();
        onlineMusic.setArtist_name("native");
        onlineMusic.setTitle("native");
        list.add(onlineMusic);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_local_list,parent,false);
            holder = new ViewHolder();
            setView(convertView, holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        setInfo(holder,position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogTool.i(TAG, " position: " + position + " " + list.get(position).toString());
                // TODO: 2017/9/22 进一步获取file_link、duration
                if (position == 0 && list.get(position).getTitle().equals("native")) {
                    return;
                }
                playMusic(list.get(position));
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("下载")
                        .setMessage("是否下载？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                LogTool.i(TAG, "setNegativeButton");
                            }
                        })
                        .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogTool.i(TAG, "setPositiveButton");
                                HttpClient.getMusicLink(list.get(position).getSong_id(), new MusicCallBack<MusicLink>() {
                                    @Override
                                    public void onSuccess(MusicLink response) {
                                        Music music = Util.onLineMusic2Music(list.get(position),
                                                response.getBitrate().getFile_link(),
                                                response.getBitrate().getFile_duration());
                                        downloadMusic(music);
                                    }

                                    @Override
                                    public void onFail(Throwable t) {

                                    }
                                });
                            }
                        })
                        .create();
                builder.show();
                return true;
            }
        });

        return convertView;
    }

    private void setView(View convertView, ViewHolder holder) {
        holder.img = (ImageView) convertView.findViewById(R.id.img_music);
        holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        holder.tvAuthor = (TextView) convertView.findViewById(R.id.tv_artist);
        convertView.setTag(holder);
    }

    private void playMusic(final OnlineMusic onlineMusic) {
        HttpClient.getMusicLink(onlineMusic.getSong_id(), new MusicCallBack<MusicLink>() {
            @Override
            public void onSuccess(MusicLink response) {
                LogTool.i(TAG, " onSuccess ");
                Music music = Util.onLineMusic2Music(onlineMusic, response.getBitrate().getFile_link(),
                        response.getBitrate().getFile_duration());
                AppCache.getPlayService().playStartMusic(music);
            }

            @Override
            public void onFail(Throwable t) {
                LogTool.i(TAG, " getMusicLink failed ");
            }
        });
    }

    private void setInfo(final ViewHolder holder,final int position) {
        if (list.size() <= 1){
            holder.tvTitle.setText("loading");
            holder.tvAuthor.setText("loading");
            LogTool.i(TAG,"begin HttpClient.getOnlineMusicList ");
            HttpClient.getOnlineMusicList(types[typePosition], 30, 0, new MusicCallBack<OnLineMusicList>() {
                @Override
                public void onSuccess(OnLineMusicList response) {
                    list = response.getSong_list();
                    setData(holder,list.get(position));
                    LogTool.i(TAG, "onSuccess");
                }

                @Override
                public void onFail(Throwable t) {
                    LogTool.i(TAG,"onFail");
                }
            });
        }else {
            setData(holder,list.get(position));
        }
    }

    private void setData(final ViewHolder holder, final OnlineMusic music) {
        LogTool.i(TAG, "set data ");
        holder.tvTitle.setText(music.getTitle());
        holder.tvAuthor.setText(music.getArtist_name());
        Glide.with(mContext).load(music.getPic_small()).placeholder(R.mipmap.default_music).into(holder.img);
    }

    private class ViewHolder{
        ImageView img;
        TextView tvTitle,tvAuthor;
    }

    private void downloadMusic(Music music){
        HttpClient.download(music, new MusicCallBack() {
            @Override
            public void onSuccess(Object response) {
                Util.ToastShort("下载完成");
            }

            @Override
            public void onFail(Throwable t) {
                Util.ToastShort("下载失败，请重新下载");
            }
        });
    }

}
