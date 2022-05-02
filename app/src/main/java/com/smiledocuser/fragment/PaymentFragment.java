package com.smiledocuser.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.smiledocuser.MainActivity;
import com.smiledocuser.R;
import com.smiledocuser.adapter.AdapterTreatment2;
import com.smiledocuser.databinding.FragmentPaymentBinding;
import com.smiledocuser.model.TreatmentModel;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.NetworkAvailablity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends Fragment {
    public String TAG = "PaymentFragment";
    FragmentPaymentBinding binding;
    APIInterface apiInterface;
    String doctorId="",amount="",final_date="",time="",name="",age="",gender="",number="",problem="",paymentType="cash";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        doctorId =  requireArguments().getString("id");
        amount =  requireArguments().getString("fees");
        final_date =  requireArguments().getString("final_date");
        time =  requireArguments().getString("time");
        name =  requireArguments().getString("name");
        age =  requireArguments().getString("age");
        gender =  requireArguments().getString("gender");
        number =  requireArguments().getString("number");
        problem =  requireArguments().getString("problem");


        binding.btnBook.setOnClickListener(v -> {
            if(NetworkAvailablity.checkNetworkStatus(getActivity()))  placeOrder();
            else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        });



    }

    private void placeOrder() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Call<ResponseBody> loginCall;
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).result.id);
        map.put("doctor_id", doctorId);
        map.put("date", final_date);
        map.put("slot_time",time);
        map.put("fees", amount);
        map.put("user_name",name);
        map.put("user_age",age);
        map.put("gender",gender);
        map.put("mobile",number);
        map.put("problem", problem);
        map.put("payment_type",paymentType);
        loginCall = apiInterface.orderPlace(DataManager.getInstance().getUserData(getActivity()).result.id,doctorId,final_date,time,amount,name,age,gender,number,problem,paymentType);
        Log.e(TAG, "Place Order Request " + map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject object=null;
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        Log.e(TAG, "Place Order Response " + responseData);
                        object = new JSONObject(responseData);
                        if(object.optString("status").equals("1")){
                            Toast.makeText(getActivity(),"Slot book successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                             getActivity().finish();
                        }


                    } else {
                        Toast.makeText(getActivity(),object.optString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    logException(e);
                    Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
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
