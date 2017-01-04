package pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import base.BasePager;

import static com.tikeyc.mobileplayer.R.id.textView;

/**
 * Created by public1 on 2017/1/4.
 */

public class AudioNetPager extends BasePager {

    private TextView textView;

    public AudioNetPager(Context context) {
        super(context);

    }

    @Override
    public View initView() {

        textView = new TextView(context);
        textView.setTextSize(24);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);


        return textView;
    }


    @Override
    public void initData() {
        super.initData();

        isInitData = true;
        textView.setText("网络音乐界面");
        Log.e("TAG","网络音乐界面");
    }
}
