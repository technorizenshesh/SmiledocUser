package com.smiledocuser.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiledocuser.R;
import com.smiledocuser.databinding.FragmentPatientDetailBinding;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.NetworkAvailablity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientDetailFragment extends Fragment {
    FragmentPatientDetailBinding binding;
    APIInterface apiInterface;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient_detail, container, false);
        return  binding.getRoot();    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(NetworkAvailablity.checkNetworkStatus(getActivity())) getProfileApi();
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();




        binding.back.setOnClickListener(v -> getActivity().onBackPressed())  ;


        binding.gender.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getActivity(), v);
            popup.getMenu().add("Male");
            popup.getMenu().add("Female");
            popup.setOnMenuItemClickListener(item -> {
                binding.gender.setText(item.getTitle());
                return true;
            });
            popup.show();
        });

        binding.paynow.setOnClickListener(v -> {
            if(TextUtils.isEmpty(binding.etAge.getText().toString())){
                Toast.makeText(requireActivity(),"Please enter age",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(binding.etProblem.getText().toString())){
                Toast.makeText(requireActivity(),"Please enter problem",
                        Toast.LENGTH_SHORT).show();
            }else {
                Bundle bundle = new Bundle();
                bundle.putString("id", requireArguments().getString("iddd"));
                bundle.putString("fees", requireArguments().getString("amount"));
                bundle.putString("final_date", requireArguments().getString("final_date"));
                bundle.putString("time", requireArguments().getString("time"));

                bundle.putString("name", binding.etName.getText().toString());
                bundle.putString("age", binding.etAge.getText() .toString());
                bundle.putString("gender", binding.gender.getText().toString());
                bundle.putString("number", binding.etNumber.getText().toString());
                bundle.putString("problem", binding.etProblem.getText() .toString());

                Navigation.findNavController(v).navigate(
                        R.id.action_navigation_PatientDetailFragment_to_navigation_PaymentlFragment,
                        bundle
                );
            }
        });

    }


    private void getProfileApi() {
        Call<ResponseBody> call = apiInterface.getProfile(DataManager.getInstance().getUserData(getActivity()).result.id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                utils.dismissProgressDialog();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        JSONObject object = new JSONObject(responseData);
                        if (object.optString("status").equals("1")) {
                            JSONObject jsonObject = object.optJSONObject("result");
                            binding.etName.setText(jsonObject.optString("first_name") + " " + jsonObject.optString("last_name"));
                            binding.etNumber.setText(jsonObject.optString("mobile"));
                            binding.gender.setText(jsonObject.optString("gender"));


                        } else {
//                            showToast(object.optString("result"));
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
//                    logException(e);
//                    showToast(e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
//                dismissProgressDialog();
//                showToast(t.getMessage());
            }
        });
    }

}
