package com.thomas.appeggtimer;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_TIME = 600; // seconds
    private static final int STARTING_POSITION = 30;

    private Button goButton;
    private CountDownTimer countDownTimer;
    private SeekBar seekBar;
    private TextView timeTextView;
    private boolean counterIsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        timeTextView = findViewById(R.id.timeTextView);
        goButton = findViewById(R.id.button);

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

    /**
     * @param view
     */
    public void buttonClicked(View view) {

        if (counterIsActive) {
            resetTimer();
        } else {
            counterIsActive = true;

            // The User can't update the seekBar
            seekBar.setEnabled(false);

            goButton.setText(R.string.StopText);

            /**
             * Takes the number of seconds the user wants to set and multiply by 1000,
             * so then we can work with milliseconds.
             * Adds 100 (a tenth of a second) to get correct result. The problem is that by the time the
             * CountDownTimer runs the code and comes back, it returns an approximate lower value.
             */
            countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000 + 100, 1000) {

                @Override
                public void onTick(long l) {
                    // Divide the number of milliseconds by 1000, so it turns to second
                    updateTimer((int) l / 1000);
                }

                @Override
                public void onFinish() {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.air_horn);
                    mediaPlayer.start();
                    resetTimer();
                }
            }.start();
        }

    }

    /**
     * Resets the timer
     */
    private void resetTimer() {
        // Set font color back to green variation
        timeTextView.setTextColor(getResources().getColor(R.color.timerGreenColor));
        timeTextView.setText(R.string.initialTimerValue);
        seekBar.setProgress(30);
        seekBar.setEnabled(true);
        countDownTimer.cancel();
        goButton.setText(R.string.goButtonText);
        counterIsActive = false;
    }

    /**
     * Updates timeTextView display.
     *
     * @param secondsLeft
     */
    private void updateTimer(int secondsLeft) {
        int hours = secondsLeft / 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String minutesString = getTimeAsString(minutes);
        String secondString = getTimeAsString(seconds);

        timeTextView.setText(minutesString + ":" + secondString);

        // Change font color when timer is less than 6
        if (minutes == 0 && seconds < 6) {
            timeTextView.setTextColor(Color.RED);
        }
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