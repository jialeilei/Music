package com.lei.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.LocalMusicListViewAdapter;
import com.lei.musicplayer.application.AppCache;


/**
 * Created by lei on 2017/8/25.
 */
public class LocalFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private LocalMusicListViewAdapter adapter;
    private ListView mLocalMusicListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_music, container, false);
        return view;
    }

    @Override
    protected void initView(View view) {
        adapter = new LocalMusicListViewAdapter(getActivity(), AppCache.getLocalMusicList(),R.layout.music_list_item);
        mLocalMusicListView = (ListView) view.findViewById(R.id.lv_local_music);
        mLocalMusicListView.setPadding(0, AppCache.getSystemStatusHeight() * 2 + 50, 0, 50);
        mLocalMusicListView.setAdapter(adapter);
        mLocalMusicListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getPlayerService().play(0, position);
    }

    public void refreshMusicList(){
        if (isAdded()){
            refreshListView();
        }
    }

    private void refreshListView(){
        adapter.setPlayingPosition(getPlayerService().play_position);
        adapter.notifyDataSetChanged();
    }
}
