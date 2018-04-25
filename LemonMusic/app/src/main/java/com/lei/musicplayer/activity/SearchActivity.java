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
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.bean.MusicLink;
import com.lei.musicplayer.bean.SearchMusic;
import com.lei.musicplayer.constant.MusicType;
import com.lei.musicplayer.http.DownloadCallBack;
import com.lei.musicplayer.http.HttpClient;
import com.lei.musicplayer.http.GetCallBack;
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
        HttpClient.getMusicLink(mSongList.get(position).getSongid(), new GetCallBack<MusicLink>() {
            @Override
            public void onSuccess(MusicLink response) {
                setMusicInfo(position, response);
                getPlayService().playStartMusic(mMusic);
                ToastTool.ToastLong(getResources().getString(R.string.playing_music));
            }

            @Override
            public void onFail(Throwable t) {
                ToastTool.ToastShort("无法获取音乐");
            }
        });
    }

    private void downloadMusic(final int position) {
        HttpClient.getMusicLink(mSongList.get(position).getSongid(), new GetCallBack<MusicLink>() {
            @Override
            public void onSuccess(MusicLink response) {
                setMusicInfo(position, response);
                ToastTool.ToastShort("正在下载");
                downloadMusic();
                downloadLrc();
                downloadAlbum();
            }

            @Override
            public void onFail(Throwable t) {
                ToastTool.ToastShort("无法获取音乐");
            }
        });

    }

    private void downloadAlbum() {

    }

    private void downloadMusic() {

        HttpClient.downloadMusic(mMusic, new DownloadCallBack() {
            @Override
            public void onMusicSuccess() {
                ToastTool.ToastShort("下载完成");
            }

            @Override
            public void onMusicFail() {
                ToastTool.ToastShort("歌曲下载失败");
            }

        });

    }

    private void downloadLrc() {
        HttpClient.downloadLrcString(mMusic);
    }

    private void setMusicInfo(int position, MusicLink response) {
        mMusic.setMusicType(MusicType.online);
        mMusic.setId(Long.parseLong(mSongList.get(position).getSongid()));
        mMusic.setUrl(response.getBitrate().getFile_link());
        mMusic.setDuration(response.getBitrate().getFile_duration() * 1000);
        mMusic.setTitle(mSongList.get(position).getSongname());
        mMusic.setArtist(mSongList.get(position).getArtistname());
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

    @Override
    public void searchMusicSuccess(List<SearchMusic.Song> songList) {
        adapter = new SearchMusicAdapter(SearchActivity.this, songList, R.layout.item_search_list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void searchMusicFail(String str) {
        ToastTool.ToastLong(str);
    }


}
