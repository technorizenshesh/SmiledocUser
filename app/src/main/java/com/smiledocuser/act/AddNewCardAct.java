package com.smiledocuser.act;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.smiledocuser.R;
import com.smiledocuser.databinding.ActivityAddNewCardBinding;

public class AddNewCardAct extends AppCompatActivity {
    ActivityAddNewCardBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_new_card);
        initViews();
    }

    private void initViews() {
        binding.imgBack.setOnClickListener(v -> finish());
    }
}
