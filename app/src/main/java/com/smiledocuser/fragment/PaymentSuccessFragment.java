package com.smiledocuser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.smiledocuser.R;
import com.smiledocuser.databinding.FragmentPaymentSuccessBinding;

public class PaymentSuccessFragment extends Fragment {
    FragmentPaymentSuccessBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_success, container, false);
        return  binding.getRoot();    }
}
