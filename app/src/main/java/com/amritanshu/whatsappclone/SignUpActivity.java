package com.amritanshu.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amritanshu.whatsappclone.Models.Users;
import com.amritanshu.whatsappclone.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().hide();   //To hide the Tool bar
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etUserName.getText().toString().isEmpty())
                {
                    binding.etUserName.setError("Enter your UserName");
                    return;
                }
                if(binding.etEmail.getText().toString().isEmpty())
                {
                    binding.etEmail.setError("Enter your email");
                    return;
                }
                if(binding.etPassword.getText().toString().isEmpty())
                {
                    binding.etPassword.setError("Enter your password");
                    return;
                }
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword
                        (binding.etEmail.getText().toString(), binding.etPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Users user = new Users(binding.etUserName.getText().toString(), binding.etEmail.getText().toString(), binding.etPassword.getText().toString(), false);
                                        String id = firebaseAuth.getCurrentUser().getUid();
                                        database.getReference().child("Users").child(id).setValue(user);

                                        //Toast.makeText(SignUpActivity.this, "User registered successfully. Please verify your email id.", Toast.LENGTH_SHORT).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);        // Or MainActivity.this
                                        builder.setTitle("Success");
                                        builder.setMessage("User registered successfully. Please verify your email id");
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        builder.show();

                                        binding.etEmail.setText("");
                                        binding.etPassword.setText("");
                                        binding.etUserName.setText("");
                                    }
                                    else
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);        // Or MainActivity.this
                                        builder.setTitle("ERROR");
                                        builder.setMessage(task.getException().getMessage());
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        builder.show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            //Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);        // Or MainActivity.this
                            builder.setTitle("ERROR");
                            builder.setMessage(task.getException().getMessage());
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                        }
                    }
                });
            }
        });

        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnSignUpGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SignUpActivity.this, GoogleAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        binding.btnSignUpFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SignUpActivity.this, FacebookAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        binding.signUpPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SignUpActivity.this, PhoneAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}