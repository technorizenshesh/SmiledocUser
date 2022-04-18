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
import com.smiledocuser.adapter.AdapterTreatment;
import com.smiledocuser.databinding.FragmentTreatmentBinding;
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

public class TreatmentFragment extends Fragment {
    public String TAG = "TreatmentFragment";
    FragmentTreatmentBinding binding;
    APIInterface apiInterface;
    AdapterTreatment adapternew;
    ArrayList<DoctoreListModal> tab_array_listnew;
    DoctoreListModal doctor_modal;
    JSONArray result_json;
    String paramd = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_treatment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        paramd = getArguments().getString("param");

        if (NetworkAvailablity.checkNetworkStatus(getActivity())) getDataAPI();
        else
            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        HomeFragment.search1.addTextChangedListener(new TextWatcher() {
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

    public void getDataAPI() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Call<ResponseBody> loginCall;
        Map<String, String> map = new HashMap<>();
        map.put("category_id", paramd);
        if (paramd.equals("0")) loginCall = apiInterface.get_all_doctor(paramd);
        else loginCall = apiInterface.get_doctor_by_category(paramd);
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
                            tab_array_listnew = new ArrayList<DoctoreListModal>();
                            result_json = object.getJSONArray("result");
                            for (int i = 0; i < result_json.length(); i++) {
                                doctor_modal = new Gson().fromJson(result_json.getJSONObject(i).toString(), DoctoreListModal.class);
                            }
                            tab_array_listnew.add(doctor_modal);
                            adapternew = new AdapterTreatment(getActivity(), tab_array_listnew);
                            binding.recyclerViewDr.setAdapter(adapternew);

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

            if (tab_array_listnew != null) {
                for (int i = 0; i < tab_array_listnew.size(); i++) {
                    String text = tab_array_listnew.get(i).getCategory_name().toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(tab_array_listnew.get(i));
                    }

                }
                adapternew.filterList(filteredList);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
