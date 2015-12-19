package com.ding.dingfood.backend.restaurant;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by curtis on 15/11/14.
 */
public class ResList {
    private static final String TAG = "RES_LIST";

    private String name;
    private long lastUsed;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<String> restaurantIds;    // FIXME: 15/11/15 Use restaurant name as ID.
    private ArrayList<String> tags;
    private ParseObject parseObject;

    public ResList (String name) {
        this.lastUsed = System.currentTimeMillis();
        this.restaurants = new ArrayList<>();
        this.restaurantIds = new ArrayList<>();
        this.name = name;
        this.tags = new ArrayList<>();
        this.parseObject = null;
    }

    public ResList (ParseObject listObj) {
        this.name = listObj.getString(Constant.RestaurantList.ListName);
        this.lastUsed = listObj.getLong(Constant.RestaurantList.LastUseTime);
        this.parseObject = listObj;
        this.restaurants = new ArrayList<>();
        this.restaurantIds = new ArrayList<>();
        JSONArray idList = listObj.getJSONArray(Constant.RestaurantList.RestaurantID);
        if (idList != null && idList.length() > 0) {
            for (int i = 0; i < idList.length(); i++) {
                try {
                    restaurantIds.add(idList.get(i).toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Parse restaurant ID from JSON failed." + e);
                    Log.e(TAG, "JSON data: " + idList.toString());
                }
            }
        }
        this.tags = new ArrayList<>();
        JSONArray tagList = listObj.getJSONArray(Constant.RestaurantList.Tags);
        if (tagList != null && tagList.length() > 0) {
            for (int i=0; i<tagList.length(); i++) {
                try {
                    tags.add(tagList.get(i).toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Parse tag from JSON failed." + e);
                    Log.e(TAG, "JSON data: " + idList.toString());
                }
            }
        }
    }

    private ParseObject toParseObj() {
        if (this.parseObject == null)
            this.parseObject = new ParseObject(Constant.RestaurantList.ClassName);

        parseObject.put(Constant.RestaurantList.ListName, this.name);
        parseObject.getObjectId();
        parseObject.put(Constant.RestaurantList.LastUseTime, this.lastUsed);
        JSONArray restaurantIdsJson = new JSONArray(this.restaurantIds);
        parseObject.put(Constant.RestaurantList.RestaurantID, restaurantIdsJson);
        JSONArray tagsJson = new JSONArray(this.tags);
        parseObject.put(Constant.RestaurantList.Tags, tagsJson);

        return this.parseObject;
    }

    public int addRestaurant(ArrayList<Restaurant> resList) {
        int cnt = 0;
        if (resList != null && resList.size() > 0) {
            for (Restaurant res : resList) {
                if (res.getRestaurantId() != null && restaurantIds.add(res.getName())) {
                    restaurants.add(res);
                    cnt++;
                }
            }
        }
        if (cnt > 0) {
            this.touch();
            this.saveToLocal();
        }

        return cnt;
    }

    public int delRestaurant(ArrayList<Restaurant> resList) {
        int cnt = 0;
        if (resList != null && resList.size() > 0) {
            for (Restaurant res : resList) {
                if (restaurants.remove(res)) {
                    res.removeFromLocal();
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public void saveToLocal() {
        this.toParseObj();
        try {
            this.parseObject.pin();
        } catch (ParseException e) {
            Log.e(TAG, "Save list to local failed: " + e);
        }
    }

    public void touch() {
        this.lastUsed = System.currentTimeMillis();
    }

    public boolean loadFromLocal() {
        if (restaurantIds == null || restaurantIds.isEmpty())
            return false;
        ParseQuery<ParseObject> resQuery = ParseQuery.getQuery(Constant.Restaurant.ClassName);
        resQuery.fromLocalDatastore();
        boolean needUpdate = false;
        for (String restaurantName : restaurantIds) {
            try {
                resQuery.whereEqualTo(Constant.Restaurant.RestaurantName, restaurantName);
                List<ParseObject> parseObjects = resQuery.find();
                if (!parseObjects.isEmpty()) {
                    ParseObject resObj = parseObjects.get(0);
                    Restaurant restaurant = new Restaurant(resObj);
                    restaurants.add(restaurant);
                }
            } catch (ParseException e) {
                restaurantIds.remove(restaurantName);
                needUpdate = true;
            }
        }

        if (needUpdate) {
            if (restaurantIds.isEmpty())
                return false;
        }

        return true;
    }

    public Restaurant randomRestaurant() {
        Random r = new Random();
        int size = restaurants.size();
        int index = r.nextInt(size * 5) % restaurants.size();
        return restaurants.get(index);
    }

    public String getName() {
        return name;
    }
}
