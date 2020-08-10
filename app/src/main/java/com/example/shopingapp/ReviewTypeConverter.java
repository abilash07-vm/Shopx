package com.example.shopingapp;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewTypeConverter {
    @TypeConverter
    public static String reviewsToJson(ArrayList<Review> reviews){
        Gson gson=new Gson();
        return gson.toJson(reviews);
    }

    @TypeConverter
    public static ArrayList<Review> jsonToReviews(String reviews){
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<Review>>(){}.getType();
        return gson.fromJson(reviews,type);
    }
}
