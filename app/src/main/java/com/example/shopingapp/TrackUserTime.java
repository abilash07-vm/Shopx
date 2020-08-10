package com.example.shopingapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TrackUserTime extends Service{
    private static final String TAG = "TrackUserTime";
    private int second=0;
    private boolean shouldFinish;
    private GroceryItem item;
    private IBinder binder=new LocalBinder();
    private DataBase db=DataBase.getInstance(this);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        trackTime();
        return binder;
    }
    public class LocalBinder extends Binder{
        TrackUserTime getService() {
            return TrackUserTime.this;
        }
    }
    private void trackTime(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (!shouldFinish){
                    try {
                        Thread.sleep(1000);
                        second++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void setItem(GroceryItem item) {
        this.item = item;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        shouldFinish=false;
        int halfMinute=second/60;
        if(item!=null && halfMinute>0)
        db.groceryDao().updateUserPoints(item.getId(),db.groceryDao().getitemById(item.getId()).getUserPoint()+halfMinute);
        Log.d(TAG, "onDestroy: userPoint After Viewed"+item.getName()+" "+db.groceryDao().getitemById(item.getId()).getUserPoint());
    }
}