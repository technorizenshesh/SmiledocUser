package com.smiledocuser.act;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.smiledocuser.R;
import com.smiledocuser.databinding.ActivityMyCardBinding;

public class MyCardAct extends AppointmentAct {
    ActivityMyCardBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_card);
        initViews();
    }

    private void initViews() {
        binding.imgBck.setOnClickListener(v -> finish());

        binding.AddNewCard.setOnClickListener(v -> {
            startActivity(new Intent(MyCardAct.this, AddNewCardAct.class));

        });

    }

}
