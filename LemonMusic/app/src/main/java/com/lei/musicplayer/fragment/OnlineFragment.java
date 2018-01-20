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
import com.lei.musicplayer.adapter.RankAdapter;
import com.lei.musicplayer.adapter.ViewPagerAdapter;
import com.lei.musicplayer.application.AppCache;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2017/8/25.
 */
public class OnlineFragment extends BaseFragment implements TabLayout.OnTabSelectedListener,ViewPager.OnPageChangeListener{

    private static final String TAG = "OnlineFragment";
    private ViewPager vpOnline;
    private TabLayout tbOnline;
    private ViewPagerAdapter viewPagerAdapter;
    private List<View> viewList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_music_new, container, false);
        return view;
    }


    @Override
    protected void initView(View view) {
        String[] types = getResources().getStringArray(R.array.online_music_list_type);
        vpOnline = (ViewPager) view.findViewById(R.id.vp_online);
        if (viewList.size() > 0){
            viewList.clear();
        }
        for (int i = 0; i < types.length; i++) {
            ListView listView = new ListView(getActivity());
            RankAdapter adapter = new RankAdapter(getActivity(),i);
            listView.setAdapter(adapter);
            viewList.add(listView);
        }

        viewPagerAdapter = new ViewPagerAdapter(viewList);
        vpOnline.setAdapter(viewPagerAdapter);
        vpOnline.setOnPageChangeListener(this);
        //tabLayout
        tbOnline = (TabLayout) view.findViewById(R.id.tablayout_online);
        tbOnline.setOnTabSelectedListener(this);
        tbOnline.setPadding(0, AppCache.getSystemStatusHeight() * 2 + 45, 0, 0);
        tbOnline.setupWithViewPager(vpOnline);
        if (tbOnline.getTabCount() > 0){
            tbOnline.removeAllTabs();
        }
        String[] titles = getResources().getStringArray(R.array.online_music_list_title);
        for (int i = 0; i < titles.length; i++) {
            tbOnline.addTab(tbOnline.newTab().setText(titles[i]));
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
    public void onDestroy() {
        super.onDestroy();
    }

}
