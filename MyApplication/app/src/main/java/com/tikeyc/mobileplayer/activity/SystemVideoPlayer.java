package com.tikeyc.mobileplayer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.VideoView;
import view.VideoView;

import com.tikeyc.mobileplayer.R;

import java.util.ArrayList;

import domain.MediaItem;
import utils.TimeUtils;


public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    private static final  int PROGRESS_TIME = 1;
    private static final int Hidden_media_controller = 2;

    ///////////////
    private VideoView videoView;
    private ArrayList<MediaItem> mediaItems;
    private int current_mediaItems_position;
    private Uri uri;
    private TimeUtils timeUtils;
    private View media_controller;

    ///////////////
    private MyReceive myReceive;//监听电量变化的广播
    private GestureDetector gestureDetector;

    private AudioManager audioManager;//调节音量
    private int current_voice;
    private int max_voice;//值域 0~15


    ///////////////
    private int screenWidth = 0;
    private int screenHeight = 0;
    private int mVideoWidth;//视频真实宽
    private int mVideoHeight;//视频真实高

    ///////////////
    private TextView textViewVideoName;
    private TextView textViewGarry;
    private TextView textViewSystemTime;
    private ImageView imageViewVoice;
    private SeekBar seekBarVoice;
    private ImageView imageViewVoiceRightIcon;
    private TextView textViewCurrentTime;
    private SeekBar seekBarCurrentTime;
    private TextView textViewTotalTime;
    private Button buttonBack;
    private Button buttonLastVideo;
    private Button buttonStartOrPauseVideo;
    private Button buttonNextVideo;
    private Button buttonSwitchScreenOrientation;



    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-01-06 11:46:12 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        textViewVideoName = (TextView)findViewById( R.id.textView_video_name );
        textViewGarry = (TextView)findViewById( R.id.textView_garry );
        textViewSystemTime = (TextView)findViewById( R.id.textView_system_time );
        imageViewVoice = (ImageView)findViewById( R.id.imageView_voice );
        seekBarVoice = (SeekBar)findViewById( R.id.seekBar_voice );
        imageViewVoiceRightIcon = (ImageView)findViewById( R.id.imageView_voice_right_icon );
        textViewCurrentTime = (TextView)findViewById( R.id.textView_current_time );
        seekBarCurrentTime = (SeekBar)findViewById( R.id.seekBar_current_time );
        textViewTotalTime = (TextView)findViewById( R.id.textView_total_time );
        buttonBack = (Button)findViewById( R.id.button_back );
        buttonLastVideo = (Button)findViewById( R.id.button_last_video );
        buttonStartOrPauseVideo = (Button)findViewById( R.id.button_start_or_pause_video );
        buttonNextVideo = (Button)findViewById( R.id.button_next_video );
        buttonSwitchScreenOrientation = (Button)findViewById( R.id.button_switch_screen_orientation );

        buttonBack.setOnClickListener( this );
        buttonLastVideo.setOnClickListener( this );
        buttonStartOrPauseVideo.setOnClickListener( this );
        buttonNextVideo.setOnClickListener( this );
        buttonSwitchScreenOrientation.setOnClickListener( this );
    }


    private boolean isFullScreen = true;
    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-01-06 11:46:12 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == buttonBack ) {
            // Handle clicks for buttonBack
            finish();

        } else if ( v == buttonLastVideo ) {
            // Handle clicks for buttonLastVideo

            if (current_mediaItems_position == 0){
                Toast.makeText(this,"已到第一个视频",Toast.LENGTH_SHORT).show();
                return;
            }
            current_mediaItems_position--;
            MediaItem mediaItem = mediaItems.get(current_mediaItems_position);
            textViewVideoName.setText(mediaItem.getName());

            videoView.setVideoPath(mediaItem.getData());

        } else if ( v == buttonStartOrPauseVideo ) {
            // Handle clicks for buttonStartOrPauseVideo

            if (videoView.isPlaying()) {
                handler.removeMessages(PROGRESS_TIME);
                videoView.pause();
            } else  {
                videoView.start();
                handler.sendEmptyMessage(PROGRESS_TIME);
            }

        } else if ( v == buttonNextVideo ) {
            // Handle clicks for buttonNextVideo

            if (current_mediaItems_position >= mediaItems.size() - 1){
                Toast.makeText(this,"已到最后一个视频",Toast.LENGTH_SHORT).show();
                return;
            }
            current_mediaItems_position++;
            MediaItem mediaItem = mediaItems.get(current_mediaItems_position);
            textViewVideoName.setText(mediaItem.getName());

            videoView.setVideoPath(mediaItem.getData());

        } else if ( v == buttonSwitchScreenOrientation ) {
            // Handle clicks for buttonSwitchScreenOrientation

            isFullScreen = !isFullScreen;
            setVideoViewSizeisFullScreen(isFullScreen);
        }

        //
        if ( v != buttonSwitchScreenOrientation ) {
            handler.removeMessages(PROGRESS_TIME);
            handler.removeMessages(Hidden_media_controller);
        }

    }


    private void setVideoViewSizeisFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
            videoView.setVideoSize(screenWidth,screenHeight);
        } else {
            int height = screenHeight;//屏幕的高
            int width = screenWidth;//屏幕的宽
            if (mVideoWidth * height < width * mVideoHeight) {
                width = height * mVideoWidth / mVideoHeight;

            } else if (mVideoWidth * height > width * mVideoHeight) {
                height = width * mVideoHeight / mVideoWidth;
            }

            videoView.setVideoSize(width,height);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);


        /**解决横竖屏切换导致的Activity重新加载的问题：在AndroidManifest.xml对应的Activity添加
         * android:configChanges="keyboardHidden|orientation|screenSize">
         * */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //
        timeUtils = new TimeUtils();
        //
        videoView = (VideoView) findViewById(R.id.videoview);
        //设置控制面板
//        videoView.setMediaController(new MediaController(this));
        findViews();
        media_controller = findViewById(R.id.media_controller);
        media_controller.setVisibility(View.GONE);//默认隐藏
        //电量广播
        initData();
        //设置监听
        setVideoViewOnListener();
        //设置视频
        getData();

        //实例化手势识别器
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isShow_media_controller) {
                    media_controller.setVisibility(View.GONE);
                } else {
                    media_controller.setVisibility(View.VISIBLE);
                }
                isShow_media_controller = !isShow_media_controller;
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (videoView.isPlaying()) {
                    handler.removeMessages(PROGRESS_TIME);
                    videoView.pause();
                } else  {
                    videoView.start();
                    handler.sendEmptyMessage(PROGRESS_TIME);
                }
                return super.onDoubleTap(e);
            }

        });



    }


    //获取传过来的音频数据
    private void getData() {
        Intent intent = getIntent();
        uri = intent.getData();

        mediaItems = (ArrayList<MediaItem>) intent.getSerializableExtra("medioItems");
        current_mediaItems_position = intent.getIntExtra("position",0);

        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(current_mediaItems_position);
            textViewVideoName.setText(mediaItem.getName());

            videoView.setVideoPath(mediaItem.getData());

        } else if (uri != null){

            videoView.setVideoURI(uri);
        } else {

        }
    }

    ////////////////////手机电量
    private void initData() {
        //注册电量广播
        myReceive = new MyReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myReceive,intentFilter);

        //过时方法
//        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        //音量
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        current_voice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        max_voice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        seekBarVoice.setMax(max_voice);
        seekBarVoice.setProgress(current_voice);
    }

    class MyReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level",0);//电量：0~100
            if (level <=0 ){
                textViewGarry.setText("电量:" + level);
            } else if (level <= 10) {
                textViewGarry.setText("电量:" + level);
            } else if (level <= 20) {
                textViewGarry.setText("电量:" + level);
            } else {
                textViewGarry.setText("电量:" + level);
            }

        }
    }

    ///////////////消息处理，进度条
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case PROGRESS_TIME:{
                    //视频进度设置
                    int current_position = videoView.getCurrentPosition();
                    seekBarCurrentTime.setProgress(current_position);
                    textViewCurrentTime.setText(timeUtils.getTime(current_position,TimeUtils.DATE_FORMAT_HOUR));

                    //设置系统时间
                    textViewSystemTime.setText(timeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_HOUR_MINUTE));
                    //每秒更新
                    handler.removeMessages(PROGRESS_TIME);
                    handler.sendEmptyMessageDelayed(PROGRESS_TIME,1000);
                }
                break;
                case Hidden_media_controller:{
                    handler.removeMessages(Hidden_media_controller);
                    media_controller.setVisibility(View.GONE);
                    isShow_media_controller = !isShow_media_controller;
                }
                break;
                case 3:{

                }
                break;
                default:
                    break;
            }
        }
    };

    ////////////////隐藏、显示media_controller
    private boolean isShow_media_controller = false;
    private float start_Y;
    private float touchRang;//屏幕高
    private int m_voice;//按下屏幕是的音量
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //把事件传递给手势识别器
        gestureDetector.onTouchEvent(event);
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (isShow_media_controller) {
//                media_controller.setVisibility(View.GONE);
//            } else {
//                media_controller.setVisibility(View.VISIBLE);
//            }
//        isShow_media_controller = !isShow_media_controller;
//        }

//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:{
//                start_Y = event.getY();
//                m_voice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                touchRang = Math.min(screenHeight,screenWidth);
//                handler.removeMessages(Hidden_media_controller);
//            }
//            break;
//            case MotionEvent.ACTION_MOVE:{
//                float end_Y = event.getY();
//                float distance_Y = start_Y - end_Y;
//                float delta = (distance_Y / touchRang) * max_voice;
//                //
//                int voice = (int) Math.min(Math.max(m_voice + delta,0),max_voice);
//                if (delta != 0) {
//                    int flag = 0;//1：把系统的调出来，0：相反
//                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,voice,flag);
//                }
//            }
//            break;
//            case MotionEvent.ACTION_UP:{
//                handler.sendEmptyMessage(Hidden_media_controller);
//            }
//            break;
//            default:
//                break;
//        }

        return super.onTouchEvent(event);
    }

    /////////////////////视频相关监听及处理
    public void setVideoViewOnListener() {
        //准备好的监听
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //
                mVideoWidth = mediaPlayer.getVideoWidth();
                mVideoHeight = mediaPlayer.getVideoHeight();
                //开始播放
                videoView.start();
                //
                int duration = mediaPlayer.getDuration();//或videoView.getDuration()

                seekBarCurrentTime.setMax(duration);
                textViewTotalTime.setText(timeUtils.getTime(duration,TimeUtils.DATE_FORMAT_HOUR));

                handler.sendEmptyMessage(PROGRESS_TIME);

                setVideoViewSizeisFullScreen(true);
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

        //设置视频进度seekBar状态监听
        seekBarCurrentTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**滑动回调
             * @param seekBar
             * @param i 进度
             * @param b 如果是用户引起：true, 自动更新：false
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    videoView.seekTo(i);
                }

            }

            /**手机触碰回调
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**手指离开回调
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //设置音量seekBar状态监听
        seekBarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**滑动回调
             * @param seekBar
             * @param i 进度
             * @param b 如果是用户引起：true, 自动更新：false
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    int flag = 0;//1：把系统的调出来，0：相反
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,flag);
                }

            }

            /**手机触碰回调
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**手指离开回调
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    //////////////////////////////
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

        //
        if (myReceive != null){
            unregisterReceiver(myReceive);
            myReceive = null;
        }
        //
        handler.removeMessages(PROGRESS_TIME);
        handler.removeMessages(Hidden_media_controller);

        super.onDestroy();
     }
}
