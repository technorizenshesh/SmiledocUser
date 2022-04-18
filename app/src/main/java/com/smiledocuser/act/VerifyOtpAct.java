package com.smiledocuser.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.smiledocuser.MainActivity;
import com.smiledocuser.R;
import com.smiledocuser.databinding.ActivityVerifyOtpBinding;
import com.smiledocuser.model.VerifyOtpModal;
import com.smiledocuser.retrofit.Constant;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.SessionManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpAct extends AppCompatActivity {
    public String TAG = "VerifyOtpAct";
    ActivityVerifyOtpBinding binding;

    private String mobile = "";
    private String id;
    private FirebaseAuth mAuth;
    private String edit_string1;
    private String edit_string2;
    private String edit_string3;
    private String edit_string4;
    private String edit_string5;
    private String edit_string6;

    private String final_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);
        FirebaseApp.initializeApp(VerifyOtpAct.this);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            mobile = bundle.getString("mobile");
        }

        sendVerificationCode();
        SetupUI();

/*
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.resend.setText("" + millisUntilFinished / 1000);
                binding.resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                binding.resend.setText("resend");
                binding.resend.setEnabled(true);
            }
        }.start();
*/
    }

    private void sendVerificationCode() {

        binding.description.setText("We have sent you an SMS on" + mobile + " with 6 digit verfication code");
/*
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.resend.setText("" + millisUntilFinished / 1000);
                binding.resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                binding.resend.setText("resend");
                binding.resend.setEnabled(true);
            }
        }.start();
*/

       /* PhoneAuthProvider.getInstance().verifyPhoneNumber(mobile.replace(" ", "")
                , 60, TimeUnit.SECONDS, this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        VerifyOtpScreen.this.id = id;
                        Toast.makeText(VerifyOtpScreen.this, "Please enter 6 digit verification code", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(VerifyOtpScreen.this, "" + phoneAuthCredential.getSmsCode(),
                                Toast.LENGTH_SHORT).show();

                        signInWithPhoneAuthCredential(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // ProjectUtil.pauseProgressDialog();
                        Toast.makeText(VerifyOtpScreen.this, "Failed" + e, Toast.LENGTH_SHORT).show();
                    }

                });*/
    }

/*
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(VerifyOtpScreen.this, MainActivity.class)
                                    .putExtra("status", "login3"));

                            Toast.makeText(VerifyOtpScreen.this, "Otp  Match SuccessFully ", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(VerifyOtpScreen.this, "Otp Not match.", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }

                        }
                    }
                });
    }
*/

    private void SetupUI() {

        binding.edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.edit2.setText("");
                    binding.edit2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.edit3.setText("");
                    binding.edit3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.edit4.setText("");
                    binding.edit4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });
        binding.edit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.edit4.setText("");
                    binding.edit4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });


        binding.verifyBtn.setOnClickListener(v -> {

            edit_string1 = binding.edit1.getText().toString();
            edit_string2 = binding.edit2.getText().toString();
            edit_string3 = binding.edit3.getText().toString();
            edit_string4 = binding.edit4.getText().toString();


            final_otp = edit_string1 + edit_string2 + edit_string3 + edit_string4;

            Log.e("final_otp>>", final_otp);

          //  PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, final_otp);

        //    signInWithPhoneAuthCredential(credential);
        });


    }






}
