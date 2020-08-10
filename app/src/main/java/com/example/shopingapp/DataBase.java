package com.example.shopingapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities =GroceryItem.class,version = 1)
@TypeConverters(ReviewTypeConverter.class)
public abstract class DataBase extends RoomDatabase {
    public abstract GroceryDao groceryDao();
    private static DataBase instance;
    public static  synchronized  DataBase getInstance(Context context){
        if(instance==null){
            instance=Room.databaseBuilder(context,DataBase.class,"item_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(initalCallback)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback initalCallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateData(instance).execute();
        }
    };
    private static class populateData extends AsyncTask<Void,Void,Void>{
        private GroceryDao groceryDao;

        public populateData(DataBase db) {
            this.groceryDao = db.groceryDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            groceryDao.insertAnItem(new GroceryItem("Milk",
                    "milk is a good form of protein and calcium",
                    "https://timesofindia.indiatimes.com/thumb/msid-64992975,imgsize-24421,width-400,resizemode-4/64992975.jpg",
                    "drink",25,8));
            groceryDao.insertAnItem(new GroceryItem("Peanuts",
                    "Peanuts are good and high quality protein source for bodybuilding.",
                    "https://tiimg.tistatic.com/fp/1/006/476/export-quality-peanuts-kernel-782.jpg",
                    "food",50,7));
            groceryDao.insertAnItem(new GroceryItem("soda","yummy soda !!!! drink and have fun",
                    "https://image.freepik.com/free-vector/soda-can-aluminium-white_1308-32368.jpg",
                    "drink",15,23));
            groceryDao.insertAnItem(new GroceryItem("Whey Protein","Take protein and grow musscles",
                    "https://store.bbcomcdn.com/images/store/skuimage/sku_OPT231/image_skuOPT231_largeImage_X_450_white.jpg",
                    "drink",1500,10));
            groceryDao.insertAnItem(new GroceryItem("Biscuits","Take a break and have some yummy snack...",
                    "https://i.pinimg.com/originals/48/2a/e7/482ae79808496510b469295af35d0592.jpg",
                    "snack",20,23));
            groceryDao.insertAnItem(new GroceryItem("Ice Cream","eat this and make the summer great",
                    "https://teddys.ie/wp-content/uploads/2016/02/menuimage.jpg",
                    "snack",20,6));
            groceryDao.insertAnItem(new GroceryItem("Cake","yummy cake for a good snack",
                    "https://cdn.sallysbakingaddiction.com/wp-content/uploads/2018/11/black-forest-cake-chocolate-ganache.jpg",
                    "snack",300,5));
            groceryDao.insertAnItem(new GroceryItem("Butter","Apply for a tasty snack",
                    "https://post.healthline.com/wp-content/uploads/sites/3/2020/02/321990_2200-800x1200.jpg",
                    "food",34,8));
            groceryDao.insertAnItem(new GroceryItem("Lemon Soda","stay chill ....",
                    "https://cdn4.vectorstock.com/i/1000x1000/94/73/lemon-soda-in-aluminum-can-vector-10739473.jpg",
                    "drink",40,7));
            groceryDao.insertAnItem(new GroceryItem("Muscle blaze protein peanut butter","good for muscle building and stay healthy",
                    "https://images-na.ssl-images-amazon.com/images/I/61SGiOWN7oL._SX569_.jpg",
                    "food",600,4));
            return null;
        }
    }
}
