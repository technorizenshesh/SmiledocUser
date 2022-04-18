package com.smiledocuser.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiledocuser.R;
import com.smiledocuser.act.AppointmentAct;
import com.smiledocuser.act.EditProfile;
import com.smiledocuser.act.MyCardAct;
import com.smiledocuser.act.ProsmileBidsAct;
import com.smiledocuser.databinding.FragmentUserProfileBinding;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.NetworkAvailablity;
import com.smiledocuser.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {
    FragmentUserProfileBinding binding;
    APIInterface apiInterface;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(NetworkAvailablity.checkNetworkStatus(getActivity())) getProfileApi();
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

       binding.textViewEditProfile.setOnClickListener ( v ->{ startActivity(new Intent(getActivity(), EditProfile.class)); });


        binding.textViewMyAppointments.setOnClickListener ( v ->{ startActivity(new Intent(getActivity(), AppointmentAct.class)); });

        binding.textViewProsmileBids.setOnClickListener ( v ->{ startActivity(new Intent(getActivity(), ProsmileBidsAct.class)); });

        binding.textViewMyCards.setOnClickListener ( v ->{ startActivity(new Intent(getActivity(), MyCardAct.class)); });

        binding.lagout.setOnClickListener( v->{ LogOutAlert(); });


    }


    public void LogOutAlert(){
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_logout_this_app));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        SessionManager.clear(getActivity(), "","");
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
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
                            binding.tvName.setText(jsonObject.optString("first_name") + " " + jsonObject.optString("last_name"));
                            binding.tvEmail.setText(jsonObject.optString("email"));
                            Glide.with(getActivity()).load(jsonObject.optString("image"))
                                    .apply(RequestOptions.circleCropTransform())
                                    .placeholder(R.drawable.ic_user_circle_24)
                                    .error(R.drawable.ic_user_circle_24)
                                    .into(binding.image);

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
