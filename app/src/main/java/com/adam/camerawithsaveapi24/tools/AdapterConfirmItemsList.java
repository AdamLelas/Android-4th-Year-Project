package com.adam.camerawithsaveapi24.tools;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adam.camerawithsaveapi24.R;
import com.adam.camerawithsaveapi24.classes.FoodItem;

import java.util.ArrayList;

public class AdapterConfirmItemsList extends ArrayAdapter<FoodItem> {
    private Activity activity;
    private ArrayList<FoodItem> list;
    private static LayoutInflater inflater = null;

    public AdapterConfirmItemsList(Activity activity, int textViewResourceId, ArrayList<FoodItem>arrayList){
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
        public TextView servings;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        final ViewHolder holder;


        try{
            if(convertView == null){
                vi = inflater.inflate(R.layout.listview_confirm_items, null);
                holder = new ViewHolder();
                holder.food_name = vi.findViewById(R.id.lv_conf_item_name_tv);
                holder.calories = vi.findViewById(R.id.lv_conf_item_calories_tv);
                holder.servings = vi.findViewById(R.id.lv_conf_item_servingsize_tv);

                vi.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) vi.getTag();
            }

            holder.food_name.setText(list.get(position).getFood_name());
            holder.calories.setText(String.valueOf(list.get(position).getCalories()));
            holder.servings.setText(String.valueOf(list.get(position).getServings()));

        }catch (Exception e){
            e.printStackTrace();
        }
        return vi;
    }



}
