package com.velen.whoplaysfirst.coinflip;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.velen.whoplaysfirst.R;
import com.velen.whoplaysfirst.viewPager.FragmentSelectionListener;

public class CoinFlipFragment extends Fragment implements FragmentSelectionListener {

    private ImageView coinImg;
    private FlipCoin handler;
    private MediaPlayer mediaPlayer;

    public CoinFlipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coin_flip, container, false);

        coinImg = view.findViewById(R.id.coinImg);
        Animation coinflip = AnimationUtils.loadAnimation(getActivity(), R.anim.animationcoinflip);
        handler = new FlipCoin(coinImg, coinflip);

        return view;
    }

    @Override
    public void onFragmentUnselected() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
        ImageView icon = getActivity().findViewById(R.id.coinIcon);
        icon.setSelected(false);
    }

    @Override
    public void onFragmentSelected() {
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.coin_sound);
    }

    class FlipCoin extends Handler {
        private short m_CoinImageValue = 0;
        private ImageView m_Coin;

        FlipCoin(ImageView iconCoin, Animation animate) {
            final Animation animation = animate;
            m_Coin = iconCoin;

            m_Coin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    mediaPlayer.start();
                    m_Coin.startAnimation(animation);
                    handler.sendMessageDelayed(Message.obtain(handler, 0, 20), 50);
                }
            });
        }

        public void handleMessage(Message msg) {
            if(m_CoinImageValue == 0) {
                m_CoinImageValue = 1;
            }
            else {
                m_CoinImageValue = 0;
            }
            m_Coin.setImageLevel(m_CoinImageValue);

            // If there are still rolls available, roll another time.
            Integer flips = (Integer) msg.obj;
            if (flips > 0) {
                CoinFlipFragment.this.handler.sendMessageDelayed(Message.obtain(CoinFlipFragment.this.handler, 0, --flips), 50);
            }
            else {
                m_Coin.setImageLevel((int)Math.round(Math.random()));
            }
        }
    }

}
