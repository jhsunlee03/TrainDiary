package com.example.traintwo;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationListener implements BottomNavigationView.OnNavigationItemSelectedListener{
    private FragmentManager fm;
    public NavigationListener(FragmentManager fragmentManager){
        fm = fragmentManager;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addnewmenu:
                fm.beginTransaction().replace(R.id.container, new JournalSlidePAgerActivityFragment()).commit();
                return true;

            case R.id.showlistmenu:
                fm.beginTransaction().replace(R.id.container, new ListPAgeFragment()).commit();
                return true;

            case R.id.sortmenu:
                fm.beginTransaction().replace(R.id.container, new SortingFragment()).commit();
                return true;


        }







        return false;
    }
}
