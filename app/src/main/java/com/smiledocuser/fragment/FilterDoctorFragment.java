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
import com.smiledocuser.adapter.AdapterFilterDoctor;
import com.smiledocuser.adapter.AdapterFilterTreatment;
import com.smiledocuser.databinding.FragmentFilterDoctorBinding;
import com.smiledocuser.model.DoctoreListModal;
import com.smiledocuser.model.GetFilterCategoryModal;
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

public class FilterDoctorFragment extends Fragment {
    public String TAG = "FilterDoctorFragment";
    FragmentFilterDoctorBinding binding;
    String filter_value="",consultantType="";
    APIInterface apiInterface;
    AdapterFilterDoctor adapterDoctor;
    DoctoreListModal get_all_category;
    ArrayList<DoctoreListModal> get_dr_list;
    JSONArray result_json;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_doctor, container, false);
        return  binding.getRoot();    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filter_value = requireArguments().getString("filter_value").toString();
        consultantType = requireArguments().getString("consultantType").toString();


        binding.imgBack.setOnClickListener(v->getActivity().onBackPressed());

        if(NetworkAvailablity.checkNetworkStatus(getActivity()))getDrFliterList(filter_value, consultantType);
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

          binding.location.setOnClickListener(v -> {
              Navigation.findNavController(v).navigate(R.id.action_getDoctorFilterList_to_navigation_doctor_map);

          });

    }

    private void getDrFliterList(String filter_value, String consultantType) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("category_id", filter_value);
        map.put("consult_type_id", consultantType);
        Call<ResponseBody> loginCall  = apiInterface.search_doctor_by_category(filter_value,consultantType);
        Log.e(TAG, "getDrFliterList Request " + map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject object=null;
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        object = new JSONObject(responseData);
                        if(object.optString("status").equals("1")){
                            get_dr_list = new ArrayList<DoctoreListModal>();
                            result_json = object.getJSONArray("result");
                            for (int i = 0 ; i< result_json.length();i++){
                                get_all_category= new Gson().fromJson(result_json.getJSONObject(i).toString(), DoctoreListModal.class);
                                get_dr_list.add(get_all_category);

                            }
                            adapterDoctor = new AdapterFilterDoctor(getActivity(),get_dr_list);
                            binding.recyclerViewDr.setAdapter(adapterDoctor);

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
