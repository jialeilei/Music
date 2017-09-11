package com.lei.musicplayer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.RankListAdapter;
import com.lei.musicplayer.bean.OnLineMusicList;
import com.lei.musicplayer.bean.OnlineMusic;
import com.lei.musicplayer.bean.SongListInfo;
import com.lei.musicplayer.constant.Extra;
import com.lei.musicplayer.http.HttpClient;
import com.lei.musicplayer.util.LogTool;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lei on 2017/9/11
 */
public class RankActivity extends BaseActivity implements
        AdapterView.OnItemClickListener,View.OnClickListener{

    private static final String TAG = "RankActivity";
    ImageView imgBack;
    TextView tvTitle;
    ListView listView;
    SongListInfo songListInfo;
    List<OnlineMusic> onlineMusicList = new ArrayList<>();
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
        adapter = new RankListAdapter(this,onlineMusicList,R.layout.item_local_list);
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
    }


    public void getInfo() {
        HttpClient.getApiService().getOnLineMusicList(songListInfo.getType(),String.valueOf(50),
                String.valueOf(0), HttpClient.METHOD_GET_MUSIC_LIST).enqueue(new Callback<OnLineMusicList>() {
            @Override
            public void onResponse(Call<OnLineMusicList> call, Response<OnLineMusicList> response) {
                if (response.isSuccessful()){
                    onlineMusicList.addAll(response.body().getSong_list());
                    LogTool.i(TAG,"onResponse isSuccessful "+onlineMusicList.size());
                    adapter.notifyDataSetChanged();
                }else {
                    LogTool.i(TAG,"onResponse");
                }
            }

            @Override
            public void onFailure(Call<OnLineMusicList> call, Throwable t) {
                LogTool.i(TAG,"onFailure");
            }
        });
    }

}
