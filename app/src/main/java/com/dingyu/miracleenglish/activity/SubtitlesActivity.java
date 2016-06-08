package com.dingyu.miracleenglish.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dingyu.miracleenglish.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dyu on 16-6-6.        Log.d(TAG, );

 */

public class SubtitlesActivity extends Activity implements AdapterView.OnItemClickListener{
    private static final String TAG = "SubtitlesActivity";

    static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> sdcardSubtitles = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.subtitles_activity);
        listView = (ListView)findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty_view));
        listView.setOnItemClickListener(this);

        initData();
    }

    private void initData(){
        List<String> subtitles = getDefaultSubtitles();
        subtitles.addAll(getSdcardSubtitles());

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subtitles);
        listView.setAdapter(adapter);
    }

    private List<String> getDefaultSubtitles(){
        String[] files = null;
        try{
            files = getAssets().list("");
        }catch (IOException e){
            e.printStackTrace();
        }

        return filterSubtitles(files);
    }

    private List<String> getSdcardSubtitles() {
        File file = new File(SDCARD_PATH, "MiracleEnglish");
        Log.d(TAG, file.getAbsolutePath()+","+file.canRead());
        return filterSubtitles(file.list());
    }

    private List<String> filterSubtitles(String[] fileNames){
        List<String> subtitles = new ArrayList<String>();
        if(fileNames != null){
            for (String file: fileNames) {
                Log.d(TAG, file);
                if(file != null && file.endsWith(".srt")){
                    subtitles.add(file);
                    sdcardSubtitles.add(file);
                }
            }
        }
        return subtitles;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String subtitle = adapter.getItem(position);

        if(sdcardSubtitles.contains(subtitle)){

        }

        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.putExtra(MainActivity.SUBTITLE_NAME, subtitle);

        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
