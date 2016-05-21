package com.applications.primoz.pushupapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

//TODO KOLEDAR, KI MU BO POVEDAL DA MORA TRENIRATI VSAK DRUG

public class MainActivity extends AppCompatActivity implements GoalClicked, HowMany, PushUps {
    SharedPreferences preferences;
    String record;
    @Bind(R.id.iv_background)
    ImageView ivBackground;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvRecordName)
    TextView tvRecordName;
    @Bind(R.id.tvRecord)
    TextView tvRecord;
    @Bind(R.id.tvAllPushUps)
    TextView tvAllPushUps;
    @Bind(R.id.tvAllPushUpsNumber)
    TextView tvAllPushUpsNumber;
    @Bind(R.id.tvPush)
    TextView tvPush;
    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @Bind(R.id.tvUpcoming)
    TextView tvUpcoming;
    @Bind(R.id.tvUpcomingDay)
    TextView tvUpcomingDay;
    @Bind(R.id.tvSets)
    TextView tvSets;
    @Bind(R.id.tvSetsNumbers)
    TextView tvSetsNumbers;
    @Bind(R.id.tvTotal)
    TextView tvTotal;
    @Bind(R.id.tvTotalNumber)
    TextView tvTotalNumber;
    @Bind(R.id.tvStart)
    Button btnStart;
    @Bind(R.id.tvChangeGoal)
    Button btnChangeGoal;
    @Bind(R.id.viewBackground)
    View viewBackground;

    int[] arraySets;


    private int color20;
    private int color100;
    private int color50;

    private int colorB20;
    private int colorB100;
    private int colorB50;
    private int izbranabarva;
    String tezavnost;
    String izbira;
    private int allpushups;

    //TODO Preveri, če je prvič prižgal App
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MyApp.setFontCapture(this, tvTitle, tvRecord, tvRecordName, tvUpcoming, tvUpcomingDay, tvSets, tvSetsNumbers, tvTotal, tvTotalNumber, tvAllPushUps, tvAllPushUpsNumber, tvPush);
        MyApp.setFontCapture(this, btnChangeGoal, btnStart);
        showSelectGoal();
        color100 = Color.parseColor("#F44336");
        color50 = Color.parseColor("#1976D2");
        color20 = Color.parseColor("#388e3c");
        colorB100 = Color.parseColor("#3cf44336");
        colorB50 = Color.parseColor("#3c1976D2");
        colorB20 = Color.parseColor("#3c388e3c");

        btnChangeGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo OPEN  FRAGMENT, change sets, total
                showSelectGoal();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo Open fragment with training data
                startTraining();

            }
        });

    }

    private void setTotalPushUps(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        allpushups = preferences.getInt("allpushups",0);
        Log.d("ALL PUSHUPS",allpushups+"");
        if(tvAllPushUpsNumber != null){
            tvAllPushUpsNumber.setText(allpushups+"");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setTotalPushUps();
    }

    private void startTraining() {
        Bundle bundle = new Bundle();
        bundle.putInt("barva", izbranabarva);
        bundle.putIntArray("sets", arraySets);

        FragmentTrain fragment = new FragmentTrain();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .commit();

    }

    private void showSelectGoal() {
        Bundle bundle = new Bundle();
        FragmentPrvic fragmentPrvic = new FragmentPrvic();
        fragmentPrvic.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragmentPrvic)
                .commit();
    }

    @Override
    public void onItemClick() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tezavnost = preferences.getString("tezavnost", "Ni tezavnosti");
        izbira = preferences.getString("izbira", "Ni izbire");
        changeBackground(izbira);
        startTraining();

    }

    private void changeBackground(String izbira) {
        Log.d("Selected: ", izbira);
        switch (izbira) {
            case "25":
                btnStart.setBackgroundColor(color20);
                viewBackground.setBackgroundColor(colorB20);
                izbranabarva = color20;
                break;
            case "50":
                btnStart.setBackgroundColor(color50);
                viewBackground.setBackgroundColor(colorB50);
                izbranabarva = color50;
                break;
            case "100":
                btnStart.setBackgroundColor(color100);
                viewBackground.setBackgroundColor(colorB100);
                izbranabarva = color100;
                break;
            default:
                break;
        }
    }


    @Override
    public void howManyCanYouDo(int pushups) {
        //TODO Zapomni si te sklece in jih dodaj v skupno število sklec...
        //TODO Glede na ta rezultat, mu daj kateri set bo moral opravit
        Log.d("PUSHUPS", pushups + "");

        if (pushups <= 5) {
            arraySets = getResources().getIntArray(R.array.Easy_Week_1_Day_1);
        } else if (pushups > 5 && pushups < 10) {
            arraySets = getResources().getIntArray(R.array.Easy_Week_2_Day_1);
        } else if (pushups > 11 && pushups < 20) {
            arraySets = getResources().getIntArray(R.array.Easy_Week_3_Day_1);
        } else {
            arraySets = getResources().getIntArray(R.array.Easy_Week_4_Day_1);
        }
        String set = "";
        int sum = 0;
        for (int i = 0; i < arraySets.length; i++) {
            sum += arraySets[i];
            String element = String.valueOf(arraySets[i]);
            if (i != arraySets.length - 1) {
                set += element + " - ";
            } else {
                set += element + "+";
            }

        }
        Log.d("STRING", set);
        tvSetsNumbers.setText(set);
        tvTotalNumber.setText(sum + " PUSH UPS");
    }

    @Override
    public void SavePushups() {
        setTotalPushUps();
    }
}
