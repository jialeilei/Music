package com.lei.musicplayer.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.LocalListAdapter;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.util.L;

/**
 * Created by lei on 2017/8/25.
 */
public class LocalFragment extends BaseFragment
        implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private static final String TAG = LocalFragment.class.getSimpleName();
    private LocalListAdapter adapter;
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
        mLocalMusicListView.setPadding(0, AppCache.getSystemStatusHeight() * 2 + 45, 0, 90);
        mLocalMusicListView.setAdapter(adapter);
        mLocalMusicListView.setOnItemClickListener(this);
        //LogTool.i(TAG," Environment.getExternalStorageDirectory():"+ Environment.getExternalStorageDirectory());
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getPlayerService().play(AppCache.getLocalMusicList(), position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        deleteDialog(position);
        return false;
    }

    private void deleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("删除")
                .setMessage("是否删除 " + AppCache.getLocalMusicList().get(position) + " ?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();
                    }
                }).create();
        builder.show();
    }

    @Override
    protected void refreshListView() {
        adapter.refreshData();
    }


    @Override
    public void onResume() {
        L.i("onResume musicSize:" + AppCache.getLocalMusicList().size());
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
