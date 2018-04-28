package com.lei.musicplayer.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.SearchMusicAdapter;
import com.lei.musicplayer.bean.SearchMusic;
import com.lei.musicplayer.persenter.SearchPresenter;
import com.lei.musicplayer.util.ToastTool;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/9/11
 */
public class SearchActivity extends BaseActivity implements
        AdapterView.OnItemClickListener,View.OnClickListener,ListView.OnItemLongClickListener,SearchPresenter.Callback{

    private static final String TAG = SearchActivity.class.getSimpleName();
    private ImageButton imgBack,imgSearch;
    private EditText etSearch;
    private ListView listView;
    private SearchMusicAdapter adapter;
    private List<SearchMusic.Song> mSongList = new ArrayList<>();
    private SearchPresenter mSearchPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rearch;
    }

    @Override
    protected void initView() {
        mSearchPresenter = new SearchPresenter(this,this);

        imgBack = (ImageButton) findViewById(R.id.img_btn_back);
        imgBack.setOnClickListener(this);
        imgSearch = (ImageButton) findViewById(R.id.img_btn_search);
        imgSearch.setOnClickListener(this);
        etSearch = (EditText) findViewById(R.id.et_search);

        listView = (ListView) findViewById(R.id.lv_search);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        adapter = new SearchMusicAdapter(this,mSongList,R.layout.item_search_list);
        listView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_btn_back:
                finish();
                break;
            case R.id.img_btn_search:
                String keyword = etSearch.getText().toString();
                if (keyword != null && keyword.length() > 0){
                    mSearchPresenter.searchMusic(keyword);
                }else {
                    ToastTool.ToastShort("search content is null");
                }
                break;
            default:

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSearchPresenter.playMusic(mSongList.get(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        downLoadDialog(position);
        return true;
    }


    private void downLoadDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("下载")
                .setMessage("是否下载？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSearchPresenter.downloadMusic(mSongList.get(position));
                    }
                }).create();
        builder.show();
    }

    @Override
    public void onSearchMusicSuccess(List<SearchMusic.Song> songList) {
        mSongList = songList;
        adapter = new SearchMusicAdapter(SearchActivity.this, songList, R.layout.item_search_list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchMusicFail(String str) {
        ToastTool.ToastLong(str);
    }

    @Override
    public void onPlayMusicSuccess(String msg) {
        ToastTool.ToastLong(msg);
    }

    @Override
    public void onPlayMusicFail(String msg) {
        ToastTool.ToastLong(msg);
    }

    @Override
    public void onDownloadMusicState(String msg) {
        ToastTool.ToastLong(msg);
    }

    @Override
    public void onDownloadMusicSuccess(String msg) {
        ToastTool.ToastLong(msg);
    }

    @Override
    public void onDownloadMusicFail(String msg) {
        ToastTool.ToastLong(msg);
    }

    @Override
    protected void onDestroy() {
        if (mSearchPresenter != null){
            mSearchPresenter.unbind();
        }
        super.onDestroy();
    }

}
