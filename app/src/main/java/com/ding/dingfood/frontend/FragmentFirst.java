package com.ding.dingfood.frontend;

/**
 * Created by user on 2015/12/8.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ding.dingfood.MainActivity;
import com.ding.dingfood.R;

public class FragmentFirst extends Fragment {

    static String Tag = "Fragment1";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Tag, "onCreate()............");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        Log.i(Tag, "onCreateView()............");
        return inflater.inflate(R.layout.fragment_layout1, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(Tag, "onActivityCreate()............");
        storeData();



    }

    public  void storeData()
    {
       //找到你要设透明背景的layout 的id
        LinearLayout setBackground =(LinearLayout) getActivity().findViewById(R.id.data1_layout);
        setBackground.setBackground(getResources().getDrawable(MainActivity.RestaurantDataList.get(0).src));
        LinearLayout setInfoBar= (LinearLayout) getActivity().findViewById(R.id.data1_infobar);
        TextView dataName = (TextView) getActivity().findViewById(R.id.data1_name);


        ImageView dataInfo= (ImageView) getActivity().findViewById(R.id.data1_info);
        dataInfo.setBackground(getResources().getDrawable(R.mipmap.ic_info_white_24dp));
        if((MainActivity.RestaurantDataList.get(0).src)!=R.mipmap.white)
        {
            setInfoBar.setBackground(getResources().getDrawable(R.color.colorAccent));
            dataInfo.setBackground(getResources().getDrawable(R.mipmap.ic_info_white_24dp));
            dataName.setText(MainActivity.RestaurantDataList.get(0).Name);
        }




        setBackground =(LinearLayout) getActivity().findViewById(R.id.data2_layout);
        setBackground.setBackground(getResources().getDrawable(MainActivity.RestaurantDataList.get(1).src));
        setInfoBar= (LinearLayout) getActivity().findViewById(R.id.data2_infobar);
        dataName = (TextView) getActivity().findViewById(R.id.data2_name);

        dataInfo= (ImageView) getActivity().findViewById(R.id.data2_info);
        if((MainActivity.RestaurantDataList.get(1).src)!=R.mipmap.white)
        {
            setInfoBar.setBackground(getResources().getDrawable(R.color.colorAccent));
            dataInfo.setBackground(getResources().getDrawable(R.mipmap.ic_info_white_24dp));
            dataName.setText(MainActivity.RestaurantDataList.get(1).Name);
        }


        setBackground =(LinearLayout) getActivity().findViewById(R.id.data3_layout);
        setBackground.setBackground(getResources().getDrawable(MainActivity.RestaurantDataList.get(2).src));
        setInfoBar= (LinearLayout) getActivity().findViewById(R.id.data3_infobar);
        dataName = (TextView) getActivity().findViewById(R.id.data3_name);

        dataInfo= (ImageView) getActivity().findViewById(R.id.data3_info);
        if((MainActivity.RestaurantDataList.get(2).src)!=R.mipmap.white)
        {
            setInfoBar.setBackground(getResources().getDrawable(R.color.colorAccent));
            dataInfo.setBackground(getResources().getDrawable(R.mipmap.ic_info_white_24dp));
            dataName.setText(MainActivity.RestaurantDataList.get(2).Name);
        }

    }
}

