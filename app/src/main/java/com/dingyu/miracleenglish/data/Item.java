package com.dingyu.miracleenglish.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dyu on 16-6-2.
 */

public class Item extends RealmObject{
    @PrimaryKey
    private int id;

    private long startTime;
    private long endTime;

    private String chinese;
    private String english;

    public Item() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getChinese() {
        return chinese;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getEnglish() {
        return english;
    }

    public String getDouble(){
        return chinese+"\r\n\r\n"+english;
    }

    @Override
    public String toString() {
        return "id="+id+",time="+startTime+"-->"+endTime+",chinese="+chinese+",english="+english;
    }
}
