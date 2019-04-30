package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.myapplication.R.id.id_DT;
import static com.example.myapplication.R.id.id_LR;
import static com.example.myapplication.R.id.id_NB;
import static com.example.myapplication.R.id.id_SVM;
import static com.example.myapplication.R.id.id_accuracy;
import static com.example.myapplication.R.id.id_userInputLabel;
//import static com.example.myapplication.R.id.id_userInput;

public class PredictActivity extends AppCompatActivity {
    //private EditText text;
    String input = null;
    private TextView userInput;
    private TextView userInputLabel;
    private TextView accuracy;
    private TextView power;

    int[] individualHeartRate = new int[29];
    int tp;
    int tn;
    int fp;
    int fn;
    double acc =0;
    private TextView executionTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        RadioGroup radioGroup = findViewById(R.id.rg_ML);
        //text = (EditText)findViewById(id_userInput);
        Bundle b = getIntent().getExtras();
        userInput = (TextView) findViewById(R.id.userInput);
        userInputLabel = (TextView) findViewById(R.id.id_userInputLabel);
        userInput.setText(b.getCharSequence("text"));

        accuracy = findViewById(id_accuracy);
        power = (TextView) findViewById(R.id.id_power);

        userInput.setVisibility(View.INVISIBLE);
        userInputLabel.setVisibility(View.INVISIBLE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Context context=getApplicationContext();
                BatteryManager mBatteryManager = (BatteryManager)context.getSystemService(Context.BATTERY_SERVICE);
                Long energy2 = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
                GraphView graph;
                PointsGraphSeries<DataPoint> series;       //an Object of the PointsGraphSeries for plotting scatter graphs
                PointsGraphSeries<DataPoint> seriesPredict;
                graph = (GraphView) findViewById(R.id.graphPrediction);
                graph.removeAllSeries();
                power.clearComposingText();
                tp=0;
                tn=0;
                fp=0;
                fn=0;
                accuracy.clearComposingText();
                accuracy.refreshDrawableState();

                if(checkedId == id_LR) {
                    //BatteryManager mBatteryManager = (BatteryManager) PredictActivity.this.getSystemService(Context.BATTERY_SERVICE);
                    //int level = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    //double mAh = (3000 * level * 0.01);
                    //Context context=getApplicationContext();
                    mBatteryManager = (BatteryManager)context.getSystemService(Context.BATTERY_SERVICE);
                    Long energy = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
                    graph.setVisibility(View.VISIBLE);

                    input = userInput.getText().toString();
                    long startTime = System.nanoTime();
                    int[] y_prev = new int[7];
                        for(int i=0;i<29;i++) {
                            int val = i+1;
                            String[] csvContent = readCVSFromAssetFolderIndividual("Rpeaks"+input+"/d"+input+"/Rpeaks"+input+"_"+val+".txt");
                            individualHeartRate[i] = csvContent.length;

                        }


                        y_prev[0]= individualHeartRate[22];

                        y_prev[1] = individualHeartRate[23];
                            if(input.equals("1")) {
                                for (int i = 0; i < 5; i++) {
                                    y_prev[i + 2] = (int) (-53 + (0.38477 * y_prev[i + 1]) + (3.0261 * y_prev[i]) - 0.10359 * (y_prev[i + 1] * y_prev[i]) + (0.054836 * y_prev[i + 1] * y_prev[i + 1]) + (0.023265 * y_prev[i] * y_prev[i]));
                                }
                            }
                            else if(input.equals("2"))
                            {
                                for (int i = 0; i < 5; i++) {
                                    y_prev[i + 2] = (int) (-248.89 + (2.4323 * y_prev[i + 1]) + (5.5857 * y_prev[i]) - 0.030383 * (y_prev[i + 1] * y_prev[i]) + (0.0043517 * y_prev[i + 1] * y_prev[i + 1]) + (-0.022826 * y_prev[i] * y_prev[i]));
                                }
                            }
                            else if(input.equals("3"))
                            {
                                for (int i = 0; i < 5; i++) {
                                    y_prev[i + 2] = (int) (-276.09 + (6.5291 * y_prev[i + 1]) + (0.82532 * y_prev[i]) - 0.020707 * (y_prev[i + 1] * y_prev[i]) + (-0.02328 * y_prev[i + 1] * y_prev[i + 1]) + (0.0088959 * y_prev[i] * y_prev[i]));
                                }
                            }
                            else{
                                for (int i = 0; i < 5; i++) {
                                    y_prev[i + 2] = (int) (-580.7 + (-14.819 * y_prev[i + 1]) + (3.5842 * y_prev[i]) +0.06388 * (y_prev[i + 1] * y_prev[i]) + (0.05094 * y_prev[i + 1] * y_prev[i + 1]) + (-0.050609 * y_prev[i] * y_prev[i]));
                                }
                            }
                        for(int i =0;i<29;i++) {

                                if (individualHeartRate[i] < 60) {
                                    series = new PointsGraphSeries<DataPoint>();
                                    series.appendData(data(i, individualHeartRate[i]), true, 35);
                                    series.setColor(Color.RED);

                                } else {
                                    series = new PointsGraphSeries<DataPoint>();
                                    series.appendData(data(i, individualHeartRate[i]), true, 35);

                                }
                            series.setSize(10);
                            graph.addSeries(series);
                            //graph.getLegendRenderer().setVisible(true);
                            series.setShape(PointsGraphSeries.Shape.POINT);
                            /*if(series.getColor()==(Color.RED))
                                series.setTitle("Bradycardia");
                            else

                                series.setTitle("Not Bradycardia");*/


                           if(i>=24)
                            {
                                seriesPredict = new PointsGraphSeries<DataPoint>();
                                seriesPredict.appendData(data(i,y_prev[i-22]),true,35);
                                if(y_prev[i-22]<60) {
                                    seriesPredict.setColor(Color.BLACK);
                                    //series.setTitle("Bradycardia");
                                }

                                else {
                                    seriesPredict.setColor(Color.GREEN);
                                    //series.setTitle("Not Bradycardia");

                                }
                                seriesPredict.setSize(10);
                                graph.addSeries(seriesPredict);
                                //graph.getLegendRenderer().setVisible(true);

                                //graph.getViewport().setMaxX(30);
                                seriesPredict.setShape(PointsGraphSeries.Shape.TRIANGLE);

                                /*if(seriesPredict.getColor()==(Color.BLACK))
                                    seriesPredict.setTitle("Bradycardia");
                                else
                                    seriesPredict.setTitle("Not Bradycardia");*/
                            }

                            graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
                            graph.getGridLabelRenderer().setVerticalAxisTitle("Heartrate");
                            graph.getViewport().setMaxX(30);
                            graph.getViewport().setXAxisBoundsManual(true);


                        }

                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime)/1000000;
                    executionTime = findViewById(R.id.id_executionTime);
                    executionTime.clearComposingText();
                    executionTime.setText(duration+" msec");

                    for(int i =0;i<5;i++)
                        {
                            if(individualHeartRate[i+24]<60 && y_prev[i+2]<60)
                                tp++;
                            else if(individualHeartRate[i+24]>60 && y_prev[i+2]<60)
                                fp++;
                            else if(individualHeartRate[i+24]>60 && y_prev[i+2]>60)
                                tn++;
                            else
                                fn++;
                        }

                    acc = 100*(tp+tn)/5;
                    accuracy.clearComposingText();
                     accuracy.setText(acc+"%");


                    Long energy1 = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
                    power.clearComposingText();
                    power.setText((energy2 - energy1)+" nWh");

                }

                if(checkedId == id_NB)
                {
                    graph.setVisibility(View.VISIBLE);

                    input = userInput.getText().toString();
                    long startTime = System.nanoTime();
                    int[] y_prev = new int[7];
                    for(int i=0;i<29;i++) {
                        int val = i+1;
                        String[] csvContent = readCVSFromAssetFolderIndividual("Rpeaks"+input+"/d"+input+"/Rpeaks"+input+"_"+val+".txt");
                        individualHeartRate[i] = csvContent.length;

                    }
                    y_prev[0]= individualHeartRate[22];

                    y_prev[1] = individualHeartRate[23];
                    if(input.equals("1")) {
                        for (int i = 0; i < 5; i++) {
                            y_prev[i + 2] = (int) (39.732 + (0.86993 * y_prev[i + 1]) + (-0.53094 * y_prev[i]));
                        }
                    }
                    else if(input.equals("2"))
                    {
                        for (int i = 0; i < 5; i++) {
                            y_prev[i + 2] = (int) (32.4 + (0.82813 * y_prev[i + 1]) + (-0.27671 * y_prev[i]));
                        }
                    }
                    else if(input.equals("3"))
                    {
                        for (int i = 0; i < 5; i++) {
                            y_prev[i + 2] = (int) (5.3425 + (0.79703 * y_prev[i + 1]) + (0.15578 * y_prev[i]));
                        }
                    }
                    else{
                        for (int i = 0; i < 5; i++) {
                            y_prev[i + 2] = (int) (37.81 + (0.53618 * y_prev[i + 1]) + (0.045928 * y_prev[i]));
                        }
                    }
                    for(int i =0;i<29;i++) {

                        if (individualHeartRate[i] < 60) {
                            series = new PointsGraphSeries<DataPoint>();
                            series.appendData(data(i, individualHeartRate[i]), true, 35);
                            series.setColor(Color.RED);

                        } else {
                            series = new PointsGraphSeries<DataPoint>();
                            series.appendData(data(i, individualHeartRate[i]), true, 35);
                        }
                        series.setSize(10);

                        graph.addSeries(series);

                        series.setShape(PointsGraphSeries.Shape.POINT);

                        if(i>=24)
                        {
                            seriesPredict = new PointsGraphSeries<DataPoint>();
                            seriesPredict.appendData(data(i,y_prev[i-22]),true,35);
                            if(y_prev[i-22]<60)
                                seriesPredict.setColor(Color.BLACK);
                            else
                                seriesPredict.setColor(Color.GREEN);
                            seriesPredict.setSize(10);

                            graph.addSeries(seriesPredict);
                            //graph.getViewport().setMaxX(30);
                            seriesPredict.setShape(PointsGraphSeries.Shape.TRIANGLE);

                        }
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("Heartrate");
                        graph.getViewport().setMaxX(30);
                        graph.getViewport().setXAxisBoundsManual(true);
                    }

                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime)/1000000;
                    executionTime = findViewById(R.id.id_executionTime);
                    executionTime.clearComposingText();
                    executionTime.setText(duration+" msec");

                    for(int i =0;i<5;i++)
                    {
                        if(individualHeartRate[i+24]<60 && y_prev[i+2]<60)
                            tp++;
                        else if(individualHeartRate[i+24]>60 && y_prev[i+2]<60)
                            fp++;
                        else if(individualHeartRate[i+24]>60 && y_prev[i+2]>60)
                            tn++;
                        else
                            fn++;
                    }

                    acc = 100*(tp+tn)/5;
                    accuracy.clearComposingText();
                    accuracy.setText(acc+"%");


                    Long energy1 = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
                    power.clearComposingText();
                    power.setText((energy2 - energy1)+" nWh");

            }

                if(checkedId == id_SVM)
                {
                    graph.setVisibility(View.VISIBLE);

                    input = userInput.getText().toString();
                    long startTime = System.nanoTime();
                    int[] y_prev = new int[7];
                    //BatteryManager mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
                    for(int i=0;i<29;i++) {
                        int val = i+1;
                        String[] csvContent = readCVSFromAssetFolderIndividual("Rpeaks"+input+"/d"+input+"/Rpeaks"+input+"_"+val+".txt");
                        individualHeartRate[i] = csvContent.length;

                    }
                    y_prev[0]= individualHeartRate[22]<60 ? 1 : -1;
                    y_prev[1] = individualHeartRate[23] < 60 ? 1 : -1;

                    if(input.equals("1")) {
                        for (int i = 0; i < 5; i++) {
                            y_prev[i + 2] = (int) ((y_prev[i]*0.5)+(y_prev[i+1]*0.5));
                        }
                    }
                    else if(input.equals("2"))
                    {
                        for (int i = 0; i < 5; i++) {
                            y_prev[i + 2] = (int) ((y_prev[i]*(-11))+(y_prev[i+1]*(-11)));
                            y_prev[i+2]= y_prev[i+2]==0 ? -1 : 1;

                        }
                    }
                    else if(input.equals("3"))
                    {
                        for (int i = 0; i < 5; i++) {
                            y_prev[i + 2] = (int) ((y_prev[i]*(-11))+(y_prev[i+1]*(-11)));
                            y_prev[i+2]= y_prev[i+2]==0 ? -1 : 1;

                        }
                    }
                    else{
                        for (int i = 0; i < 5; i++) {
                            y_prev[i + 2] = (int) ((y_prev[i]*(-11))+(y_prev[i+1]*(-11)));
                            y_prev[i+2]= y_prev[i+2]==0 ? -1 : 1;
                        }
                    }
                    for(int i =0;i<29;i++) {

                        if (individualHeartRate[i] < 60) {
                            series = new PointsGraphSeries<DataPoint>();
                            series.appendData(data(i, individualHeartRate[i]<60 ? 1 : -1), true, 35);
                            series.setColor(Color.RED);

                        } else {
                            series = new PointsGraphSeries<DataPoint>();
                            series.appendData(data(i, individualHeartRate[i]<60 ? 1 : -1), true, 35);
                        }
                        series.setSize(10);

                        graph.addSeries(series);

                        series.setShape(PointsGraphSeries.Shape.POINT);

                        if(i>=24)
                        {
                            seriesPredict = new PointsGraphSeries<DataPoint>();
                            seriesPredict.appendData(data(i,y_prev[i-22]),true,35);
                            if(y_prev[i-22]==1)
                                seriesPredict.setColor(Color.BLACK);
                            else
                                seriesPredict.setColor(Color.GREEN);
                            seriesPredict.setSize(10);

                            graph.addSeries(seriesPredict);
                            //graph.getViewport().setMaxX(30);
                            seriesPredict.setShape(PointsGraphSeries.Shape.TRIANGLE);

                        }
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("ClassLabel");
                        graph.getViewport().setMaxX(30);
                        graph.getViewport().setXAxisBoundsManual(true);
                    }

                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime)/1000000;
                    executionTime = findViewById(R.id.id_executionTime);
                    executionTime.clearComposingText();
                    executionTime.setText(duration+" msec");

                    for(int i =0;i<5;i++)
                    {
                        if(individualHeartRate[i+24]<60 && y_prev[i+2]==1)
                            tp++;
                        else if(individualHeartRate[i+24]>60 && y_prev[i+2]==1)
                            fp++;
                        else if(individualHeartRate[i+24]>60 && y_prev[i+2]==-1)
                            tn++;
                        else
                            fn++;
                    }

                    acc = 100*(tp+tn)/5;
                    accuracy.clearComposingText();
                    accuracy.setText(acc+"%");

                    /*BatteryManager mBatteryManager = (BatteryManager) PredictActivity.this.getSystemService(Context.BATTERY_SERVICE);
                    int level = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    double mAh = (3000 * level * 0.01);
                    power.clearComposingText();
                    power.setText(mAh+" mAh");*/
                    Long energy1 = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
                    power.clearComposingText();
                    power.setText((energy2 - energy1)+" nWh");

                }



                if(checkedId == id_DT) {

                    graph.setVisibility(View.VISIBLE);

                    input = userInput.getText().toString();
                    long startTime = System.nanoTime();
                    int[] y_prev = new int[7];
                    for(int i=0;i<29;i++) {
                        int val = i+1;
                        String[] csvContent = readCVSFromAssetFolderIndividual("Rpeaks"+input+"/d"+input+"/Rpeaks"+input+"_"+val+".txt");
                        individualHeartRate[i] = csvContent.length;

                    }
                    y_prev[0]= individualHeartRate[22];

                    y_prev[1] = individualHeartRate[23];

                    if(input.equals("1")) {
                        for (int i = 0; i < 5; i++) {

                            if(y_prev[i]<58.5)
                            {
                                y_prev[i+2] = 59;
                            }
                            else{
                                if(y_prev[i+1]>=61.5)
                                {
                                    y_prev[i+2]= 59;
                                }
                                else
                                {
                                    if(y_prev[i]<60.5)
                                        y_prev[i+2]=60;
                                    else{
                                        y_prev[i+2]=55;
                                    }
                                }
                            }
                        }
                    }
                    else if(input.equals("2")) {
                        for (int i = 0; i < 5; i++) {

                            if(y_prev[i+1]<68.5)
                            {
                                y_prev[i+2] = 64;
                            }
                            else{
                                if(y_prev[i+1]>=74)
                                {
                                    y_prev[i+2]= 65;
                                }
                                else
                                {
                                    y_prev[i+2] = 72;
                                }
                            }
                        }
                    }
                    else if(input.equals("3")) {
                        for (int i = 0; i < 5; i++) {

                            if(y_prev[i+1]>=82.5)
                            {
                                y_prev[i+2] = 113;
                            }
                            else{
                                if(y_prev[i]>=74)
                                {
                                    y_prev[i+2]=76;
                                }
                                else
                                {
                                   y_prev[i+2] = 73;
                                }
                            }
                        }
                    }
                    else{
                        for (int i = 0; i < 5; i++) {

                            if(y_prev[i]>=97.5)
                            {
                                y_prev[i+2] = 97;
                            }
                            else{
                                if(y_prev[i]>=94.5)
                                {
                                    y_prev[i+2]= 91;
                                }
                                else
                                {
                                    if(y_prev[i+1]<87.5)
                                        y_prev[i+2]=89;
                                    else{
                                        if(y_prev[i+1]>=97.5)
                                            y_prev[i+2]=95;
                                        else
                                            y_prev[i+2] = 88;
                                    }
                                }
                            }
                        }
                    }
                    for(int i =0;i<29;i++) {

                        if (individualHeartRate[i] < 60) {
                            series = new PointsGraphSeries<DataPoint>();
                            series.appendData(data(i, individualHeartRate[i]), true, 35);
                            series.setColor(Color.RED);

                        } else {
                            series = new PointsGraphSeries<DataPoint>();
                            series.appendData(data(i, individualHeartRate[i]), true, 35);
                        }
                        series.setSize(10);

                        graph.addSeries(series);

                        series.setShape(PointsGraphSeries.Shape.POINT);

                        if(i>=24)
                        {
                            seriesPredict = new PointsGraphSeries<DataPoint>();
                            seriesPredict.appendData(data(i,y_prev[i-22]),true,35);
                            if(y_prev[i-22]<60)
                                seriesPredict.setColor(Color.BLACK);
                            else
                                seriesPredict.setColor(Color.GREEN);
                            seriesPredict.setSize(10);

                            graph.addSeries(seriesPredict);
                            //graph.getViewport().setMaxX(30);
                            seriesPredict.setShape(PointsGraphSeries.Shape.TRIANGLE);

                        }

                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("Heartrate");
                        graph.getViewport().setMaxX(30);
                        graph.getViewport().setXAxisBoundsManual(true);
                    }

                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime)/1000000;
                    executionTime = findViewById(R.id.id_executionTime);
                    executionTime.clearComposingText();
                    executionTime.setText(duration+" msec");

                    for(int i =0;i<5;i++)
                    {
                        if(individualHeartRate[i+24]<60 && y_prev[i+2]<60)
                            tp++;
                        else if(individualHeartRate[i+24]>60 && y_prev[i+2]<60)
                            fp++;
                        else if(individualHeartRate[i+24]>60 && y_prev[i+2]>60)
                            tn++;
                        else
                            fn++;
                    }

                    acc = 100*(tp+tn)/5;
                    accuracy.clearComposingText();
                    accuracy.setText(acc+"%");

                    /*BatteryManager mBatteryManager = (BatteryManager) PredictActivity.this.getSystemService(Context.BATTERY_SERVICE);
                    int level = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    double mAh = (3000 * level * 0.01);
                    power.clearComposingText();
                    power.setText(mAh+" mAh");*/
                    Long energy1 = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
                    power.clearComposingText();
                    power.setText((energy2 - energy1)+" nWh");

                }


                }
        });

        Button back = findViewById(R.id.id_back);


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public DataPoint data(int x,int y){

        DataPoint v = new DataPoint(x,y);

        return v;
    }


    private String[] readCVSFromAssetFolderIndividual(String file){
        //List<String[]> csvLine = new ArrayList<>();
        String[] content = null;
        try {
            InputStream inputStream = getAssets().open(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while((line = br.readLine()) != null){
                content = line.split(" ");
                //csvLine.add(content);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
