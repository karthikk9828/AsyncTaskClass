package com.example.asynctaskclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    EditText etNumTimes;
    Button btnRoll;
    TextView tvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumTimes = findViewById(R.id.etNumTimes);
        btnRoll = findViewById(R.id.btnRoll);
        tvResults = findViewById(R.id.tvResults);

        btnRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int numOfTimes = Integer.parseInt(etNumTimes.getText().toString().trim());

                new ProcessDiceInBackground().execute(numOfTimes);
            }
        });
    }

    public class ProcessDiceInBackground extends AsyncTask<Integer, Integer, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);

            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(Integer.parseInt(etNumTimes.getText().toString().trim()));
            dialog.show();
        }

        @Override
        protected String doInBackground(Integer... integers) {

            int ones = 0, twos = 0, threes = 0, fours= 0, fives = 0, sixes = 0, randomNum;

            Random random = new Random();

            String result;

            double currentProgress = 0;
            double previousProgress = 0;

            for(int i=0; i < integers[0]; i++) {

                currentProgress = (double) i / integers[0];

                // update progress after every 2%
                if(currentProgress - previousProgress >= 0.02) {

                    publishProgress(i);
                    previousProgress = currentProgress;
                }

                randomNum = random.nextInt(6) + 1;

                switch (randomNum) {

                    case 1:
                        ones++;
                        break;

                    case 2:
                        twos++;
                        break;

                    case 3:
                        threes++;
                        break;

                    case 4:
                        fours++;
                        break;

                    case 5:
                        fives++;
                        break;

                    default:
                        sixes++;
                }
            }

            result = "Results: \n1: " + ones + "\n2: " + twos + "\n3: "+ threes
                    + "\n4: "+ fours + "\n5: " + fives + "\n6: " + sixes;

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();

            tvResults.setText(s);

            Toast.makeText(MainActivity.this, "Process done!", Toast.LENGTH_SHORT).show();
        }
    }
}
