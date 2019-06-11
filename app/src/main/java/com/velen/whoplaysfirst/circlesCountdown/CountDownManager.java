package com.velen.whoplaysfirst.circlesCountdown;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class CountDownManager {

    private static final int INTERVAL = 1000;
    private TextView countDownTxt;
    private Handler handler = new Handler();
    private int currentSec = 5;
    private CountDownEndListener listener;

    public CountDownManager(TextView countDownTxt, CountDownEndListener listener) {
        this.countDownTxt = countDownTxt;
        this.listener = listener;
    }

    private Runnable timeChanger = new Runnable() {
        @Override
        public void run() {
            if(currentSec > 1) {
                currentSec--;
                countDownTxt.setText(String.valueOf(currentSec));
                handler.postDelayed(timeChanger, INTERVAL);
            } else {
                countDownTxt.setVisibility(View.GONE);
                handler.removeCallbacksAndMessages(null);
                listener.onCountDownEnd();
            }
        }
    };

    public void startCountDown(int seconds) {
        currentSec = seconds;
        countDownTxt.setText(String.valueOf(currentSec));
        countDownTxt.setVisibility(View.VISIBLE);
        handler.postDelayed(timeChanger, INTERVAL);
    }

    public void stopCountDown() {
        countDownTxt.setVisibility(View.GONE);
        handler.removeCallbacksAndMessages(null);
    }

}
