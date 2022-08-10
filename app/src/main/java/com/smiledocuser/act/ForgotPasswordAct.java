package com.smiledocuser.act;

import static com.smiledocuser.retrofit.Constant.emailPattern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.smiledocuser.MainActivity;
import com.smiledocuser.R;
import com.smiledocuser.databinding.ActivityForgotPassBinding;
import com.smiledocuser.model.VerifyOtpModal;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.retrofit.Constant;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.NetworkAvailablity;
import com.smiledocuser.utils.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordAct extends AppCompatActivity   {
    public static String TAG = "ForgotPasswordAct";
    ActivityForgotPassBinding binding;
    APIInterface apiInterface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_pass);
        initView();
    }

    private void initView() {
        binding.ivBack.setOnClickListener(v -> {finish();});

        binding.btnSend.setOnClickListener(v -> {
            validation();
        });
    }

    private void validation() {
        if (binding.etEmail.getText().toString().equals("")) {
            binding.etEmail.setError(getString(R.string.please_enter_email));
            binding.etEmail.setFocusable(true);
        } else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            binding.etEmail.setError(getString(R.string.wrong_email));
            binding.etEmail.setFocusable(true);
        }
        else {
            if(NetworkAvailablity.checkNetworkStatus(ForgotPasswordAct.this)) forGotPass(); else
                Toast.makeText(ForgotPasswordAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }


    private void forGotPass() {
        DataManager.getInstance().showProgressMessage(ForgotPasswordAct.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",binding.etEmail.getText().toString());
        Log.e(TAG,"ForgotPass Request :"+map.toString());
        Call<ResponseBody> signupCall = apiInterface.forgotPass(map);
        signupCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                   // if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        JSONObject object = new JSONObject(responseData);
                        Log.e(TAG, "ForgotPass Response :" + responseData);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(ForgotPasswordAct.this, getString(R.string.send_successfully), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ForgotPasswordAct.this, object.getString("result"), Toast.LENGTH_SHORT).show();
                        }

                   // }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }
        });

    }


}
