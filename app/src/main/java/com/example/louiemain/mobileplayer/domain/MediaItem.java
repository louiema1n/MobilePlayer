package com.example.louiemain.mobileplayer.domain;

import java.io.Serializable;

/**
 * @description 媒体Item
 * @author&date Created by louiemain on 2018/3/13 23:22
 */
public class MediaItem implements Serializable {
    private String displayName;
    private long duration;
    private long size;
    private String data;
    private String artist;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
