package com.adam.camerawithsaveapi24;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

/**
 * Created by Adam on 31/01/2018.
 */

public class DataHolder {

    @NonNull private List<Concept> holderList = new ArrayList<>();

    public DataHolder(ClarifaiOutput<Concept> val){
        holderList = val.data();
    }

    public DataHolder setHolderList(@NonNull List<Concept> value){
        this.holderList = value;
//        notifyDataSetChanged();
        return this;
    }

    public List<Concept> getHolderList(){
        return holderList;
    }

}
