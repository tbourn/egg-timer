package com.thomas.appeggtimer;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_TIME = 600; // seconds
    private static final int STARTING_POSITION = 0;

    private SeekBar seekBar;
    private TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        timeTextView = findViewById(R.id.timeTextView);

        seekBar.setMax(MAX_TIME);
        seekBar.setProgress(STARTING_POSITION);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int minutes = i / 60;
                int seconds = i - minutes * 60;

                String minutesString = getTimeAsString(minutes);
                String secondString = getTimeAsString(seconds);

                timeTextView.setText(minutesString + ":" + secondString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * Converts time to String and checks String object's length. If length is equal to 1, then adds
     * an extra zero prefix.
     * @param time
     * @return
     */

    private String getTimeAsString(int time) {
        String timeString = Integer.toString(time);
        if (timeString.length() == 1) {
            timeString = 0 + timeString;
        }
        return timeString;
    }
}