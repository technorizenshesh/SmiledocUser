package com.smiledocuser.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.smiledocuser.R;
import com.smiledocuser.adapter.AdapterTreatment2;
import com.smiledocuser.adapter.BidsAdapter;
import com.smiledocuser.databinding.ActivityAddNewCardBinding;
import com.smiledocuser.databinding.ActivityProsmileBinding;
import com.smiledocuser.databinding.ActivityProsmileBindingImpl;
import com.smiledocuser.model.BidsModel;
import com.smiledocuser.model.TreatmentModel;
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

public class ProsmileBidsAct extends AppCompatActivity {
    public String TAG = "ProsmileBidsAct";
    ActivityProsmileBinding binding;
    APIInterface apiInterface;
    ArrayList<BidsModel.Result>arrayList;
    BidsAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prosmile);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();
         binding.imgBack.setOnClickListener(v -> finish());

         adapter = new BidsAdapter(ProsmileBidsAct.this,arrayList);
         binding.rvBids.setAdapter(adapter);

         getDataAPI();
    }



    public void getDataAPI(){
        DataManager.getInstance().showProgressMessage(ProsmileBidsAct.this, getString(R.string.please_wait));
        Call<ResponseBody> loginCall;
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(ProsmileBidsAct.this).result.id);
        loginCall = apiInterface.get_all_bids(DataManager.getInstance().getUserData(ProsmileBidsAct.this).result.id);
        Log.e(TAG, "Login Request " + map);
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
                            BidsModel  bidsModel = new Gson().fromJson(responseData, BidsModel.class);
                            binding.tvNotFound.setVisibility(View.GONE);
                            arrayList.clear();
                            arrayList.addAll(bidsModel.getResult());
                            adapter.notifyDataSetChanged();
                        }


                    } else {
                        binding.tvNotFound.setVisibility(View.VISIBLE);
                        arrayList.clear();

                        adapter.notifyDataSetChanged();

                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                   // logException(e);
                    Toast.makeText(ProsmileBidsAct.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



}
