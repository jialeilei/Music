package com.lei.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.ViewPagerAdapter;
import com.lei.musicplayer.adapter.HomeListAdapter;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.Music;
import com.lei.musicplayer.database.DatabaseClient;
import com.lei.musicplayer.util.L;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener,
        ViewPager.OnPageChangeListener,TabLayout.OnTabSelectedListener{

    private static final String TAG = HomeFragment.class.getSimpleName();
    private TabLayout tbHome;
    ViewPager homeViewPager;
    ViewPagerAdapter pagerAdapter;
    public static HomeListAdapter playlistAdapter;
    ListView lvPlaylist;
    List<View> homeViews = new ArrayList<>();
    List<Music> playlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    protected void initView(View view) {

        homeViewPager = (ViewPager) view.findViewById(R.id.vp_home);
        View viewPlaylist = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_playlist, null);
        viewPlaylist.setPadding(0,0,0,90);
        View viewCollection = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_playlist, null);
        lvPlaylist = (ListView) viewPlaylist.findViewById(R.id.lv_playlist);
        //playlist
        playlist = DatabaseClient.getMusic();
        for (int i = 0; i < playlist.size(); i++) {
            L.i(TAG, "position: " + i + " musicId: " + playlist.get(i).getId());
        }
        playlistAdapter = new HomeListAdapter(getActivity(),playlist,R.layout.item_local_list);
        lvPlaylist.setAdapter(playlistAdapter);
        if (homeViews.size() > 0){
            homeViews.clear();
            L.i(TAG, "size: " + homeViews.size());
        }
        homeViews.add(viewPlaylist);
        homeViews.add(viewCollection);

        pagerAdapter = new ViewPagerAdapter(homeViews);
        homeViewPager.setAdapter(pagerAdapter);
        homeViewPager.setOnPageChangeListener(this);

        //tabLayout
        tbHome = (TabLayout) view.findViewById(R.id.tab_layout_home);
        tbHome.setPadding(0, AppCache.getSystemStatusHeight() * 2 + 45, 0, 0);
        tbHome.setOnTabSelectedListener(this);
        tbHome.setupWithViewPager(homeViewPager);
        if (tbHome.getTabCount() > 0){
            tbHome.removeAllTabs();
        }
        String[] titles = {getResources().getString(R.string.home_playlist), getResources().getString(R.string.home_collection)};
        for (int i = 0; i < titles.length; i++) {
            L.i(TAG, "titles: " + titles[i]);
            tbHome.addTab(tbHome.newTab().setText(titles[i]));
        }

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }


    private void setTitleStatus(int i) {
        homeViewPager.setCurrentItem(i);
        switch (i){
            case 0:

                break;
            case 1:

                break;
            default:

                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitleStatus(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void refreshListView() {
        playlistAdapter.refreshData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
