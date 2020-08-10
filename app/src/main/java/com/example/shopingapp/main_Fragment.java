package com.example.shopingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class main_Fragment extends Fragment {
    private static final String TAG = "main_Fragment";
    private BottomNavigationView bottomNavigationView;
    private RecyclerView newItemRecyView,popularItemRecyView,suggesteditemRecyView;
    private GroceryItemsAdaptor newItemsAdaptor,popularItemAdaptor,suggestedItemAdaptor;
    private DataBase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main,container,false);
        initAllElements(view);
        initBottomView();

        initRecyView();

        return view;
    }

    private void initRecyView() {
        ArrayList<GroceryItem> newItems= (ArrayList<GroceryItem>) db.groceryDao().getAllItems();
        Comparator<GroceryItem> newItemComparator=new Comparator<GroceryItem>() {
            @Override
            public int compare(GroceryItem o1, GroceryItem o2) {
                return o1.getId()-o2.getId();
            }
        };
        Comparator<GroceryItem> reverseComporator= Collections.reverseOrder(newItemComparator);
        Collections.sort(newItems,reverseComporator);
        newItemsAdaptor.setItems(newItems);

        ArrayList<GroceryItem> popularItems= (ArrayList<GroceryItem>) db.groceryDao().getAllItems();
        Comparator<GroceryItem> popularItemComparator=new Comparator<GroceryItem>() {
            @Override
            public int compare(GroceryItem o1, GroceryItem o2) {
                return o1.getPopularityPoint()-o2.getPopularityPoint();
            }
        };
        Comparator<GroceryItem> reversepopularComparator=Collections.reverseOrder(popularItemComparator);
        Collections.sort(popularItems,reversepopularComparator);
        popularItemAdaptor.setItems(popularItems);

        ArrayList<GroceryItem> suggestedItems= (ArrayList<GroceryItem>) db.groceryDao().getAllItems();
        Comparator<GroceryItem> suggestComparator=new Comparator<GroceryItem>() {
            @Override
            public int compare(GroceryItem o1, GroceryItem o2) {
                return o1.getUserPoint()-o2.getUserPoint();
            }
        };
        Comparator<GroceryItem> suggestedreverseComparator=Collections.reverseOrder(suggestComparator);
        Collections.sort(suggestedItems,suggestedreverseComparator);
        suggestedItemAdaptor.setItems(suggestedItems);
    }

    private void initAllElements(View view) {
        bottomNavigationView=view.findViewById(R.id.bottomNavView);

        newItemRecyView=view.findViewById(R.id.newitemsRecyview);
        newItemsAdaptor=new GroceryItemsAdaptor(getActivity(),"main");
        newItemRecyView.setAdapter(newItemsAdaptor);
        newItemRecyView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));

        popularItemRecyView=view.findViewById(R.id.popularitemsRecyview);
        popularItemAdaptor=new GroceryItemsAdaptor(getActivity(),"main");
        popularItemRecyView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        popularItemRecyView.setAdapter(popularItemAdaptor);


        suggesteditemRecyView=view.findViewById(R.id.suggesteditemsRecyview);
        suggestedItemAdaptor=new GroceryItemsAdaptor(getActivity(),"main");
        suggesteditemRecyView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        suggesteditemRecyView.setAdapter(suggestedItemAdaptor);

        db=DataBase.getInstance(getActivity());
    }

    private void initBottomView() {
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Toast.makeText(getContext(),item+" selected",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.search:
                        Intent intent=new Intent(getActivity(),SearchActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.cart:
                        Intent intent1=new Intent(getActivity(),CartActivity.class);
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecyView();
    }
}
