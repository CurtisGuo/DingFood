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

import com.ding.dingfood.R;

public class FragmentSecond extends Fragment {
    static String Tag = "Fragment2";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.i(Tag, "onCreate()............");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        Log.i(Tag, "onCreateView()............");

        return inflater.inflate(R.layout.fragment_layout2, container, false);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(Tag, "onActivityCreate()............");

        /*
        //添加Fragment1的響應事件
        Button button2 = (Button) getActivity().findViewById(R.id.layout2_button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button v2 = (Button) getActivity().findViewById(R.id.layout2_button1);//找到你要设透明背景的layout 的id
                v2.getBackground().setAlpha(255);//0~255透明度值
            }
        });*/
    }

   }