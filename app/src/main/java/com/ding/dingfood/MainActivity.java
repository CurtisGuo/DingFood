package com.ding.dingfood;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ding.dingfood.backend.DataOperator;
import com.ding.dingfood.frontend.activity.DingFoodActitivy;
import com.parse.Parse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "BOiMs1Hj1bSLyLb1p3IHAtRtjQpE4wvOHUtUjnyX", "DCYjcPjkyyHg21qN0MOsrKjso0dTVkdB1Gf8AcEH");

        DataOperator dataOperator = DataOperator.getDataOperator();
        dataOperator.loadRestaurantLists();
        //dataOperator.cleanAllLocalData();
        //dataOperator.createTestData();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //延遲1.5秒intent activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MainActivity.this, DingFoodActitivy.class);
                startActivity(intent);
            }
        }, 1500);
    }
}

