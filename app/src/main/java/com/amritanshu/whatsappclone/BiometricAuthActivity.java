package com.amritanshu.whatsappclone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.Executor;

public class BiometricAuthActivity extends AppCompatActivity {

    TextView tvAuthStatus;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo  promptInfoBelow10, promptInfoAbove10;

    //@RequiresApi(api = Build.VERSION_CODES.P)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_auth);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().hide();
        tvAuthStatus = findViewById(R.id.tvAuthStatus);

        //init values
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(BiometricAuthActivity.this, executor, new BiometricPrompt.AuthenticationCallback(){
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                //If any error come while auth
                tvAuthStatus.setText("Error " + errString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Failed to auth
                tvAuthStatus.setText("Authentication Failed");
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Auth Successful
                Intent intent = new Intent(BiometricAuthActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
                tvAuthStatus.setText("Successfully Authenticated");
            }
        });

        // Setup title, description on auth dialog
        promptInfoBelow10 = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint")
                .setNegativeButtonText("Cancel")
                .build();


        promptInfoAbove10 = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint or face")
                .setNegativeButtonText("Cancel")
                .build();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)
        {
            biometricPrompt.authenticate(promptInfoBelow10);
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            biometricPrompt.authenticate(promptInfoAbove10);
        }
        else
        {
            Intent intent = new Intent(BiometricAuthActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
        }
    }
}