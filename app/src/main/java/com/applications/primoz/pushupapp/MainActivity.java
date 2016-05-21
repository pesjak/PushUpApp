package com.applications.primoz.pushupapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

//TODO KOLEDAR, KI MU BO POVEDAL DA MORA TRENIRATI VSAK DRUG

public class MainActivity extends AppCompatActivity implements GoalClicked, HowMany, PushUps {

    private static final int GOAL25 = 25;
    private static final int GOAL50 = 50;
    private static final int GOAL100 = 100;

    private static final int EASY = 0;
    private static final int MEDIUM = 1;
    private static final int HARD = 2;

    private static final int WEEK1 = 0;
    private static final int WEEK2 = 1;
    private static final int WEEK3 = 2;
    private static final int WEEK4 = 3;
    private static final int WEEK5 = 4;
    private static final int WEEK6 = 5;
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

    int tezavnost;
    int izbira;

    private int allpushups;
    private int record;
    private boolean firstTimer = true;

    private HashMap<Integer, HashMap<Integer, HashMap<Integer, int[]>>> hashDif;   //Hashmap<ZAHTEVNOST,Hashmap<TEDEN, HASHMAP<DAN, ARRAYSETA > > >
    HashMap<Integer, HashMap<Integer, int[]>> hashTeden;
    HashMap<Integer, int[]> hashDan;


    int[] array0;
    int[] array1;
    int[] array2;
    int[] array3;
    int[] array4;
    int[] array5;
    int[] array6;
    int[] array7;
    int[] array8;
    int[] array9;
    int[] array10;
    int[] array11;
    int[] array12;
    int[] array13;
    int[] array14;
    int[] array15;
    int[] array16;
    int[] array17;

    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MyApp.setFontCapture(this, tvTitle, tvRecord, tvRecordName, tvUpcoming, tvSets, tvSetsNumbers, tvTotal, tvTotalNumber, tvAllPushUps, tvAllPushUpsNumber, tvPush);
        MyApp.setFontCapture(this, btnChangeGoal, btnStart);
        color100 = Color.parseColor("#F44336");
        color50 = Color.parseColor("#1976D2");
        color20 = Color.parseColor("#388e3c");
        colorB100 = Color.parseColor("#3cf44336");
        colorB50 = Color.parseColor("#3c1976D2");
        colorB20 = Color.parseColor("#3c388e3c");

        getVseSete();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        firstTimer = preferences.getBoolean("firstTimer", true);

        if (firstTimer) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTimer", false);
            editor.apply();
            firstTimer = false;
            showSelectGoal();
        } else {
            izbira = preferences.getInt("izbira", -1);
            tezavnost = preferences.getInt("tezavnost", -1);
            Log.d("Izbira", izbira + "");
            Log.d("Tezavnost", tezavnost + "");
            changeBackground(izbira);
        }

        setTotalPushUps();
        setRecord();
        setSetsPushups();

        materialDialog = new MaterialDialog.Builder(this)
                .title("Changing your goal?")
                .content("Your can change your goal, but your sets will depend of your test that you will have to do again. Choose wisely.")
                .positiveText("Yes I AM AWARE")
                .negativeText("Cancel")
                .onAny(new MaterialDialog.SingleButtonCallback(){
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        switch (which) {
                            default:
                                Toast.makeText(getApplicationContext(), "Lucky I warned you XD", Toast.LENGTH_SHORT).show();
                                break;
                            case POSITIVE:
                                Toast.makeText(getApplicationContext(), "GL HF", Toast.LENGTH_SHORT).show();
                                showSelectGoal();
                                break;
                            case NEGATIVE:
                                Toast.makeText(getApplicationContext(), "Lucky I warned you XD", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).build();
        btnChangeGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.show();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTraining();

            }
        });

    }

    private void getVseSete() {
        //2131427328 PRVI ID od tega arraya

        //TODO Prosim te,..... nared bol pametno...
        hashDif = new HashMap<>();


        /*
        int st = 0;
        int idOfArray = R.array.Easy_Week_1_Day_1;
        Log.d("ID",""+getResources().getIntArray(idOfArray));
      //  int idOfZadnjiArray = R.array.Hard_Week_6_Day_3 - idOfArray;
        for (int tezavnost = 0; tezavnost < 3; tezavnost++) { //Easy,Medium,Hard
            for (int teden = 0; teden < 6; teden++) {
                for (int dan = 0; dan < 3; dan++) {  //Ker so 3je
                    int[] array = getResources().getIntArray(idOfArray + st);
                    hashDan.put(dan,array);
                    hashTeden.put(teden,hashDan);
                    hashDif.put(tezavnost,hashTeden);
                    st++;
                }
            }
        }*/

        array0 = getResources().getIntArray(R.array.Easy_Week_1_Day_1);
        array1 = getResources().getIntArray(R.array.Easy_Week_1_Day_2);
        array2 = getResources().getIntArray(R.array.Easy_Week_1_Day_3);
        array3 = getResources().getIntArray(R.array.Easy_Week_2_Day_1);
        array4 = getResources().getIntArray(R.array.Easy_Week_2_Day_2);
        array5 = getResources().getIntArray(R.array.Easy_Week_2_Day_3);
        array6 = getResources().getIntArray(R.array.Easy_Week_3_Day_1);
        array7 = getResources().getIntArray(R.array.Easy_Week_3_Day_2);
        array8 = getResources().getIntArray(R.array.Easy_Week_3_Day_3);
        array9 = getResources().getIntArray(R.array.Easy_Week_4_Day_1);
        array10 = getResources().getIntArray(R.array.Easy_Week_4_Day_2);
        array11 = getResources().getIntArray(R.array.Easy_Week_4_Day_3);
        array12 = getResources().getIntArray(R.array.Easy_Week_5_Day_1);
        array13 = getResources().getIntArray(R.array.Easy_Week_5_Day_2);
        array14 = getResources().getIntArray(R.array.Easy_Week_5_Day_3);
        array15 = getResources().getIntArray(R.array.Easy_Week_6_Day_1);
        array16 = getResources().getIntArray(R.array.Easy_Week_6_Day_2);
        array17 = getResources().getIntArray(R.array.Easy_Week_6_Day_3);


        putAlinHARDHash(0, array0,
                array1,
                array2,
                array3,
                array4,
                array5,
                array6,
                array7,
                array8,
                array9,
                array10,
                array11,
                array12,
                array13,
                array14,
                array15,
                array16,
                array17);


        array0 = getResources().getIntArray(R.array.Medium_Week_1_Day_1);
        array1 = getResources().getIntArray(R.array.Medium_Week_1_Day_2);
        array2 = getResources().getIntArray(R.array.Medium_Week_1_Day_3);
        array3 = getResources().getIntArray(R.array.Medium_Week_2_Day_1);
        array4 = getResources().getIntArray(R.array.Medium_Week_2_Day_2);
        array5 = getResources().getIntArray(R.array.Medium_Week_2_Day_3);
        array6 = getResources().getIntArray(R.array.Medium_Week_3_Day_1);
        array7 = getResources().getIntArray(R.array.Medium_Week_3_Day_2);
        array8 = getResources().getIntArray(R.array.Medium_Week_3_Day_3);
        array9 = getResources().getIntArray(R.array.Medium_Week_4_Day_1);
        array10 = getResources().getIntArray(R.array.Medium_Week_4_Day_2);
        array11 = getResources().getIntArray(R.array.Medium_Week_4_Day_3);
        array12 = getResources().getIntArray(R.array.Medium_Week_5_Day_1);
        array13 = getResources().getIntArray(R.array.Medium_Week_5_Day_2);
        array14 = getResources().getIntArray(R.array.Medium_Week_5_Day_3);
        array15 = getResources().getIntArray(R.array.Medium_Week_6_Day_1);
        array16 = getResources().getIntArray(R.array.Medium_Week_6_Day_2);
        array17 = getResources().getIntArray(R.array.Medium_Week_6_Day_3);

        putAlinHARDHash(1, array0,
                array1,
                array2,
                array3,
                array4,
                array5,
                array6,
                array7,
                array8,
                array9,
                array10,
                array11,
                array12,
                array13,
                array14,
                array15,
                array16,
                array17);
        array0 = getResources().getIntArray(R.array.Hard_Week_1_Day_1);
        array1 = getResources().getIntArray(R.array.Hard_Week_1_Day_2);
        array2 = getResources().getIntArray(R.array.Hard_Week_1_Day_3);
        array3 = getResources().getIntArray(R.array.Hard_Week_2_Day_1);
        array4 = getResources().getIntArray(R.array.Hard_Week_2_Day_2);
        array5 = getResources().getIntArray(R.array.Hard_Week_2_Day_3);
        array6 = getResources().getIntArray(R.array.Hard_Week_3_Day_1);
        array7 = getResources().getIntArray(R.array.Hard_Week_3_Day_2);
        array8 = getResources().getIntArray(R.array.Hard_Week_3_Day_3);
        array9 = getResources().getIntArray(R.array.Hard_Week_4_Day_1);
        array10 = getResources().getIntArray(R.array.Hard_Week_4_Day_2);
        array11 = getResources().getIntArray(R.array.Hard_Week_4_Day_3);
        array12 = getResources().getIntArray(R.array.Hard_Week_5_Day_1);
        array13 = getResources().getIntArray(R.array.Hard_Week_5_Day_2);
        array14 = getResources().getIntArray(R.array.Hard_Week_5_Day_3);
        array15 = getResources().getIntArray(R.array.Hard_Week_6_Day_1);
        array16 = getResources().getIntArray(R.array.Hard_Week_6_Day_2);
        array17 = getResources().getIntArray(R.array.Hard_Week_6_Day_3);
        putAlinHARDHash(2, array0,
                array1,
                array2,
                array3,
                array4,
                array5,
                array6,
                array7,
                array8,
                array9,
                array10,
                array11,
                array12,
                array13,
                array14,
                array15,
                array16,
                array17);


    }

    void putAlinHARDHash(int tez, int[]... arrays) {
        int dan = 0;
        int teden = 0;
        hashDan = new HashMap<>();
        hashTeden = new HashMap<>();
        for (int[] array : arrays) {
            if (dan < 3) {
                hashDan.put(dan, array);
                dan++;
            } else {
                dan = 0;
                hashTeden.put(teden, hashDan);
                teden++;
                hashDan = new HashMap<>();
                hashDan.put(dan, array);
                dan++;
            }

        }
        hashTeden.put(teden, hashDan);
        hashDif.put(tez, hashTeden);


    }

    private void setTotalPushUps() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        allpushups = preferences.getInt("allpushups", 0);
        //   Log.d("ALL PUSHUPS",allpushups+"");
        if (tvAllPushUpsNumber != null) {
            tvAllPushUpsNumber.setText(allpushups + "");
        }
    }

    private void setRecord() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        record = preferences.getInt("record", 0);
        //   Log.d("RECORD", record + "");
        if (tvRecord != null) {
            tvRecord.setText(record + "");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setTotalPushUps();
        setRecord();
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

    private void startTestStrenght() {
        Bundle bundle = new Bundle();
        FragmentTestStrenght fragmentTestStrenght = new FragmentTestStrenght();
        fragmentTestStrenght.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragmentTestStrenght)
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tezavnost = preferences.getInt("tezavnost", -1);
        izbira = preferences.getInt("izbira", -1);
        changeBackground(izbira);
        //startTraining();
        startTestStrenght();

    }

    private void changeBackground(int izbira) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        switch (izbira) {
            case 25:
                btnStart.setBackgroundColor(color20);
                viewBackground.setBackgroundColor(colorB20);
                tvUpcoming.setTextColor(color20);
                izbranabarva = color20;
                editor.putInt("izbira", GOAL25);
                editor.apply();

                break;
            case 50:
                btnStart.setBackgroundColor(color50);
                viewBackground.setBackgroundColor(colorB50);
                tvUpcoming.setTextColor(color50);
                izbranabarva = color50;
                editor.putInt("izbira", GOAL50);
                editor.apply();
                break;
            case 100:
                btnStart.setBackgroundColor(color100);
                viewBackground.setBackgroundColor(colorB100);
                tvUpcoming.setTextColor(color100);
                izbranabarva = color100;
                editor.putInt("izbira", GOAL100);
                editor.apply();
                break;
            default:
                break;
        }
        tvUpcoming.setText("YOUR GOAL IS: " + izbira + " PUSHUPS");

    }


    @Override
    public void howManyCanYouDo(int pushups) {
        //TODO Zapomni si te sklece in jih dodaj v skupno število sklec...
        //TODO Glede na ta rezultat, mu daj kateri set bo moral opravit
        //   Log.d("PUSHUPS", pushups + "");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();


        if (pushups <= 5) {
            editor.putInt("tezavnost", EASY);
            editor.putInt("teden", WEEK1);
            //  arraySets = getResources().getIntArray(R.array.Easy_Week_1_Day_1);
        } else if (pushups > 5 && pushups < 10) {
            editor.putInt("tezavnost", MEDIUM);
            editor.putInt("teden", WEEK1);
            //    arraySets = getResources().getIntArray(R.array.Easy_Week_2_Day_1);
        } else if (pushups > 11 && pushups < 20) {

            editor.putInt("tezavnost", HARD);
            editor.putInt("teden", WEEK1);
            //    arraySets = getResources().getIntArray(R.array.Easy_Week_3_Day_1);
        } else {
            editor.putInt("tezavnost", MEDIUM);
            editor.putInt("teden", WEEK3);
            //    arraySets = getResources().getIntArray(R.array.Easy_Week_4_Day_1);
        }
        editor.putInt("dan", 0);

        editor.apply();


        setSetsPushups();

    }


    private void setSetsPushups() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int difficulty = preferences.getInt("tezavnost", 0);
        int teden = preferences.getInt("teden", 0);
        int dan = preferences.getInt("dan", 0);

        Log.d("DIF", difficulty + "");
        Log.d("TEDEN", teden + "");
        Log.d("DAN", dan + "");

        hashTeden = hashDif.get(difficulty);
        hashDan = hashTeden.get(teden);
        arraySets = hashDan.get(dan);


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

    @Override
    public void SaveRecord() {
        setRecord();
    }

    @Override
    public void ChangeSet() {
        changeHisSet();
    }

    @Override
    public void CheckGoal(int number) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tezavnost = preferences.getInt("tezavnost", -1);
        izbira = preferences.getInt("izbira", -1);
        int teden = preferences.getInt("teden", -1);
        if (number > izbira) {
            if (tezavnost <= 2) {
                Toast.makeText(MainActivity.this, "Changing your goal...", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("tezavnost", tezavnost + 1);
                editor.putInt("teden", teden + 1);
                Log.d("Izbira", izbira + "");
                if (izbira == 25) {
                    izbira = izbira + 25;
                    changeBackground(izbira);
                } else if (izbira == 50) {
                    izbira = izbira + 50;
                    changeBackground(izbira);
                }
                editor.putInt("izbira", izbira);
                editor.apply();
            } else {
                Toast.makeText(MainActivity.this, "...Oh i can't... Do what you want.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void changeHisSet() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //     int difficulty = preferences.getInt("tezavnost", 0);
        int teden = preferences.getInt("teden", 0);
        int dan = preferences.getInt("dan", 0);

        SharedPreferences.Editor editor = preferences.edit();
        //   editor.putInt("tezavnost", difficulty);
        if (dan >= 3) {
            editor.putInt("teden", teden + 1);
            editor.putInt("dan", 0);

        } else {
            editor.putInt("dan", dan + 1);
        }
        editor.apply();

        setSetsPushups();

    }

}
