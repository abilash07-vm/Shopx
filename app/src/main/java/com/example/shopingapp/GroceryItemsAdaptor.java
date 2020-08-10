package com.example.shopingapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import static com.example.shopingapp.groceryItemActivity.key;

public class GroceryItemsAdaptor extends RecyclerView.Adapter<GroceryItemsAdaptor.ViewHolder> {
    private static final String TAG = "GroceryItemsAdaptor";
    private ArrayList<GroceryItem> items=new ArrayList<>();
    private DataBase db;
    private Context context;
    private String activityName;

    public GroceryItemsAdaptor(Context context,String activityName) {
        this.context = context;
        this.activityName=activityName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(items.get(position).getName());
        holder.price.setText(String.valueOf(items.get(position).getPrice()));
        Glide.with(context)
                .asBitmap()
                .load(items.get(position).getImageurl())
                .into(holder.img);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,groceryItemActivity.class);
                intent.putExtra(key,items.get(position));
                context.startActivity(intent);
                if(activityName.equals("search")){
                    db.groceryDao().updateUserPoints(items.get(position).getId(),db.groceryDao().getitemById(items.get(position).getId()).getUserPoint()+1);
                    Log.d(TAG, "onClick: userPoint After searching"+items.get(position).getName()+" "+db.groceryDao().getitemById(items.get(position).getId()).getUserPoint());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<GroceryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView parent;
        private TextView name,price;
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent =itemView.findViewById(R.id.parent);
            name=itemView.findViewById(R.id.groceryName);
            price=itemView.findViewById(R.id.groceryPrice);
            img=itemView.findViewById(R.id.groceryimg);
            db=DataBase.getInstance(context);
        }
    }
}
