package cn.jzvd.demo;

import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

import static cn.jzvd.Jzvd.backPress;

public class MainActivity extends AppCompatActivity {

    private VpAdapter adapter;
    private ViewPager viewPager;
    private BottomNavigationViewEx bottomNavigationViewEx;

    private SparseIntArray items;// used for change ViewPager selected item
    private List<Fragment> fragments;// used for ViewPager adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        bottomNavigationViewEx = findViewById(R.id.bottom_navigation);
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(true);
    }

    private void initData() {
        fragments = new ArrayList<>(4);
        items = new SparseIntArray(4);

        Fragment_1_Base basicsFragment = new Fragment_1_Base();
        Fragment_2_Custom customFragment = new Fragment_2_Custom();
        Fragment_3_List complexFragment = new Fragment_3_List();
        Fragment_4_More otherFragment = new Fragment_4_More();

        fragments.add(basicsFragment);
        fragments.add(customFragment);
        fragments.add(complexFragment);
        fragments.add(otherFragment);

        items.put(R.id.i_base, 0);
        items.put(R.id.i_custom, 1);
        items.put(R.id.i_complex, 2);
        items.put(R.id.i_other, 3);

        adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(items.size());
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            bottomNavigationViewEx.setCurrentItem(position);
            Jzvd.releaseAllVideos();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        private int previousPosition = -1;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int position = items.get(item.getItemId());
            if (previousPosition != position) {
                previousPosition = position;
                viewPager.setCurrentItem(position);
            }
            return true;
        }
    };

    private void initEvent() {
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Jzvd.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.goOnPlayOnPause();
    }

    @Override
    public void onBackPressed() {
        if (backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Jzvd.releaseAllVideos();
        navigationItemSelectedListener = null;
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(null);
        viewPager.removeOnPageChangeListener(pageChangeListener);
    }
}
