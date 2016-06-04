package com.dingyu.miracleenglish.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dingyu.miracleenglish.R;
import com.dingyu.miracleenglish.adapter.ItemAdapter;
import com.dingyu.miracleenglish.data.Item;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import static com.dingyu.miracleenglish.adapter.ItemAdapter.MODEL_CHINESE;
import static com.dingyu.miracleenglish.adapter.ItemAdapter.MODEL_DOUBLE;
import static com.dingyu.miracleenglish.adapter.ItemAdapter.MODEL_ENGLISH;


public class MainActivity extends Activity implements SwipeFlingAdapterView.onFlingListener, SwipeFlingAdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = "MainActivity";

    private SwipeFlingAdapterView swipeFlingAdapterView;
    private Button modelButton;
    private Button playButton;

    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeFlingAdapterView = (SwipeFlingAdapterView)findViewById(R.id.swipe_view);
        modelButton = (Button)findViewById(R.id.model_button);
        playButton = (Button)findViewById(R.id.play_button);

        adapter = new ItemAdapter(this);
        swipeFlingAdapterView.setAdapter(adapter);
        swipeFlingAdapterView.setFlingListener(this);
        swipeFlingAdapterView.setOnItemClickListener(this);

        modelButton.setOnClickListener(this);
        playButton.setOnClickListener(this);

        Item item = new Item(0,0,"七国对这些野蛮人", "The Seven Kingdoms have waged war");
        adapter.addItem(item);

        item = new Item(0,0,"宣战数百年", "against these savages for centuries");
        adapter.addItem(item);
    }

    @Override
    public void removeFirstObjectInAdapter() {
        Log.d(TAG, "remove object");
        adapter.removeItem(0);
    }

    @Override
    public void onLeftCardExit(Object dataObject) {

    }

    @Override
    public void onRightCardExit(Object dataObject) {

    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        Item item = new Item(0,0,"七国对这些野蛮人", "The Seven Kingdoms have waged war");
        adapter.addItem(item);
    }

    @Override
    public void onScroll(float scrollProgressPercent) {
        Log.v(TAG, "scrollProgressPercent="+scrollProgressPercent);
        View view = swipeFlingAdapterView.getSelectedView();
        float alpha = scrollProgressPercent > 0 ? 1 - scrollProgressPercent / 2: 1 + scrollProgressPercent/2;
        if(alpha >= 1.0) alpha = 1.0f;
        if(alpha <= 0.0) alpha = 0.0f;
        view.setAlpha(alpha);
    }

    @Override
    public void onItemClicked(int itemPosition, Object dataObject) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                break;

            default:
                break;
        }
    }
}