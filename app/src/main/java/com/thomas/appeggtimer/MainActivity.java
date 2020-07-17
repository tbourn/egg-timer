/*
 * MIT License
 *
 * Copyright (c) 2020 Thomas Bournaveas
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
    private static final int STARTING_POSITION = MAX_TIME / 2;

    private Button goButton;
    private CountDownTimer countDownTimer;
    private SeekBar seekBar;
    private TextView timeTextView;
    private boolean counterIsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goButton = findViewById(R.id.button);

        timeTextView = findViewById(R.id.timeTextView);
        timeTextView.setText(getTimerAsString(STARTING_POSITION));

        seekBar = findViewById(R.id.seekBar);
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
     *  Once the button is pressed, the method checks if counter is active. If true, it resets the timer.
     *  Otherwise, it activates the counter, disables the state of the seekBar and sets its visibility to invisible.
     *  Also, it changes the goButton text and starts a new CountDownTimer, where inside onTick method
     *  updateTimer method is called and inside onFinish method the media file is specified. Finally,
     *  inside the onFinish method, it resets the timer and sets the seekBar's visibility to visible.
     *
     * @param view the view object
     */
    public void buttonClicked(View view) {

        if (counterIsActive) {
            resetTimer();
        } else {
            counterIsActive = true;

            // The User can't update the seekBar
            seekBar.setEnabled(false);
            seekBar.setVisibility(View.INVISIBLE);

            goButton.setText(R.string.StopText);

            /*
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
                    seekBar.setVisibility(View.VISIBLE);
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
        timeTextView.setText(getTimerAsString(STARTING_POSITION));
        seekBar.setProgress(STARTING_POSITION);
        seekBar.setEnabled(true);
        seekBar.setVisibility(View.VISIBLE);
        countDownTimer.cancel();
        goButton.setText(R.string.goButtonText);
        counterIsActive = false;
    }

    /**
     * Updates timeTextView display.
     *
     * @param secondsLeft the seconds
     */
    private void updateTimer(int secondsLeft) {
        String timeAsString = getTimerAsString(secondsLeft);
        timeTextView.setText(timeAsString);
    }

    /**
     * Returns seconds as String representation in the form of HH:MM:SS
     *
     * @param secondsLeft the seconds
     * @return the time as String in the form of HH:MM:SS
     */
    private String getTimerAsString(int secondsLeft) {
        int hours = secondsLeft / 3600;
        int minutes = (secondsLeft % 3600) / 60;
        int seconds = secondsLeft % 60;

        String hoursString = getPrefixedTime(hours);
        String minutesString = getPrefixedTime(minutes);
        String secondString = getPrefixedTime(seconds);
        String displayText = minutesString + ":" + secondString;

        if (hours != 0) {
            displayText = hoursString + ":" + displayText;
        }

        // Change font color when timer is less than 6
        if (hours == 0 && minutes == 0 && seconds < 6) {
            timeTextView.setTextColor(Color.RED);
        } else {
            timeTextView.setTextColor(getResources().getColor(R.color.timerGreenColor));
        }
        return displayText;
    }

    /**
     * Converts time to String and checks String object's length. If length is equal to 1, then adds
     * an extra zero prefix.
     *
     * @param time the time in String format
     * @return the time as String, prefixed with zeros if necessary
     */
    private String getPrefixedTime(int time) {
        String timeString = Integer.toString(time);
        if (timeString.length() == 1) {
            timeString = 0 + timeString;
        }
        return timeString;
    }

}
