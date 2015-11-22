package com.ding.dingfood.backend.restaurant;

/**
 * Created by curtis on 15/11/15.
 */
public class Constant {
    public class RestaurantList {
        public static final String ClassName = "ResLists";
        public static final String ListName = "name";
        public static final String LastUseTime = "lastUsed";
        public static final String RestaurantID = "restaurantIds";
        public static final String Tags = "tags";
    }

    public class Restaurant {
        public static final String ClassName = "Restaurant";
        public static final String RestaurantName = "name";
        public static final String Latitude = "latitude";
        public static final String Longitude = "longitude";
        public static final String Address = "address";
        public static final String RestaurantType = "type";
        public static final String PriceUpperBound = "upperBound";
        public static final String PriceLowerBound = "lowerBound";
        public static final String PriceAverage = "average";

    }

    public class Image {
        public static final String ClassName = "Images";
        public static final String ImageName = "imageName";
        public static final String ImageFile = "imageFile";
        public static final String ImageSuffix = "_icon";

    }

    public static final String DefaultListName = "default";
}
