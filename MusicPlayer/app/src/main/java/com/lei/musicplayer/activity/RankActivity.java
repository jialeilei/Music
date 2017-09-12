package com.lei.musicplayer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.RankListAdapter;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.bean.MusicLink;
import com.lei.musicplayer.bean.OnLineMusicList;
import com.lei.musicplayer.bean.OnlineMusic;
import com.lei.musicplayer.bean.SongListInfo;
import com.lei.musicplayer.constant.Extra;
import com.lei.musicplayer.constant.MusicType;
import com.lei.musicplayer.http.HttpClient;
import com.lei.musicplayer.http.MusicCallBack;
import com.lei.musicplayer.util.LogTool;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/9/11
 */
public class RankActivity extends BaseActivity implements
        AdapterView.OnItemClickListener,View.OnClickListener,AdapterView.OnItemLongClickListener{

    private static final String TAG = "RankActivity";
    ImageView imgBack;
    TextView tvTitle;
    ListView listView;
    SongListInfo songListInfo;
    List<OnlineMusic> mOnlineMusicList = new ArrayList<>();
    RankListAdapter  adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        songListInfo = (SongListInfo) getIntent().getSerializableExtra(Extra.SONG_LIST_INFO);
        setTitle("title");
        initView();
        getInfo();
    }

    private void initView() {

        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(songListInfo.getTitle());
        listView = (ListView) findViewById(R.id.lv_rank);
        adapter = new RankListAdapter(this, mOnlineMusicList,R.layout.item_local_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            default:

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogTool.i(TAG,"position "+position);
        mOnlineMusic = mOnlineMusicList.get(position);
        getMusicLink(mOnlineMusic.getSong_id());
    }

    /**
     * Callback method to be invoked when an item in this view has been
     * clicked and held.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need to access
     * the data associated with the selected item.
     *
     * @param parent   The AbsListView where the click happened
     * @param view     The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     * @return true if the callback consumed the long click, false otherwise
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        LogTool.i(TAG," LongClick ");

        return false;
    }

    private void getMusicLink(String songId) {
        HttpClient.getMusicLink(songId, new MusicCallBack<MusicLink>() {
            @Override
            public void onSuccess(MusicLink response) {
                LogTool.i(TAG, "onSuccess link; " + response.getBitrate().getFile_link());
                playOnlineMusic(response.getBitrate().getFile_link(),response.getBitrate().getFile_duration()*1000);

            }

            @Override
            public void onFail(Throwable t) {
                LogTool.i(TAG, "onFail " + t);
            }
        });
    }

    OnlineMusic mOnlineMusic;
    private void playOnlineMusic(String file_link,int duration) {
       /* long id;
        String title;
        String artist;
        long duration;
        long size;
        String url;
        String lrc
        String album;
        String albumKey;
        //image of album
        String albumArt="";
        int musicType;*/
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
        getPlayService().playStartMusic(info);

        HttpClient.download(info, new MusicCallBack() {
            @Override
            public void onSuccess(Object response) {
                LogTool.i(TAG, "onSuccess " + response.toString());
            }

            @Override
            public void onFail(Throwable t) {
                LogTool.i(TAG, "download onFail " + t);
            }
        });


    }


    public void getInfo() {
        HttpClient.getOnlineMusicList(songListInfo.getType(), 50, 0, new MusicCallBack<OnLineMusicList>() {
            @Override
            public void onSuccess(OnLineMusicList response) {
                mOnlineMusicList.addAll(response.getSong_list());
                LogTool.i(TAG, "onResponse isSuccessful " + mOnlineMusicList.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Throwable t) {
                LogTool.i(TAG,"onFailure");
            }
        });
    }

}
