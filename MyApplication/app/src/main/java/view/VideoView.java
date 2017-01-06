package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by public1 on 2017/1/6.
 */

public class VideoView extends android.widget.VideoView {


    /**代码中创建调用
     * @param context
     */
    public VideoView(Context context) {
        this(context,null);
    }

    /**布局文件中创建调用
     * @param context
     * @param attrs
     */
    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**当需要设置样式的时候
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }


    /**设置视频尺寸
     * @param videoWidth
     * @param videoHeight
     */
    public  void setVideoSize(int videoWidth, int videoHeight) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = videoWidth;
        layoutParams.height = videoHeight;
        setLayoutParams(layoutParams);
    }
}
