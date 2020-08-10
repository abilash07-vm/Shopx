package com.example.shopingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import static com.example.shopingapp.all_Categories_Dialog.activity;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cart:
                        Intent intent=new Intent(MainActivity.this,CartActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.categories:
                        all_Categories_Dialog dialog=new all_Categories_Dialog();
                        Bundle bundle=new Bundle();
                        bundle.putString(activity,"main_activity");
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(),"all_categories");
                        break;
                    case R.id.aboutus:
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this)
                                .setTitle("About Us")
                                .setMessage("Visit My Website For More Details..")
                                .setPositiveButton("Visit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent1=new Intent(MainActivity.this,WebActivity.class);
                                        startActivity(intent1);
                                    }
                                });
                        builder.create().show();
                        break;
                    case R.id.terms:
                        AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Terms And Condition")
                                .setMessage("This App is Safe and Your details are safe. No condition for this app just enjoy it")
                                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder1.create().show();
                        break;
                    case R.id.licences:
                        LisenceDialog dialog1=new LisenceDialog();
                        dialog1.show(getSupportFragmentManager(),"lisence");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containers,new main_Fragment());
        transaction.commit();
    }

    private void initViews() {
        drawer=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView);
        toolbar=findViewById(R.id.toolbar);
    }
}