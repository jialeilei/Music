package com.lei.musicplayer.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lei.musicplayer.R;


public class HomeCollectionFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_collection, container, false);
        return view;
    }

    @Override
    protected void initView(View view) {

    }

}
