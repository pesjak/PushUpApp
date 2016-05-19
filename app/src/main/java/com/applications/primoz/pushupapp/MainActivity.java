package com.applications.primoz.pushupapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //TODO Preveri, če je prvič prižgal App
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showSelectGoal();
    }

    private void showSelectGoal() {
        FragmentPrvic fragmentPrvic = new FragmentPrvic();
        fragmentPrvic.setArguments(getIntent().getExtras());
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragmentPrvic)
                .commit();
    }
}
