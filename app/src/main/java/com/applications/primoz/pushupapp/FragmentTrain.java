package com.applications.primoz.pushupapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

//TODO NAJ TA REZULTAT NE VPLIVA NA SETE

public class FragmentTrain extends Fragment {


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
    int currentSet = 0;

    Activity activityMain;
    HowMany howMany;
    CountDownTimer timer;

    public FragmentTrain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_train, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        activityMain = getActivity();
        howMany = (HowMany) activityMain;
        MyApp.setFontCapture(context, tvTitle, tvCurrentToGo, tvToGo);
        MyApp.setFontCapture(context, btnAbort);
        black = Color.parseColor("#1e1e1e");
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int color = getArguments().getInt("barva");
        rlTrain.setBackgroundColor(color);


        hashMapSets = new HashMap<>();

        if (getArguments().getIntArray("sets") == null) {
            /*
            *

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="2 - "
                android:textColor="@color/colorSets"
                android:textSize="24sp" />

            *
            * */
            tvSet = new TextView(context);
            tvSet.setText("0");
            tvSet.setTextSize(24);
            tvSet.setTextColor(black);
            tvSet.setLayoutParams(layoutParams);
            MyApp.setFontCapture(context, tvSet);
            llSets.addView(tvSet);
            tvCurrentToGo.setText("0");
            tvToGo.setText("Give everything you got");
            btnAbort.setText("THAT IS IT FOR ME");

        } else {
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
        }
        btnAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howMany.howManyCanYouDo(number);
                if (timer != null) {
                    timer.cancel();
                }
                closeFragment();
            }
        });

        rlCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberinSession += 1;
                //TODO SHRANI PODATKE O SKLECIH ZA REKORD... VSE

                if (timer != null) {
                    timer.cancel();
                    timer.onFinish();
                    timer = null;
                } else if (sets != null && sets.length > 1) {
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
                        timeout();
                    }

                    tvCurrentToGo.setText(String.valueOf(number));
                    if (lastset) {
                        tvCurrentToGo.setText(number + "");
                        number += 2;
                    }
                } else {
                    number += 1;
                    tvSet.setText(String.valueOf(number));
                    tvCurrentToGo.setText(String.valueOf(number));
                }
            }
        });

        return view;
    }

    private void timeout() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (tvCurrentToGo != null) {
                    tvCurrentToGo.setText(millisUntilFinished / 1000 + "");
                    tvToGo.setText("REST NOW, press again to skip");
                }
            }

            @Override
            public void onFinish() {
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


}
