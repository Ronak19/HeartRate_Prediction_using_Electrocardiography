package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
//import org.math.plot.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.widget.TextView;
import android.widget.*;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;
import android.graphics.Color;



public class MainActivity extends AppCompatActivity  {

    private TextView heartrate;
    private TextView executionTime;
    int[] individualHeartRate = new int[29];
    private EditText text;
    String input = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         text = (EditText)findViewById(R.id.id_userInput);


        Button detect = findViewById(R.id.id_detectButton);


        detect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GraphView graph;
                long startTime = System.nanoTime();
                input = text.getText().toString();
                PointsGraphSeries<DataPoint> series;       //an Object of the PointsGraphSeries for plotting scatter graphs
                graph = (GraphView) findViewById(R.id.graph);
                graph.removeAllSeries();
                for(int i=0;i<29;i++) {
                    int val = i+1;
                    String[] csvContent = readCVSFromAssetFolderIndividual("Rpeaks"+input+"/d"+input+"/Rpeaks"+input+"_"+val+".txt");
                    individualHeartRate[i] = csvContent.length;

                    if(individualHeartRate[i]<60) {
                        series = new PointsGraphSeries<DataPoint>();
                        series.appendData(data(i, individualHeartRate[i]), true, 35);
                        series.setSize(10);
                        series.setColor(Color.RED);

                    }
                    else{
                        series = new PointsGraphSeries<DataPoint>();
                        series.setSize(10);
                        series.appendData(data(i, individualHeartRate[i]), true, 35);
                    }
                    graph.addSeries(series);
                    graph.getViewport().setMaxX(30);
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
                    graph.getGridLabelRenderer().setVerticalAxisTitle("Heartrate");
                    series.setShape(PointsGraphSeries.Shape.POINT);

                }


                String[] csvContent1 = readCVSFromAssetFolder("Rpeaks"+input+"/Rpeaks"+input+".txt");
                heartrate = findViewById(R.id.id_heartrate);
                int overAllHeartRate = csvContent1.length / 30;
                heartrate.setText(String.valueOf(overAllHeartRate));
                long endTime = System.nanoTime();
                long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
                executionTime = findViewById(R.id.id_executionTime);
                executionTime.clearComposingText();
                executionTime.setText(duration+" msec");
            }
                //printCVSContent(csvContent);



        });

        Button predictionButton = findViewById(R.id.id_prediction);
        predictionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), PredictActivity.class);

                Bundle b = new Bundle();
                b.putString("text",text.getText().toString());
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });



        }

    public DataPoint data(int x,int y){
            DataPoint v = new DataPoint(x,y);
        return v;
    }

    private String[] readCVSFromAssetFolder(String file){
        String[] content = null;
        try {
            InputStream inputStream = getAssets().open(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while((line = br.readLine()) != null){
                content = line.split(" ");
            }
            /*for(int i=0;i<content.length;i++){
                csvLine.add(content[i]);
            }*/
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
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

    private void printCVSContent(List<String[]> result){
        String cvsColumn = "";
        for (int i = 0; i < result.size(); i++){
            String[] rows = result.get(i);
            cvsColumn += rows[0]+" ";
        }
        heartrate.setText(cvsColumn);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
