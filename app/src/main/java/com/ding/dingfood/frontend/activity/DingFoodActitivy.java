package com.ding.dingfood.frontend.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ding.dingfood.R;
import com.ding.dingfood.backend.geolocation;
import com.ding.dingfood.frontend.adapter.FragmentAdapter;
import com.ding.dingfood.frontend.fragment.DingFoodFragment;
import com.ding.dingfood.frontend.fragment.RestaurantFragment;
import com.ding.dingfood.frontend.fragment.HistoryFragment;
import com.ding.dingfood.frontend.listener.ShakeListener;

import java.util.ArrayList;
import java.util.List;


public class DingFoodActitivy extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Fragment> dingfoodFragments = new ArrayList<>();

    public DingFoodFragment getDingFoodFragment() {
        return dingFoodFragment;
    }

    private FragmentAdapter fragmentAdapter;
    /**Fragments**/
    private DingFoodFragment dingFoodFragment;
    private HistoryFragment historyFragment;
    private RestaurantFragment restaurantFragment;

    private SensorManager sensorManager;
    private ShakeListener shakeListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dingfood);
        viewPager = (ViewPager)findViewById(R.id.id_viewPager);
        init();
        initShakeListener();

    }

    private void init() {
        dingFoodFragment = new DingFoodFragment();
        historyFragment = new HistoryFragment();
        restaurantFragment = new RestaurantFragment();

        dingfoodFragments.add(historyFragment);
        dingfoodFragments.add(dingFoodFragment);
        dingfoodFragments.add(restaurantFragment);

        fragmentAdapter = new FragmentAdapter( this.getSupportFragmentManager(), dingfoodFragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(1);

    }

    private void initShakeListener() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        shakeListener = new ShakeListener(DingFoodActitivy.this);
        sensorManager.registerListener(shakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }





    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(shakeListener);
        super.onPause();
    }

    public HistoryFragment getHistoryFragment() {
        return historyFragment;
    }
}
