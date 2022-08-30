package com.davidvignon.go4lunch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.davidvignon.go4lunch.view.fragments.listView.ListViewFragment;
import com.davidvignon.go4lunch.view.fragments.mapView.MapViewFragment;
import com.davidvignon.go4lunch.view.fragments.workMates.WorkMatesFragment;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * getItem is called to instantiate the fragment for the given page.
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 : return MapViewFragment.newInstance();
            case 1 : return ListViewFragment.newInstance();
            case 2 : return WorkMatesFragment.newInstance();
            default: return null;
        }
    }

    /**
     * @return the number of pages
     */
    @Override
    public int getCount() {
        return 3;
    }
}
