package com.velen.whoplaysfirst.dice;


import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.velen.whoplaysfirst.R;
import com.velen.whoplaysfirst.vibration.VibrationUtil;
import com.velen.whoplaysfirst.viewPager.FragmentSelectionListener;

import java.util.Random;
import java.util.Stack;

public class DiceFragment extends Fragment implements FragmentSelectionListener {

    private ImageView dice1Img, dice2Img, dice3Img, dice4Img;
    private LinearLayout linearLayoutBot;
    private TextView resultTxt;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private MediaPlayer mediaPlayerRoll, mediaPlayerAdd;
    private Button plusBtn, minusBtn;
    private Stack<ImageView> diceStack;
    private int sum, rollsFinished;

    public DiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dice, container, false);
        dice1Img = view.findViewById(R.id.dice1Img);
        dice1Img.setColorFilter(Color.parseColor("#9A1608"));
        dice2Img = view.findViewById(R.id.dice2Img);
        dice2Img.setColorFilter(Color.parseColor("#9A1608"));
        dice3Img = view.findViewById(R.id.dice3Img);
        dice3Img.setColorFilter(Color.parseColor("#9A1608"));
        dice4Img = view.findViewById(R.id.dice4Img);
        dice4Img.setColorFilter(Color.parseColor("#9A1608"));

        resultTxt = view.findViewById(R.id.resultTxt);
        linearLayoutBot = view.findViewById(R.id.linearLayoutBot);

        diceStack = new Stack<>();
        diceStack.push(dice1Img);
        diceStack.push(dice2Img);

        plusBtn = view.findViewById(R.id.plusBtn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!diceStack.contains(dice2Img)) {
                    diceStack.push(dice2Img);
                    mediaPlayerAdd.start();
                    dice2Img.setVisibility(View.VISIBLE);
                    dice2Img.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
                } else if(!diceStack.contains(dice3Img)) {
                    diceStack.push(dice3Img);
                    mediaPlayerAdd.start();
                    showBottomRow(true);
                    dice3Img.setVisibility(View.VISIBLE);
                    dice3Img.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
                } else if(!diceStack.contains(dice4Img)) {
                    diceStack.push(dice4Img);
                    mediaPlayerAdd.start();
                    showBottomRow(true);
                    dice4Img.setVisibility(View.VISIBLE);
                    dice4Img.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
                }
            }
        });

        minusBtn = view.findViewById(R.id.minusBtn);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(diceStack.contains(dice2Img)) {
                    mediaPlayerAdd.start();
                    diceStack.pop().setVisibility(View.GONE);
                    if(!diceStack.contains(dice3Img)) {
                        showBottomRow(false);
                    }
                }

            }
        });

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if(count == 1) {
                    animateDice();
                }
            }
        });

        return view;
    }

    private void showBottomRow(boolean show) {
        int visible = show ? View.VISIBLE : View.GONE;
        if(linearLayoutBot.getVisibility() != visible) {
            linearLayoutBot.setVisibility(visible);
        }
    }

    private int randomDiceValue() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    private void animateDice() {
        final Animation anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        final Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        final Animation anim3 = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        final Animation anim4 = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mediaPlayerRoll.start();
                sum = 0;
                rollsFinished = 0;
                VibrationUtil.vibrate(getActivity(), 100);
                hideResultText();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int value = randomDiceValue();
                int res = getResources().getIdentifier("dice_" + value, "drawable", "com.velen.whoplaysfirst");

                if (animation == anim1) {
                    dice1Img.setImageResource(res);
                } else if (animation == anim2) {
                    dice2Img.setImageResource(res);
                } else if (animation == anim3) {
                    dice3Img.setImageResource(res);
                } else if (animation == anim4) {
                    dice4Img.setImageResource(res);
                }
                sum = sum + value;
                rollsFinished++;
                if(rollsFinished == diceStack.size()) {
                    showResultText(String.valueOf(sum));
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        anim1.setAnimationListener(animationListener);
        anim2.setAnimationListener(animationListener);
        anim3.setAnimationListener(animationListener);
        anim4.setAnimationListener(animationListener);

        dice1Img.startAnimation(anim1);
        if(diceStack.contains(dice2Img)) {
            dice2Img.startAnimation(anim2);
        }
        if(diceStack.contains(dice3Img)) {
            dice3Img.startAnimation(anim3);
        }
        if(diceStack.contains(dice4Img)) {
            dice4Img.startAnimation(anim4);
        }
    }

    private void showResultText(String item) {
        resultTxt.setText("Result: " + item);
        resultTxt.setVisibility(View.VISIBLE);
    }

    private void hideResultText() {
        resultTxt.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
    }

    @Override
    public void onFragmentUnselected() {
        hideResultText();
        if(mediaPlayerRoll != null) {
            mediaPlayerRoll.release();
        }
        if(mediaPlayerAdd != null) {
            mediaPlayerAdd.release();
        }
        ImageView icon = getActivity().findViewById(R.id.diceIcon);
        icon.setSelected(false);
    }

    @Override
    public void onFragmentSelected() {
        mediaPlayerRoll = MediaPlayer.create(getActivity(), R.raw.dice_sound);
        mediaPlayerAdd = MediaPlayer.create(getActivity(), R.raw.pop);
    }
}
