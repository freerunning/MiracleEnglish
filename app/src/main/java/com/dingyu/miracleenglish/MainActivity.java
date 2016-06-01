package com.dingyu.miracleenglish;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;


public class MainActivity extends Activity implements SwipeFlingAdapterView.onFlingListener, SwipeFlingAdapterView.OnItemClickListener {
    private static final String TAG = "MainActivity";

    private SwipeFlingAdapterView swipeFlingAdapterView;

    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeFlingAdapterView = (SwipeFlingAdapterView)findViewById(R.id.swipe_view);

        arrayList = new ArrayList<String>();
        arrayList.add("luo");
        arrayList.add("just");
        arrayList.add("fuck");
        arrayList.add("li");
        arrayList.add("ju");
        arrayList.add("en");
        arrayList.add("!");

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.text_view, arrayList);

        swipeFlingAdapterView.setAdapter(arrayAdapter);
        swipeFlingAdapterView.setFlingListener(this);
        swipeFlingAdapterView.setOnItemClickListener(this);
    }

    @Override
    public void removeFirstObjectInAdapter() {
        Log.d(TAG, "remove object");
        arrayList.remove(0);
        arrayAdapter.notifyDataSetChanged();
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
}
