package com.lei.musicplayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.lei.musicplayer.AppConstant;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.FragmentAdapter;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.Mp3Info;
import com.lei.musicplayer.fragment.LocalFragment;
import com.lei.musicplayer.fragment.OnlineFragment;
import com.lei.musicplayer.service.PlayerService;
import com.lei.musicplayer.service.PlayerServiceListener;
import com.lei.musicplayer.util.LogTool;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener ,ViewPager.OnPageChangeListener,
        SeekBar.OnSeekBarChangeListener,View.OnClickListener,PlayerServiceListener{

    private static final String TAG = "MainActivity";
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    private int play_progress = 0;

    //view
    ImageButton img_next, img_play, img_category;
    TextView tvMusicName,tvMusicAuthor,tvMusicDuration;
    SeekBar mSeekBarCurrent;
    Gson gson = new Gson();
    String TOP_RANK_URL = "http://music.baidu.com/top/dayhot/?pst=shouyeTop";
    String BASE_URL = "http://music.baidu.com/";
    String DOWNLOAD_URL = "data/music/links?songIds=276867440";//songIds=276867440

    ViewPager viewPager;
    TextView tvLocal,tvOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getPlayService().setOnPlayerListener(this);
        initView();
        setListener();
        setReceiver();
        //setAnimation();
    }

    private void setAnimation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initView() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tvLocal = (TextView) findViewById(R.id.tv_local);
        tvOnline = (TextView) findViewById(R.id.tv_online);
        changeTitleColor(0);

        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        LocalFragment localFragment = new LocalFragment();
        OnlineFragment onlineFragment = new OnlineFragment();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(localFragment);
        fragmentAdapter.addFragment(onlineFragment);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        img_category = (ImageButton) findViewById(R.id.img_category);
        img_category.setOnClickListener(this);

        //bottom play
        img_play = (ImageButton) findViewById(R.id.img_play);
        img_next = (ImageButton) findViewById(R.id.img_next);
        tvMusicName = (TextView) findViewById(R.id.tv_music_name);
        tvMusicAuthor = (TextView) findViewById(R.id.tv_music_author);
        tvMusicDuration = (TextView) findViewById(R.id.tv_music_duration);
        mSeekBarCurrent = (SeekBar) findViewById(R.id.seek_bar_current);
    }

    private void setListener() {
        viewPager.setOnPageChangeListener(this);
        tvLocal.setOnClickListener(this);
        tvOnline.setOnClickListener(this);
        img_play.setOnClickListener(this);
        img_next.setOnClickListener(this);
        mSeekBarCurrent.setOnSeekBarChangeListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setReceiver() {
        mainReceiver = new MainReceiver();
        // 创建IntentFilter
        IntentFilter filter = new IntentFilter();
        // 指定BroadcastReceiver监听的Action
        filter.addAction(AppConstant.ACTION_STATE);
        filter.addAction(AppConstant.ACTION_PROGRESS);
        // 注册BroadcastReceiver
        registerReceiver(mainReceiver, filter);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //LogTool.i(TAG,"onPageScrolled: " + position);
    }

    @Override
    public void onPageSelected(int position) {
        LogTool.i(TAG, "onPageSelected: " + position);
        changeTitleColor(position);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        play_progress = progress * (int) AppCache.getPlayingMp3Info().getDuration() / 100;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        sendPlayInfo(AppConstant.ACTION_PLAY_STOP);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void changeTitleColor(int position) {
        if (position == 0){
            tvLocal.setTextColor(getResources().getColor(R.color.white));
            tvOnline.setTextColor(getResources().getColor(R.color.gray_d));

        }else {
            tvOnline.setTextColor(getResources().getColor(R.color.white));
            tvLocal.setTextColor(getResources().getColor(R.color.gray_d));
        }

    }

    private MainReceiver mainReceiver;

    public class MainReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case AppConstant.ACTION_STATE:
                    setMusicInfo();
                    String state = intent.getStringExtra(AppConstant.MSG_STATE);
                    if (state.equals(AppConstant.STATE_PLAYING)){
                        img_play.setImageResource(R.mipmap.player_playing);
                    }else {
                        img_play.setImageResource(R.mipmap.player_stop);
                    }
                    break;
                case AppConstant.ACTION_PROGRESS:
                    play_progress = intent.getIntExtra(AppConstant.MSG_PROGRESS,0);
                    int progress = play_progress * 100 / (int) AppCache.getPlayingMp3Info().getDuration();
                    mSeekBarCurrent.setProgress(progress);
//                    Log.i(TAG, "progress " + progress +
//                            " duration: " + ((int) listMp3.get(play_position).getDuration())
//                            + " play_progress: " + play_progress);
                    break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_next:
                sendPlayInfo(AppConstant.ACTION__NEXT);
                getPlayService().playNext();
                break;
            case R.id.img_play:
                sendPlayInfo(AppConstant.ACTION_PLAY_STOP);
                break;
            case R.id.tv_local:
                viewPager.setCurrentItem(0);
                changeTitleColor(0);
                break;
            case R.id.tv_online:
                viewPager.setCurrentItem(1);
                changeTitleColor(1);
                break;
            case R.id.img_category:
                drawer.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }


    private void setMusicInfo(){
        tvMusicName.setText(AppCache.getPlayingMp3Info().getTitle());
        tvMusicAuthor.setText(AppCache.getPlayingMp3Info().getArtist());
    }

    /*
    * 启动服务播放音乐
    * */
    private void sendPlayInfo(String action){
        if (AppCache.getLocalMusicList() != null ){
            //Mp3Info info = listMp3.get(play_position);
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra(AppConstant.MSG_PROGRESS, play_progress);
            intent.setClass(MainActivity.this, PlayerService.class);
            startService(intent);
        }
    }

    @Override
    public void onPlayerServiceProgress(int progress) {

    }

    @Override
    public void onPlayerServiceStop() {

    }

    @Override
    public void onPlayerServicePlay() {

    }

    //view
    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
