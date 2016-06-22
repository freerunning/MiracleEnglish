package com.dingyu.miracleenglish.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dyu on 16-6-20.
 */

public class InitDataEvent {
    private List<String> subtitleFileName = new ArrayList<String>();

    public InitDataEvent(List<String> subtitleFileName) {
        this.subtitleFileName = subtitleFileName;
    }

    public List<String> getSubtitleFileName() {
        return subtitleFileName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("InitDataEvent");
        for (String name : subtitleFileName) {
            builder.append(name);
            builder.append(";");
        }
        return builder.toString();
    }
}
