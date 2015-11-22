package com.ding.dingfood.frontend.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ding.dingfood.R;
import com.ding.dingfood.frontend.adapter.HistoryItemAdapter;
import com.ding.dingfood.frontend.adapter.RestaurantItemAdapter;
import com.ding.dingfood.frontend.vo.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kent on 2015/11/14.
 */
public class HistoryFragment extends Fragment {

    private List<Item> items = new ArrayList<Item>();
    private View view;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_history, container, false);
        listView = (ListView)view.findViewById(R.id.history_listView);


        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    public void addListViewItem(String title) {
        items.add(new Item(title));
        HistoryItemAdapter customAdapter = new HistoryItemAdapter(view.getContext(), R.layout.listview_history, items);
        listView.setAdapter(customAdapter);
    }
}
