package com.ding.dingfood;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ding.dingfood.backend.DataOperator;
import com.ding.dingfood.backend.geolocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.parse.Parse;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    public static Context context;
    public GoogleApiClient mGoogleApiClient;
    public geolocation geo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        context  = this.getApplicationContext();
         geo = new geolocation(context);

       // googleInfo.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //dataOperator.cleanAllLocalData();
        //dataOperator.createTestData();

    }

    public void DingFoodFromGoogleMap(View v)
    {
        ImageView imageTest=(ImageView) findViewById(R.id.imageView);
        TextView textTest=(TextView) findViewById(R.id.textView);
        textTest.setText(geo.name.get(0));
    }
    public static Context getAppContext() {
        return MainActivity.context;
    }

    @Override
    protected void onStart(){
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

