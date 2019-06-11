package com.velen.whoplaysfirst;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.github.florent37.bubbletab.BubbleTab;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.velen.whoplaysfirst.viewPager.PageAdapter;

public class MainActivity extends AppCompatActivity {

    private final static String AD_ID = "ca-app-pub-8012215063928736/1560305904";
    private final static String TEST_AD_ID = "ca-app-pub-3940256099942544/6300978111";
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private int lastViewPagerPosition;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        viewPager = findViewById(R.id.viewPager);
        pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setPageTransformer(true, new DefaultTransformer());

        BubbleTab bubbleTab = findViewById(R.id.bubbleTab);
        bubbleTab.setupWithViewPager(viewPager);

        setupAds();
        setupPageChangeListener();
    }

    private void setupPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pageAdapter.getFragment(lastViewPagerPosition).onFragmentUnselected();
                pageAdapter.getFragment(position).onFragmentSelected();
                lastViewPagerPosition = position;
            }
        });
    }

    private void setupAds() {
        MobileAds.initialize(this, "ca-app-pub-8012215063928736~8449337839");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("574D7CB006FF000F389382A5D96A8DE4").build();
        mAdView.loadAd(adRequest);
    }

}
