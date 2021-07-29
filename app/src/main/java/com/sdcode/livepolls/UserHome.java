package com.sdcode.livepolls;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sdcode.livepolls.extraclasses.Messagee;
import com.sdcode.livepolls.fragment.CreatePollFragment;
import com.sdcode.livepolls.fragment.MyPollsFragment;
import com.sdcode.livepolls.fragment.PublicPollsFragment;


public class UserHome extends AppCompatActivity {
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreatePollFragment()).commit();

        navigationView = findViewById(R.id.bottom_navigation);


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.menu_create_pol) {
                    CreatePollFragment fragment = new CreatePollFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.commit();
                }

                if (id == R.id.menu_my_polls) {
                    MyPollsFragment fragment = new MyPollsFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.commit();
                }
                if (id == R.id.menu_public_polls) {
                    PublicPollsFragment fragment = new PublicPollsFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
        navigationView.setSelectedItemId(R.id.menu_create_pol);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menu_MyAccount):
            case (R.id.menu_AboutUs):
                Messagee.message(getApplicationContext(), "Coming soon!");
                return true;
            case (R.id.menu_Logout):

                SharedPreferences preferences = getSharedPreferences("userDetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userEmail", null);
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}