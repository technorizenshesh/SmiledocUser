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

import com.google.gson.Gson;
import com.smiledocuser.R;
import com.smiledocuser.adapter.AdapterDoctor;
import com.smiledocuser.adapter.AdapterFilterDoctor;
import com.smiledocuser.adapter.AdapterTreatment;
import com.smiledocuser.databinding.FragmentDoctorBinding;
import com.smiledocuser.model.DoctoreListModal;
import com.smiledocuser.model.TabModal;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.NetworkAvailablity;

import org.json.JSONArray;
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

public class DoctorFragment extends Fragment {
    public String TAG = "DoctorFragment";
    FragmentDoctorBinding binding;
    APIInterface apiInterface;
    AdapterDoctor adapter;
    ArrayList<DoctoreListModal> get_category_list ;
    DoctoreListModal get_all_category ;
    JSONArray result_json;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor, container, false);
        return  binding.getRoot();    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.imgBack.setOnClickListener(v->getActivity().onBackPressed());


        binding.location.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_navigation_dashboard_to_navigation_doctor_map);

        });


        if(NetworkAvailablity.checkNetworkStatus(getActivity()))GetDrListAPI();
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

    }

    private void GetDrListAPI() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();
        map.put("category_id", "1");
        Call<ResponseBody> loginCall  = apiInterface.get_all_doctor("1");
        Log.e(TAG, "GetDrListAPI Request " + map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject  object=null;
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        object = new JSONObject(responseData);
                        if(object.optString("status").equals("1")){
                            get_category_list = new ArrayList<DoctoreListModal>();
                            result_json = object.getJSONArray("result");
                            for (int i = 0 ; i< result_json.length();i++){
                                get_all_category= new Gson().fromJson(result_json.getJSONObject(i).toString(), DoctoreListModal.class);
                                get_category_list.add(get_all_category);

                            }
                            adapter = new AdapterDoctor(getActivity(),get_category_list);
                            binding.recyclerViewDr.setAdapter(adapter);

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
