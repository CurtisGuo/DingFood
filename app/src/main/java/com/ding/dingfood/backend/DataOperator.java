package com.ding.dingfood.backend;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.ding.dingfood.backend.restaurant.Constant;
import com.ding.dingfood.backend.restaurant.ResList;
import com.ding.dingfood.backend.restaurant.Restaurant;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by curtis on 15/11/14.
 * Used to exchange data with parse server.
 */
public class DataOperator {
    private static final String TAG = "DATA_OPERATOR";

    private static Object lock = new Object();
    private static DataOperator singleton;

    private ArrayList<ResList> lists;
    private ResList defaultList;
    private ResList currentList;

    private DataOperator() {}

    public static DataOperator getDataOperator() {
        if (singleton == null) {
            synchronized (lock) {
                if (singleton == null) {
                    singleton = new DataOperator();
                }
            }
        }
        return singleton;
    }

    public boolean loadRestaurantLists() {
        synchronized (lock) {
            ParseUser user = ParseUser.getCurrentUser();
            if (user == null) {
                Log.i(TAG, "User not log in. Try to load list from local storage.");
                this.lists = new ArrayList<>();

                // Try to load all lists from local storage:
                ParseQuery<ParseObject> listQuery = ParseQuery.getQuery(Constant.RestaurantList.ClassName);
                listQuery.fromLocalDatastore();
                try {
                    List<ParseObject> parseObjectLists = listQuery.find();
                    for (ParseObject listObj : parseObjectLists) {
                        ResList resList = new ResList(listObj);
                        if (resList.getName().equals(Constant.DefaultListName))
                            this.defaultList = resList;
                        resList.loadFromLocal();
                        this.lists.add(resList);
                    }

                    // Default not exist, create a default one.
                    // This condition should not happen in normal.
                    if (this.defaultList == null) {
                        this.defaultList = new ResList(Constant.DefaultListName);
                        ParseQuery<ParseObject> allResQuery = ParseQuery.getQuery(Constant.Restaurant.ClassName);
                        allResQuery.fromLocalDatastore();
                        List<ParseObject> allResObjList = allResQuery.find();
                        ArrayList<Restaurant> allResList = new ArrayList<>();
                        for (ParseObject resObj : allResObjList) {
                            Restaurant res = new Restaurant(resObj);
                            allResList.add(res);
                        }
                        if (!allResList.isEmpty()) {
                            this.defaultList.addRestaurant(allResList);
                        }
                    }
                } catch (ParseException e) {
                    Log.i(TAG, "No restaurants lint found on local storage.");
                }
                if (this.lists.isEmpty()) {
                    // First use, create a default list by google map.
                    // todo: Load from google map.
                }
            } else {
                // todo: Get user lists.
            }

            return true;
        }
    }

    public ResList createList(String name) {
        if (name == null || name.isEmpty())
            return null;

        ResList resList = new ResList(name);
        resList.saveToLocal();
        this.lists.add(resList);

        return resList;
    }

    public Restaurant createRestaurant(String name, double latitude, double longitude,
                                       String imageName, String address, String type,
                                       int upperBound, int lowerBound, int average, Bitmap image) {
        if (name == null || name.isEmpty())
            return null;

        Restaurant restaurant = new Restaurant(name, latitude, longitude, imageName,
                type, upperBound, lowerBound, average, address, image);
        restaurant.saveToLocal();
        addRestaurantToList(restaurant, defaultList);
        return restaurant;
    }

    public void addRestaurantToList(Restaurant restaurant, ResList list) {
        ArrayList<Restaurant> resArray = new ArrayList<>();
        resArray.add(restaurant);
        list.addRestaurant(resArray);
    }

    public void showImage(final Restaurant restaurant, final ImageView imageView, Context context) {
        final ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(context, "Waiting", "Downloading image...", true);
        if (restaurant.getImage() == null) {


            ParseFile imageFile = null;
            ParseQuery<ParseObject> imageQuery = new ParseQuery<>(Constant.Image.ClassName);
            // Search local storage first.
            imageQuery.fromLocalDatastore();
            imageQuery.whereEqualTo(Constant.Image.ImageName, restaurant.getImageName());
            try {
                List<ParseObject> parseObjects = imageQuery.find();
                if (parseObjects != null && !parseObjects.isEmpty()) {
                    imageFile = (ParseFile) parseObjects.get(0).get(Constant.Image.ImageFile);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (imageFile == null) {
                // If can't find in local storage, try to download from server.
                imageQuery = new ParseQuery<ParseObject>(Constant.Image.ClassName);
                imageQuery.whereEqualTo(Constant.Image.ImageName, restaurant.getImageName());
                imageQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            ParseObject imageObj = objects.get(0);
                            ParseFile imgFile = imageObj.getParseFile(Constant.Image.ImageFile);
                            if (imgFile != null) {
                                restaurant.setImage(imgFile);
                                restaurant.saveToLocal();
                                imgFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null) {
                                            // Decode the Byte[] into
                                            // Bitmap
                                            Bitmap bmp = BitmapFactory
                                                    .decodeByteArray(
                                                            data, 0,
                                                            data.length);

                                            // Set the Bitmap into the
                                            // ImageView
                                            imageView.setImageBitmap(bmp);
                                        } else {
                                            Log.w(TAG, "There was a problem downloading the data.");
                                        }
                                        // Close progress dialog
                                        progressDialog.dismiss();
                                    }
                                });
                            } else {
                                // Close progress dialog
                                progressDialog.dismiss();
                            }
                        }
                    }
                });

                // The remaining things are all done in the callbacks.
                return;
            } else {
                restaurant.setImage(imageFile);
                restaurant.saveToLocal();
            }
        }
        restaurant.getImage().getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    // Decode the Byte[] into
                    // Bitmap
                    Bitmap bmp = BitmapFactory
                            .decodeByteArray(
                                    data, 0,
                                    data.length);

                    // Set the Bitmap into the
                    // ImageView
                    imageView.setImageBitmap(bmp);

                    // Close progress dialog
                    progressDialog.dismiss();
                } else {
                    Log.w(TAG, "There was a problem downloading the data.");
                }
            }
        });
    }

    public void cleanAllLocalData() {
        try {
            ParseObject.unpinAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ResList> getLists() {
        return lists;
    }

    public ResList getDefaultList() {
        return defaultList;
    }

    public void setCurrentList(ResList currentList) {
        this.currentList = currentList;
    }

    public ResList getCurrentList() {
        if (currentList == null)
            return defaultList;
        else
            return currentList;
    }

   /* public void createTestData() {
        //ResList fastList = this.createList("速食");
        final Restaurant kfc = this.createRestaurant("築地鮮魚",
                1.0,
                2.0,
                "zhudi",
                "XXXXXXXX",
                "速食",
                100,
                100,
                100);
        Restaurant mac = this.createRestaurant("麥當勞",
                3.0,
                4.0,
                "mac",
                "XXXXXXXX",
                "速食",
                100,
                100,
                100);
        Restaurant mb = this.createRestaurant("銀湯匙",
                5.0,
                6.0,
                "silver",
                "XXXXXXXX",
                "速食",
                100,
                100,
                100);

    }*/

    public void cleanTestData() {
        cleanAllLocalData();
    }


    /* demo code.

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "BOiMs1Hj1bSLyLb1p3IHAtRtjQpE4wvOHUtUjnyX", "DCYjcPjkyyHg21qN0MOsrKjso0dTVkdB1Gf8AcEH");

        final DataOperator dataOperator = DataOperator.getDataOperator();
        boolean res = dataOperator.loadRestaurantLists();



        Button button = (Button) findViewById(R.id.button);
        final ImageView imageView = (ImageView)findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataOperator.showImage(dataOperator.getCurrentList().randomRestaurant(), imageView, MainActivity.this);
            }
        });
    */

    /*
    public void signupUser(String username, String passwd, String email) {
        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@example.com");

        user.put("phone", "650-253-0000");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
    */
}
