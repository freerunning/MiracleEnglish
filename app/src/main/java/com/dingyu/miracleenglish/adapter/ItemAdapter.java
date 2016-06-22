package com.dingyu.miracleenglish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dingyu.miracleenglish.R;
import com.dingyu.miracleenglish.data.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dyu on 16-6-2.
 */

public class ItemAdapter extends BaseAdapter {
    private static final String TAG = "ItemAdapter";

    public static final int MODEL_DOUBLE = 0;
    public static final int MODEL_CHINESE = 1;
    public static final int MODEL_ENGLISH = 2;

    private int model = MODEL_DOUBLE;
    private List<Item> items = new ArrayList<Item>();
    private int total;
    private Context context;
    private LayoutInflater inflater;

    public ItemAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public int getModel() {
        return model;
    }

    public void setModel(AdapterView view, int model){
        this.model = model;
        updateView(0, view);
    }


    public void setItems(List<Item> items){
        this.items.clear();
        this.items.addAll(items);
        total = items.size();
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        this.items.remove(position);
        notifyDataSetChanged();
    }

    public void removeItems(int start, int length){
        while (length > 0){
            this.items.remove(start);
            length--;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Item getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.item, parent, false);
            holder.itemId = (TextView)convertView.findViewById(R.id.item_id);
            holder.textView = (TextView) convertView.findViewById(R.id.text_view);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        Item item = getItem(position);
        setView(holder, item);

        return convertView;
    }

    public void updateView(int position, AdapterView view){
        int first = view.getFirstVisiblePosition();
        int last = view.getLastVisiblePosition();

        if(position - first >= 0 && last - position >= 0){
            View updateView = view.getChildAt(position - first);
            if(updateView != null){
                ViewHolder holder = (ViewHolder)updateView.getTag();
                Item item = getItem(position);
                setView(holder, item);
            }
        }
    }

    private void setView(ViewHolder holder, Item item){
        if(model == MODEL_CHINESE){
            holder.textView.setText(item.getChinese());
        }else if(model == MODEL_ENGLISH){
            holder.textView.setText(item.getEnglish());
        }else {
            holder.textView.setText(item.getDouble());
        }
        holder.itemId.setText(item.getId()+"/"+total);
    }

    public static class ViewHolder{
        TextView itemId;
        TextView textView;
    }
}
