package com.applications.primoz.pushupapp;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

//TODO NAJ TA REZULTAT NE VPLIVA NA SETE

public class FragmentTrain extends Fragment implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;


    Context context;
    TextView tvSet;
    LinearLayout.LayoutParams layoutParams;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.llSets)
    LinearLayout llSets;
    @Bind(R.id.rlTop)
    RelativeLayout rlTop;
    @Bind(R.id.tvCurrentToGo)
    TextView tvCurrentToGo;
    @Bind(R.id.tvToGo)
    TextView tvToGo;
    @Bind(R.id.btnAbort)
    Button btnAbort;
    @Bind(R.id.rlCenter)
    RelativeLayout rlCenter;
    @Bind(R.id.rlTrain)
    RelativeLayout rlTrain;
    private int[] sets;

    private HashMap<Integer, Integer> hashMapSets;

    public static final int DELAY = 250;
    int black;
    int number = 0;
    int numberinSession = 0;
    boolean lastset = false;
    boolean timerRunning = false;
    int currentSet = 0;

    boolean prvic = false;
    boolean sklec = false;
    Activity activityMain;
    HowMany howMany;
    PushUps pushUps;
    CountDownTimer timer;
    int record = 0;

    public FragmentTrain() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_train, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        activityMain = getActivity();
        mSensorManager = (SensorManager) activityMain.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        howMany = (HowMany) activityMain;


        pushUps = (PushUps) activityMain;
        MyApp.setFontCapture(context, tvTitle, tvCurrentToGo, tvToGo);
        MyApp.setFontCapture(context, btnAbort);
        black = Color.parseColor("#1e1e1e");
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int color = getArguments().getInt("barva");
        rlTrain.setBackgroundColor(color);
        hashMapSets = new HashMap<>();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        record = preferences.getInt("record", 0);
        prvic = preferences.getBoolean("PRVIC", false);


        sets = getArguments().getIntArray("sets");
        String all = "";
        tvSet = new TextView(context);
        for (int i = 0; i < sets.length; i++) {
            String element = String.valueOf(sets[i]);
            hashMapSets.put(i, sets[i]);
            if (i == 0) {
                tvSet.setText(sets[i] + "");
            }
            if (i != sets.length - 1) {
                all += element + " - ";
            } else {
                all += element + "+";
            }
        }
        Log.d("FRAGMENT", all);
        tvSet.setText(all);
        tvSet.setTextSize(24);
        tvSet.setTextColor(black);
        tvSet.setLayoutParams(layoutParams);
        MyApp.setFontCapture(context, tvSet);
        llSets.addView(tvSet);
        putRepinSet();

        number = hashMapSets.get(0);


        btnAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lastset) {
                    int prevscore = preferences.getInt("allpushups", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    Log.d("ALL", numberinSession + "");
                    editor.putInt("allpushups", numberinSession + prevscore);
                    editor.apply();
                    if (timerRunning) {
                        timerRunning = false;
                        timer.cancel();
                    }
                    int goal = preferences.getInt("izbira", 25);
                    if (number > goal) {
                        Toast.makeText(context, "You achived your: " + goal + " pushups... ^^ ", Toast.LENGTH_SHORT).show();
                    }
                    //  howMany.howManyCanYouDo(number);
                    pushUps.SavePushups();
                    pushUps.SaveRecord();
                    pushUps.ChangeSet();
                    pushUps.CheckGoal(number);
                }
                pushUps.SavePushups();
                pushUps.SaveRecord();
                closeFragment();
            }
        });

        rlCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  sklec = true;
                if (timerRunning) {
                    timer.cancel();
                    timer.onFinish();
                }
                //else {
               //     pushupDo();
               // }
               // sklec = false;
            }
        });

        return view;
    }

    private void checkRecord() {
        if (record < number) {
            record = number;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("record", record);
            editor.apply();
        }
    }

    private void timeout(int s) {
        timer = new CountDownTimer(s * 1005, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerRunning = true;

                if (millisUntilFinished <= 6000) {
                    if (tvToGo != null && rlCenter != null) {
                        rlCenter.setClickable(false);
                        tvToGo.setText("Get Ready");
                    }
                } else {
                    if (tvToGo != null) {
                        tvToGo.setText("REST NOW, press again to skip");
                    }
                }

                if (tvCurrentToGo != null) {
                    tvCurrentToGo.setText(millisUntilFinished / 1000 + "");
                }
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                tvCurrentToGo.setText("" + 0);
                rlCenter.setClickable(true);
                currentSet += 1;
                putRepinSet();
                if (tvToGo != null) {
                    tvToGo.setText("GIVE IT YOUR ALL");
                }
            }
        };

        timer.start();
    }

    private void putRepinSet() {
        int numberofReps = hashMapSets.get(currentSet);
        if (tvCurrentToGo != null) {
            tvCurrentToGo.setText("" + numberofReps);
            number = numberofReps;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void closeFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(this);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("EVENT", event.values[0] + "");

        if (event.values[0] < 1.0 && !sklec && !timerRunning) {
            pushupDo();
        }
    }

    private void pushupDo() {
        if (sets != null && sets.length > 1) {
            numberinSession += 1;
            number -= 1;
            if (currentSet >= hashMapSets.size() - 1) {
                if (number <= 0) {
                    putRepinSet();
                    tvToGo.setText("Give everything you got");
                    btnAbort.setText("COMPLETED");
                    lastset = true;
                    number = hashMapSets.get(hashMapSets.size() - 1);
                    tvCurrentToGo.setText(number + "");
                }

            } else if (number <= 0) {
                timeout(60);
            }
            if (tvCurrentToGo != null) {
                tvCurrentToGo.setText(String.valueOf(number));
            }
            if (lastset) {
                tvCurrentToGo.setText(number + "");
                number += 2;
            }

            checkRecord();

        } else {
            numberinSession += 1;
            number += 1;
            checkRecord();
            tvSet.setText(String.valueOf(number));
            tvCurrentToGo.setText(String.valueOf(number));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
