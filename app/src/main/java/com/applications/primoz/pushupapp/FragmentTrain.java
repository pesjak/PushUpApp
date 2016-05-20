package com.applications.primoz.pushupapp;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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

import butterknife.Bind;
import butterknife.ButterKnife;


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
    private String[] sets;

    int black;


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
        MyApp.setFontCapture(context, tvTitle, tvCurrentToGo, tvToGo);
        MyApp.setFontCapture(context, btnAbort);
        black = Color.parseColor("#1e1e1e");

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        int color = getArguments().getInt("barva");
        if (getArguments().getStringArray("sets") == null) {
            rlTrain.setBackgroundColor(color);
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
            llSets.addView(tvSet);
            tvCurrentToGo.setText("0");
            tvToGo.setText("Give everything you got");
        } else {
            sets = getArguments().getStringArray("sets");
            for (String set : sets) {
                Log.d("Set: ", set);
            }

        }
        btnAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });
        return view;
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
