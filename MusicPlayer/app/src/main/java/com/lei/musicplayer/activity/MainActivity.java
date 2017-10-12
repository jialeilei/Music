package com.lei.musicplayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.lei.musicplayer.constant.AppConstant;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.FragmentAdapter;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.fragment.HomeFragment;
import com.lei.musicplayer.fragment.LocalFragment;
import com.lei.musicplayer.fragment.OnlineFragment;
import com.lei.musicplayer.fragment.PlayFragment;
import com.lei.musicplayer.service.PlayerService;
import com.lei.musicplayer.service.OnPlayMusicListener;
import com.lei.musicplayer.util.Util;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener ,ViewPager.OnPageChangeListener,
        SeekBar.OnSeekBarChangeListener,View.OnClickListener,OnPlayMusicListener {

    private static final String TAG = "MainActivity";
    DrawerLayout drawer;
    NavigationView navigationView;
    private int play_progress = 0;
    private int seekBarProgress = 0;
    //view
    private LocalFragment localFragment;
    private OnlineFragment onlineFragment;
    private HomeFragment homeFragment;
    private PlayFragment playFragment;
    ImageButton img_next, img_play, img_category;
    ImageView img_bottom;
    TextView tvMusicName,tvMusicAuthor,tvMusicDuration;
    SeekBar mSeekBarCurrent;
    ViewPager viewPager;
    TextView tvLocal,tvOnline,tvHome;
    private boolean haveLrc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPlayService().setOnPlayerListener(this);
        initView();
        setListener();
    }

    private void initView() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tvLocal = (TextView) findViewById(R.id.tv_local);
        tvOnline = (TextView) findViewById(R.id.tv_online);
        tvHome = (TextView)findViewById(R.id.tv_home);
        changeTitleColor(0);

        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        homeFragment = new HomeFragment();
        localFragment = new LocalFragment();
        onlineFragment = new OnlineFragment();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(homeFragment);
        fragmentAdapter.addFragment(localFragment);
        fragmentAdapter.addFragment(onlineFragment);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        img_category = (ImageButton) findViewById(R.id.img_category);
        img_category.setOnClickListener(this);
        //bottom play
        img_play = (ImageButton) findViewById(R.id.img_play);
        img_next = (ImageButton) findViewById(R.id.img_next);
        img_bottom = (ImageView) findViewById(R.id.img_music_bottom);
        tvMusicName = (TextView) findViewById(R.id.tv_music_name);
        tvMusicAuthor = (TextView) findViewById(R.id.tv_music_author);
        tvMusicDuration = (TextView) findViewById(R.id.tv_music_duration);
        mSeekBarCurrent = (SeekBar) findViewById(R.id.seek_bar_current);
        setBottomMusicInfo();

    }

    private void setBottomMusicInfo() {
        tvMusicName.setText(AppCache.getPlayingMusic().getTitle());
        tvMusicAuthor.setText(AppCache.getPlayingMusic().getArtist());
        String imgPath = AppCache.getPlayingMusic().getAlbumArt();
        Glide.with(this).load(imgPath).placeholder(R.mipmap.default_music).into(img_bottom);
    }

    private void setListener() {
        viewPager.setOnPageChangeListener(this);
        tvLocal.setOnClickListener(this);
        tvOnline.setOnClickListener(this);
        tvHome.setOnClickListener(this);
        img_play.setOnClickListener(this);
        img_next.setOnClickListener(this);
        img_bottom.setOnClickListener(this);
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

    }

    @Override
    public void onPageSelected(int position) {
        changeTitleColor(position);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBarProgress = progress * (int) AppCache.getPlayingMusic().getDuration() / 100;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //sendPlayInfo(AppConstant.ACTION_PLAY_STOP,true);
        getPlayService().seekBarPlay(seekBarProgress);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void changeTitleColor(int position) {
        switch (position){
            case 0:
                tvLocal.setTextColor(getResources().getColor(R.color.gray_e));
                tvOnline.setTextColor(getResources().getColor(R.color.gray_e));
                tvHome.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                tvLocal.setTextColor(getResources().getColor(R.color.white));
                tvOnline.setTextColor(getResources().getColor(R.color.gray_e));
                tvHome.setTextColor(getResources().getColor(R.color.gray_e));
                break;
            case 2:
                tvLocal.setTextColor(getResources().getColor(R.color.gray_e));
                tvOnline.setTextColor(getResources().getColor(R.color.white));
                tvHome.setTextColor(getResources().getColor(R.color.gray_e));

                break;
        }

    }

    private MainReceiver mainReceiver;

    public class MainReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case AppConstant.ACTION_STATE:

                    break;
                case AppConstant.ACTION_PROGRESS:

                    break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_next:
                //sendPlayInfo(AppConstant.ACTION__NEXT, false);
                getPlayService().playNext();
                break;
            case R.id.img_play:
                //sendPlayInfo(AppConstant.ACTION_PLAY_STOP,false);
                getPlayService().playOrStop();

                break;
            case R.id.img_music_bottom:
                showPlayFragment();
                break;
            case R.id.tv_local:
                viewPager.setCurrentItem(1);
                changeTitleColor(1);
                break;
            case R.id.tv_online:
                viewPager.setCurrentItem(2);
                changeTitleColor(2);
                break;
            case R.id.tv_home:
                viewPager.setCurrentItem(0);
                changeTitleColor(0);
                break;
            case R.id.img_category:
                drawer.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }

    private void showPlayFragment() {
        if (isShowingFragment){
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (playFragment == null){
            playFragment = new PlayFragment();
            ft.replace(android.R.id.content,playFragment);
        }else {
            ft.show(playFragment);
        }
        //ft.commitAllowingStateLoss();
        ft.commitNow();
        isShowingFragment = true;
        isShowedFragment = true;


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playFragment.updateInfo();
                if (haveLrc){
                    playFragment.setLrc();
                }
            }
        },1000);
    }

    boolean isShowingFragment = false;
    boolean isShowedFragment = false;//是否展示过

    public void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(playFragment);
        ft.commitAllowingStateLoss();
        isShowingFragment = false;
    }

    /*
    * 启动服务播放音乐
    * */
    private void sendPlayInfo(String action,boolean usingSeekBar){
        if (AppCache.getLocalMusicList() != null ){
            Intent intent = new Intent();
            intent.setAction(action);
            if (usingSeekBar){
                intent.putExtra(AppConstant.MSG_PROGRESS, play_progress);
            }
            intent.setClass(MainActivity.this, PlayerService.class);
            startService(intent);
        }
    }

    @Override
    public void onMusicCurrentPosition(int currentPosition) {
        play_progress = currentPosition;
        int progress = play_progress * 100 / (int) AppCache.getPlayingMusic().getDuration();
        mSeekBarCurrent.setProgress(progress);
        tvMusicDuration.setText("" + Util.formatTime(play_progress));
        if (playFragment != null && isShowingFragment){
            playFragment.updateProgress(currentPosition);
        }

    }

    @Override
    public void onMusicStop() {
        img_play.setImageResource(R.mipmap.default_stop);
    }

    @Override
    public void onMusicPlay() {
        setBottomMusicInfo();
        img_play.setImageResource(R.mipmap.default_playing);
        localFragment.refreshMusicList();
        homeFragment.refreshMusicList();
    }




    @Override
    public void onMusicChange() {

        if (Util.initLrc(AppCache.getPlayingMusic()) != null){
            haveLrc = true;
        }else {
            haveLrc = false;
        }
        if (isShowedFragment){
            playFragment.setLrc();
        }
    }

    @Override
    public void onMusicComplete() {
        img_play.setImageResource(R.mipmap.default_stop);
    }

    //view
    @Override
    public void onBackPressed() {

        if (isShowingFragment) {
            hidePlayingFragment();
            return;
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
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
