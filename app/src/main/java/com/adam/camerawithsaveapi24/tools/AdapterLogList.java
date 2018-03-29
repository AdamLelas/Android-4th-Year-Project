package com.adam.camerawithsaveapi24.tools;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adam.camerawithsaveapi24.R;
import com.adam.camerawithsaveapi24.classes.FoodItem;

import java.util.ArrayList;

public class AdapterLogList  extends ArrayAdapter<FoodItem>{

    private Activity activity;
    private ArrayList<FoodItem> list;
    private static LayoutInflater inflater = null;

    public AdapterLogList(Activity activity, int textViewResourceId, ArrayList<FoodItem> arrayList){
        super(activity, textViewResourceId, arrayList);
        try{
            this.activity = activity;
            this.list = arrayList;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getCount(){
        return list.size();
    }


    public static class ViewHolder {
        public TextView f_name;
        public TextView serving_size;
        public TextView servings;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        final ViewHolder holder;

        try{
            if (convertView == null) {
                vi = inflater.inflate(R.layout.listview_display_fooditems, null);
                holder = new ViewHolder();

                holder.f_name = vi.findViewById(R.id.display_fi_name);
                holder.serving_size = vi.findViewById(R.id.display_fi_weight);
                holder.servings = vi.findViewById(R.id.display_fi_servings);

                vi.setTag(holder);
            }
            else{
                holder = (ViewHolder) vi.getTag();
            }
            String ss = String.valueOf(list.get(position).getServing_weight_grams()) + "g";
            holder.f_name.setText(list.get(position).getFood_name());
            holder.serving_size.setText(ss);
            holder.servings.setText(String.valueOf(list.get(position).getServings()));


        } catch (Exception e){

        }
        return vi;
    }





}
