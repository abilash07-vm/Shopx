package com.example.shopingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdaptor extends RecyclerView.Adapter<ReviewAdaptor.ViewHolder> {
    private ArrayList<Review> allReviews=new ArrayList<>();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.reviewUserName.setText("Name : "+allReviews.get(position).getUsername());
        holder.reviewText.setText(allReviews.get(position).getText());
        holder.reviewDate.setText(allReviews.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return allReviews.size();
    }

    public void setAllReviews(ArrayList<Review> allReviews) {
        this.allReviews = allReviews;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView reviewUserName,reviewText,reviewDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewUserName=itemView.findViewById(R.id.reviewName);
            reviewText=itemView.findViewById(R.id.reviewText);
            reviewDate=itemView.findViewById(R.id.reviewDate);
        }
    }
}
