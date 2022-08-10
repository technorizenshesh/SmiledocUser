package com.smiledocuser.fragment;

import android.app.Activity;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.smiledocuser.R;
import com.smiledocuser.adapter.AdapterFilterDoctor;
import com.smiledocuser.databinding.FragmentDoctorMapBinding;
import com.smiledocuser.model.DoctoreListModal;
import com.smiledocuser.model.ModelLocation;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.utils.DataManager;

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

public class DoctorMapFragment extends Fragment {
    public String TAG = "DoctorMapFragment";

    FragmentDoctorMapBinding binding;
    APIInterface apiInterface;
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    ArrayList<ModelLocation.Result>arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_map, container, false);
        return  binding.getRoot();    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiClient.getClient().create(APIInterface .class);
        arrayList = new ArrayList<>();
        getList();

        binding.ivBack.setOnClickListener(v->getActivity().onBackPressed());


    }


    private void getList () {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",DataManager.getInstance().getUserData(getActivity()).result.id);
        map.put("lat", "25.8813336");
        map.put("lon","78.3132836");
        Call<ResponseBody> loginCall  = apiInterface.get_nearest_doctor(DataManager.getInstance().getUserData(getActivity()).result.id,"25.8813336","78.3132836");
        Log.e(TAG, "get Nearest Doc Request " + map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject object=null;
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        object = new JSONObject(responseData);
                        Log.e(TAG, "get Nearest Doc Response " + responseData);
                        if(object.optString("status").equals("1")){
                            ModelLocation modelLocation = new Gson().fromJson(responseData, ModelLocation.class);
                             arrayList.clear();
                             arrayList.addAll(modelLocation.getResult());
                             mapFragment = (SupportMapFragment) getChildFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    mMap = googleMap;
                                    mMap.clear();
                                    for(int i = 0 ; i < arrayList.size() ; i++) {
                                        createMarker(Double.parseDouble(arrayList.get(i).getLat()), Double.parseDouble(arrayList.get(i).getLon()), "Location", "", R.drawable.pin_marker);
                                    }
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(arrayList.get(0).getLat()),Double.parseDouble(arrayList.get(0).getLon())), 13.0f));

                                }
                            });
                        }


                    } else {
                        Toast.makeText(getActivity(),object.optString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
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

 /*   @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        for(int i = 0 ; i < arrayList.size() ; i++) {
            createMarker(Double.parseDouble(arrayList.get(i).getLat()), Double.parseDouble(arrayList.get(i).getLon()), "Location", "", R.drawable.pin_marker);
        }


    }*/





    public Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
    }


}
