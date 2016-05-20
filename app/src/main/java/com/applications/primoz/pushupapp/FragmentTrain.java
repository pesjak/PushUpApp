package com.applications.primoz.pushupapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    Context context;
    @Bind(R.id.rlTrain)
    RelativeLayout rlTrain;

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

        int color = getArguments().getInt("barva");
        rlTrain.setBackgroundColor(color);

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
