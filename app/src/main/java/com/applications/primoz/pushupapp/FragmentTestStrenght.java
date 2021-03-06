package com.applications.primoz.pushupapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTestStrenght extends Fragment implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

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
    @Bind(R.id.rlCenter)
    RelativeLayout rlCenter;
    @Bind(R.id.btnAbort)
    Button btnAbort;
    @Bind(R.id.rlTrain)
    RelativeLayout rlTrain;
    @Bind(R.id.tvTestStrenghtSet)
    TextView tvTestStrenghtSet;
    private Context context;
    private FragmentActivity activityMain;
    private HowMany howMany;
    private PushUps pushUps;
    private HashMap<Integer, Integer> hashMapSets;
    private int record;
    private boolean prvic;
    private int number;
    private int numberinSession;
    private int orange;
    private int goal;

    boolean sklec = false;

    public FragmentTestStrenght() {
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
        View view = inflater.inflate(R.layout.fragment_test_strenght, container, false);
        ButterKnife.bind(this, view);

        context = getContext();
        activityMain = getActivity();
        mSensorManager = (SensorManager) activityMain.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        howMany = (HowMany) activityMain;
        pushUps = (PushUps) activityMain;
        MyApp.setFontCapture(context, tvTitle, tvCurrentToGo, tvToGo, tvTestStrenghtSet);
        MyApp.setFontCapture(context, btnAbort);
        orange = Color.parseColor("#e2611b");
        rlTrain.setBackgroundColor(orange);
        hashMapSets = new HashMap<>();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        record = preferences.getInt("record", 0);
        //Log.d("Record", record + "");
        goal = preferences.getInt("izbira", 25);
        if (tvTestStrenghtSet != null) {
            tvTestStrenghtSet.setText("YOUR RECORD: " + String.valueOf(record));
        }
        prvic = preferences.getBoolean("PRVIC", false);


        btnAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howMany.howManyCanYouDo(numberinSession);
                int prevscore = preferences.getInt("allpushups", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("allpushups", numberinSession + prevscore);
                editor.apply();
                if (numberinSession > goal) {
                    Toast.makeText(context, "Wow...I guess that is it ^^ " + goal, Toast.LENGTH_SHORT).show();
                }
                pushUps.SavePushups();
                pushUps.SaveRecord();
                pushUps.CheckGoal(numberinSession);
                howMany.howManyCanYouDo(numberinSession);



                closeFragment();
            }
        });

        rlCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  sklec = true;
                //  pushupDo();
                //  sklec = false;
            }
        });
        return view;
    }

    private void checkRecord() {
        Log.d("Number", numberinSession + "");
        if (record < numberinSession) {
            record = numberinSession;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("record", record);
            editor.apply();
            if (tvTestStrenghtSet != null) {
                tvTestStrenghtSet.setText("YOUR RECORD: " + String.valueOf(record));
            }
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
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("EVENT", event.values[0] + "");

        if (event.values[0] <= 1.0) {
            pushupDo();
        }
    }

    private void pushupDo() {
        numberinSession += 1;
        Log.d("Number", numberinSession + "");
        if (record <= numberinSession) {
            if (tvTestStrenghtSet != null) {
                tvTestStrenghtSet.setText("YOUR RECORD: " + record);
            }
        }
        if (tvCurrentToGo != null) {
            tvCurrentToGo.setText(String.valueOf(numberinSession));
        }
        checkRecord();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
