package com.lei.musicplayer.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lei.musicplayer.R;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.bean.Mp3Info;
import com.lei.musicplayer.view.LrcView;


public class PlayFragment extends BaseFragment implements View.OnClickListener,View.OnTouchListener{

    private static final String TAG = "PlayFragment";
    RelativeLayout rlTop;
    ImageView imgDown;
    static TextView tvName,tvAuthor;
    public static LrcView lrcView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play,container,false);
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    protected void initView(View view) {

        rlTop = (RelativeLayout) view.findViewById(R.id.rl_top);
        rlTop.setPadding(0, AppCache.getSystemStatusHeight(),0,0);
        imgDown = (ImageView) view.findViewById(R.id.img_down);
        imgDown.setOnClickListener(this);
        tvName = (TextView) view.findViewById(R.id.tv_music_name);
        tvAuthor = (TextView) view.findViewById(R.id.tv_music_author);
        lrcView = (LrcView) view.findViewById(R.id.lrc_view);

    }

    public static void updateInfo(){
        Mp3Info info = AppCache.getPlayingMp3Info();
        tvAuthor.setText(info.getArtist());
        tvName.setText(info.getTitle());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        updateInfo();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_down:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //intercept event
        return true;
    }
}
