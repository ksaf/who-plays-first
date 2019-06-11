package com.velen.whoplaysfirst.circles;


import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.velen.whoplaysfirst.R;
import com.velen.whoplaysfirst.SelectionEndListener;
import com.velen.whoplaysfirst.circlesCountdown.CountDownEndListener;
import com.velen.whoplaysfirst.circlesCountdown.CountDownManager;
import com.velen.whoplaysfirst.viewPager.FragmentSelectionListener;

public class CirclesFragment extends Fragment implements CountDownEndListener, SelectionEndListener, FragmentSelectionListener {

    private CirclesView circlesView;
    private Button startBtn;
    private TextView countDownTxt, startTxt;
    private ImageView arrowImage;
    private CountDownManager countDownManager;

    public CirclesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circles, container, false);

        ConstraintLayout layout = view.findViewById(R.id.layout);
        arrowImage = view.findViewById(R.id.arrowImage);
        arrowImage.setVisibility(View.GONE);
        circlesView = new CirclesView(getActivity());
        circlesView.setSelectionEndListener(this);
        circlesView.getCircleColorManager().setSelectionEndListener(this);
        circlesView.getCircleColorManager().setContext(getActivity());
        //circlesView.getCircleColorManager().setRotateManager(new RotateManager(this, arrowImage));
        circlesView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.addView(circlesView);
        startBtn = view.findViewById(R.id.startBtn);
        startTxt = view.findViewById(R.id.startTxt);
        countDownTxt = view.findViewById(R.id.countDownTxt);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBtn.setVisibility(View.GONE);
                startTxt.setVisibility(View.GONE);
                startCountDown();
            }
        });

        return view;
    }

    public void removeAllCircles() {
        circlesView.removeAllCircles();
    }

    private void startCountDown() {
        arrowImage.setVisibility(View.GONE);
        countDownManager = new CountDownManager(countDownTxt, this);
        countDownManager.startCountDown(5);
    }

    private void startSpinning() {
        circlesView.getCircleColorManager().startChangingColors();
    }

    private void stopSpinning() {
        circlesView.getCircleColorManager().stopChangingColors();
    }

    @Override
    public void onCountDownEnd() {
        startSpinning();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSpinning();
            }
        }, 4000);
    }

    @Override
    public void onSelectionEnd() {
        if(countDownManager != null) {
            countDownManager.stopCountDown();
        }
        startBtn.setVisibility(View.VISIBLE);
        startTxt.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentUnselected() {
        removeAllCircles();
        ImageView icon = getActivity().findViewById(R.id.dotIcon);
        icon.setSelected(false);
    }

    @Override
    public void onFragmentSelected() {
    }
}
