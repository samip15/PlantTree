package com.example.planttree.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planttree.R;
import com.example.planttree.provider.PlantContract;
import com.example.planttree.utils.PlantUtils;

public class PlantListAdapter extends RecyclerView.Adapter<PlantListAdapter.PlantViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    PlantListAdapter(Context context,Cursor cursor)
    {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.plant_list_item,parent,false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int idIndex = mCursor.getColumnIndex(PlantContract.PlantEntry._ID);
        int createTimeIndex = mCursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_CREATION_TIME);
        int waterTimeIndex = mCursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME);
        int plantTypeIndex = mCursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_PLANT_TYPE);

        long plantId = mCursor.getLong(idIndex);
        int plantType = mCursor.getInt(plantTypeIndex);
        long createdAt = mCursor.getLong(createTimeIndex);
        long wateredAt = mCursor.getLong(waterTimeIndex);
        long timeNow = System.currentTimeMillis();

        // image resource that shows the plant size
        int imgRes = PlantUtils.getPlantImgRes(mContext,timeNow - createdAt,
                timeNow - wateredAt, plantType);

        holder.plantImageView.setImageResource(imgRes);
        holder.plantNameView.setText(String.valueOf(plantId));
        holder.plantImageView.setTag(plantId);
    }

    public void swapCursor(Cursor newCursor)
    {
        if (mCursor != null && mCursor !=newCursor) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (mCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {

        ImageView plantImageView;
        TextView plantNameView;

        public PlantViewHolder(View itemView) {
            super(itemView);
            plantImageView = itemView.findViewById(R.id.plant_list_item_iv);
            plantNameView = itemView.findViewById(R.id.plant_list_item_name);
        }
    }
}



