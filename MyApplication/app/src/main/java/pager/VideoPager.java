package pager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tikeyc.mobileplayer.R;
import com.tikeyc.mobileplayer.activity.SystemVideoPlayer;

import java.util.ArrayList;

import adapter.VideoPagerAdapter;
import base.BasePager;
import domain.MediaItem;

/**
 * Created by public1 on 2017/1/4.
 */

public class VideoPager extends BasePager {

    private ListView listView;
    private TextView textView_no_media;
    private ProgressBar progressBar;


    private ArrayList<MediaItem> mediaItems;

    private VideoPagerAdapter listAdapter;
    /**
     * 刷新UI
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems != null && mediaItems.size() > 0) {
                isInitData = true;
                textView_no_media.setVisibility(View.GONE);

                //
                listAdapter = new VideoPagerAdapter(context,mediaItems);
                listView.setAdapter(listAdapter);
            } else {
                isInitData = false;
                textView_no_media.setVisibility(View.VISIBLE);
            }

            //
            progressBar.setVisibility(View.GONE);
        }
    };

    public VideoPager(Context context) {
        super(context);

    }

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.video_pager,null);

        listView = (ListView) view.findViewById(R.id.listView);
        textView_no_media = (TextView) view.findViewById(R.id.textView_no_media);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MediaItem mediaItem = mediaItems.get(i);
                Log.e("TAG",mediaItem.toString());

                //1调用系统所有播放器-隐式意图
//                Intent intent = new Intent();
//                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//                context.startActivity(intent);

                //2调用自定义播放器
//                Intent intent = new Intent(context, SystemVideoPlayer.class);
//                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//                context.startActivity(intent);

                //3传递列表数据
                Intent intent = new Intent(context, SystemVideoPlayer.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("medioItems",mediaItems);
                intent.putExtras(bundle);
                intent.putExtra("position",i);
                context.startActivity(intent);

            }
        });
        return view;
    }


    @Override
    public void initData() {
        super.initData();

        //加载本地视频时间
        getDataFromLocalVideo();


        Log.e("TAG","本地视频界面");
        
    }


    /**
     * 从本地sdcard获取
     * 方式一:遍历sdcard，后缀名（慢）
     * 方式二:从内容提供者里面获取（媒体扫描器）
     * 如果是6.0以上须动态获取sdcard权限
     */
    private void getDataFromLocalVideo() {


        new Thread(){
            @Override
            public void run() {
                super.run();

                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,//名称
                        MediaStore.Video.Media.DURATION,//时长
                        MediaStore.Video.Media.SIZE,//大小
                        MediaStore.Video.Media.DATA,//视频数据（视频绝对地址）
                        MediaStore.Video.Media.ARTIST,//歌曲的演唱者
                };

                //
                mediaItems = new ArrayList<MediaItem>();
                Cursor cursor = contentResolver.query(uri,objs,null,null,null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        MediaItem mediaItem = new MediaItem();
                        mediaItems.add(mediaItem);
                        //
                        String name = cursor.getString(0);
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);
                        mediaItem.setArtist(artist);
                    }
                    cursor.close();
                }

                //刷新UI
                handler.sendEmptyMessageDelayed(1,2000);
            }
        }.start();
    }



}
