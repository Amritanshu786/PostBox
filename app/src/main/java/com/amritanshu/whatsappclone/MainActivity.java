package com.amritanshu.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.amritanshu.whatsappclone.Adapter.FragmentsAdapter;
import com.amritanshu.whatsappclone.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    
    ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        firebaseAuth = FirebaseAuth.getInstance();
        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settings:
                Intent intent = new Intent (MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                Intent intentLogOut = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intentLogOut);
                finish();
                break;
            case R.id.groupChat:
                Intent intentGroupChat = new Intent(MainActivity.this, GroupChatActivity.class);
                startActivity(intentGroupChat);
                break;
        }
        return true;
    }
}