package com.dingyu.miracleenglish.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by dyu on 16-6-16.
 */

public class ConstantsUtil {
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();

    public static final File STROAGE_DIR_FILE = new File(SDCARD_PATH, "MiracleEnglish");
}
