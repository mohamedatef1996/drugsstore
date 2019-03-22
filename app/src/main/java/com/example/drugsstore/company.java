package com.example.drugsstore;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class company extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        BottomNavigationView bottomNav = findViewById(R.id.company_navagtion);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,
                    new comp_profile()).commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_profile:
                            selectedFragment = new comp_profile();
                            break;
                        case R.id.na_add:
                            selectedFragment = new comp_addproduct();
                            break;
                        case R.id.nav_myproduct:
                            selectedFragment = new comp_product();
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
    }
