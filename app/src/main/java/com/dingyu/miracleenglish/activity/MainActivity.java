package com.dingyu.miracleenglish.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dingyu.miracleenglish.R;
import com.dingyu.miracleenglish.data.Item;
import com.dingyu.miracleenglish.data.Subtitle;
import com.dingyu.miracleenglish.event.ConstantsEvent;
import com.dingyu.miracleenglish.event.InitDataEvent;
import com.dingyu.miracleenglish.parse.ParseSrt;
import com.dingyu.miracleenglish.util.ConstantsUtil;
import com.dingyu.miracleenglish.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dyu on 16-6-6.
 */

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(this);

        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        listView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        EventBus.getDefault().post(ConstantsEvent.START_INIT_DATA);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(String event) {
        if (TextUtils.equals(event, ConstantsEvent.START_INIT_DATA)) {
            LogUtil.v("event=" + event);

            List<String> subtitleFileName = new ArrayList<String>();

            Realm realm = Realm.getDefaultInstance();
            RealmResults<Subtitle> subtitles = realm.where(Subtitle.class).findAll();
            for (Subtitle subtitle : subtitles) {
                LogUtil.v("readlm title=" + subtitle.getTitle());
                subtitleFileName.add(subtitle.getTitle());
            }

            List<String> sdcardSubtitleFileName = getSdcardSubtitles();
            for (final String fileName : sdcardSubtitleFileName) {
                if (subtitleFileName.contains(fileName)) {
                    continue;
                }

                subtitleFileName.add(fileName);
                LogUtil.v("sdcard title=" + fileName);

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        try {
                            Subtitle subtitle = realm.createObject(Subtitle.class);
                            subtitle.setTitle(fileName);
                            subtitle.setCurrentItemIndex(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            EventBus.getDefault().post(new InitDataEvent(subtitleFileName));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(InitDataEvent event) {
        LogUtil.v("event=" + event.toString());

        listView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, event.getSubtitleFileName());
        listView.setAdapter(adapter);
    }

    private List<String> getSdcardSubtitles() {
        LogUtil.d(ConstantsUtil.STROAGE_DIR_FILE.getAbsolutePath());
        return filterSubtitles(ConstantsUtil.STROAGE_DIR_FILE.list());
    }

    private List<String> filterSubtitles(String[] fileNames) {
        List<String> subtitles = new ArrayList<String>();
        if (fileNames != null) {
            for (String file : fileNames) {
                LogUtil.d(file);
                if (file != null && file.endsWith(".srt")) {
                    subtitles.add(file);
                }
            }
        }
        return subtitles;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String subtitle = adapter.getItem(position);
        ActivityUtil.startSubtitlePlayActivity(this, subtitle);
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
        EventBus.getDefault().unregister(this);
    }
}
