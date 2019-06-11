package com.velen.whoplaysfirst.viewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.velen.whoplaysfirst.circles.CirclesFragment;
import com.velen.whoplaysfirst.coinflip.CoinFlipFragment;
import com.velen.whoplaysfirst.dice.DiceFragment;
import com.velen.whoplaysfirst.wheel.WheelFragment;

import java.util.HashMap;
import java.util.Map;

public class PageAdapter extends FragmentPagerAdapter{

    private Map<Integer, FragmentSelectionListener> fragmentMap = new HashMap<>();

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new CirclesFragment();
                fragmentMap.put(position, (FragmentSelectionListener) fragment);
                break;
            case 1:
                fragment = new WheelFragment();
                fragmentMap.put(position, (FragmentSelectionListener) fragment);
                break;
            case 2:
                fragment = new DiceFragment();
                fragmentMap.put(position, (FragmentSelectionListener) fragment);
                break;
            case 3:
                fragment = new CoinFlipFragment();
                fragmentMap.put(position, (FragmentSelectionListener) fragment);
                break;
            default:
                fragment = new CirclesFragment();
                fragmentMap.put(position, (FragmentSelectionListener) fragment);
                break;
        }

        return fragment;
    }

    public FragmentSelectionListener getFragment(int position) {
        return fragmentMap.get(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
