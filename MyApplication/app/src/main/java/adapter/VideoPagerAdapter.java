package adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tikeyc.mobileplayer.R;

import java.util.ArrayList;

import domain.MediaItem;
import pager.VideoPager;
import utils.TimeUtils;

/**
 * Created by public1 on 2017/1/5.
 */

public class VideoPagerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MediaItem> mediaItems;

    class ViewHolder {
        ImageView video_icon;
        TextView video_name;
        TextView video_duration;
        TextView video_size;
    }

    public VideoPagerAdapter(Context context,ArrayList<MediaItem> mediaItems) {
        this.context = context;
        this.mediaItems = mediaItems;
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mediaItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(context, R.layout.item_video_pager,null);
            //
            ImageView video_icon = (ImageView) view.findViewById(R.id.imageView_icon);
            TextView video_name = (TextView) view.findViewById(R.id.textView_name);
            TextView video_duration = (TextView) view.findViewById(R.id.textView_duration);
            TextView video_size = (TextView) view.findViewById(R.id.textView_size);
            //
            viewHolder = new ViewHolder();
            viewHolder.video_icon = video_icon;
            viewHolder.video_name = video_name;
            viewHolder.video_duration = video_duration;
            viewHolder.video_size = video_size;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        MediaItem mediaItem = mediaItems.get(i);
//            viewHolder.video_icon.setImageBitmap(mediaItem.get);
        viewHolder.video_name.setText(mediaItem.getName());
        //
        TimeUtils timeUtils = new TimeUtils();
//            String duration = timeUtils.formatDuring(mediaItem.getDuration());
        String duration = timeUtils.getTime(mediaItem.getDuration(),TimeUtils.DATE_FORMAT_HOUR);
        viewHolder.video_duration.setText(duration);
//            //
        String  size = Formatter.formatFileSize(context,mediaItem.getSize());
        viewHolder.video_size.setText(size);

        return view;
    }
}
