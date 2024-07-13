package com.example.otpapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Second extends AppCompatActivity {
    EditText e1;
    Button b1;
    String phone;
    String otp;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        phone = getIntent().getStringExtra("mobile").toString();
        e1 = findViewById(R.id.editTextText2);
        b1 = findViewById(R.id.button2);
        firebaseAuth = FirebaseAuth.getInstance();
        genotp();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e1.getText().toString();
                if(s1.isEmpty())
                {
                    Toast.makeText(Second.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(s1.length()<6)
                    {
                        Toast.makeText(Second.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp,s1);
                        signInWithPhoneAuthCredential(credential);
                    }
                }
            }
        });


    }private void genotp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        otp = s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(Second.this, "OTP MISMATCHED", Toast.LENGTH_SHORT).show();

                    }
                }
        );

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Second.this, "DataBase Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Second.this,Third.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(Second.this, "OTP MISMATCHED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}