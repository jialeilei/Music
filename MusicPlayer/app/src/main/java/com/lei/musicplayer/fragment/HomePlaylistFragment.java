package com.lei.musicplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.HomePlaylistAdapter;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.util.LogTool;


public class HomePlaylistFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private static final String TAG = "HomePlaylistFragment";
    private ListView lvPlayList;
    private HomePlaylistAdapter playlistAdapter;
    private HomeFragment parentFragment;

    public void HomePlaylistFragment(HomeFragment fragment){
        parentFragment = fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_playlist, container, false);
        return view;
    }

    @Override
    protected void initView(View view) {
//        lvPlayList = (ListView) view.findViewById(R.id.lv_playlist);
//        if (AppCache.getMusicPlaylist().size() > 0){
//            setListViewData();
//        }else {
//
//        }
//
//        lvPlayList.setOnItemClickListener(this);

    }

    private void setListViewData() {
        playlistAdapter = new HomePlaylistAdapter(getActivity(), AppCache.getMusicPlaylist(),R.layout.item_local_list);
        lvPlayList.setAdapter(playlistAdapter);


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onResume() {
        LogTool.i(TAG,"onResume getMusicPlaylist().size(): " + AppCache.getMusicPlaylist().size());
        super.onResume();
//        if (AppCache.getMusicPlaylist().size() == 0){
//            lvPlayList.setVisibility(View.INVISIBLE);
//        }else {
//            lvPlayList.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onStart() {
        LogTool.i(TAG,"onStart getMusicPlaylist().size(): " + AppCache.getMusicPlaylist().size());
        super.onStart();
    }
}
