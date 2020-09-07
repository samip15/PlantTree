package com.example.planttree.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.example.planttree.R;

public class PlantUtils {

    private static final long MINUTE_MILLISECONDS = 1000 * 60;
    private static final long HOUR_MILLISECONDS = MINUTE_MILLISECONDS * 60;
    private static final long DAY_MILLISECONDS = HOUR_MILLISECONDS * 24;

    // every 2 hours user should water
    public static final long MIN_AGE_BETWEEN_WATER = HOUR_MILLISECONDS * 2;
    // 6 hours danger to the plant
    static final long DANGER_AGE_WITHOUT_WATER = HOUR_MILLISECONDS * 6;
    // 12 hours after plant die
    public static final long MAX_AGE_WITHOUT_WATER = HOUR_MILLISECONDS * 12;
    static final long TINY_AGE = DAY_MILLISECONDS * 0; // plants start tiny
    static final long JUVENILE_AGE = DAY_MILLISECONDS * 1; // 1 day old
    static final long FULLY_GROWN_AGE = DAY_MILLISECONDS * 2; // 2 days old

    public enum PlantStatus{ ALIVE, DYING, DEAD};

    public enum PlantSize{TINY,JUVENILE,FULLY_GROWN};

    /**
     * Returns the right image resource to display the plant
     */
    public static int getPlantImgRes(Context context, long plantAge, long waterAge, int type)
    {
        //check if plant status is dead
        PlantStatus plantStatus = PlantStatus.ALIVE;
        if(waterAge> MAX_AGE_WITHOUT_WATER)
        {
            plantStatus = PlantStatus.DEAD;
        }
        else if(waterAge > DANGER_AGE_WITHOUT_WATER)
        {
            plantStatus = PlantStatus.DYING;
        }

        //update the plant age
        if(plantAge > FULLY_GROWN_AGE)
        {
            return getPlantImg(context,type,plantStatus,PlantSize.FULLY_GROWN);
        }
        else if(plantAge>JUVENILE_AGE)
        {
            return getPlantImg(context,type,plantStatus,PlantSize.JUVENILE);
        }
        else if(plantAge>TINY_AGE)
        {
            return getPlantImg(context,type,plantStatus,PlantSize.TINY);
        }
        else{
            return R.drawable.empty_pot;
        }
    }

    /**
     * Returns us the image according the size type and status
     */
    public static  int getPlantImg(Context context,int type, PlantStatus status, PlantSize plantSize)
    {
        Resources res = context.getResources();
        TypedArray plantTypes = res.obtainTypedArray(R.array.plant_types);
        String resName = plantTypes.getString(type);
        //plant status
        if(status == PlantStatus.DYING)
        {
            resName += "_danger";
        }
        else if(status == PlantStatus.DEAD)
        {
            resName += "_dead";
        }
        //plant size
        if(plantSize == PlantSize.TINY)
        {
            resName += "_1";
        }
        else  if(plantSize == PlantSize.JUVENILE)
        {
            resName += "_2";
        }
        else if(plantSize == PlantSize.FULLY_GROWN)
        {
            resName += "_3";
        }
        return res.getIdentifier(resName,"drawable",context.getPackageName());
    }


    public static String getPlantTypeName(Context context,int type)
    {
        Resources res = context.getResources();
        TypedArray plantTypes = res.obtainTypedArray(R.array.plant_types);
        String resName = plantTypes.getString(type);
        int resId = res.getIdentifier(resName,"string",context.getPackageName());
        try{
            return context.getResources().getString(resId);
        }
        catch (Resources.NotFoundException ex)
        {
            return context.getResources().getString(R.string.unknown_type);
        }
    }

    public static int getDisplayAgeInt(long milliSeconds) {
        int days = (int) (milliSeconds / DAY_MILLISECONDS);
        if (days >= 1) return days;
        int hours = (int) (milliSeconds / HOUR_MILLISECONDS);
        if (hours >= 1) return hours;
        return (int) (milliSeconds / MINUTE_MILLISECONDS);
    }

    public static String getDisplayAgeUnit(Context context, long milliSeconds) {
        int days = (int) (milliSeconds / DAY_MILLISECONDS);
        if (days >= 1) return context.getString(R.string.days);
        int hours = (int) (milliSeconds / HOUR_MILLISECONDS);
        if (hours >= 1) return context.getString(R.string.hours);
        return context.getString(R.string.minutes);
    }
}
