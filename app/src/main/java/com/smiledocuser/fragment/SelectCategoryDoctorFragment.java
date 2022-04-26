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

import com.google.gson.Gson;
import com.smiledocuser.R;
import com.smiledocuser.adapter.ShowDrAdapter;
import com.smiledocuser.databinding.FragmentShowDoctorsBinding;
import com.smiledocuser.model.DoctorsModel;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.NetworkAvailablity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCategoryDoctorFragment extends Fragment {
    public String TAG = "SelectCategoryDoctorFragment";
    FragmentShowDoctorsBinding binding;
    APIInterface apiInterface;
    String catId="";
    ArrayList<DoctorsModel.Result>arrayList;
    ShowDrAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_doctors, container, false);
        return  binding.getRoot();    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrayList = new ArrayList<>();


        catId =   getArguments().getString("id").toString();
        binding.tvTitle.setText(getArguments().getString("title"));

        binding.imgBack.setOnClickListener(v->getActivity().onBackPressed());


        adapter = new ShowDrAdapter(getActivity(),arrayList);
        binding.recyclerViewDr.setAdapter(adapter);


      /*  binding.location.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_navigation_dashboard_to_navigation_doctor_map);

        });*/


        if(NetworkAvailablity.checkNetworkStatus(getActivity()))GetDrListAPI(catId);
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

    }


    private void GetDrListAPI(String catId) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("category_id", catId);
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).result.id);
        Call<DoctorsModel> loginCall  = apiInterface.get_all_doctor_cate(map);
        Log.e(TAG, "GetDrListAPI Request " + map);
        loginCall.enqueue(new Callback<DoctorsModel>() {
            @Override
            public void onResponse(Call<DoctorsModel> call, Response<DoctorsModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    if (response.isSuccessful()) {
                        DoctorsModel data = response.body();
                        String responseString = new Gson().toJson(response.body());
                        Log.e(TAG, "GetDrListAPI Response :" + responseString);
                        if(data.getStatus().equals("1")){
                            arrayList.clear();
                            arrayList.addAll(data.getResult());
                            adapter.notifyDataSetChanged();
                        }


                        else {
                            arrayList.clear();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(),data.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    logException(e);
                    Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DoctorsModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });



    }

    public void logException(Exception e) {
        Log.e(TAG, e.getMessage());
    }


}
