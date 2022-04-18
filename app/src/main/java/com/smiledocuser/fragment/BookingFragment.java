package com.smiledocuser.fragment;

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
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.smiledocuser.R;
import com.smiledocuser.databinding.FragmentBookingBinding;
import com.smiledocuser.model.DoctoreListModal;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.NetworkAvailablity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingFragment extends Fragment {
    public String TAG = "BookingFragment";
    FragmentBookingBinding binding;
    APIInterface apiInterface;
    String iddd="",fees="",category_name="",first_namee="",last_namee="";
    JSONArray result_json;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking, container, false);
        return  binding.getRoot();    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iddd = requireArguments().getString("id").toString();


        binding.back.setOnClickListener(v->getActivity().onBackPressed());

        if(NetworkAvailablity.checkNetworkStatus(getActivity()))getDrDetails();
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.bookingAppoinment.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", iddd);
            bundle.putString("fees", fees);
            Navigation.findNavController(v).navigate(R.id.action_bookingFragment_to_bookingAppointment,bundle);
        });
    }

    private void getDrDetails() {
        Call<ResponseBody> call = apiInterface.doctor_details(iddd);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        JSONObject object = new JSONObject(responseData);
                        if (object.optString("status").equals("1")) {
                            result_json = object.getJSONArray("result");
                            for (int i = 0 ; i< result_json.length();i++){
                                JSONObject jsonObject  = result_json.getJSONObject(i);
                                first_namee = jsonObject.optString("first_name");
                                last_namee = jsonObject.optString("last_name");
                                category_name = jsonObject.optString("category_name");
                                fees = jsonObject.optString("fees");

                                binding.drName.setText(first_namee + " " + last_namee);
                                binding.nameParticipates.setText(first_namee + " " + last_namee);
                                binding.specility.setText(category_name);
                                binding.price.setText(fees);
                                binding.about.setText(jsonObject.optString("about_us"));
                                binding.time.setText(jsonObject.optString("open_time")+" - "+jsonObject.optString("close_time"));

                                binding.Pateints.setText("10+");
                                binding.Experience.setText(jsonObject.optString("experience"));
                                binding.Certificates.setText("Received");
                                binding.Education.setText(jsonObject.optString("education_name")+" "+jsonObject.optString("education_year"));
                                binding.rating.setText(jsonObject.optString("doctor_rating"));

                            }


                        } else {
//                            showToast(object.optString("result"));
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                   logException(e);
//                    showToast(e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
               DataManager.getInstance().hideProgressMessage();
//                showToast(t.getMessage());
            }
        });
    }

    public void logException(Exception e) {
        Log.e(TAG, e.getMessage());
    }
}
