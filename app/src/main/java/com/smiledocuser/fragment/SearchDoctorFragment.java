package com.smiledocuser.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.smiledocuser.adapter.AdapterSearchDoctor;
import com.smiledocuser.adapter.AdapterTreatment;
import com.smiledocuser.databinding.FragmentSearchDoctorBinding;
import com.smiledocuser.model.DoctoreListModal;
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

public class SearchDoctorFragment extends Fragment {
    public String TAG = "SearchDoctorFragment";
    FragmentSearchDoctorBinding binding;
    APIInterface apiInterface;
    ArrayList<DoctoreListModal> get_category_list;
    DoctoreListModal get_all_category;
    AdapterSearchDoctor adapter;
    JSONArray result_json;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_doctor, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        if (NetworkAvailablity.checkNetworkStatus(getActivity())) getSearchByDrAPI();
        else
            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getFilterSearch(s.toString());
            }
        });

    }

    private void getSearchByDrAPI() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("category_id", "1");
        Call<ResponseBody> loginCall = apiInterface.get_all_doctor("1");
        Log.e(TAG, "Login Request " + map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject object = null;
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        object = new JSONObject(responseData);
                        if (object.optString("status").equals("1")) {
                            get_category_list = new ArrayList<DoctoreListModal>();
                            result_json = object.getJSONArray("result");
                            for (int i = 0; i < result_json.length(); i++) {
                                get_all_category = new Gson().fromJson(result_json.getJSONObject(i).toString(), DoctoreListModal.class);
                            }
                            get_category_list.add(get_all_category);
                            adapter = new AdapterSearchDoctor(getActivity(), get_category_list);
                            binding.recyclerViewDr.setAdapter(adapter);

                        }


                    } else {
                        Toast.makeText(getActivity(), object.optString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    logException(e);
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void getFilterSearch(String query) {
        try {
            query = query.toLowerCase();

            final ArrayList<DoctoreListModal> filteredList = new ArrayList<DoctoreListModal>();

            if (get_category_list != null) {
                for (int i = 0; i < get_category_list.size(); i++) {
                    String text = get_category_list.get(i).getCategory_name().toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(get_category_list.get(i));
                    }

                }
                adapter.filterList(filteredList);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
