package com.dingyu.miracleenglish.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dyu on 16-6-2.
 */

public class Item extends RealmObject{
    @PrimaryKey
    private int id;

    private int startTime;
    private int endTime;

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

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getEndTime() {
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
