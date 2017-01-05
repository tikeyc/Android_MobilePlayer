package com.tikeyc.mobileplayer.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.tikeyc.mobileplayer.R;

import java.util.ArrayList;

import base.BasePager;
import fragment.MyPagerFragment;
import pager.AudioNetPager;
import pager.AudioPager;
import pager.VideoNetPager;
import pager.VideoPager;


/**
 * AndroidManifest.xml中设置launchMode为singleTask
 */
public class MainActivity extends FragmentActivity {

    private FrameLayout frameLayout;
    private RadioGroup radioGroup;


    private ArrayList<BasePager> basePagers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        frameLayout = (FrameLayout) findViewById(R.id.fl_main_content);
        radioGroup = (RadioGroup) findViewById(R.id.tab_bar_button_radioGroup);

        //
        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));
        basePagers.add(new VideoNetPager(this));
        basePagers.add(new AudioPager(this));
        basePagers.add(new AudioNetPager(this));

        //
        radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        radioGroup.check(R.id.rb_video);

        //如果是6.0以上须动态获取sdcard权限
        isGrantExternalRW(this);
    }

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     * @param activity
     * @return
     */

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }


    private int position;
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i) {
                case R.id.rb_video:{
                    position = 0;
                }
                break;
                case R.id.rb_net_video:{
                    position = 1;
                }
                break;
                case R.id.rb_audio:{
                    position = 2;
                }
                break;
                case R.id.rb_net_audio:{
                    position = 3;
                }
                break;
                default:
                    break;
            }

            setFragment();
        }
    }

    /**
     * 把页面添加早Fragment中
     */
    private void setFragment() {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main_content,new MyPagerFragment(getBasePager()));
        fragmentTransaction.commit();
    }


    /**
     * @return
     */
    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if (basePager != null && !basePager.isInitData) {
            basePager.initData();
        }
        return basePager;
    }

}
