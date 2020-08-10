package com.example.shopingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static com.example.shopingapp.SecondCartFragment.ORDER_KEY;

public class SucessPaymentFragment extends Fragment {
    private static final String TAG = "SucessPaymentFragment";
    private DataBase db;
    private Button btnSucess;
    private TextView txtSucess;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_payment_sucess,container,false);
        btnSucess=view.findViewById(R.id.btnContinueShopping);
        txtSucess=view.findViewById(R.id.txtSucess);
        db=DataBase.getInstance(getActivity());

        Bundle bundle=getArguments();
        if(bundle!=null){
            String jsonOrder=bundle.getString(ORDER_KEY);
            if(jsonOrder!=null){
                Gson gson=new Gson();
                Type type=new TypeToken<Order>(){}.getType();
                Order order=gson.fromJson(jsonOrder,type);
                if(order!=null){
                    if(order.isSuccess()){
                        txtSucess.setText("Your Payment " +
                                "\nis Sucessful" +
                                "\n\nYour Order will be arrived \nwithin 2 or 3 days");
                        btnSucess.setText("Continue Shoping");
                        for(GroceryItem item:order.getItems()){
                            db.groceryDao().updatePopularityPoints(item.getId(),item.getPopularityPoint()+1);
                            db.groceryDao().updateUserPoints(item.getId(),item.getUserPoint()+4);
                            db.groceryDao().setQuatity(item.getId(),0);
                            Log.d(TAG, "onCreateView: userPoint After purshasing"+item.getName()+" "+db.groceryDao().getitemById(item.getId()).getUserPoint());
                        }

                        btnSucess.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(),MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }else{
                        txtSucess.setText("Your Payment failed");
                        btnSucess.setText("Try Again");
                        btnSucess.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle1=new Bundle();
                                bundle1.putString(ORDER_KEY,jsonOrder);
                                ThirdCartFragment thirdCartFragment=new ThirdCartFragment();
                                thirdCartFragment.setArguments(bundle1);

                                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.container,thirdCartFragment);
                                transaction.commit();
                            }
                        });
                    }

                }

            }
        }

        return view;
    }
}
