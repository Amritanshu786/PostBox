package com.amritanshu.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.amritanshu.whatsappclone.Models.Users;
import com.amritanshu.whatsappclone.databinding.ActivityPhoneAuthBinding;
import com.amritanshu.whatsappclone.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    ActivityPhoneAuthBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country_code = binding.etCountryCode.getText().toString();
                String phone = binding.etPhoneNumber.getText().toString();
                String phoneNumber = "+" + country_code + "" + phone;
                if (!country_code.isEmpty() || !phone.isEmpty()) {
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(PhoneAuthActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                } else {
                    Toast.makeText(PhoneAuthActivity.this, "Please Enter Country Code and Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(PhoneAuthActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                //sometime the code is not detected automatically
                //so user has to manually enter the code
                Toast.makeText(PhoneAuthActivity.this, "OTP has been Sent", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent otpIntent = new Intent(PhoneAuthActivity.this, OTPActivity.class); //OTP.class
                        otpIntent.putExtra("firebaseAuth", s);
                        otpIntent.putExtra("userName", binding.etPhoneUserName.getText().toString());
                        otpIntent.putExtra("phoneNumber", binding.etPhoneNumber.getText().toString());
                        startActivity(otpIntent);
                    }
                }, 100);

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user !=null){
            sendToMain();
        }
    }
    private void sendToMain(){
        Intent mainIntent = new Intent(PhoneAuthActivity.this , MainActivity.class);
        startActivity(mainIntent);
        //finish();
    }
    private void signIn(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendToMain();
                }else{
                    Toast.makeText(PhoneAuthActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}