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
import com.smiledocuser.FilterClickListener;
import com.smiledocuser.R;
import com.smiledocuser.adapter.AdapterFilterTreatment;
import com.smiledocuser.adapter.AdapterTreatment;
import com.smiledocuser.databinding.FragmentFilterBinding;
import com.smiledocuser.model.DoctoreListModal;
import com.smiledocuser.model.GetFilterCategoryModal;
import com.smiledocuser.model.GetFilterConsaltantModal;
import com.smiledocuser.model.TabModal;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.utils.DataManager;

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

public class FilterFragment extends Fragment implements FilterClickListener {
    public String TAG = "FilterFragment";
    FragmentFilterBinding binding;
    String value="0",value2="0",category_name="",iddd="",name="";
    AdapterFilterTreatment adapter;
    GetFilterCategoryModal get_all_category;
    JSONArray result_json;
    ArrayList<GetFilterCategoryModal> get_category_list;
    APIInterface apiInterface;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false);
        return  binding.getRoot();    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imgBack.setOnClickListener(v -> getActivity().onBackPressed());
        binding.loginBtn.setOnClickListener(v-> {
                Bundle bundle =new  Bundle();
            bundle.putString("filter_value",value);
            bundle.putString("consultantType",value2);

            Navigation.findNavController(v).navigate(R.id.action_filterFragment_to_getDoctorFilterList,bundle);
            //  action_filterFragment_to_getDoctorFilterList
            //Log.e("FilterType>>>>", "" + value + "ConsultantType>>>>" + value2)

        });

        GetFilterAPI();
        
    }

    private void GetFilterAPI() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("category_id", "1");
        Call<ResponseBody> loginCall  = apiInterface.category_list(DataManager.getInstance().getUserData(getActivity()).result.id);
        Log.e(TAG, "Login Request " + map);
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
                            get_category_list = new ArrayList<GetFilterCategoryModal>();
                            result_json = object.getJSONArray("result");
                            for (int i = 0 ; i< result_json.length();i++){
                                JSONObject jsonObject  = result_json.getJSONObject(i);
                                category_name = jsonObject.optString("category_name");
                                iddd = jsonObject.optString("id");

                                get_all_category =new  GetFilterCategoryModal();
                                get_all_category.setDrname1(category_name);
                                get_all_category.setIdd(iddd);
                                get_category_list.add(get_all_category);

                            }
                            adapter = new AdapterFilterTreatment(getActivity(),get_category_list,FilterFragment.this);
                            binding.recyclerViewDrStatus.setAdapter(adapter);

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


    @Override
    public void treatmentClick(String s) {
        value = s;
    }

    @Override
    public void consultTypeClick(String s) {
        value2 = s;
    }


    public void logException(Exception e) {
        Log.e(TAG, e.getMessage());
    }
}
