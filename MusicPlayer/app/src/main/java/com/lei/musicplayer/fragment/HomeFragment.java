package com.lei.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.adapter.HomePagerAdapter;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.util.LogTool;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private static final String TAG = "HomeFragment";
    LinearLayout rlTitle;
    TextView tvPlayList, tvCollection;
    ViewPager homeViewPager;
    HomePagerAdapter pagerAdapter;
    List<View> homeViews = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    protected void initView(View view) {
        rlTitle = (LinearLayout) view.findViewById(R.id.rl_title);
        rlTitle.setPadding(0, AppCache.getSystemStatusHeight() * 2 + 45, 0, 0);
        tvPlayList = (TextView) view.findViewById(R.id.tv_playlist);
        tvCollection = (TextView) view.findViewById(R.id.tv_collection);
        homeViewPager = (ViewPager) view.findViewById(R.id.vp_home);
        View viewPlaylist = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_playlist, null);
        View viewCollection = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_collection, null);
        if (homeViews.size() > 0){
            homeViews.clear();
            LogTool.i(TAG,"size: " + homeViews.size());
        }
        homeViews.add(viewPlaylist);
        homeViews.add(viewCollection);
        pagerAdapter = new HomePagerAdapter(homeViews);
        homeViewPager.setAdapter(pagerAdapter);
        setListener();
    }

    private void setListener() {
        tvPlayList.setOnClickListener(this);
        tvCollection.setOnClickListener(this);
        homeViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_playlist:
                setTitleStatus(0);
                break;
            case R.id.tv_collection:
                setTitleStatus(1);
                break;
        }
    }

    private void setTitleStatus(int i) {
        homeViewPager.setCurrentItem(i);
        switch (i){
            case 0:
                tvPlayList.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvCollection.setTextColor(getResources().getColor(R.color.text_color));
                break;
            case 1:
                tvPlayList.setTextColor(getResources().getColor(R.color.text_color));
                tvCollection.setTextColor(getResources().getColor(R.color.colorPrimary));
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
    public void onDestroy() {
        super.onDestroy();
    }
}
