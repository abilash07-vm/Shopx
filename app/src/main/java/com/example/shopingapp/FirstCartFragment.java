package com.example.shopingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.shopingapp.SecondCartFragment.ORDER_KEY;

public class FirstCartFragment extends Fragment implements CartAdaptor.DeleteItems {
    @Override
    public void OnDeleteItems(GroceryItem item) {
        db.groceryDao().setQuatity(item.getId(),0);
        setAdaptor();
    }
    private RecyclerView recyclerView;
    private TextView yourCart,totalAmount,amount;
    private ImageView imageView;
    private Button btnNext;
    private RelativeLayout relativeLayout;
    private CartAdaptor adaptor;
    private DataBase db;
    private ArrayList<GroceryItem> cartElements;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_cart_1,container,false);
        initView(view);

        setAdaptor();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container,new SecondCartFragment());
                transaction.commit();
            }
        });
        return view;
    }

    private void setAdaptor() {
        int total=0;
        adaptor=new CartAdaptor(getActivity(),this);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartElements= (ArrayList<GroceryItem>) db.groceryDao().getCartItems();
        if(cartElements.size()>0){
            for(GroceryItem item:cartElements){
                total+=(item.getCartQuantity()*item.getPrice());
            }
            relativeLayout.setVisibility(View.GONE);
            adaptor.setCartItems(cartElements);
            yourCart.setVisibility(View.VISIBLE);
            totalAmount.setVisibility(View.VISIBLE);
            amount.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            amount.setText(String.valueOf(total));
        }else{
            relativeLayout.setVisibility(View.VISIBLE);
            yourCart.setVisibility(View.GONE);
            totalAmount.setVisibility(View.GONE);
            amount.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }
    }

    private void initView(View view) {
        recyclerView=view.findViewById(R.id.recyView);
        yourCart=view.findViewById(R.id.txtDescription);
        totalAmount=view.findViewById(R.id.totalAmount);
        amount=view.findViewById(R.id.amount);
        imageView=view.findViewById(R.id.rupee);
        btnNext=view.findViewById(R.id.btnNext1);
        relativeLayout=view.findViewById(R.id.emptyRelLayout);
        db=DataBase.getInstance(getActivity());
    }


}
