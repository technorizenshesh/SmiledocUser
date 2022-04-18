package com.smiledocuser.act;

import static com.smiledocuser.retrofit.Constant.emailPattern;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.smiledocuser.MainActivity;
import com.smiledocuser.R;
import com.smiledocuser.databinding.ActivityLoginBinding;
import com.smiledocuser.model.VerifyOtpModal;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.retrofit.Constant;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.GPSTracker;
import com.smiledocuser.utils.NetworkAvailablity;
import com.smiledocuser.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAct extends AppCompatActivity {
    public String TAG = "LoginAct";
    ActivityLoginBinding binding;
    APIInterface apiInterface;
    String latitude = "",longitude = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        initViews();
    }

    private void initViews() {

        binding.loginBtn.setOnClickListener(v -> { validation(); });


        binding.regBtn1.setOnClickListener(v -> {
             startActivity(new Intent(LoginAct.this,RegisterAct.class));
        });
    }




    private void validation() {
        if (binding.etEmail.getText().toString().equals("")) {
            Toast.makeText(LoginAct.this, getString(R.string.please_enter_email_address), Toast.LENGTH_SHORT).show();
        } else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            Toast.makeText(LoginAct.this, getString(R.string.invalid_email_address), Toast.LENGTH_SHORT).show();
        }else if (binding.edtPassword.getText().toString().equals("")) {
            Toast.makeText(LoginAct.this, getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
        }
        else {
            if(NetworkAvailablity.checkNetworkStatus(LoginAct.this)) userLogin();
            else Toast.makeText(LoginAct.this,getString(R.string.network_failure), Toast.LENGTH_LONG).show();
        }
    }

    private void userLogin() {
        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("email", binding.etEmail.getText().toString());
        map.put("password", binding.edtPassword.getText().toString());
        map.put("register_id", "textggg");
        map.put("lat", latitude);
        map.put("lon", longitude);
        map.put("type", Constant.USER);
        Log.e(TAG, "Login Request " + map);
        Call<ResponseBody> loginCall = apiInterface.login(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        //JSONObject object = new JSONObject(responseData);
                        VerifyOtpModal userModel = new Gson().fromJson(responseData, VerifyOtpModal.class);

                        if (userModel.status.equals("1")) {
                            SessionManager.writeString(LoginAct.this, Constant.USER_INFO,responseData) ;
                             startActivity(new Intent(LoginAct.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        } else {
                            Toast.makeText(LoginAct.this,userModel.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logException(e);
                    Toast.makeText(LoginAct.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    public void logException(Exception e) {
        Log.e(TAG, e.getMessage());
    }


    private void getCurrentLocation() {
        GPSTracker track = new GPSTracker(this);
        if (track.canGetLocation()) {
            latitude = String.valueOf(track.getLatitude());
            longitude = String.valueOf(track.getLongitude());

        } else {
            track.showSettingsAlert();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
    }



}
