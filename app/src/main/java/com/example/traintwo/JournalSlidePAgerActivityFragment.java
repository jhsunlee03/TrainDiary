package com.example.traintwo;

import android.Manifest;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class JournalSlidePAgerActivityFragment extends Fragment {
    //number of pages
    private static int NUM_PAGES = 5;

    private ViewPager2 viewPager;

    private FragmentStateAdapter pageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_pager, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle extras = getArguments();
        int position = NUM_PAGES;

        if (extras != null) {
            if (extras.containsKey("pos")) {
                position = extras.getInt("pos");
            }
        }

        //add button
        DataBaseHandler db = new DataBaseHandler(getContext());
        NUM_PAGES = db.getJournalsCount() + 1;
        viewPager = view.findViewById(R.id.pager);
        pageAdapter = new ScreenSlidePagerAdapter(getActivity());
        viewPager.setAdapter(pageAdapter);

        //set viewpager width
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.width = width * 9 / 10;
        viewPager.setLayoutParams(params);
//user permission for saving
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        viewPager.setCurrentItem(position, true);
    }
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter{
        public ScreenSlidePagerAdapter(FragmentActivity fa){super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new JournalSlidePageFragment(position);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}

