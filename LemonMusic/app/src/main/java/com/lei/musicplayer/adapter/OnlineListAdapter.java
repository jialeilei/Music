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
import com.lei.musicplayer.bean.OnLineMusicList;
import com.lei.musicplayer.bean.OnlineMusic;
import com.lei.musicplayer.bean.SongListInfo;
import com.lei.musicplayer.http.HttpClient;
import com.lei.musicplayer.http.GetCallBack;
import com.lei.musicplayer.util.L;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/9/10.
 */
public class OnlineListAdapter extends BaseAdapter {

    private static final String TAG = "OnlineListAdapter";
    List<SongListInfo> mData = new ArrayList<>();
    Context mContext;

    public OnlineListAdapter(List<SongListInfo> data,Context context){
        mData = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OnlineViewHolder holder;
        SongListInfo info = mData.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_online_list,parent,false);
            holder = new OnlineViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (OnlineViewHolder) convertView.getTag();
        }

        setInfo(info, holder);
        return convertView;
    }

    private void setInfo(final SongListInfo info, final OnlineViewHolder holder) {
        holder.musicTitle.setText(info.getTitle());
        if (info.getCoverUrl() == null){
            holder.music1.setText("waiting...");
            holder.music2.setText("waiting...");
            holder.music3.setText("waiting...");

            HttpClient.getOnlineMusicList(info.getType(), 3, 0, new GetCallBack<OnLineMusicList>() {
                @Override
                public void onSuccess(OnLineMusicList response) {
                    praseData(response, info);
                    setData(info, holder);
                }

                @Override
                public void onFail(Throwable t) {
                    L.i(TAG, "response fail ");
                }
            });

        }else {
            setData(info, holder);
        }

    }

    private void praseData(OnLineMusicList body, SongListInfo info) {
        List<OnlineMusic> list = body.getSong_list();
        info.setCoverUrl(body.getBillboard().getPic_s260());
        if (list.size()>0){
            info.setMusic1("1."+list.get(0).getTitle()+"-"+list.get(0).getArtist_name());
        }else {
            info.setMusic1("");
        }
        if (list.size()>1){
            info.setMusic2("2." + list.get(1).getTitle() + "-" + list.get(1).getArtist_name());
        }else {
            info.setMusic2("");
        }
        if (list.size()>2){
            info.setMusic3("3." + list.get(2).getTitle() + "-" + list.get(2).getArtist_name());
        }else {
            info.setMusic3("");
        }
    }

    private void setData(SongListInfo info, OnlineViewHolder holder) {
        //LogTool.i(TAG," setData "+info.getCoverUrl());
        holder.music1.setText(info.getMusic1());
        holder.music2.setText(info.getMusic2());
        holder.music3.setText(info.getMusic3());
        Glide.with(mContext)
                .load(info.getCoverUrl())
                .crossFade()//淡入淡出
                .placeholder(R.mipmap.default_music)
                .into(holder.img);
    }


    class OnlineViewHolder{
        TextView music1,music2,music3;
        TextView musicTitle;
        ImageView img;

        public OnlineViewHolder(View view){
            music1 = (TextView) view.findViewById(R.id.tv_music_1);
            music2 = (TextView) view.findViewById(R.id.tv_music_2);
            music3 = (TextView) view.findViewById(R.id.tv_music_3);
            musicTitle = (TextView) view.findViewById(R.id.tv_music_title);
            img = (ImageView) view.findViewById(R.id.img_cover);
        }
    }

}
