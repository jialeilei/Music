package com.lei.musicplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.activity.RankActivity;
import com.lei.musicplayer.adapter.OnlineListAdapter;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.SongListInfo;
import com.lei.musicplayer.constant.Extra;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/8/25.
 */
public class OnlineFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private static final String TAG = "OnlineFragment";
    ListView lv_online;
    OnlineListAdapter adapter;
    List<SongListInfo> infos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_music, container, false);
        return view;
    }


    @Override
    protected void initView(View view) {
        lv_online = (ListView) view.findViewById(R.id.lv_online_music);
        lv_online.setPadding(0, AppCache.getSystemStatusHeight() * 2 + 45, 0, 50);
        if (infos.isEmpty()){
            String [] titles = getResources().getStringArray(R.array.online_music_list_title);
            String [] types = getResources().getStringArray(R.array.online_music_list_type);
            for (int i = 0; i < types.length; i++) {
                SongListInfo info = new SongListInfo();
                info.setType(types[i]);
                info.setTitle(titles[i]);
                infos.add(info);
            }
        }
        adapter = new OnlineListAdapter(infos,getActivity());
        lv_online.setAdapter(adapter);
        lv_online.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //LogTool.i(TAG,"Position: " + position);
        SongListInfo info = infos.get(position);
        Intent intent = new Intent(getActivity(), RankActivity.class);
        intent.putExtra(Extra.SONG_LIST_INFO,info);
        startActivity(intent);
    }

}
