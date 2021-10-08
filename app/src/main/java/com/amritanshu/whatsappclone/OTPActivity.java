package com.amritanshu.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amritanshu.whatsappclone.Models.Users;
import com.amritanshu.whatsappclone.databinding.ActivityOTPBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class OTPActivity extends AppCompatActivity {

    ActivityOTPBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private String OTP, phoneUserName, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOTPBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        OTP = getIntent().getStringExtra("firebaseAuth");
        phoneUserName = getIntent().getStringExtra("userName");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verification_code = binding.etOTP.getText().toString();
                if(!verification_code.isEmpty()){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP , verification_code);
                    signIn(credential);
                }else{
                    Toast.makeText(OTPActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Users user = new Users(phoneUserName, phoneNumber);
                    String id = task.getResult().getUser().getUid();
                    database.getReference().child("Users").child(id).setValue(user);
                    database.getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("isVerified").setValue(true);
                    sendToMain();
                    Toast.makeText(OTPActivity.this, "Signed in with Phone", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(OTPActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser !=null){
            sendToMain();
        }
    }

    private void sendToMain(){
        startActivity(new Intent(OTPActivity.this , MainActivity.class));
        //finish();
    }
}