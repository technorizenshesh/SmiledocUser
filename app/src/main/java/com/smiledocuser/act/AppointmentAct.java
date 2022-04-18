package com.smiledocuser.act;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.smiledocuser.R;
import com.smiledocuser.databinding.ActivityAddNewCardBinding;
import com.smiledocuser.databinding.ActivityAppointmentBinding;

public class AppointmentAct extends AppCompatActivity {
    ActivityAppointmentBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment);
        initViews();
    }

    private void initViews() {
        binding.imgback.setOnClickListener(v -> finish());
    }
}
