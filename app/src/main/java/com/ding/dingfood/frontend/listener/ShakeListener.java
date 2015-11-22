package com.ding.dingfood.frontend.listener;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ding.dingfood.R;
import com.ding.dingfood.backend.DataOperator;
import com.ding.dingfood.backend.restaurant.ResList;
import com.ding.dingfood.backend.restaurant.Restaurant;
import com.ding.dingfood.frontend.activity.DingFoodActitivy;

import java.util.Random;

/**
 * Created by Kent on 2015/11/14.
 */
public class ShakeListener implements SensorEventListener {

    private AppCompatActivity activity;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private boolean isShaked = false;
    private Vibrator mVibrator;
    private ImageView srImageView;
    private TextView srTextView;

    public ShakeListener(AppCompatActivity activity) {
        this.activity = activity;

        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        mVibrator = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
    }

    public void onSensorChanged(SensorEvent se) {
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z* z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter

        if (mAccel > 10 && !isShaked) {
            isShaked = true;
            /*Toast toast = Toast.makeText(this.activity.getApplicationContext(), "Device has shaken ", Toast.LENGTH_SHORT);
            toast.show();*/
            //震動
            mVibrator.vibrate(500);
            setupShakeResultData();


            //2秒內不重覆觸發shake
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isShaked = false;
                }
            }, 2000);

        }
    }

    private int index = 1;
    private void setupShakeResultData() {
        //int randow =  new Random().nextInt(3)+1;
        srImageView = (ImageView)activity.findViewById(R.id.shake_result_imageView);
        DataOperator dataOperator = DataOperator.getDataOperator();
        ResList resList = dataOperator.getCurrentList();
        Restaurant restaurant = resList.randomRestaurant();
        dataOperator.showImage(restaurant, srImageView, ((DingFoodActitivy)activity).getDingFoodFragment().getContext());
        ((DingFoodActitivy)activity).getHistoryFragment().addListViewItem(restaurant.getName());
/*
        if(index == 1){
            srImageView.setImageResource(R.drawable.img1);
            ((DingFoodActitivy)activity).getHistoryFragment().addListViewItem("築地鮮魚");
        } else if(index == 2){
            srImageView.setImageResource(R.drawable.img2);
            ((DingFoodActitivy)activity).getHistoryFragment().addListViewItem("麥當勞");
        } else{
            srImageView.setImageResource(R.drawable.img3);
            ((DingFoodActitivy)activity).getHistoryFragment().addListViewItem("銀湯匙");
        }
        index++;
        if(index > 3){index=1;}
*/




    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
