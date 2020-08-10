package com.example.shopingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class groceryItemActivity extends AppCompatActivity implements AddReviewDialog.AddReview {


    private static final String TAG = "groceryItemActivity";
    public static final String key="GroceryItemKey";
    private RecyclerView reviewRecView;
    private TextView itemName,itemPrice,itemDescription,txtAddReview;
    private ImageView itemImage,firstFilledStar,firstUnfilledStar,secondFilledStar,secondUnfilledStar,thirdFilledStar,thirdUnfilledStar;
    private Button btnAddToCard;
    private RelativeLayout firstStarRelLayout,secondStarRelLayout,thirdStarRelLayout;
    private GroceryItem incommingItem;
    private MaterialToolbar toolbar;
    private DataBase db;
    private int val;
    private ReviewAdaptor adaptor=new ReviewAdaptor();

    private TrackUserTime mservice;
    private boolean isBound;

    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackUserTime.LocalBinder binder= (TrackUserTime.LocalBinder) service;
            mservice=binder.getService();
            isBound=true;
            mservice.setItem(incommingItem);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound=false;
        }
    };

    @Override
    public void AddaReview(ArrayList<Review> review) {
        db.groceryDao().updateUserPoints(incommingItem.getId(),db.groceryDao().getitemById(incommingItem.getId()).getUserPoint()+3);
        adaptor.setAllReviews(review);
        Log.d(TAG, "AddaReview: userPoint After reviewing"+incommingItem.getName()+" "+db.groceryDao().getitemById(incommingItem.getId()).getUserPoint());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_item);
        initViews();

        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        if(intent!=null){
            incommingItem=intent.getParcelableExtra(key);
            if(incommingItem!=null){
                db.groceryDao().updateUserPoints(incommingItem.getId(),db.groceryDao().getitemById(incommingItem.getId()).getUserPoint()+1);
                Log.d(TAG, "onCreate: userPoint After Viewed"+incommingItem.getName()+" "+db.groceryDao().getitemById(incommingItem.getId()).getUserPoint());
                itemName.setText(incommingItem.getName());
                itemPrice.setText(String.valueOf(incommingItem.getPrice()));
                itemDescription.setText(incommingItem.getDescription());
                Glide.with(this)
                        .asBitmap()
                        .load(incommingItem.getImageurl())
                        .into(itemImage);
                ArrayList<Review> reviews= (ArrayList<Review>) db.groceryDao().getitemById(incommingItem.getId()).getReviews();
                reviewRecView.setAdapter(adaptor);
                if(reviews.size()>0){
                    adaptor.setAllReviews(reviews);
                }
                reviewRecView.setLayoutManager(new LinearLayoutManager(this));
                btnAddToCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        val=db.groceryDao().getitemById(incommingItem.getId()).getCartQuantity();
                        db.groceryDao().setQuatity(incommingItem.getId(),val+1);

                        Intent intent1=new Intent(groceryItemActivity.this,CartActivity.class);
                        startActivity(intent1);
                    }
                });
                txtAddReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddReviewDialog dialog=new AddReviewDialog();
                        Bundle bundle=new Bundle();
                        bundle.putParcelable(key,incommingItem);
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(),"Add review");

                    }
                });
                handleRating();
            }
        }
    }

    private void handleRating() {

        switch (incommingItem.getRate()){
            case 0:
                firstUnfilledStar.setVisibility(View.VISIBLE);
                firstFilledStar.setVisibility(View.GONE);
                secondUnfilledStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdUnfilledStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 1:
                firstUnfilledStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondUnfilledStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdUnfilledStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 2:
                firstUnfilledStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondUnfilledStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdUnfilledStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 3:
                firstUnfilledStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondUnfilledStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdUnfilledStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        firstStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(incommingItem.getRate()!=1){
                    db.groceryDao().updateAData(incommingItem.getId(),1);
                    db.groceryDao().updateUserPoints(incommingItem.getId(),db.groceryDao().getitemById(incommingItem.getId()).getUserPoint()+(1-incommingItem.getRate())*2);
                    incommingItem.setRate(1);
                    handleRating();
                }
            }
        });
        secondStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(incommingItem.getRate()!=2){
                    db.groceryDao().updateAData(incommingItem.getId(),2);
                    db.groceryDao().updateUserPoints(incommingItem.getId(),db.groceryDao().getitemById(incommingItem.getId()).getUserPoint()+(2-incommingItem.getRate())*2);
                    incommingItem.setRate(2);
                    handleRating();
                }
            }
        });
        thirdStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(incommingItem.getRate()!=3){
                    db.groceryDao().updateAData(incommingItem.getId(),3);
                    db.groceryDao().updateUserPoints(incommingItem.getId(),db.groceryDao().getitemById(incommingItem.getId()).getUserPoint()+(3-incommingItem.getRate())*2);
                    incommingItem.setRate(3);
                    handleRating();
                }
            }
        });
        Log.d(TAG, "handleRating: userPoint After rating"+incommingItem.getName()+" "+db.groceryDao().getitemById(incommingItem.getId()).getUserPoint());
    }

    private void initViews() {
        reviewRecView=findViewById(R.id.reviewsRecyView);
        itemName=findViewById(R.id.itemName);
        itemPrice=findViewById(R.id.itemPrice);
        itemDescription=findViewById(R.id.itemDescription);
        itemImage=findViewById(R.id.itemImage);
        firstFilledStar=findViewById(R.id.firstFilledStar);
        firstUnfilledStar=findViewById(R.id.firstUnfillStar);
        firstStarRelLayout=findViewById(R.id.firstStarRecLayout);
        secondFilledStar=findViewById(R.id.secondFilledStar);
        secondUnfilledStar=findViewById(R.id.secondUnfillStar);
        secondStarRelLayout=findViewById(R.id.secondStarRecLayout);
        thirdFilledStar=findViewById(R.id.thirdFilledStar);
        thirdUnfilledStar=findViewById(R.id.thirdUnfillStar);
        thirdStarRelLayout=findViewById(R.id.thirdStarRecLayout);
        btnAddToCard=findViewById(R.id.btnAddToCart);
        txtAddReview=findViewById(R.id.addReviews);
        toolbar=findViewById(R.id.toolbar);
        db=DataBase.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(this,TrackUserTime.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound){
            unbindService(connection);
        }
    }
}