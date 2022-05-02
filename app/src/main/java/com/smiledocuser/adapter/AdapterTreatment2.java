package com.smiledocuser.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiledocuser.R;
import com.smiledocuser.databinding.AdapterTreatmentBinding;
import com.smiledocuser.model.TreatmentModel;

import java.util.ArrayList;

public class AdapterTreatment2 extends RecyclerView.Adapter<AdapterTreatment2.ViewHolder> {
    Context context;
    ArrayList<TreatmentModel> arrayList;

    public AdapterTreatment2(Context context, ArrayList<TreatmentModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterTreatmentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_treatment, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.nameParticipates.setText(arrayList.get(position).getName());
        //   holder.binding.nameParticipates.setText(arrayList.get(position).getFirst_name()+" "+arrayList.get(position).getLast_name());
        holder.binding.specility.setText(arrayList.get(position).getName());
        holder.binding.price.setText(" $ " + arrayList.get(position).getPrice());
        if (arrayList.get(position).getImage() != null) {
            Glide.with(context)
                    .load(arrayList.get(position).getImage())
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_user_circle_24)
                    .error(R.drawable.ic_user_circle_24)
                    .into(holder.binding.img1);
        } else {
            holder.binding.img1.setImageResource(R.drawable.ic_user_circle_24);
        }


        holder.binding.layoutMain.setOnClickListener (v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", arrayList.get(position).getCategory_id());
            bundle.putString("title", arrayList.get(position).getName());
            Log.e("Cate===id",arrayList.get(position).getCategory_id());
            Navigation.findNavController(v).navigate(R.id.navigation_select_by_category_doctor, bundle);
        }  );

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterTreatmentBinding binding;
        public ViewHolder(@NonNull AdapterTreatmentBinding itemView) {
            super(itemView.getRoot());
            binding =itemView;



        }
    }

    public void filterList(ArrayList<TreatmentModel> filterlist) {
        arrayList = filterlist;
        notifyDataSetChanged();
    }

}

