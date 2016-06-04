package com.dingyu.miracleenglish.data;

/**
 * Created by dyu on 16-6-2.
 */

public class Item {
    private long startTime;
    private long endTime;

    private String chinese;
    private String english;

    public Item(long startTime, long endTime, String chinese, String english) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.chinese = chinese;
        this.english = english;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getChinese() {
        return chinese;
    }

    public String getEnglish() {
        return english;
    }

    public String getDouble(){
        return chinese+"\r\n\r\n"+english;
    }
}
