package com.dingyu.miracleenglish.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.dingyu.miracleenglish.R;
import com.dingyu.miracleenglish.adapter.ItemAdapter;
import com.dingyu.miracleenglish.data.Item;
import com.dingyu.miracleenglish.data.Subtitle;
import com.dingyu.miracleenglish.parse.ParseSrt;
import com.dingyu.miracleenglish.util.ConstantsUtil;
import com.dingyu.miracleenglish.util.LogUtil;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.File;
import java.util.List;

import io.realm.Realm;

import static com.dingyu.miracleenglish.adapter.ItemAdapter.MODEL_CHINESE;
import static com.dingyu.miracleenglish.adapter.ItemAdapter.MODEL_DOUBLE;
import static com.dingyu.miracleenglish.adapter.ItemAdapter.MODEL_ENGLISH;


public class SubtitlePlayActivity extends Activity implements SwipeFlingAdapterView.onFlingListener, SwipeFlingAdapterView.OnItemClickListener, View.OnClickListener {
    public static final String SUBTITLE_NAME = "subtitle_name";

    private static final int MESSAGE_STOP = 0;

    private SwipeFlingAdapterView swipeFlingAdapterView;
    private Button resetButton;
    private Button modelButton;
    private Button playButton;

    private ItemAdapter adapter;
    private Subtitle subtitle;
    private List<Item> items;

    private Realm realm;

    private MediaPlayer mediaPlayer;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_STOP){
                if(mediaPlayer != null && mediaPlayer.getCurrentPosition() > msg.arg1+500){
                    LogUtil.d("stop "+mediaPlayer.getCurrentPosition());
                    mediaPlayer.pause();
                }else {
                    Message message = new Message();
                    message.what = msg.what;
                    message.arg1 = msg.arg1;
                    handler.sendMessageDelayed(message, 500);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subtitle_play_activity);

        swipeFlingAdapterView = (SwipeFlingAdapterView)findViewById(R.id.swipe_view);
        resetButton = (Button)findViewById(R.id.reset_button);
        modelButton = (Button)findViewById(R.id.model_button);
        playButton = (Button)findViewById(R.id.play_button);

        adapter = new ItemAdapter(this);
        swipeFlingAdapterView.setAdapter(adapter);
        swipeFlingAdapterView.setFlingListener(this);
        swipeFlingAdapterView.setOnItemClickListener(this);

        resetButton.setOnClickListener(this);
        modelButton.setOnClickListener(this);
        playButton.setOnClickListener(this);

        String subtitleFileName = getIntent().getExtras().getString(SUBTITLE_NAME);

        realm = Realm.getDefaultInstance();

        subtitle = realm.where(Subtitle.class).equalTo("title", subtitleFileName).findFirst();

        items = ParseSrt.parse(subtitle.getTitle());
        adapter.setItems(items);
        adapter.removeItems(0, subtitle.getCurrentItemIndex());

        LogUtil.v("title="+subtitleFileName);

        String mp3FileName = subtitleFileName.replace(".ch&en.srt", ".mp3");
        File file = new File(ConstantsUtil.STROAGE_DIR_FILE, mp3FileName);
        if(file.exists()){
            mediaPlayer = new MediaPlayer();
            try{
                mediaPlayer.setDataSource(file.getPath());
                mediaPlayer.prepare();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }

        handler.removeMessages(MESSAGE_STOP);

        super.onDestroy();
    }

    @Override
    public void removeFirstObjectInAdapter() {
        LogUtil.d("remove object");
        adapter.removeItem(0);

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                subtitle.setCurrentItemIndex(subtitle.getCurrentItemIndex()+1);
            }
        });
    }

    @Override
    public void onLeftCardExit(Object dataObject) {

    }

    @Override
    public void onRightCardExit(Object dataObject) {

    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
    }

    @Override
    public void onScroll(float scrollProgressPercent) {
        View view = swipeFlingAdapterView.getSelectedView();
        float alpha = scrollProgressPercent > 0 ? 1 - scrollProgressPercent / 2: 1 + scrollProgressPercent/2;
        if(alpha >= 1.0) alpha = 1.0f;
        if(alpha <= 0.0) alpha = 0.0f;
        if(view != null) view.setAlpha(alpha);
    }

    @Override
    public void onItemClicked(int itemPosition, Object dataObject) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reset_button:
                adapter.setItems(items);
                realm.executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm) {
                        subtitle.setCurrentItemIndex(0);
                    }
                });
                break;

            case R.id.model_button:
                int model = adapter.getModel();
                if(model == MODEL_CHINESE){
                    model = MODEL_ENGLISH;
                    modelButton.setText("英");
                }else if(model == MODEL_ENGLISH){
                    model = MODEL_DOUBLE;
                    modelButton.setText("中英");
                }else if(model == MODEL_DOUBLE){
                    model = MODEL_CHINESE;
                    modelButton.setText("中");
                }
                adapter.setModel(swipeFlingAdapterView, model);
                break;

            case R.id.play_button:
                Item item = adapter.getItem(0);
                play(item.getStartTime()-500, item.getEndTime());
                break;

            default:
                break;
        }
    }

    private void play(int start, int end){
        if(mediaPlayer != null){
            try{
                start = start >= 0 ? start:0;
                LogUtil.v("position="+start+"-"+end+","+mediaPlayer.getCurrentPosition());
                mediaPlayer.seekTo(start);
                mediaPlayer.start();
                LogUtil.v("2:"+mediaPlayer.getCurrentPosition());
                Message message = new Message();
                message.what = MESSAGE_STOP;
                message.arg1 = end;
                handler.sendMessageDelayed(message, 500);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
