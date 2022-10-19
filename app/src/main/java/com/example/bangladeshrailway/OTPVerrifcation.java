package com.example.bangladeshrailway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPVerrifcation extends AppCompatActivity {
    String name,email,nid,pass,phone;
    EditText otp_input;
    Button confrim;
    String codebySystem;
    TextView number_showing;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverrifcation);

        otp_input= findViewById(R.id.otp);
        confrim=findViewById(R.id.submit);
        auth = FirebaseAuth.getInstance();

        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");
        nid = getIntent().getStringExtra("nid");
        pass = getIntent().getStringExtra("pass");
        phone = getIntent().getStringExtra("phone_number");
        String final_ph_num = "+88"+phone;

        number_showing = findViewById(R.id.number_show);
        number_showing.setText("Sending OTP to  : "+phone);
        sendVerificationCodeToUser(final_ph_num);

        auth = FirebaseAuth.getInstance();


        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String manual_otp = otp_input.getText().toString();
            if(!manual_otp.isEmpty()){
                verifycode(manual_otp);
            }
            }
        });
    }

    private void sendVerificationCodeToUser(String phone) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(OTPVerrifcation.this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                codebySystem=s;
                            }
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                String code = phoneAuthCredential.getSmsCode();
                                if(code!=null){
                                    otp_input.setText(code);
                                    verifycode(code);
                                }
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OTPVerrifcation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })// OnVerificationStateChangedCallbacks

                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codebySystem,code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(OTPVerrifcation.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                    createUseraccount(email,pass);
                    startActivity(new Intent(OTPVerrifcation.this,UserProfile.class));
                    finish();
                }
                else{
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(OTPVerrifcation.this, "Verification failed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OTPVerrifcation.this,Register.class));
                    }
                }
            }
        });
    }
public  void createUseraccount(String email,String pass){
    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                Toast.makeText(OTPVerrifcation.this, "Account Registration Successful", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(OTPVerrifcation.this, "Account Registration Failed", Toast.LENGTH_SHORT).show();
        }

    });
}

}


