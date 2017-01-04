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

public class VideoPager extends BasePager {

    private TextView textView;

    public VideoPager(Context context) {
        super(context);

    }

    @Override
    public View initView() {

        textView = new TextView(context);
        textView.setTextSize(24);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);


        return textView;
    }


    @Override
    public void initData() {
        super.initData();

        isInitData = true;
        textView.setText("本地视频界面");
        Log.e("TAG","本地视频界面");
        
    }
}
