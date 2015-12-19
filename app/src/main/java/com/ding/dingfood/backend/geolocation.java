package com.ding.dingfood.backend;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.ding.dingfood.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by gene on 11/15/15.
 */
public class geolocation{

    public String longitude, latitude;
    public String apikey = new String("AIzaSyAwUSeOAyzsFKeBWqUizpEH14YrRsaUoh0");
    public String googleLocation = new String("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=LATITUDE,LONGITUDE&radius=RADIUS&types=food&key=APIKEY");
    public String googleFoodPic = new String("https://maps.googleapis.com/maps/api/place/photo?maxwidth=MAXWIDTH&photoreference=REFERENCE&key=APIKEY");
    public String MAXWIDTH = new String("400");
    public String RADIUS = new String ("500");
    public String reference = null;
    public int jsonDataNo = 0;
    public LocationManager locationManager;
    public String jsonRestaurantList = "";
    public ArrayList<String> name = new ArrayList<>();
    public ArrayList<String> imageREF = new ArrayList<>();
    public ArrayList<Bitmap> image = new ArrayList<>();
    URLConnection conn;
    //public static Context context;

    public DataOperator dataOperator = DataOperator.getDataOperator();
    // dataOperator.loadRestaurantLists();

    //---------------------------------------------------------------------------------------------- Constructor
    public geolocation(Context context) {

        Context contextMain = MainActivity.getAppContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        //setSupportActionBar(toolbar);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            Log.d("testIF", "succeed");
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());
                Log.d("longitude", longitude);
                Log.d("latitude", latitude);
            }

            replaceLatLongKey(); //prepare URL to parse for jsonRestaurantList

            buildDataOperator(); //grab url and store everything into parse

        }
    }

    //---------------------------------------------------------------------------------------------- Threading
    //---------------------------------------------------------------------------------------------- Build Data
    //---------------------------------------------------------------------------------------- Store in Operator


    public void replaceLatLongKey(){
        googleLocation=googleLocation.replace("LATITUDE",latitude);
        googleLocation=googleLocation.replace("LONGITUDE",longitude);
        googleLocation=googleLocation.replace("APIKEY",apikey);
        googleLocation=googleLocation.replace("RADIUS",RADIUS);
    }

    public void buildDataOperator(){
        new infoAsync().execute();
    }

    //---------------------------------------------------------------------------------------------- Threading

    class infoAsync extends AsyncTask<Void, Integer, String> {
        protected void onPreExecute() {
            Log.d("PreExceute", "On pre Exceute......");
        }

        protected String doInBackground(Void... arg0) {
            Log.d("DoINBackGround", "On doInBackground...");

            for (int i = 0; i < 10; i++) {
                Integer in = new Integer(i);
                publishProgress(i);
            }

            getRestaurantList(); //obtain restaurant list from URL in json format, Stored in jsonRestaurantList
            storeData(); // stores data into cache
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {
            Log.d("update", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            updateDataOperator();
        }
    }

    //---------------------------------------------------------------------------------- Store in Data Operator


     //-------------------------------------------------------------------------------------- Get data from URL
     public void getRestaurantList(){
         URL url;

         try {
             Log.d("get rest list", googleLocation);
             // get URL content
             String address=googleLocation;
             url = new URL(address);
             conn = url.openConnection();

             // open the stream and put it into BufferedReader
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(conn.getInputStream()));

             String inputLine;
             while ((inputLine = br.readLine()) != null) {
                 jsonRestaurantList += inputLine;
                 //System.out.println(inputLine);
             }
             Log.i("URL", jsonRestaurantList);
             br.close();

             //System.out.println("Done");

         } catch (MalformedURLException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

    private void updateDataOperator() { //-----------------------------CURTIS--------------------add to Parse

        //random.setText(name.get(0));
       // bye.setImageBitmap(image.get(0));
        // total data => jsonDataNo
    }

    //----------------------------------------------------------------------------------- Store data into cache

    public void storeData(){ //stores data temporarily into cache

        Log.d("storeData", "" + "store");
        String temp = "";
        Log.d("final", jsonRestaurantList);
        final JSONObject obj;
        try {
            obj = new JSONObject(jsonRestaurantList);
            final JSONArray results = obj.getJSONArray("results");
            jsonDataNo = results.length();
            for (int i = 0; i < jsonDataNo; ++i) {
                final JSONObject places = results.getJSONObject(i);

                name.add(places.getString("name"));
                // temp = new String(places.getString("name"));
                Log.d("store Name", "" + name.get(i));
                if(places.has("photos")){
                    JSONArray jsnTMP = places.getJSONArray("photos");
                    JSONObject jsnOBJ = jsnTMP.getJSONObject(0);
                    imageREF.add(jsnOBJ.getString("photo_reference"));
                }
                else{
                    imageREF.add("none");
                }
                Log.d("img1", imageREF.get(i));
                image.add(downloadImage(imageREF.get(i)));
                //mTextView.setText(output);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------------------- Download Image

    public Bitmap downloadImage(String imgREF){
        if(imgREF == "none"){ //-------------------------------------------------replace with a default image
            return null;
        }
        String imgURL = new String(googleFoodPic);
        imgURL = imgURL.replace("REFERENCE", imgREF);
        imgURL = imgURL.replace("MAXWIDTH", MAXWIDTH);
        imgURL = imgURL.replace("APIKEY", apikey);

        try {
            java.net.URL url = new java.net.URL(imgURL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.d("image", imgURL);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }



}
