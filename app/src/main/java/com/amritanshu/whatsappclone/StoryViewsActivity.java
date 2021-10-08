package com.amritanshu.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amritanshu.whatsappclone.Fragments.StatusFragment;
import com.amritanshu.whatsappclone.databinding.ActivityStoryViewsBinding;

public class StoryViewsActivity extends AppCompatActivity {

    ActivityStoryViewsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoryViewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoryViewsActivity.this, MainActivity.class));
            }
        });
    }
}