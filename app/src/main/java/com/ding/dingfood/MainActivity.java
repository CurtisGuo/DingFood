package com.ding.dingfood;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ding.dingfood.backend.DataOperator;
import com.ding.dingfood.backend.geolocation;
import com.ding.dingfood.frontend.DefaultListItemPage;
import com.ding.dingfood.frontend.DetailPage;
import com.ding.dingfood.frontend.FragmentFirst;
import com.ding.dingfood.frontend.FragmentSecond;
import com.ding.dingfood.frontend.FragmentThird;
import com.ding.dingfood.frontend.ListItemPage;
import com.ding.dingfood.frontend.MapPage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.parse.Parse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,SensorEventListener {

    static public Integer looper = 0;

    public GoogleApiClient mGoogleApiClient;
    //public geolocation geo;
    static public geolocation geo;
    public Integer resNo=0;
    public int photoNo=0;
    ImageView resPic;
    static String Tag = "MainActivity";
    //注意:導入時均爲support.v4.app/view 保持一致
    public static ViewPager viewPager1;
    public static Context context;
    private FragmentPagerAdapter fpAdapter;
    private List<Fragment> listData;
    private LinearLayout changelayout;
    private ImageView tabImage;
    private TextView actionBarName;
    private int shakeON=1;
    public   int dingcount=0;
    public  int firsttime=0;
    public SensorManager sm;
    public Sensor sr;
    private int delay=0;


    public class Restaurant
    {
        public String Name;
        public int src;
    }
    public static  List<Restaurant> RestaurantDataList;
    public static Restaurant data1;
    public static Restaurant data2;
    public static Restaurant data3;
    public  static Restaurant sendData;
    public  Restaurant addData ;

    public static FragmentFirst fragmentFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendData = new Restaurant();


        //注意:設置無標題需要在setContentView前調用 否則會崩潰
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "BOiMs1Hj1bSLyLb1p3IHAtRtjQpE4wvOHUtUjnyX", "DCYjcPjkyyHg21qN0MOsrKjso0dTVkdB1Gf8AcEH");

        DataOperator dataOperator = DataOperator.getDataOperator();
        dataOperator.loadRestaurantLists();

        mGoogleApiClient = new GoogleApiClient
                .Builder( this )
                .enableAutoManage( this, 0, this )
                .addApi( Places.GEO_DATA_API )
                .addApi( Places.PLACE_DETECTION_API )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .build();

        geo = new geolocation(this);
        geo.initialize(this);

        Log.i(Tag, "onCreate()............");
        //初始化資料
        storeRestaurantData();
        //初始化設置ViewPager
        setViewPager();
        sm= (SensorManager) getSystemService(SENSOR_SERVICE);
        sr=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


    }
    @Override
    protected void onStart(){
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    //----------------------------------------------------------------------------------------SensorChange
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x,y,z;

        x=event.values[0];
        y=event.values[1];
        z=event.values[2];
        if(delay>0)
            delay--;
        else
        {
            if((Math.abs(x) + Math.abs(y) +Math.abs(z) >35)&&shakeON==1)
            {
                DingFood();
                delay=5;
            }
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public  void onResume()
    {
        super.onResume();
        sm.registerListener(this,sr,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public  void onPause()
    {
        super.onPause();
        sm.unregisterListener(this);
    }

    //----------------------------------------------------------------------------------------BackPress
    //返回鍵操作設定返回前一頁
    @Override
    public void onBackPressed() {
        if (viewPager1.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            viewPager1.setCurrentItem(1);
        } else {
            // Otherwise, select the previous step.
            viewPager1.setCurrentItem(viewPager1.getCurrentItem() - 1);
        }
    }
    //----------------------------------------------------------------------------------------gotoListItemPage
    public void gotoListItemPage(View v)
    {

        Intent it = new Intent(this,ListItemPage.class);
        startActivity(it);


    }//----------------------------------------------------------------------------------------gotoDefaultListItemPage
    public void gotoDefaultListItemPage(View v)
    {

        Intent it = new Intent(this,DefaultListItemPage.class);
        startActivity(it);


    }
    //----------------------------------------------------------------------------------------gotoDetail
    public void gotoDetail(View v)
    {
        if(firsttime!=0)
        {
            Intent it = new Intent(this,DetailPage.class);
            startActivity(it);
        }

    }
    //----------------------------------------------------------------------------------------gotoMap
    public void gotoMap(View v)
    {
        if(firsttime!=0)
        {
            Intent it = new Intent(this,MapPage.class);
            startActivity(it);
        }

    }
    public void detailOne(View v)
    {
        sendData.Name=RestaurantDataList.get(0).Name;
        sendData.src=RestaurantDataList.get(0).src;
        Intent it = new Intent(this,DetailPage.class);
        startActivity(it);
    }

    public void detailTwo(View v)
    {
        sendData.Name=RestaurantDataList.get(1).Name;
        sendData.src=RestaurantDataList.get(1).src;
        Intent it = new Intent(this,DetailPage.class);
        startActivity(it);
    }

    public void detailThree(View v)
    {
        sendData.Name=RestaurantDataList.get(2).Name;
        sendData.src=RestaurantDataList.get(2).src;
        Intent it = new Intent(this,DetailPage.class);
        startActivity(it);
    }
    //初始化資料
//----------------------------------------------------------------------------------------DataSetUp
    public void storeRestaurantData()
    {
        RestaurantDataList = new ArrayList<Restaurant>();
        data1= new Restaurant();
        data1.Name="no have any record yet,please swipe to right and Ding!";
        data1.src=R.mipmap.white;

        data2= new Restaurant();
        data2.Name="Empty2";
        data2.src=R.mipmap.white;

        data3= new Restaurant();
        data3.Name="Empty3";
        data3.src=R.mipmap.white;

        RestaurantDataList.add(data1);
        RestaurantDataList.add(data2);
        RestaurantDataList.add(data3);

    }
    //----------------------------------------------------------------------------------VIewPagerSetUp
    private void setViewPager() {
        //初始化數據
        viewPager1 = (ViewPager) findViewById(R.id.viewpager1);
        // viewPager1.setPageTransformer(true, new ZoomOutPageTransformer());
        listData = new ArrayList<Fragment>();
        fragmentFirst = new FragmentFirst();
        FragmentSecond fragmentSecond = new FragmentSecond();
        FragmentThird fragmentThird = new FragmentThird();
        //三個布局加入列表
        listData.add(fragmentFirst);
        listData.add(fragmentSecond);
        listData.add(fragmentThird);
        //ViewPager相當于一組件容器 實現頁面切換
        fpAdapter =new FragmentPagerAdapter(getSupportFragmentManager())
        {

            @Override
            public int getCount()
            {
                return listData.size();
            }
            @Override
            public Fragment getItem(int arg0)
            {
                return listData.get(arg0);
            }
        };
        //設置適配器
        viewPager1.setAdapter(fpAdapter);
        //滑屏變換圖標


//----------------------------------------------------------------------------------------swipePage
        viewPager1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        shakeON=0;
                        actionBarName=(TextView) findViewById(R.id.actionBar_name);
                        actionBarName.setText("History");
                        changelayout=(LinearLayout) findViewById(R.id.bottomLayout1);
                        // changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
                        tabImage = (ImageView) findViewById(R.id.tab_image1);
                        tabImage.setColorFilter(getResources().getColor(R.color.colorTextfocused), android.graphics.PorterDuff.Mode.MULTIPLY);

                        changelayout=(LinearLayout) findViewById(R.id.bottomLayout2);
                        // changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
                        tabImage = (ImageView) findViewById(R.id.tab_image2);
                        tabImage.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

                        changelayout=(LinearLayout) findViewById(R.id.bottomLayout3);
                        //changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
                        tabImage = (ImageView) findViewById(R.id.tab_image3);
                        tabImage.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

                        break;
                    case 1:
                        shakeON=1;
                        actionBarName=(TextView) findViewById(R.id.actionBar_name);
                        actionBarName.setText("dingFOOD");
                        changelayout=(LinearLayout) findViewById(R.id.bottomLayout2);
                        //changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
                        tabImage = (ImageView) findViewById(R.id.tab_image1);
                        tabImage.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

                        changelayout=(LinearLayout) findViewById(R.id.bottomLayout1);
                        //changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
                        tabImage = (ImageView) findViewById(R.id.tab_image2);
                        tabImage.setColorFilter(getResources().getColor(R.color.colorTextfocused), android.graphics.PorterDuff.Mode.MULTIPLY);

                        changelayout=(LinearLayout) findViewById(R.id.bottomLayout3);
                        //changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
                        tabImage = (ImageView) findViewById(R.id.tab_image3);
                        tabImage.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                        break;
                    case 2:
                        shakeON=0;
                        actionBarName=(TextView) findViewById(R.id.actionBar_name);
                        actionBarName.setText("List");
                        changelayout=(LinearLayout) findViewById(R.id.bottomLayout3);
                        //changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
                        tabImage = (ImageView) findViewById(R.id.tab_image1);
                        tabImage.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

                        changelayout=(LinearLayout) findViewById(R.id.bottomLayout1);
                        //changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
                        tabImage = (ImageView) findViewById(R.id.tab_image2);
                        tabImage.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

                        changelayout=(LinearLayout) findViewById(R.id.bottomLayout2);
                        //changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
                        tabImage = (ImageView) findViewById(R.id.tab_image3);
                        tabImage.setColorFilter(getResources().getColor(R.color.colorTextfocused), android.graphics.PorterDuff.Mode.MULTIPLY);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        viewPager1.setCurrentItem(1);
        Log.i(Tag, "setViewPager()............");
    }
//----------------------------------------------------------------------------------------not now

//----------------------------------------------------------------------------------------Ding Food
    //按鍵動作產生資料並在第四次動作後強行切換到history頁面

    public void DingFood()
    {
        if(looper == geo.jsonDataNo){
            looper = 0;
        }

        firsttime=1;
        TextView mainAddress= (TextView) findViewById(R.id.main_address);
        FrameLayout mapButton= (FrameLayout) findViewById(R.id.main_mapbutton);
        TextView detailText=(TextView) findViewById(R.id.main_moreDetailText);

        addData = new Restaurant();
        Vibrator vb=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        TextView mainResName= (TextView) findViewById(R.id.main_resName);
        mainResName.setTextColor(getResources().getColor(R.color.colorTextDark));
        ImageView mainResImage= (ImageView) findViewById(R.id.main_resImage);
        FrameLayout infoIcan= (FrameLayout) findViewById(R.id.main_infoIcon);
        //infoIcan.setImageResource(R.mipmap.ic_info_black_24dp);
        infoIcan.setBackground(getResources().getDrawable(R.color.colorPrimary));
        mapButton.setBackground(getResources().getDrawable(R.color.colorBlue));
        detailText.setTextColor(getResources().getColor(R.color.colorTextDark));


        if(dingcount==0)
        {
            vb.vibrate(100);

            addData.Name="Cottage Waffle";
            //addData.Name= Geolocation.name.get(0);
            addData.src=R.mipmap.cottagewaffle;

            sendData.Name=addData.Name;
            sendData.src=addData.src;

            mainResName.setText(geo.name.get(looper));
            mainAddress.setText(geo.distance.get(looper));
           // mainResImage.setBackground(getResources().getDrawable(addData.src));
            if(geo.image.get(looper).get(0) == null){
                mainResImage.setImageResource(R.mipmap.nophoto);
            }
            else{
                mainResImage.setImageBitmap(geo.image.get(looper).get(0));
            }

            RestaurantDataList.set(dingcount, addData);
            fragmentFirst.storeData();
            dingcount++;
        }
        else if(dingcount==1)
        {
            vb.vibrate(100);
            addData.Name="McDonald's";
            addData.src=R.mipmap.macdonald;
            sendData.Name=addData.Name;
            sendData.src=addData.src;

           // mainResName.setText(addData.Name);
          //  mainResImage.setBackground(getResources().getDrawable(addData.src));

            if(geo.image.get(looper).get(0) == null){
                mainResImage.setImageResource(R.mipmap.nophoto);
            }
            else{
                mainResImage.setImageBitmap(geo.image.get(looper).get(0));
            }
            mainResName.setText(geo.name.get(looper));
            mainAddress.setText(geo.distance.get(looper));
            RestaurantDataList.set(dingcount, addData);
            fragmentFirst.storeData();
            dingcount++;
        }
        else if(dingcount==2)
        {
            vb.vibrate(100);
            addData.Name="NOLA Kitchen";
            addData.src=R.mipmap.nola;
            sendData.Name=addData.Name;
            sendData.src=addData.src;
        //    mainResName.setText(addData.Name);
        //    mainResImage.setBackground(getResources().getDrawable(addData.src));
            if(geo.image.get(looper).get(0) == null){
                mainResImage.setImageResource(R.mipmap.nophoto);
            }
            else{
                mainResImage.setImageBitmap(geo.image.get(looper).get(0));
            }
            mainResName.setText(geo.name.get(looper));
            mainAddress.setText(geo.distance.get(looper));
            RestaurantDataList.set(dingcount, addData);
            fragmentFirst.storeData();
            dingcount++;
        }
        else
        {

            sendData.Name=RestaurantDataList.get(2).Name;
            sendData.src=RestaurantDataList.get(2).src;
            dingcount=0;
            viewPager1.setCurrentItem(0);
        }
        looper++;

    }
    //----------------------------------------------------------------------------------------HistoryTab
    public void HistoryTab(View v)
    {
        viewPager1.setCurrentItem(0);
        changelayout=(LinearLayout) findViewById(R.id.bottomLayout1);
        changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
        changelayout=(LinearLayout) findViewById(R.id.bottomLayout2);
        changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
        changelayout=(LinearLayout) findViewById(R.id.bottomLayout3);
        changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));

    }
    //----------------------------------------------------------------------------------------MainTab
    public void MainTab(View v)
    {
        viewPager1.setCurrentItem(1);
        changelayout=(LinearLayout) findViewById(R.id.bottomLayout2);
        // changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
        changelayout=(LinearLayout) findViewById(R.id.bottomLayout1);
        //changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
        changelayout=(LinearLayout) findViewById(R.id.bottomLayout3);
        //changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));

    }
    //----------------------------------------------------------------------------------------ListTab
    public void ListTab(View v)
    {
        viewPager1.setCurrentItem(2);
        changelayout=(LinearLayout) findViewById(R.id.bottomLayout3);
        // changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
        changelayout=(LinearLayout) findViewById(R.id.bottomLayout2);
        //changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));
        changelayout=(LinearLayout) findViewById(R.id.bottomLayout1);
        //changelayout.setBackground(getResources().getDrawable(R.color.colorOnclick));

    }
    public static Context getAppContext() {
        return MainActivity.context;
    }
}



