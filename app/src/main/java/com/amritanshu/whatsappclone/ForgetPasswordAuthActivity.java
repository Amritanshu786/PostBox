package com.amritanshu.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.amritanshu.whatsappclone.databinding.ActivityForgetPasswordAuthBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordAuthActivity extends AppCompatActivity {

    ActivityForgetPasswordAuthBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        binding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=binding.etEmailId.getText().toString().trim();

                if(email.isEmpty())
                {
                    binding.etEmailId.setError("Email is Required!");
                    binding.etEmailId.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    binding.etEmailId.setError("Please provide valid email!");
                    binding.etEmailId.requestFocus();
                    return;
                }

                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgetPasswordAuthActivity.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgetPasswordAuthActivity.this, SigninActivity.class));
                        }
                        else
                        {
                            Toast.makeText(ForgetPasswordAuthActivity.this, "Kindly provide the correct email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}