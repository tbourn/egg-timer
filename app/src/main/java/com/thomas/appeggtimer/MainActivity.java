package com.thomas.appeggtimer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
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
                updateTimer(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void buttonClicked(View view) {
        // Take the number of seconds the user wants to set and multiply by 1000,
        // so then we can work with milliseconds
        new CountDownTimer(seekBar.getProgress() * 1000, 1000) {

            @Override
            public void onTick(long l) {
                // Divide the number of milliseconds by 1000, so it turns to second
                updateTimer((int) l / 1000);
            }

            @Override
            public void onFinish() {
                Log.i("Finished", "Timer all done");
            }
        }.start();
    }

    /**
     * Updates timeTextView display.
     *
     * @param secondsLeft
     */

    public void updateTimer(int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String minutesString = getTimeAsString(minutes);
        String secondString = getTimeAsString(seconds);

        timeTextView.setText(minutesString + ":" + secondString);
    }

    /**
     * Converts time to String and checks String object's length. If length is equal to 1, then adds
     * an extra zero prefix.
     *
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