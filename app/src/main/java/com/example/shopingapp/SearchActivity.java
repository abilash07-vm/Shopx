package com.example.shopingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.shopingapp.all_Categories_Dialog.activity;
import static com.example.shopingapp.all_Categories_Dialog.category;

public class SearchActivity extends AppCompatActivity implements all_Categories_Dialog.GetCategory{
    @Override
    public void onGetCategory(String category) {
        ArrayList<GroceryItem> items= (ArrayList<GroceryItem>) db.groceryDao().getItemsByCategory(category);
        adaptor.setItems(items);
    }

    private static final String TAG = "SearchActivity";
    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private EditText searchBox;
    private TextView firstCategories,secondCategories,thirdCategories,seeAllCategories;
    private ImageView btnSearch;
    private GroceryItemsAdaptor adaptor;
    private DataBase db;
    private ArrayList<GroceryItem> allItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        initBottomView();
        allItems= (ArrayList<GroceryItem>) db.groceryDao().getAllItems();

        setSupportActionBar(toolbar);
        adaptor=new GroceryItemsAdaptor(this,"search");
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        Intent intent=getIntent();
        if(intent!=null){
            String selectedCategory=intent.getStringExtra(category);
            if(selectedCategory!=null){
                adaptor.setItems((ArrayList<GroceryItem>) db.groceryDao().getItemsByCategory(selectedCategory));
            }
        }
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearch();
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                initSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ArrayList<String> allCategories= (ArrayList<String>) db.groceryDao().getAllCategories();
        if(allCategories!=null){
            Log.d(TAG, "onCreate: "+allCategories);
            if(allCategories.size()<3){
                showCategory(allCategories,allCategories.size());
            }else{
                showCategory(allCategories,3);
            }

        }
        seeAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_Categories_Dialog dialog=new all_Categories_Dialog();
                Bundle bundle=new Bundle();
                bundle.putString(activity,"search_activity");
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(),"All Categories dialog");
            }
        });
    }

    private void showCategory(final ArrayList<String> allCategories, int size) {
        switch (size){
            case 1:
                firstCategories.setVisibility(View.VISIBLE);
                firstCategories.setText(allCategories.get(0));
                secondCategories.setVisibility(View.GONE);
                thirdCategories.setVisibility(View.GONE);
                firstCategories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items= (ArrayList<GroceryItem>) db.groceryDao().getItemsByCategory(allCategories.get(0));
                        if(items!=null){
                            adaptor.setItems(items);
                        }
                    }
                });
                break;
            case 2:
                firstCategories.setVisibility(View.VISIBLE);
                firstCategories.setText(allCategories.get(0));
                secondCategories.setVisibility(View.VISIBLE);
                secondCategories.setText(allCategories.get(1));
                thirdCategories.setVisibility(View.GONE);
                firstCategories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items= (ArrayList<GroceryItem>) db.groceryDao().getItemsByCategory(allCategories.get(0));
                        if(items!=null){
                            adaptor.setItems(items);
                        }
                    }
                });
                secondCategories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items= (ArrayList<GroceryItem>) db.groceryDao().getItemsByCategory(allCategories.get(1));
                        if(items!=null){
                            adaptor.setItems(items);
                        }
                    }
                });
                break;
            case 3:
                firstCategories.setVisibility(View.VISIBLE);
                firstCategories.setText(allCategories.get(0));
                secondCategories.setVisibility(View.VISIBLE);
                secondCategories.setText(allCategories.get(1));
                thirdCategories.setVisibility(View.VISIBLE);
                thirdCategories.setText(allCategories.get(2));
                firstCategories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items= (ArrayList<GroceryItem>) db.groceryDao().getItemsByCategory(allCategories.get(0));
                        if(items!=null){
                            adaptor.setItems(items);
                        }
                    }
                });
                secondCategories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items= (ArrayList<GroceryItem>) db.groceryDao().getItemsByCategory(allCategories.get(1));
                        if(items!=null){
                            adaptor.setItems(items);
                        }
                    }
                });
                thirdCategories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items= (ArrayList<GroceryItem>) db.groceryDao().getItemsByCategory(allCategories.get(2));
                        if(items!=null){
                            adaptor.setItems(items);
                        }
                    }
                });
                break;
            default:
                firstCategories.setVisibility(View.GONE);
                secondCategories.setVisibility(View.GONE);
                thirdCategories.setVisibility(View.GONE);
        }
    }

    private void initSearch() {
        String text=searchBox.getText().toString().trim();
        if(!text.equals("")){
            if(allItems!=null){
                ArrayList<GroceryItem> items=new ArrayList<>();

                for(GroceryItem item:allItems){
                    String[] splitedItems=item.getName().split(" ");
                    for(int i=0;i<splitedItems.length;i++){
                        int l=text.length();
                        if((l<splitedItems[i].length() && text.equalsIgnoreCase(splitedItems[i].substring(0,l))) || (splitedItems[i].equalsIgnoreCase(text)) || checkMulti(text,splitedItems)){
                            items.add(item);
                            break;
                        }
                    }
                }
                adaptor.setItems(items);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
        else{
            recyclerView.setVisibility(View.GONE);
        }
    }

    private boolean checkMulti(String text, String[] splitedItems) {
        String[] splitedText=text.split(" ");
        if(splitedText.length <=1){
            return false;
        }
        int count=0;
        List<String> splitedItemInList=Arrays.asList(splitedItems);
        for(String str:splitedText){
            if(splitedItemInList.contains(str)){
                count++;
            }else {
                for(String string:splitedItemInList){
                    if(string.length()>str.length() && str.equalsIgnoreCase(string.substring(0,str.length()))){
                        count++;
                        break;
                    }
                }
            }
        }
        return count == splitedText.length;
    }

    private void initBottomView() {
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Intent intent=new Intent(SearchActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case R.id.search:
                        break;
                    case R.id.cart:
                        Intent intent1=new Intent(SearchActivity.this,CartActivity.class);
                        startActivity(intent1);
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initViews() {
        toolbar=findViewById(R.id.toolbar);
        bottomNavigationView=findViewById(R.id.bottomNavView);
        recyclerView=findViewById(R.id.recyView);
        searchBox=findViewById(R.id.searchBox);
        btnSearch=findViewById(R.id.btnSearch);
        firstCategories=findViewById(R.id.firstCategories);
        secondCategories=findViewById(R.id.secondCategories);
        thirdCategories=findViewById(R.id.thirdCategories);
        seeAllCategories=findViewById(R.id.seeAllCategories);
        db=DataBase.getInstance(this);
    }
}