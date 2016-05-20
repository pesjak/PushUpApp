package com.applications.primoz.pushupapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GoalClicked {
    SharedPreferences preferences;
    @Bind(R.id.iv_background)
    ImageView ivBackground;
    @Bind(R.id.tvAllPushUps)
    TextView tvAllPushUps;
    @Bind(R.id.tvAllPushUpsNumber)
    TextView tvAllPushUpsNumber;
    @Bind(R.id.tvPush)
    TextView tvPush;
    @Bind(R.id.tvRecordName)
    TextView tvRecordName;
    @Bind(R.id.tvRecord)
    TextView tvRecord;
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
    TextView tvStart;
    @Bind(R.id.tvChangeGoal)
    TextView tvChangeGoal;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;


    private int color20;
    private int color100;
    private int color50;

    //TODO Preveri, če je prvič prižgal App
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MyApp.setFontCapture(this, tvTitle,tvRecord, tvRecordName, tvStart, tvUpcoming, tvUpcomingDay, tvSets, tvSetsNumbers, tvTotal, tvTotalNumber, tvAllPushUps, tvAllPushUpsNumber, tvPush, tvChangeGoal);
        showSelectGoal();
        color100 = Color.parseColor("#F44336");
        color50 = Color.parseColor("#1976D2");
        color20 = Color.parseColor("#e6388e3c");
    }

    private void showSelectGoal() {
        FragmentPrvic fragmentPrvic = new FragmentPrvic();
        fragmentPrvic.setArguments(getIntent().getExtras());
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragmentPrvic)
                .commit();

    }

    @Override
    public void onItemClick() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tezavnost = preferences.getString("tezavnost", "Ni tezavnosti");
        String izbira = preferences.getString("izbira", "Ni izbire");
        changeBackground(izbira);

    }


    private void changeBackground(String izbira) {
        Log.d("Selected: ", izbira);
        switch (izbira) {
            case "25":
                tvStart.setBackgroundColor(color20);
                break;
            case "50":
                tvStart.setBackgroundColor(color50);
                break;
            case "100":
                tvStart.setBackgroundColor(color100);
                break;
            default:
                break;
        }
    }
}
