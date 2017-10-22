package com.lei.musicplayer.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.SearchMusicAdapter;
import com.lei.musicplayer.bean.Lrc;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.bean.MusicLink;
import com.lei.musicplayer.bean.SearchMusic;
import com.lei.musicplayer.constant.MusicType;
import com.lei.musicplayer.http.HttpClient;
import com.lei.musicplayer.http.MusicCallBack;
import com.lei.musicplayer.util.LogTool;
import com.lei.musicplayer.util.ToastTool;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/9/11
 */
public class SearchActivity extends BaseActivity implements
        AdapterView.OnItemClickListener,View.OnClickListener,ListView.OnItemLongClickListener{

    private static final String TAG = "SearchActivity";

    private ImageButton imgBack,imgSearch;
    private EditText etSearch;
    private ListView listView;
    private SearchMusicAdapter adapter;
    private List<SearchMusic.Song> mSongList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rearch);
        initView();
    }

    private void initView() {
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_btn_back:
                finish();
                break;
            case R.id.img_btn_search:
                String keyword = etSearch.getText().toString();
                if (keyword != null || keyword != ""){
                    toSearchMusic(keyword);
                }
                break;
            default:

                break;
        }
    }

    private void toSearchMusic(String keyword) {
        HttpClient.getSearchMusic(keyword, new MusicCallBack<SearchMusic>() {
            @Override
            public void onSuccess(SearchMusic response) {
                if (response.getSong().size() > 0) {
                    LogTool.i(TAG, "onSuccess size: " + response.getSong().size() + " "
                            + response.getSong().toString());
                    mSongList = response.getSong();
                    adapter = new SearchMusicAdapter(SearchActivity.this, mSongList,
                            R.layout.item_search_list);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(Throwable t) {
                LogTool.i(TAG, "onFail " + t);
            }
        });
    }

    private Music mMusic = new Music();
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        playMusic(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        downLoadDialog(position);
        return true;
    }

    private void playMusic(final int position) {
        HttpClient.getMusicLink(mSongList.get(position).getSongid(), new MusicCallBack<MusicLink>() {
            @Override
            public void onSuccess(MusicLink response) {
                setMusicInfo(position, response);
                getPlayService().playStartMusic(mMusic);
                ToastTool.ToastLong(SearchActivity.this, getResources().getString(R.string.playing_music));
            }

            @Override
            public void onFail(Throwable t) {
                ToastTool.ToastShort("无法获取音乐");
            }
        });
    }

    private void downloadMusic(final int position) {
        HttpClient.getMusicLink(mSongList.get(position).getSongid(), new MusicCallBack<MusicLink>() {
            @Override
            public void onSuccess(MusicLink response) {
                setMusicInfo(position, response);
                downloadMusic();
                downloadLrc();
            }

            @Override
            public void onFail(Throwable t) {
                ToastTool.ToastShort("无法获取音乐");
            }
        });

    }

    private void downloadMusic() {
        HttpClient.downloadMusic(mMusic, new MusicCallBack() {
            @Override
            public void onSuccess(Object response) {
                ToastTool.ToastShort("下载完成");
            }

            @Override
            public void onFail(Throwable t) {
                ToastTool.ToastShort("歌曲下载失败");
            }
        });
    }

    private void downloadLrc() {
        HttpClient.downloadLrc(mMusic, new MusicCallBack<Lrc>() {
            @Override
            public void onSuccess(Lrc response) {
            }

            @Override
            public void onFail(Throwable t) {
                ToastTool.ToastShort("歌词下载失败");
            }
        });
    }

    private void setMusicInfo(int position, MusicLink response) {
        mMusic.setMusicType(MusicType.online);
        mMusic.setId(Long.parseLong(mSongList.get(position).getSongid()));
        mMusic.setUrl(response.getBitrate().getFile_link());
        mMusic.setDuration(response.getBitrate().getFile_duration() * 1000);
        mMusic.setTitle(mSongList.get(position).getSongname());
        mMusic.setArtist(mSongList.get(position).getArtistname());
        LogTool.i(TAG, "Duration: " + mMusic.getDuration());
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
                        downloadMusic(position);
                    }
                }).create();
        builder.show();
    }


}
