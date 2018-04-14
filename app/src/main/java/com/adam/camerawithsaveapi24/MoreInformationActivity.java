package com.adam.camerawithsaveapi24;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adam.camerawithsaveapi24.classes.FoodItem;
import com.adam.camerawithsaveapi24.classes.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.adam.camerawithsaveapi24.tools.NutritionCalculator.*;
import static com.adam.camerawithsaveapi24.tools.Utility.changeDate;
import static com.adam.camerawithsaveapi24.tools.Utility.formatDouble;

public class MoreInformationActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private ArrayList<FoodItem> foodItemsList;

    private UserDetails userDetails;


    private int age;
    private double cals;
    private String gender;

    private TextView calCon;
    private TextView tFatCon;
    private TextView sFatCon;
    private TextView cholCon;
    private TextView carbCon;
    private TextView sugarCon;
    private TextView fiberCon;
    private TextView sodiumCon;
    private TextView proteinCon;
    private TextView potasCon;
    private TextView calciumCon;
    private TextView ironCon;
    private TextView aCon;
    private TextView b1Con;
    private TextView b2Con;
    private TextView b3Con;
    private TextView b5Con;
    private TextView b6Con;
    private TextView b9Con;
    private TextView b12Con;
    private TextView cCon;
    private TextView dCon;
    private TextView eCon;
    private TextView kCon;

    private TextView calGoal;
    private TextView tFatGoal;
    private TextView sFatGoal;
    private TextView cholGoal;
    private TextView carbGoal;
    private TextView sugarGoal;
    private TextView fiberGoal;
    private TextView sodiumGoal;
    private TextView proteinGoal;
    private TextView potasGoal;
    private TextView calciumGoal;
    private TextView ironGoal;
    private TextView aGoal;
    private TextView b1Goal;
    private TextView b2Goal;
    private TextView b3Goal;
    private TextView b5Goal;
    private TextView b6Goal;
    private TextView b9Goal;
    private TextView b12Goal;
    private TextView cGoal;
    private TextView dGoal;
    private TextView eGoal;
    private TextView kGoal;

    private TextView calRem;
    private TextView tFatRem;
    private TextView sFatRem;
    private TextView cholRem;
    private TextView carbRem;
    private TextView sugarRem;
    private TextView fiberRem;
    private TextView sodiumRem;
    private TextView proteinRem;
    private TextView potasRem;
    private TextView calciumRem;
    private TextView ironRem;
    private TextView aRem;
    private TextView b1Rem;
    private TextView b2Rem;
    private TextView b3Rem;
    private TextView b5Rem;
    private TextView b6Rem;
    private TextView b9Rem;
    private TextView b12Rem;
    private TextView cRem;
    private TextView dRem;
    private TextView eRem;
    private TextView kRem;
    private TextView tvDate;


    private ImageView dateback, dateforward, goback;

    private double dailyCarb, dailyCal, dailyChol, dailyFib, dailySugar, dailyPro, dailySatFat, dailyTotFat, dailyVA,
            dailyVB1, dailyVB2, dailyVB3, dailyVB5, dailyVB6, dailyVB9, dailyVB12, dailyVC,
            dailyVD, dailyVE, dailyVK, dailyIron, dailyCalcium, dailySodium, dailyPotas;

    private String timevalue;
    private Date datetime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_information);


        goback = findViewById(R.id.more_info_goback);
        goback.setOnClickListener(this);
        dateback = findViewById(R.id.more_info_date_minus);
        dateback.setOnClickListener(this);
        dateforward = findViewById(R.id.more_info_date_plus);
        dateforward.setOnClickListener(this);

        calCon = findViewById(R.id.info_cons_calories);
        tFatCon = findViewById(R.id.info_cons_totfat);
        sFatCon = findViewById(R.id.info_cons_satfat);
        cholCon = findViewById(R.id.info_cons_chol);
        carbCon = findViewById(R.id.info_cons_carbs);
        sugarCon = findViewById(R.id.info_cons_sugar);
        fiberCon = findViewById(R.id.info_cons_fiber);
        sodiumCon = findViewById(R.id.info_cons_sodium);
        proteinCon = findViewById(R.id.info_cons_protein);
        potasCon = findViewById(R.id.info_cons_potas);
        calciumCon = findViewById(R.id.info_cons_calcium);
        ironCon = findViewById(R.id.info_cons_iron);
        aCon = findViewById(R.id.info_cons_va);
        b1Con = findViewById(R.id.info_cons_vb1);
        b2Con = findViewById(R.id.info_cons_vb2);
        b3Con = findViewById(R.id.info_cons_vb3);
        b5Con = findViewById(R.id.info_cons_vb5);
        b6Con = findViewById(R.id.info_cons_vb6);
        b9Con = findViewById(R.id.info_cons_vb9);
        b12Con = findViewById(R.id.info_cons_vb12);
        cCon = findViewById(R.id.info_cons_vc);
        dCon = findViewById(R.id.info_cons_vd);
        eCon = findViewById(R.id.info_cons_ve);
        kCon = findViewById(R.id.info_cons_vk);
        calGoal = findViewById(R.id.info_goal_calories);
        tFatGoal = findViewById(R.id.info_goal_totfat);
        sFatGoal = findViewById(R.id.info_goal_satfat);
        cholGoal = findViewById(R.id.info_goal_chol);
        carbGoal = findViewById(R.id.info_goal_carbs);
        sugarGoal = findViewById(R.id.info_goal_sugar);
        fiberGoal = findViewById(R.id.info_goal_fiber);
        sodiumGoal = findViewById(R.id.info_goal_sodium);
        proteinGoal = findViewById(R.id.info_goal_prottein);
        potasGoal = findViewById(R.id.info_goal_potas);
        calciumGoal = findViewById(R.id.info_goal_calcium);
        ironGoal = findViewById(R.id.info_goal_iron);
        aGoal = findViewById(R.id.info_goal_va);
        b1Goal = findViewById(R.id.info_goal_vb1);
        b2Goal = findViewById(R.id.info_goal_vb2);
        b3Goal = findViewById(R.id.info_goal_vb3);
        b5Goal = findViewById(R.id.info_goal_vb5);
        b6Goal = findViewById(R.id.info_goal_vb6);
        b9Goal = findViewById(R.id.info_goal_vb9);
        b12Goal = findViewById(R.id.info_goal_vb12);
        cGoal = findViewById(R.id.info_goal_vc);
        dGoal = findViewById(R.id.info_goal_vd);
        eGoal = findViewById(R.id.info_goal_ve);
        kGoal = findViewById(R.id.info_goal_vk);
        calRem = findViewById(R.id.info_rem_calories);
        tFatRem = findViewById(R.id.info_rem_totfat);
        sFatRem = findViewById(R.id.info_rem_satfat);
        cholRem = findViewById(R.id.info_rem_chol);
        carbRem = findViewById(R.id.info_rem_carbs);
        sugarRem = findViewById(R.id.info_rem_sugar);
        fiberRem = findViewById(R.id.info_rem_fiber);
        sodiumRem = findViewById(R.id.info_rem_sodium);
        proteinRem = findViewById(R.id.info_rem_protein);
        potasRem = findViewById(R.id.info_rem_potas);
        calciumRem = findViewById(R.id.info_rem_calcium);
        ironRem = findViewById(R.id.info_rem_iron);
        aRem = findViewById(R.id.info_rem_va);
        b1Rem = findViewById(R.id.info_rem_vb1);
        b2Rem = findViewById(R.id.info_rem_vb2);
        b3Rem = findViewById(R.id.info_rem_vb3);
        b5Rem = findViewById(R.id.info_rem_vb5);
        b6Rem = findViewById(R.id.info_rem_vb6);
        b9Rem = findViewById(R.id.info_rem_vb9);
        b12Rem = findViewById(R.id.info_rem_vb12);
        cRem = findViewById(R.id.info_rem_vc);
        dRem = findViewById(R.id.info_rem_vd);
        eRem = findViewById(R.id.info_rem_ve);
        kRem = findViewById(R.id.info_rem_vk);
        tvDate = findViewById(R.id.info_date);


        //      Firebase
        if(isNetworkAvailable()) {
            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            dbRef = database.getReference();
            user = mAuth.getCurrentUser();
        }

        foodItemsList = new ArrayList<>();

        //        Misc
        datetime = new Date();
        timevalue = new SimpleDateFormat("yyyy-MM-dd").format(datetime);

        gatherUserDetails();
        gatherTodaysFood();
    }

    /**
     * Detects if network is available, returns null if no internet access
     * duplication of method required as it will not work from a static context
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    private void gatherUserDetails() {
        DatabaseReference userDetailsRef = dbRef.child("users").child(user.getUid()).child("user-details");
        userDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                extractUserDetails(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void extractUserDetails(DataSnapshot ds) {
        userDetails = ds.getValue(UserDetails.class);
        if (userDetails != null) {
            gender = userDetails.getGender();
            age = userDetails.getAge();
            cals = CalcHarrisBenedictBMRMetric(userDetails.getGender(), userDetails.getHeight(),
                    userDetails.getWeight(), userDetails.getAge(), userDetails.getActivity_level(), userDetails.getGoal_weight());
        }
    }

    private void extractFoodLog(DataSnapshot dataSnapshot) {
        foodItemsList.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            FoodItem tFood = (ds.getValue(FoodItem.class));
            if (tFood != null) {
                tFood.setFood_name(ds.getKey());
            }
            foodItemsList.add(tFood);
        }
        getAllValues();
    }


    private void gatherTodaysFood() {
        DatabaseReference logRef = dbRef.child("users").child(user.getUid()).child("log").child(timevalue);
        logRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                extractFoodLog(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void getAllValues() {
//        Reset Daily totals
        dailyCarb = 0;
        dailyCal = 0;
        dailyChol = 0;
        dailyFib = 0;
        dailyPro = 0;
        dailySatFat = 0;
        dailyTotFat = 0;
        dailyVA = 0;
        dailyVB1 = 0;
        dailyVB2 = 0;
        dailyVB3 = 0;
        dailyVB5 = 0;
        dailyVB6 = 0;
        dailyVB9 = 0;
        dailyVB12 = 0;
        dailyVC = 0;
        dailyVD = 0;
        dailyVE = 0;
        dailyVK = 0;
        dailyIron = 0;
        dailyCalcium = 0;
        dailySugar = 0;
        dailySodium = 0;
        dailyPotas = 0;

//        Populate daily totals
        for (FoodItem i : foodItemsList) {
            double servings = i.getServings();
            dailyCarb = dailyCarb + (i.getCarbs() * servings);
            dailyCal = dailyCal + (i.getCalories() * servings);
            dailyChol = dailyChol + (i.getCholesterol() * servings);
            dailyFib = dailyFib + (i.getFiber() * servings);
            dailyPro = dailyPro + (i.getProtein() * servings);
            dailySatFat = dailySatFat + (i.getSaturated_fat() * servings);
            dailyTotFat = dailyTotFat + (i.getTotal_fat() * servings);
            dailyVA = dailyVA + (i.getVitamin_a() * servings);
            dailyVB1 = dailyVB1 + (i.getVitamin_b1() * servings);
            dailyVB2 = dailyVB2 + (i.getVitamin_b2() * servings);
            dailyVB3 = dailyVB3 + (i.getVitamin_b3() * servings);
            dailyVB5 = dailyVB5 + (i.getVitamin_b5() * servings);
            dailyVB6 = dailyVB6 + (i.getVitamin_b6() * servings);
            dailyVB9 = dailyVB9 + (i.getVitamin_b9() * servings);
            dailyVB12 = dailyVB12 + (i.getVitamin_b12() * servings);
            dailyVC = dailyVC + (i.getVitamin_c() * servings);
            dailyVD = dailyVD + (i.getVitamin_d() * servings);
            dailyVE = dailyVE + (i.getVitamin_e() * servings);
            dailyVK = dailyVK + (i.getVitamin_k() * servings);
            dailyIron = dailyIron + (i.getIron() * servings);
            dailyCalcium = dailyCalcium + (i.getCalcium() * servings);
            dailySugar = dailySugar + (i.getSugar() * servings);
            dailySodium = dailySodium + (i.getSodium() * servings);
            dailyPotas = dailyPotas + (i.getPotassium() * servings);
        }
        setValues();
    }

    private void setValues() {
        tvDate.setText(timevalue);

        calCon.setText(formatDouble((dailyCal)));
        tFatCon.setText(formatDouble(dailyTotFat));
        sFatCon.setText(formatDouble(dailySatFat));
        cholCon.setText(formatDouble(dailyChol));
        carbCon.setText(formatDouble(dailyCarb));
        sugarCon.setText(formatDouble(dailySugar));
        fiberCon.setText(formatDouble(dailyFib));
        sodiumCon.setText(formatDouble(dailySodium));
        proteinCon.setText(formatDouble(dailyPro));
        potasCon.setText(formatDouble(dailyPotas));
        calciumCon.setText(formatDouble(dailyCalcium));
        ironCon.setText(formatDouble(dailyIron));
        aCon.setText(formatDouble(dailyVA));
        b1Con.setText(formatDouble(dailyVB1));
        b2Con.setText(formatDouble(dailyVB2));
        b3Con.setText(formatDouble(dailyVB3));
        b5Con.setText(formatDouble(dailyVB5));
        b6Con.setText(formatDouble(dailyVB6));
        b9Con.setText(formatDouble(dailyVB9));
        b12Con.setText(formatDouble(dailyVB12));
        cCon.setText(formatDouble(dailyVC));
        dCon.setText(formatDouble(dailyVD));
        eCon.setText(formatDouble(dailyVE));
        kCon.setText(formatDouble(dailyVK));

        calGoal.setText(formatDouble(cals));
        tFatGoal.setText(formatDouble(CalcRecommendedPercentFat(cals)));
        sFatGoal.setText(formatDouble(GetRecommendedPercentSaturatedFat(age)));
        cholGoal.setText(formatDouble(GetRecommendedPercentCholesterol()));
        carbGoal.setText(formatDouble(CalcRecommendedPercentCarbohydrates(cals)));
        sugarGoal.setText(formatDouble(GetRecommendedSugarLimit(age)));
        fiberGoal.setText(formatDouble(GetRecommendedPercentFiber(age)));
        sodiumGoal.setText(formatDouble(GetRecommendedPercentSodium(age)));
        proteinGoal.setText(formatDouble(CalcRecommendedPercentProtein(cals)));
        potasGoal.setText(formatDouble(GetRecommendedPercentPotassium(age)));
        calciumGoal.setText(formatDouble(CalcRecommendedPercentCalcium(age)));
        ironGoal.setText(formatDouble(GetRecommendedIron(age)));
        aGoal.setText(formatDouble(CalcRecommendedPercentVitaminA(age, gender)));
        b1Goal.setText(formatDouble(CalcRecommendedPercentVitaminB1(gender)));
        b2Goal.setText(formatDouble(CalcRecommendedPercentVitaminB2(gender)));
        b3Goal.setText(formatDouble(CalcRecommendedPercentVitaminB3(gender)));
        b5Goal.setText(formatDouble(CalcRecommendedPercentVitaminB5()));
        b6Goal.setText(formatDouble(CalcRecommendedPercentVitaminB6(age, gender)));
        b9Goal.setText(formatDouble(CalcRecommendedPercentVitaminB9()));
        b12Goal.setText(formatDouble(CalcRecommendedPercentVitaminB12()));
        cGoal.setText(formatDouble(CalcRecommendedPercentVitaminC(gender)));
        dGoal.setText(formatDouble(CalcRecommendedPercentVitaminD(age)));
        eGoal.setText(formatDouble(CalcRecommendedPercentVitaminE()));
        kGoal.setText(formatDouble(CalcRecommendedPercentVitaminK(gender)));

        calRem.setText(formatDouble(cals - dailyCal));
        tFatRem.setText(formatDouble(CalcRecommendedPercentFat(cals) - dailyTotFat));
        sFatRem.setText(formatDouble(GetRecommendedPercentSaturatedFat(age) - dailySatFat));
        cholRem.setText(formatDouble(GetRecommendedPercentCholesterol() - dailyChol));
        carbRem.setText(formatDouble(CalcRecommendedPercentCarbohydrates(cals) - dailyCarb));
        sugarRem.setText(formatDouble(GetRecommendedSugarLimit(age) - dailySugar));
        fiberRem.setText(formatDouble(GetRecommendedPercentFiber(age) - dailyFib));
        sodiumRem.setText(formatDouble(GetRecommendedPercentSodium(age) - dailySodium));
        proteinRem.setText(formatDouble(CalcRecommendedPercentProtein(cals) - dailyPro));
        potasRem.setText(formatDouble(GetRecommendedPercentPotassium(age) - dailyPotas));
        calciumRem.setText(formatDouble(CalcRecommendedPercentCalcium(age) - dailyCalcium));
        ironRem.setText(formatDouble(GetRecommendedIron(age) - dailyIron));
        aRem.setText(formatDouble(CalcRecommendedPercentVitaminA(age, gender) - dailyVA));
        b1Rem.setText(formatDouble(CalcRecommendedPercentVitaminB1(gender) - dailyVB1));
        b2Rem.setText(formatDouble(CalcRecommendedPercentVitaminB2(gender) - dailyVB2));
        b3Rem.setText(formatDouble(CalcRecommendedPercentVitaminB3(gender) - dailyVB3));
        b5Rem.setText(formatDouble(CalcRecommendedPercentVitaminB5() - dailyVB5));
        b6Rem.setText(formatDouble(CalcRecommendedPercentVitaminB6(age, gender) - dailyVB6));
        b9Rem.setText(formatDouble(CalcRecommendedPercentVitaminB9() - dailyVB9));
        b12Rem.setText(formatDouble(CalcRecommendedPercentVitaminB12() - dailyVB12));
        cRem.setText(formatDouble(CalcRecommendedPercentVitaminC(gender) - dailyVC));
        dRem.setText(formatDouble(CalcRecommendedPercentVitaminD(age) - dailyVD));
        eRem.setText(formatDouble(CalcRecommendedPercentVitaminE() - dailyVE));
        kRem.setText(formatDouble(CalcRecommendedPercentVitaminK(gender) - dailyVK));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == goback.getId()) {
            Intent backupIntent = new Intent(this, MainActivity.class);
            startActivity(backupIntent);
        } else if (v.getId() == dateback.getId()) {
            datetime = changeDate(datetime, -1);
            timevalue = new SimpleDateFormat("yyyy-MM-dd").format(datetime);
            gatherTodaysFood();
        } else if (v.getId() == dateforward.getId()) {
            if (!timevalue.equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy").format(new Date()))) {
                datetime = changeDate(datetime, 1);
                timevalue = new SimpleDateFormat("yyyy-MM-dd").format(datetime);

                gatherTodaysFood();
            }
        }
    }
}

