package com.ding.dingfood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ding.dingfood.backend.DataOperator;
import com.ding.dingfood.backend.geolocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.parse.Parse;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    //public Context context;
    public GoogleApiClient mGoogleApiClient;
    //public geolocation geo;
    public geolocation geo;
    public int resNo=0;
    public int photoNo=0;
    ImageView resPic;

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
        //context  = this.getApplicationContext();
        geo = new geolocation(this);
        geo.initialize(this);

       // googleInfo.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //dataOperator.cleanAllLocalData();
        //dataOperator.createTestData();
        resPic = (ImageView) findViewById(R.id.imageView2);
        Button button = (Button) findViewById(R.id.nextRes);
        Button buttonPhoto = (Button) findViewById(R.id.button);
        buttonPhoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(photoNo<geo.jsonPicNo.get(resNo)){
                    photoNo++;
                    ImageView imageTest=(ImageView) findViewById(R.id.imageView);
                    TextView textTest=(TextView) findViewById(R.id.textView);
                    textTest.setText(geo.name.get(resNo));
                    resPic.setImageBitmap(geo.image.get(resNo).get(photoNo));
                }
                else photoNo=0;
            }
        });
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(resNo<geo.jsonDataNo){
                    photoNo=0;
                    ImageView imageTest=(ImageView) findViewById(R.id.imageView);
                     TextView textTest=(TextView) findViewById(R.id.textView);
                     textTest.setText(geo.name.get(resNo));
                    resPic.setImageBitmap(geo.image.get(resNo).get(photoNo));
                    resNo++;
                }
                else resNo=0;

            }
        });
    }



    /*public static Context getAppContext() {
        return MainActivity.context;
    }*/

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

