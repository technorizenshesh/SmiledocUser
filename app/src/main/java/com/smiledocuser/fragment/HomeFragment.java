package com.smiledocuser.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.smiledocuser.MainActivity;
import com.smiledocuser.R;
import com.smiledocuser.act.LoginAct;
import com.smiledocuser.adapter.AdapterTreatment;
import com.smiledocuser.databinding.FragmentHomeBinding;
import com.smiledocuser.model.DoctoreListModal;
import com.smiledocuser.model.TabModal;
import com.smiledocuser.model.VerifyOtpModal;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.retrofit.Constant;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.GPSTracker;
import com.smiledocuser.utils.SessionManager;

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

public class HomeFragment extends Fragment {
    public  String TAG = "HomeFragment";
    FragmentHomeBinding binding;


    ImageView filterr;
    String get_id = "", idd = "", category_name = "", search = "", category_id = "0";
    public static  EditText search1;
   // ViewPager view_pager1;
   // TabLayout tab_layout1;
    TabModal tabModal;
    JSONArray result_json;
   // DetailsAdapter adapter;


    AdapterTreatment adapternew;
    ArrayList<TabModal> tab_array_list;
    //private var recycler_view_dr: RecyclerView? = null
    ArrayList<DoctoreListModal> tab_array_listnew;
    DoctoreListModal doctor_modal;

    String latitude = "";
    String longitude = "";

    TextView tvType, search_docotor, tvSeeAll;
    APIInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search1 = binding.search;

        binding.tvType.setOnClickListener(v -> {} /*showPopup(it)*/);



        binding.filter.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_filterFragment);

        });


        binding.searchDocotor.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_navigation_search_doctor);
        });
        binding.tvSeeAll.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_navigation_doctor);
        });


        getCategoryAPI();


        getDataAPI();

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

    public void  getCategoryAPI(){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).result.id);
        Call<ResponseBody> loginCall = apiInterface.category_list(DataManager.getInstance().getUserData(getActivity()).result.id);
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
                            tab_array_list = new ArrayList<TabModal>();
                            result_json = object.getJSONArray("result");
                            tabModal = new TabModal();
                            tabModal.setId("0");
                            tabModal.setName("All");

                            tab_array_list.add(tabModal);
                            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("All"));
                            for (int i = 0 ; i< result_json.length();i++){
                                JSONObject jsonObject = result_json.getJSONObject(i);
                                category_name = jsonObject.optString("category_name");
                                idd = jsonObject.optString("id");

                                tabModal = new TabModal();
                                tabModal.setId(idd);
                                tabModal.setName(category_name);

                                tab_array_list.add(tabModal);

                            }


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




    public void getDataAPI(){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Call<ResponseBody> loginCall;
        Map<String, String> map = new HashMap<>();
        map.put("category_id", category_id);
        if(category_id.equals("0"))     loginCall = apiInterface.get_all_doctor(category_id);
        else   loginCall = apiInterface.get_doctor_by_category(category_id);
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
                            tab_array_listnew = new ArrayList<DoctoreListModal>();
                            result_json = object.getJSONArray("result");
                            for (int i = 0 ; i< result_json.length();i++){
                              doctor_modal = new Gson().fromJson(result_json.getJSONObject(i).toString(),DoctoreListModal.class);
                            }
                            tab_array_listnew.add(doctor_modal);
                            adapternew = new AdapterTreatment(getActivity(),tab_array_listnew);
                            binding.recyclerViewDr.setAdapter(adapternew);

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


    private void getCurrentLocation() {
        GPSTracker track = new GPSTracker(getActivity());
        if (track.canGetLocation()) {
            latitude = String.valueOf(track.getLatitude());
            longitude = String.valueOf(track.getLongitude());
            binding.tvLocation.setText(DataManager.getInstance().getAddress(getActivity(),Double.parseDouble(latitude),Double.parseDouble(longitude)));
        } else {
            track.showSettingsAlert();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCurrentLocation();
    }






    public void getFilterSearch(String query){
        try {
            query = query.toLowerCase();

            final ArrayList<DoctoreListModal> filteredList = new ArrayList<DoctoreListModal>();

            if(tab_array_listnew != null) {
                for (int i = 0; i < tab_array_listnew.size(); i++) {
                    String text = tab_array_listnew.get(i).getCategory_name().toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(tab_array_listnew.get(i));
                    }

                }
                adapternew.filterList(filteredList);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }




}
