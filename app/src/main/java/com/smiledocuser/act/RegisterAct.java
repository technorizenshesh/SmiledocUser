package com.smiledocuser.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.smiledocuser.MainActivity;
import com.smiledocuser.R;
import com.smiledocuser.databinding.ActivityLoginBinding;
import com.smiledocuser.databinding.ActivityRegisterBinding;
import com.smiledocuser.model.VerifyOtpModal;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.retrofit.Constant;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.GPSTracker;
import com.smiledocuser.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterAct extends AppCompatActivity {
    public String TAG = "LoginAct";
    ActivityRegisterBinding binding;
    APIInterface apiInterface;
    String latitude = "",longitude = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        binding.loginBtn1.setOnClickListener(v -> finish());

        binding.regBtn.setOnClickListener(v -> {
            SignupAPI();
        });

        binding.dob.setOnClickListener(v -> {
            Calendar myCalendar = Calendar.getInstance();

            int selectedYear = myCalendar.get(Calendar.YEAR);
            int selectedMonth = myCalendar.get(Calendar.MONTH);
            int selectedDayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> binding.dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
            datePickerDialog.show();
        });

        binding.genderName.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add("Male");
            popup.getMenu().add("Female");
            popup.setOnMenuItemClickListener(item -> {
                binding.genderName.setText(item.getTitle());
                return true;
            });
            popup.show();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
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

    private void SignupAPI() {
        if (binding.layout2.getVisibility() == View.VISIBLE) {
            if (binding.firstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(RegisterAct.this, "please enter first name", Toast.LENGTH_SHORT).show();
            } else if (binding.lastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(RegisterAct.this, "please enter last name", Toast.LENGTH_SHORT).show();
            } else if (binding.genderName.getText().toString().trim().isEmpty()) {
                Toast.makeText(RegisterAct.this, "please enter gender", Toast.LENGTH_SHORT).show();
            }  else if (binding.zipCode.getText().toString().trim().isEmpty()) {
                Toast.makeText(RegisterAct.this, "please enter zip code", Toast.LENGTH_SHORT).show();
            }
            else if (binding.zipCode.getText().toString().trim().isEmpty()) {
                Toast.makeText(RegisterAct.this, "please enter zip code", Toast.LENGTH_SHORT).show();
            }

            else {
                SignupAPiCall();
            }
        } else {

            if (binding.edtMobile.getText().toString().trim().isEmpty()) {
                Toast.makeText(RegisterAct.this, "please enter mobile ", Toast.LENGTH_SHORT).show();
            } else if (binding.email.getText().toString().trim().isEmpty()) {
                Toast.makeText(RegisterAct.this, "please enter email  ", Toast.LENGTH_SHORT).show();
            } else if (binding.edtPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(RegisterAct.this, "please enter password ", Toast.LENGTH_SHORT).show();
            } else if (binding.dob.getText().toString().trim().isEmpty()) {
                Toast.makeText(RegisterAct.this, "please enter date of birth  ", Toast.LENGTH_SHORT).show();
            } else {
                binding.layout2.setVisibility(View.VISIBLE);
                binding.layout1.setVisibility(View.GONE);

                binding.image1.setBackgroundDrawable(getDrawable(R.drawable.ic_radio_on_3x));
                binding.image2.setBackgroundDrawable(getDrawable(R.drawable.ic_radio));

                binding.text1.setTextColor(getResources().getColor(R.color.gray));
                binding.text2.setTextColor(getResources().getColor(R.color.black));

                binding.textBut.setText("Submit");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.layout2.getVisibility() == View.VISIBLE) {
            binding.layout2.setVisibility(View.GONE);
            binding.layout1.setVisibility(View.VISIBLE);

            binding.image1.setBackgroundDrawable(getDrawable(R.drawable.ic_radio));
            binding.image2.setBackgroundDrawable(getDrawable(R.drawable.ic_circle));

            binding.text1.setTextColor(getResources().getColor(R.color.black));
            binding.text2.setTextColor(getResources().getColor(R.color.gray));

            binding.textBut.setText("Create Account");
        } else {
            super.onBackPressed();

        }
    }

    private void SignupAPiCall() {
        DataManager.getInstance().showProgressMessage(RegisterAct.this,getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("first_name", binding.firstName.getText().toString().trim());
        map.put("last_name", binding.lastName.getText().toString().trim());
        map.put("email", binding.email.getText().toString().trim());
        map.put("mobile", /*binding.ccp.getSelectedCountryCodeWithPlus() +*/ binding.edtMobile.getText().toString());
        map.put("password", binding.edtPassword.getText().toString());
        map.put("date_of_birth", binding.dob.getText().toString());
        map.put("gender", binding.genderName.getText().toString().trim());
        map.put("zipcode", binding.zipCode.getText().toString().trim());
        map.put("lat", latitude);
        map.put("lon", longitude);
        map.put("register_id","dsdsdsdsdddsdsd" );
        map.put("category_id","" );
        map.put("type", Constant.USER);
        Log.e("MapMap", "LOGIN REQUEST" + map);
        Call<ResponseBody> call = apiInterface.signup(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";

                        JSONObject object = new JSONObject(responseData);
                        if (object.optString("status").equals("1")) {
                            VerifyOtpModal userModel = new Gson().fromJson(responseData, VerifyOtpModal.class);

                           // SessionManager.writeString(RegisterAct.this,Constant.USER_ID, userModel.result.id);
                           // SessionManager.writeBoolean(RegisterAct.this,Constant.IS_LOGIN, true);
                            startActivity(new Intent(RegisterAct.this, LoginAct.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)/*.putExtra("from","Signup")*/);
                            Toast.makeText(RegisterAct.this, object.optString("message"), Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(RegisterAct.this, object.optString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    logException(e);
                    Toast.makeText(RegisterAct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

}
