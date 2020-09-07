package com.example.planttree.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planttree.R;
import com.example.planttree.utils.PlantUtils;

public class PlantTypeAdapter extends RecyclerView.Adapter<PlantTypeAdapter.PlantTypeViewHolder>  {

    Context mContext;
    TypedArray mPlantType;

    public PlantTypeAdapter(Context context)
    {
        mContext = context;
        mPlantType = context.getResources().obtainTypedArray(R.array.plant_types);
    }

    @NonNull
    @Override
    public PlantTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.plant_type_list_item,parent,false);
        return new PlantTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantTypeViewHolder holder, int position) {
        int imgRes = PlantUtils.getPlantImg(
                mContext,
                position,
                PlantUtils.PlantStatus.ALIVE,
                PlantUtils.PlantSize.FULLY_GROWN);
        holder.plantImageView.setImageResource(imgRes);
        holder.plantTypeText.setText(PlantUtils.getPlantTypeName(mContext,position));
        holder.plantImageView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if(mPlantType == null) return  0;
        return mPlantType.length();
    }

    public class PlantTypeViewHolder extends RecyclerView.ViewHolder {
        ImageView plantImageView;
        TextView plantTypeText;

        public PlantTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImageView =  itemView.findViewById(R.id.plant_type_iv);
            plantTypeText =  itemView.findViewById(R.id.plant_type_text);
        }
    }
}
