package com.example.shopingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.ViewHolder> {
    public interface DeleteItems{
        void OnDeleteItems(GroceryItem item);
    }
    private DeleteItems deleteItems;
    private Context context;
    private Fragment fragment;
    private ArrayList<GroceryItem> cartItems=new ArrayList<>();

    public CartAdaptor(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.itemName.setText(cartItems.get(position).getName());
        holder.price.setText(String.valueOf(cartItems.get(position).getPrice()));
        holder.quantity.setText(String.valueOf(cartItems.get(position).getCartQuantity()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context)
                        .setTitle("Delete From Cart")
                        .setMessage("Are you sure to delete "+cartItems.get(position).getName()+" from your cart ?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    deleteItems= (DeleteItems) fragment;
                                    deleteItems.OnDeleteItems(cartItems.get(position));
                                }catch (ClassCastException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setCartItems(ArrayList<GroceryItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName,price,delete,quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.itemName);
            price=itemView.findViewById(R.id.txtPrice);
            quantity=itemView.findViewById(R.id.quantity);
            delete=itemView.findViewById(R.id.btnDelete);
        }
    }
}
