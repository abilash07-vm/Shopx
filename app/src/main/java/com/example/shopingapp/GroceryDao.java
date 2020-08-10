package com.example.shopingapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface GroceryDao {

    @Insert
    void insertAnItem(GroceryItem item);

    @Query("SELECT * FROM GroceryItems")
    List<GroceryItem> getAllItems();

    @Query("UPDATE groceryitems SET rate=:rate WHERE id=:id")
    void updateAData(int id,int rate);

    @Query("UPDATE groceryitems SET reviews=:reviews WHERE id=:id")
    void updateReview(int id, ArrayList<Review> reviews);

    @Query("SELECT * FROM groceryitems WHERE id=:id")
    GroceryItem getitemById(int id);

    @Query("SELECT DISTINCT category FROM groceryitems")
    List<String> getAllCategories();

    @Query("SELECT * FROM GroceryItems WHERE category=:category")
    List<GroceryItem> getItemsByCategory(String category);

    @Query("SELECT * FROM GroceryItems WHERE cartQuantity>0 ORDER BY cartQuantity DESC")
    List<GroceryItem> getCartItems();

    @Query("UPDATE GROCERYITEMS SET cartQuantity=:value WHERE id=:id")
    void setQuatity(int id,int value);

    @Query("UPDATE GROCERYITEMS SET popularityPoint=:value WHERE id=:id")
    void updatePopularityPoints(int id ,int value);

    @Query("UPDATE GROCERYITEMS SET userPoint=:value WHERE id=:id")
    void updateUserPoints(int id ,int value);
}
