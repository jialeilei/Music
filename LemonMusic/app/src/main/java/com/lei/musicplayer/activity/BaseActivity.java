package com.lei.musicplayer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.service.PlayerService;


/**
 * Created by lei on 2017/8/3.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        setSystemBarTransparent();
        initView();
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void initData(){}

    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected boolean checkPlayService(){
        if (AppCache.getPlayService() == null){
            throw new NullPointerException("PlayerService is null");
        }
        return true;
    }

    public PlayerService getPlayService(){
        PlayerService service = AppCache.getPlayService();
        if (service == null){
            startActivity(new Intent(this,SplashActivity.class));
            AppCache.clearStack();
        }
        return service;
    }

}
