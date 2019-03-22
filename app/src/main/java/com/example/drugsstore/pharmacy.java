package com.example.drugsstore;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class pharmacy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        BottomNavigationView bottomNav = findViewById(R.id.pharmacy_navgation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.ph_container,
                    new ph_profile()).commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_ph_profile:
                            selectedFragment = new ph_profile();
                            break;
                        case R.id.nav_ph_order:
                            selectedFragment = new comp_recyclerview();
                            break;
                        case R.id.nav_myproduct:
                            selectedFragment = new comp_product();
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.ph_container,
                            selectedFragment).commit();

                    return true;
                }
            };
    }
