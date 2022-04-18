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
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;
import com.smiledocuser.R;
import com.smiledocuser.adapter.AdapterBooking;
import com.smiledocuser.adapter.AdapterFilterDoctor;
import com.smiledocuser.databinding.FragmentBookingAppointmentBinding;
import com.smiledocuser.listener.TimeSlotClickListener;
import com.smiledocuser.model.DoctoreListModal;
import com.smiledocuser.model.TimeSlote;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.NetworkAvailablity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingAppointmentFragment extends Fragment implements TimeSlotClickListener {
    public String TAG = "BookingAppointmentFragment";
    FragmentBookingAppointmentBinding binding;
    String iddd = "", amount = "",final_date="",start="",end="",status="",time="";
    APIInterface apiInterface;
    AdapterBooking adapterBooking;
    ArrayList<TimeSlote> tab_array_list;
    TimeSlote time_modal;
    JSONObject result_json;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_appointment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        iddd = requireArguments().getString("id").toString();
        amount = requireArguments().getString("fees").toString();

        binding.back.setOnClickListener(v -> getActivity().onBackPressed());


        String currentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String currentMonth =new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String currentDay = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());





        binding.dayDatePicker.setStartDate(
                Integer.parseInt(currentDay),
                Integer.parseInt(currentMonth),
                Integer.parseInt(currentYear)
        );


        binding.dayDatePicker.setEndDate(11, 3, 2027);



        binding.dayDatePicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {

                if (date != null) {
                    Log.e("date_formate>>>>>>", date.toString());

                    String strs[] = date.toString().split(" ");
                    Log.e("string1>>>", strs[1]);
                    Log.e("string12>>>", strs[2]);
                    Log.e("string13>>>", strs[5]);

                    final_date = strs[1] + "-" + strs[2] + "-" + strs[5];

                    Log.e("final_date>>>", final_date);

                   if (NetworkAvailablity.checkNetworkStatus(getActivity()))getDataAPI(final_date);
                    else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();


                    //val stringArray: Array<String> = string.split(",")

                    //Toast.makeText(context, "" + date, Toast.LENGTH_SHORT).show()
                }
                
                
            }
        });
        
        
        


        binding.callLayout.setOnClickListener(v -> {
            binding.allLayout.setBackground(null);
            binding.callLayout.setBackground(getActivity().getDrawable(R.drawable.btn_baground));
            binding.messageLayout.setBackground(null);
            binding.videolayout.setBackground(null);

        });


        binding.messageLayout.setOnClickListener(v -> {
            binding.allLayout.setBackground(null);
            binding.callLayout.setBackground(null);
            binding.messageLayout.setBackground(getActivity().getDrawable(R.drawable.btn_baground));
            binding.videolayout.setBackground(null);

        });

        binding.videolayout.setOnClickListener(v -> {
            binding.allLayout.setBackground(null);
            binding.callLayout.setBackground(null);
            binding.messageLayout.setBackground(null);
            binding.videolayout.setBackground(getActivity().getDrawable(R.drawable.btn_baground));

        });


        binding.allLayout.setOnClickListener(v -> {
            binding.allLayout.setBackground(getActivity().getDrawable(R.drawable.btn_baground));
            binding.callLayout.setBackground(null);
            binding.messageLayout.setBackground(null);
            binding.videolayout.setBackground(null);

        });


        binding.bookappointment.setOnClickListener(v -> {
            if (final_date.equals("")) {
                Toast.makeText(requireActivity(), "Please select date", Toast.LENGTH_SHORT).show();
            } else if (time.equals("")) {
                Toast.makeText(requireActivity(), "Please select time", Toast.LENGTH_SHORT).show();
            } else {

                Bundle bundle = new Bundle();
                bundle.putString("id", iddd);
                bundle.putString("fees", amount);
                bundle.putString("final_date", final_date);
                bundle.putString("time", time);


                Navigation.findNavController(v).navigate(R.id.action_bookingAppointment_to_navigation_PatientDetailFragment, bundle);
            }


        });


    }

    private void getDataAPI(String final_date) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("date", final_date);
        map.put("doctor_id", iddd);
        Call<ResponseBody> loginCall  = apiInterface.doctor_time_slote(final_date,iddd);
        Log.e(TAG, "getSlotsss Request " + map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject object=null;
                     JSONArray time;
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        object = new JSONObject(responseData);
                        if(object.optString("status").equals("1")){
                            tab_array_list = new ArrayList<TimeSlote>();
                            result_json = object.getJSONObject("result");
                            time = result_json.getJSONArray("time");
                            for (int i = 0 ; i< time.length();i++){
                                JSONObject jsonObject  = time.getJSONObject(i);
                                start = jsonObject.optString("start");
                                end = jsonObject.optString("end");
                                status = jsonObject.optString("status");

                                time_modal = new TimeSlote();
                                time_modal.setStart(start);
                                time_modal.setEnd_name(end);
                                time_modal.setStatus(status);


                                tab_array_list.add(time_modal);


                            }
                            adapterBooking = new AdapterBooking(getActivity(),tab_array_list,BookingAppointmentFragment.this);
                            binding.seatAvailable.setAdapter(adapterBooking);

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

    @Override
    public void onClick(TimeSlote timeSlote) {
        time= timeSlote.getStart();
    }
}
