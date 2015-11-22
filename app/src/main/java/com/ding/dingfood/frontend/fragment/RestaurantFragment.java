package com.ding.dingfood.frontend.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ding.dingfood.R;
import com.ding.dingfood.frontend.adapter.RestaurantItemAdapter;
import com.ding.dingfood.frontend.vo.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kent on 2015/11/14.
 */
public class RestaurantFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        ListView listView = (ListView)view.findViewById(R.id.restaurant_listView);
        List<Item> items = new ArrayList<Item>();
        items.add(new Item("Default"));
        items.add(new Item("AddRestaurant"));
        RestaurantItemAdapter customAdapter = new RestaurantItemAdapter(view.getContext(), R.layout.listview_restaurant, items);
        listView.setAdapter(customAdapter);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }
}
