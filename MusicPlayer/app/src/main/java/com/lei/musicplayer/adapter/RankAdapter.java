package com.lei.musicplayer.adapter;

import android.content.Context;
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
import com.lei.musicplayer.constant.MusicType;
import com.lei.musicplayer.http.HttpClient;
import com.lei.musicplayer.http.MusicCallBack;
import com.lei.musicplayer.util.LogTool;
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
        LogTool.i(TAG,"RankAdapter " + position);
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
            holder.img = (ImageView) convertView.findViewById(R.id.img_music);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.tv_artist);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        setInfo(holder,position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogTool.i(TAG," position: "+position + " " + list.get(position).toString());
                // TODO: 2017/9/22 进一步获取file_link、duration
                if (position == 0 && list.get(position).getTitle().equals("native")){
                    return;
                }
                playMusic(list.get(position));
            }
        });
        return convertView;
    }

    private void playMusic(final OnlineMusic onlineMusic) {
        HttpClient.getMusicLink(onlineMusic.getSong_id(), new MusicCallBack<MusicLink>() {
            @Override
            public void onSuccess(MusicLink response) {
                LogTool.i(TAG," onSuccess ");
                onLineMusic2Music(onlineMusic,response.getBitrate().getFile_link(),
                        response.getBitrate().getFile_duration());
            }

            @Override
            public void onFail(Throwable t) {
                LogTool.i(TAG," getMusicLink failed ");
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

    private Music onLineMusic2Music(OnlineMusic mOnlineMusic,String file_link,int duration) {
        Music info = new Music();
        info.setId(Long.parseLong(mOnlineMusic.getSong_id()));
        info.setTitle(mOnlineMusic.getTitle());
        info.setArtist(mOnlineMusic.getArtist_name());
        info.setDuration(duration);
        info.setUrl(file_link);
        info.setLrcLink(mOnlineMusic.getLrclink());
        info.setAlbum(mOnlineMusic.getAlbum_title());
        info.setAlbumArt(mOnlineMusic.getPic_small());
        info.setMusicType(MusicType.online);
        AppCache.getPlayService().playStartMusic(info);
        return info;
    }
}
