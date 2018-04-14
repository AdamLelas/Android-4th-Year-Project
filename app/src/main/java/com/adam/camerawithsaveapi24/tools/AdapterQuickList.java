package com.adam.camerawithsaveapi24.tools;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adam.camerawithsaveapi24.R;
import com.adam.camerawithsaveapi24.classes.FoodItem;
import static com.adam.camerawithsaveapi24.tools.Utility.*;

import java.util.ArrayList;

public class AdapterQuickList extends ArrayAdapter<FoodItem> {
    private Activity activity;
    private ArrayList<FoodItem> list;
    private static LayoutInflater inflater = null;

    public AdapterQuickList(Activity activity, int textViewResourceId, ArrayList<FoodItem>arrayList){
        super(activity, textViewResourceId, arrayList);
        try{
            this.activity = activity;
            this.list = arrayList;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }catch (Exception e){

        }
    }

    public int getCount() {return list.size();}

    public static class ViewHolder {
        public TextView food_name;
        public TextView calories;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        final ViewHolder holder;

        try{
            if(convertView == null){
                vi = inflater.inflate(R.layout.listview_quick_add, null);
                holder = new ViewHolder();
                holder.food_name = vi.findViewById(R.id.lv_conf_item_name_tv);
                holder.calories = vi.findViewById(R.id.lv_conf_item_calories_tv);
                vi.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) vi.getTag();
            }

            holder.food_name.setText(list.get(position).getFood_name());
            holder.calories.setText(formatDouble(list.get(position).getCalories()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return vi;
    }



}
