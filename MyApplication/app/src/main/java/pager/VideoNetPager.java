package pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import base.BasePager;

/**
 * Created by public1 on 2017/1/4.
 */

public class VideoNetPager extends BasePager {

    private TextView textView;

    public VideoNetPager(Context context) {
        super(context);

    }

    @Override
    public View initView() {

        textView = new TextView(context);
        textView.setTextSize(24);
        textView.setTextColor(Color.CYAN);
        textView.setGravity(Gravity.CENTER);


        return textView;
    }


    @Override
    public void initData() {
        super.initData();

        isInitData = true;
        textView.setText("网络视频界面");
        Log.e("TAG","网络视频界面");
    }
}
