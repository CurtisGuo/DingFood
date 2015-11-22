package com.ding.dingfood.frontend.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ding.dingfood.R;
import com.ding.dingfood.frontend.vo.Item;

import java.util.List;

/**
 * Created by Kent on 2015/11/15.
 */
public class HistoryItemAdapter extends ArrayAdapter<Item> {

    public HistoryItemAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public HistoryItemAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            Log.i("Item flag", "convertView is:" + convertView);
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_history, null);
        }

        Item item = getItem(position);
        Log.i("Item flag","item title:"+item.getTitle());
        if (item != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.history_item_textView);
            ImageView iv1 = (ImageView)v.findViewById(R.id.history_item_imageView);
            Log.i("Item flag","tt1:"+tt1);
            if (tt1 != null) {
                tt1.setText(item.getTitle());
                iv1.setImageResource(R.mipmap.ic_launcher);
            }


        }

        return v;
    }
}

