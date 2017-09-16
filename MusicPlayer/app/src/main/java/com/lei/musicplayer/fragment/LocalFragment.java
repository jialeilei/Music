package com.lei.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.LocalListAdapter;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.util.LogTool;


/**
 * Created by lei on 2017/8/25.
 */
public class LocalFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private static final String TAG = "LocalFragment";
    public static LocalListAdapter adapter;
    private ListView mLocalMusicListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_music, container, false);
        return view;
    }

    @Override
    protected void initView(View view) {
        adapter = new LocalListAdapter(getActivity(), AppCache.getLocalMusicList(),R.layout.item_local_list);
        mLocalMusicListView = (ListView) view.findViewById(R.id.lv_local_music);
        mLocalMusicListView.setPadding(0, AppCache.getSystemStatusHeight() * 2 + 45, 0, 50);
        mLocalMusicListView.setAdapter(adapter);
        mLocalMusicListView.setOnItemClickListener(this);
        //LogTool.i(TAG," Environment.getExternalStorageDirectory():"+ Environment.getExternalStorageDirectory());
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getPlayerService().playStartMusic(AppCache.getLocalMusicList().get(position));
    }

    public void refreshMusicList(){
        if (isAdded()){
            refreshListView();
        }
    }

    private void refreshListView(){
        adapter.notifyDataSetChanged();
    }


    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        LogTool.i(TAG, "local onDestroy");
        super.onDestroy();
    }
}
