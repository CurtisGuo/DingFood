package com.ding.dingfood.backend.restaurant;

import android.graphics.Bitmap;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by curtis on 15/11/14.
 */
public class Restaurant {
    private static final String TAG = "RESTAURANT";

    private String name;
    private double latitude;
    private double longitude;
    private String imageName;
    private ParseFile image;
    private String address;
    private String type;
    private int upperBound;
    private int lowerBound;
    private int average;
    private ParseObject parseObject;
    private Bitmap bitImage;

    public Restaurant(String name, double latitude, double longitude, String imageName,
                      String type, int upperBound, int lowerBound, int average, String address, Bitmap bitImage) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageName = imageName;
        this.image = null;
        this.address = address;
        this.type = type;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.average = average;
        this.bitImage = bitImage;
    }

    public Restaurant(ParseObject resObj) {
        this.name = resObj.getString(Constant.Restaurant.RestaurantName);
        this.latitude = resObj.getLong(Constant.Restaurant.Latitude);
        this.longitude = resObj.getLong(Constant.Restaurant.Longitude);
        this.imageName = resObj.getString(Constant.Image.ImageName);
        this.image = resObj.getParseFile(Constant.Image.ImageFile);
        this.address = resObj.getString(Constant.Restaurant.Address);
        this.type = resObj.getString(Constant.Restaurant.RestaurantType);
        this.upperBound = resObj.getInt(Constant.Restaurant.PriceUpperBound);
        this.lowerBound = resObj.getInt(Constant.Restaurant.PriceLowerBound);
        this.average = resObj.getInt(Constant.Restaurant.PriceAverage);
        this.parseObject = resObj;
    }

    private ParseObject toParseObj() {
        if (this.parseObject == null)
            this.parseObject = new ParseObject(Constant.Restaurant.ClassName);

        parseObject.put(Constant.Restaurant.RestaurantName, name);
        parseObject.put(Constant.Restaurant.Latitude, latitude);
        parseObject.put(Constant.Restaurant.Longitude, longitude);
        parseObject.put(Constant.Image.ImageName, imageName);
        if (image != null)
            parseObject.put(Constant.Image.ImageFile, image);
        parseObject.put(Constant.Restaurant.Address, address);
        parseObject.put(Constant.Restaurant.RestaurantType, type);
        parseObject.put(Constant.Restaurant.PriceUpperBound, upperBound);
        parseObject.put(Constant.Restaurant.PriceLowerBound, lowerBound);
        parseObject.put(Constant.Restaurant.PriceAverage, average);

        return parseObject;
    }

    public void saveToLocal() {
        this.toParseObj();
        try {
            this.parseObject.pin();
        } catch (ParseException e) {
            Log.e(TAG, "Save restaurant to local failed: " + e);
        }
    }

    public void removeFromLocal() {
        // if parseObject is not set, means this object is never stored nor read from the storage.
        if (parseObject != null) {
            try {
                parseObject.unpin();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParseFile getImage() {
        return image;
    }

    public void setImage(ParseFile image) {
        this.image = image;
    }

    public String getRestaurantId() {
        if (parseObject == null)
            return null;
        else if (parseObject.getObjectId() == null)
            return name;
        else
            return parseObject.getObjectId();
    }
/*
    @Override
    public void done(ParseException e) {
        if (e == null) {
            // Saved successfully.
            this.id = parseObject.getObjectId();
        } else {
            // The save failed.
            Log.d(TAG, "Save restaurant data to local storage failed. Error: " + e);
        }
    }
    */
}
