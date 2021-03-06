package domain;

import java.io.Serializable;

/**
 * Created by public1 on 2017/1/5.
 *
 * 代表一个视频或音频
 */

//实现Serializable接口 序列化对象，以便用Bundle putSerializable()传递数组对象
public class MediaItem implements Serializable {

    private String name;
    private long duration;
    private long size;
    private String data;
    private String artist;

    public String getName() {
        return name;
    }

    public long getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    public String getData() {
        return data;
    }

    public String getArtist() {
        return artist;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public void setSize(long size) {
        this.size = size;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", data='" + data + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}
