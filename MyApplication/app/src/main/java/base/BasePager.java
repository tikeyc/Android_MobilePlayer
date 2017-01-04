package base;

import android.content.Context;
import android.view.View;

/**
 * Created by public1 on 2017/1/4.
 */

public abstract class BasePager {

    public final Context context;
    public final View rootView;
    public boolean isInitData = false;

    public BasePager(Context context) {

        this.context = context;

        rootView = initView();
    }

    /**
     * 由子类强制实现
     */
    public abstract View initView();


    /**
     * 由子类实现
     */
    public void initData() {

    }
}
