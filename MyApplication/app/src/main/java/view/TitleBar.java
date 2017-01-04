package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tikeyc.mobileplayer.R;

/**自定义标题栏
 * Created by public1 on 2017/1/4.
 */

public class TitleBar extends LinearLayout implements View.OnClickListener {

    Context context;

    private View textView_search;
    private View rl_gift;
    private View rl_add;

    /**代码创建时调用
     * @param context
     */
    public TitleBar(Context context) {
        super(context);

        this.context = context;
    }


    /**布局文件使用该给时会调用
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, AttributeSet attrs) {
//        super(context, attrs);
        this(context,attrs,0);

        this.context = context;
    }


    /**当需要设置样式的时候
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
    }


    /**
     * 当布局文件加载完成的时候回调该方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //
        textView_search = getChildAt(1);
        rl_gift = getChildAt(2);
        rl_add = getChildAt(3);

        //设置点击事件
        textView_search.setOnClickListener(this);
        rl_gift.setOnClickListener(this);
        rl_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_search:{
                Log.e("TAG","textView_search");
            }
            break;
            case R.id.rl_gift:{
                Log.e("TAG","rl_gift");
            }
            break;
            case R.id.rl_add:{
                Log.e("TAG","rl_add");
            }
            break;
            default:
                break;
        }
    }
}
