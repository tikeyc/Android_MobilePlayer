package com.tikeyc.mobileplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.tikeyc.mobileplayer.R;

public class SystemVideoPlayer extends Activity {

    private VideoView videoView;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);


        /**解决横竖屏切换导致的Activity重新加载的问题：在AndroidManifest.xml对应的Activity添加
         * android:configChanges="keyboardHidden|orientation|screenSize">
         * */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //
        videoView = (VideoView) findViewById(R.id.videoview);
        //设置控制面板
        videoView.setMediaController(new MediaController(this));

        //设置监听
        setVideoViewOnListener();
        //开始播放
        Intent intent = getIntent();
        uri = intent.getData();
        if (uri != null) {
            videoView.setVideoURI(uri);
            videoView.start();
        }
    }


    public void setVideoViewOnListener() {
        //准备好的监听
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

            }
        });

        //播放完成的监听
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //播放完成自动退出
                finish();
            }
        });

        //播放出错的监听
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(SystemVideoPlayer.this,"播放出错",Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
