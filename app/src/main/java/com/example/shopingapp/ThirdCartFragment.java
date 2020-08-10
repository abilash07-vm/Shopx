package com.example.shopingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.shopingapp.SecondCartFragment.ORDER_KEY;

public class ThirdCartFragment extends Fragment {
    private static final String TAG = "ThirdCartFragment";
    private TextView orders,price,address,phoneNumber;
    private RadioGroup radioGroup;
    private RadioButton creditCard,gpay;
    private String jsonOrder2;
    private Button btnBack,btnFinish;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cart_3,container,false);
        initViews(view);
        final Bundle bundle=getArguments();
        if(bundle!=null){
            final String jsonOrder=bundle.getString(ORDER_KEY);
            if(jsonOrder!=null){
                final Gson gson=new Gson();
                Type type=new TypeToken<Order>(){}.getType();
                final Order order=gson.fromJson(jsonOrder,type);
                if(order!=null){
                    String items="";
                    for(GroceryItem i:order.getItems()){
                        items+= "\n\t" +i.getName();
                    }
                    orders.setText(items);
                    price.setText(String.valueOf(order.getTotalPrice()));
                    address.setText(order.getAddress());
                    phoneNumber.setText(order.getPhoneNumber());
                    if(order.getPaymentMethod()!=null && order.getPaymentMethod().equalsIgnoreCase("GPay")){
                        ((RadioButton)radioGroup.findViewById(R.id.gpay)).setChecked(true);
                    }
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle1=new Bundle();
                            if(jsonOrder2==null)
                            bundle1.putString(ORDER_KEY,jsonOrder);
                            else
                                bundle1.putString(ORDER_KEY,jsonOrder2);
                            SecondCartFragment secondCartFragment=new SecondCartFragment();
                            secondCartFragment.setArguments(bundle1);
                            FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.container,secondCartFragment);
                            transaction.commit();
                        }
                    });
                    btnFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (radioGroup.getCheckedRadioButtonId()){
                                case R.id.credit:
                                    order.setPaymentMethod("Credit Card");
                                    break;
                                case R.id.gpay:
                                    order.setPaymentMethod("GPay");
                                    break;
                                default:
                                    break;
                            }
                            // TODO: 06-08-2020 upload
                            order.setSuccess(true);
                            jsonOrder2=gson.toJson(order);

                            HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor()
                                    .setLevel(HttpLoggingInterceptor.Level.BODY);
                            OkHttpClient client=new OkHttpClient.Builder()
                                    .addInterceptor(interceptor)
                                    .build();

                            Retrofit retrofit=new Retrofit.Builder()
                                    .baseUrl("https://jsonplaceholder.typicode.com/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .client(client)
                                    .build();

                            OrderEndPoint endPoint=retrofit.create(OrderEndPoint.class);
                            Call<Order> call=endPoint.newOrder(order);
                            call.enqueue(new Callback<Order>() {
                                @Override
                                public void onResponse(Call<Order> call, Response<Order> response) {
                                    Log.d(TAG, "onResponse: code"+response);
                                    if(response.isSuccessful()){
                                        Gson gson1=new Gson();
                                        String jsonOrder3=gson1.toJson(order);
                                        Bundle bundle1=new Bundle();
                                        bundle1.putString(ORDER_KEY,jsonOrder3);
                                        SucessPaymentFragment sucessPaymentFragment=new SucessPaymentFragment();
                                        sucessPaymentFragment.setArguments(bundle1);
                                        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.container,sucessPaymentFragment);
                                        transaction.commit();
                                    }

                                }

                                @Override
                                public void onFailure(Call<Order> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }
                    });
                }
            }
        }
        return view;
    }

    private void initViews(View view) {
        orders=view.findViewById(R.id.orderName);
        address=view.findViewById(R.id.orderAddress);
        phoneNumber=view.findViewById(R.id.orderPhoneNumber);
        price=view.findViewById(R.id.orderPrice);
        radioGroup=view.findViewById(R.id.radiogrp);
        btnBack=view.findViewById(R.id.btnBack);
        btnFinish=view.findViewById(R.id.btnFinish);
        creditCard=view.findViewById(R.id.credit);
        gpay=view.findViewById(R.id.gpay);
    }
}
