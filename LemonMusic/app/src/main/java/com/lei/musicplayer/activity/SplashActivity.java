package com.lei.musicplayer.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import com.lei.musicplayer.R;
import com.lei.musicplayer.application.AppCache;
import com.lei.musicplayer.database.DatabaseClient;
import com.lei.musicplayer.service.PlayerService;
import com.lei.musicplayer.service.ScanCallBack;
import com.lei.musicplayer.util.L;
import com.lei.musicplayer.util.ToastTool;
import com.lei.musicplayer.util.Util;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        init();
        getPermission();
        checkPermisson();
    }

    private void init() {
        AppCache.init(getApplication());
        Util.init(this);
        DatabaseClient.init(this);
        ToastTool.init(this);
    }

    private void checkPermisson() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isPermission())
                    checkService();
                else
                    checkPermisson();
            }
        },500);
    }

    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 2;

    private void getPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }


    private boolean isPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void checkService() {
        if (AppCache.getPlayService() == null){
            startPlayService();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindPlayerService();
                }
            }, 2000);
        }else {
            startMainActivity();
            finish();
        }
    }

    private void bindPlayerService() {
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }


    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void startPlayService() {
        Intent intent = new Intent(this, PlayerService.class);
        startService(intent);
    }

    PlayerServiceConnection conn = new PlayerServiceConnection();
    public class PlayerServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService playerService = (PlayerService) ((PlayerService.PlayerBinder) service).getService();
            AppCache.setPlayService(playerService);

            playerService.scanLocalMusic(new ScanCallBack() {
                @Override
                public void onFail(String msg) {
                    L.i(TAG, "scanLocalMusic onFail " + msg);
                    startMainActivity();
                    finish();
                }

                @Override
                public void onFinish() {
                    L.i(TAG, "onSuccess " + AppCache.getLocalMusicList().size());
                    startMainActivity();
                    finish();
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            L.i(TAG, "onServiceDisconnected name: " + name);
        }
    };

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        if (conn != null){
            unbindService(conn);
        }
        super.onDestroy();
    }
//    @Override
//    protected void onDestroy() {
//        if (conn != null && AppCache.getPlayService() == null){
//            unbindService(conn);
//        }
//        super.onDestroy();
//    }
}
