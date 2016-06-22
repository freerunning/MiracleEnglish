package com.dingyu.miracleenglish.activity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by dyu on 16-6-15.
 */

public class ActivityUtil {
    public static void startSubtitlePlayActivity(Context context, String subtitle){
        Intent intent = new Intent();
        intent.setClass(context, SubtitlePlayActivity.class);
        intent.putExtra(SubtitlePlayActivity.SUBTITLE_NAME, subtitle);

        context.startActivity(intent);
    }
}
