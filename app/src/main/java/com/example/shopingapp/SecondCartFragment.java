package com.example.shopingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SecondCartFragment extends Fragment {
    public static final String ORDER_KEY="Order_Key";
    private EditText txtAddress,txtPincode,txtPhoneNumber,txtEmail;
    private TextView txtWarning;
    private Button btnBack,btnNext;
    private DataBase db;
    private String payment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cart_2,container,false);
        initViews(view);
        Bundle bundle=getArguments();
        if(bundle!=null) {
            String jsonOrder = bundle.getString(ORDER_KEY);
            if (jsonOrder != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<Order>() {
                }.getType();
                Order order = gson.fromJson(jsonOrder, type);
                txtAddress.setText(order.getAddress());
                txtPincode.setText(order.getPinCode());
                txtPhoneNumber.setText(order.getPhoneNumber());
                txtEmail.setText(order.getEmail());
                payment=order.getPaymentMethod();
            }
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container,new FirstCartFragment());
                transaction.commit();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address=txtAddress.getText().toString(),pinCode=txtPincode.getText().toString(),phoneNumber=txtPhoneNumber.getText().toString(),email=txtEmail.getText().toString();
                if(address.equals("") || pinCode.equals("") || phoneNumber.equals("") || email.equals("")){
                    txtWarning.setVisibility(View.VISIBLE);
                    txtWarning.setText("Please fill all the details to proceed...");
                }else{
                    txtWarning.setVisibility(View.GONE);
                    ArrayList<GroceryItem> cartItems= (ArrayList<GroceryItem>) db.groceryDao().getCartItems();
                    double total=0;
                    if(cartItems!=null){
                        for(GroceryItem item:cartItems){
                            total+=(item.getCartQuantity()*item.getPrice());
                        }
                        Order order=new Order(cartItems,address,pinCode,phoneNumber,email,total,payment,false);
                        Gson gson=new Gson();
                        String jsonOrder=gson.toJson(order);
                        Bundle bundle=new Bundle();
                        bundle.putString(ORDER_KEY,jsonOrder);
                        ThirdCartFragment thirdCartFragment=new ThirdCartFragment();
                        thirdCartFragment.setArguments(bundle);
                        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container,thirdCartFragment);
                        transaction.commit();
                    }
                }
            }
        });
        return view;
    }


    private void initViews(View view) {
        txtAddress=view.findViewById(R.id.address);
        txtPincode=view.findViewById(R.id.pinCode);
        txtPhoneNumber=view.findViewById(R.id.phoneNumber);
        txtEmail=view.findViewById(R.id.email);
        txtWarning=view.findViewById(R.id.warning);
        btnBack=view.findViewById(R.id.btnback);
        btnNext=view.findViewById(R.id.btnNext2);
        db=DataBase.getInstance(getActivity());
    }
}
