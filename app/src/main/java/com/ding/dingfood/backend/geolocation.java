package com.ding.dingfood.backend;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.ding.dingfood.backend.restaurant.GpsTracker;

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
import java.util.List;

/**
 * Created by gene on 11/15/15.
 */



public class geolocation {

    public Activity activity;
    Context mContext;
    public String longitude, latitude;
    public String apikey = new String("AIzaSyBUxgT4WS5GuToywaVvAXvypZj2Hhqwjv0");
    public String googleLocation = new String("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=LATITUDE,LONGITUDE&radius=RADIUS&types=food&key=APIKEY");
    public String googleFoodPic = new String("https://maps.googleapis.com/maps/api/place/photo?maxwidth=MAXWIDTH&photoreference=REFERENCE&key=APIKEY");
    public String googlePlaceIDJson = new String ("https://maps.googleapis.com/maps/api/place/details/json?placeid=PLACEID&key=APIKEY");
    public String getGooglePlaceIDJson_tmp = null;
    public String placeIDJsonContainer = "";
    public String MAXWIDTH = new String("400");
    public String RADIUS = new String ("500");
    public String reference = null;
    public LocationManager locationManager;
    public String jsonRestaurantList = "";
    public ArrayList<String> imageREF = new ArrayList<>();
    public ArrayList<String> placeID = new ArrayList<>();
    //public ArrayList<Bitmap> image = new ArrayList<>();


    public int jsonDataNo = 0;
    public ArrayList<Integer> jsonPicNo = new ArrayList<>();

    public ArrayList<String> name = new ArrayList<>();

    public ArrayList<String> address = new ArrayList<>();

    public ArrayList<String> telephoneNumber = new ArrayList<>();

    public ArrayList<String> isOpen = new ArrayList<>();
    public ArrayList<List<String>> openingHours = new ArrayList<List<String>>();
    public List<String> openingReference = new ArrayList<String>();
    public ArrayList<Integer> jsonOpenNo = new ArrayList<>();

    public ArrayList<List<Bitmap>> image = new ArrayList<List<Bitmap>>();
    public List<Bitmap> photoReference = new ArrayList<Bitmap>();
    public int picNo = 0;
    public int openHoursNo = 0;
    public int openNo = 0;
    URLConnection conn;

    public Coordinates  locationCoor = new Coordinates();
    public ArrayList<Coordinates> coordinates = new ArrayList<>();

    public ArrayList<String> distance = new ArrayList<>();
    public ArrayList<String> duration = new ArrayList<>();
    public String jsonDistance = "";
    public String google_dist_duration_Json = new String ("http://maps.googleapis.com/maps/api/directions/json?origin=startLAT,startLNG&destination=endLAT,endLNG&sensor=false&mode=walking&alternatives=true");
    //public static MainActivity2 context = null;
   // Context context;

    private int storeDataIterator = 0;

    public DataOperator dataOperator = DataOperator.getDataOperator();
    // dataOperator.loadRestaurantLists();

    public geolocation(){
        locationManager = null;
    }

    public geolocation(Context context) {

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }





    //---------------------------------------------------------------------------------------------- Constructor

        //Context contextMain = MainActivity2.getAppContext();
        //locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        //setSupportActionBar(toolbar);
    public void initialize(Context context){
        this.activity =  (Activity) context;
        mContext = context;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            //Log.d("testIF", "succeed");
            GpsTracker tracker = new GpsTracker(context);
            Location location = tracker.getLocation();//locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                Log.d("testIF", "succeed");
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
        private ProgressDialog progressDialog;
        protected void onPreExecute() {
            Log.d("PreExceute", "On pre Exceute......");
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Fetching Data, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
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

      /*  protected void onProgressUpdate(Integer... a) {
            Log.d("update", "You are in progress update ... " + a[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }*/
        protected void onProgressUpdate(String... progress) {
        //    prgs.setProgress(10);
        }

        protected void onPostExecute(String result) {
            updateDataOperator();
            progressDialog.hide();
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
            if(jsonDataNo>8){
                jsonDataNo = 8;
            }
            for (storeDataIterator = 0; storeDataIterator < jsonDataNo; ++storeDataIterator) {
                final JSONObject places = results.getJSONObject(storeDataIterator);

                name.add(places.getString("name"));
                Log.d("store Name", "" + name.get(storeDataIterator));
                placeID.add(places.getString("place_id"));
                Log.d("place id", "" + placeID.get(storeDataIterator));
                if(places.has("photos")){
                    JSONArray jsnTMP = places.getJSONArray("photos");
                    JSONObject jsnOBJ = jsnTMP.getJSONObject(0);
                    imageREF.add(jsnOBJ.getString("photo_reference"));
                    picNo = 1;
                }
                else{
                    imageREF.add("none");
                    picNo = 0;
                }
                Log.d("img1", imageREF.get(storeDataIterator));
                photoReference.add(downloadImage(imageREF.get(storeDataIterator)));

                JSONObject jsonCoor = places.getJSONObject("geometry").getJSONObject("location");
                locationCoor.latitude = jsonCoor.getString("lat");
                locationCoor.longitude = jsonCoor.getString("lng");
                coordinates.add(locationCoor);
                locationCoor = new Coordinates();


                //mTextView.setText(output);
                replacePlaceID();//--------------------------------------------------replace API and place ID
                getPhotoList();//-----------------------------------------------obtain json via URL parsing
                storeAddress();//-----------------------------------------------obtain Address
                storeTelephone();//-----------------------------------------------obtain Telephone
                storeOpeningHours();//-----------------------------------------------obtain Opening Hours
                storePhotos();//-------------------------------------------------download Photos
                getDurDistJSON();//-------------------------------------------------obtain JSON format of ADD
                storeDistanceDuration();//-----------------------------------------------obtain Distance and Duration
                placeIDJsonContainer = "";
                jsonDistance = "";
               // image.add(new ArrayList<Bitmap>());
                image.add(photoReference);//-------------------------------------------2D array of photos of all Res
                photoReference = new ArrayList<Bitmap>();
            }
           } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeDistanceDuration() {
        try {                   //-------------------------all photos for a restaurant stored in photoReference
            JSONObject obj = new JSONObject(jsonDistance);
            //placeIDJsonContainer = "";
            JSONObject legs = obj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);
            distance.add(legs.getJSONObject("distance").getString("text"));
            duration.add(legs.getJSONObject("duration").getString("text"));
            Log.d("Distance Duration", "success");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDurDistJSON() {
        String imgURL = new String(google_dist_duration_Json);
        imgURL = imgURL.replace("startLAT", latitude);
        imgURL = imgURL.replace("startLNG", longitude);
        imgURL = imgURL.replace("endLAT", coordinates.get(storeDataIterator).latitude);
        imgURL = imgURL.replace("endLNG", coordinates.get(storeDataIterator).longitude);
        Log.d("dist Dur", imgURL);
        URL url;
        try {
            url = new URL(imgURL);
            conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                jsonDistance += inputLine;
                //System.out.println(inputLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeOpeningHours() {
        try {                   //-------------------------all photos for a restaurant stored in photoReference
            JSONObject obj = new JSONObject(placeIDJsonContainer);
            //placeIDJsonContainer = "";
            JSONObject results = obj.getJSONObject("result");
            openingReference = new ArrayList<String>();
            if(results.has("opening_hours")) {
                JSONObject openObj = results.getJSONObject("opening_hours");
                if(openObj.has("open_now")){
                    isOpen.add(openObj.getString("open_now"));
                }
                else{
                    isOpen.add("NONE");
                }

                if(openObj.has("weekday_text")){
                    JSONArray openArray = openObj.getJSONArray("weekday_text");
                    openNo = openArray.length();
                    jsonOpenNo.add(openNo);
                    for (int i = 0; i < openArray.length(); i++) {
                        openingReference.add(openArray.getString(i));
                    }
                }
                else{
                    jsonOpenNo.add(0);
                    openingReference.add("NONE");
                }
                openingHours.add(openingReference);
            }
            else {
                isOpen.add("NONE");
                jsonOpenNo.add(0);
                openingReference.add("NONE");
                openingHours.add(openingReference);
            }
            Log.d("open", "success");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeTelephone() {
        try {                   //-------------------------all photos for a restaurant stored in photoReference
            JSONObject obj = new JSONObject(placeIDJsonContainer);
            //placeIDJsonContainer = "";
            JSONObject results = obj.getJSONObject("result");
            if(results.has("formatted_phone_number")) {
                telephoneNumber.add(results.getString("formatted_phone_number"));
            }
            else {
                telephoneNumber.add("Not Provided.");
            }
            Log.d("telephone", "success");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeAddress() {
        try {                   //-------------------------all Address for a restaurant
            JSONObject obj = new JSONObject(placeIDJsonContainer);
            //placeIDJsonContainer = "";
            JSONObject results = obj.getJSONObject("result");
            if(results.has("formatted_address")) {
                address.add(results.getString("formatted_address"));
            }
            else {
                address.add("Not Provided.");
            }
            Log.d("address", "success");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storePhotos() {//---------------------------------------total photos will be stored in picNo
        try {                   //-------------------------all photos for a restaurant stored in photoReference
            JSONObject obj = new JSONObject(placeIDJsonContainer);
            //placeIDJsonContainer = "";
            JSONObject results = obj.getJSONObject("result");
            if(results.has("photos")) {
                JSONArray photoArray = results.getJSONArray("photos");
                picNo += photoArray.length();
                jsonPicNo.add(picNo);
                for (int i = 0; i < photoArray.length(); i++) {
                    JSONObject jsnOBJ = photoArray.getJSONObject(i);
                    photoReference.add(downloadImage(jsnOBJ.getString("photo_reference")));
                }
            }
            else {
                jsonPicNo.add(0);
                photoReference.add(downloadImage("none"));
            }
                Log.d("img", "success");
            }
         catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void replacePlaceID() {
        getGooglePlaceIDJson_tmp=googlePlaceIDJson.replace("APIKEY",apikey);
        getGooglePlaceIDJson_tmp=getGooglePlaceIDJson_tmp.replace("PLACEID",placeID.get(placeID.size()-1));
    }


    private void getPhotoList() {
        URL url;

        try {
            Log.d("get photo", getGooglePlaceIDJson_tmp);
            // get URL content
            String address=getGooglePlaceIDJson_tmp;
            url = new URL(address);
            conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                placeIDJsonContainer += inputLine;
                //System.out.println(inputLine);
            }
            Log.i("full photo list", placeIDJsonContainer);
            br.close();

            //System.out.println("Done");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
