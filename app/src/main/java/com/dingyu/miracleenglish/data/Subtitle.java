package com.dingyu.miracleenglish.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dyu on 16-6-20.
 */

public class Subtitle extends RealmObject{
    @PrimaryKey
    private String title;

    private int currentItemIndex;

    public Subtitle() {
    }

    public int getCurrentItemIndex() {
        return currentItemIndex;
    }

    public void setCurrentItemIndex(int index) {
        this.currentItemIndex = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
