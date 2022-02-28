package com.example.database.Sistem_Manual;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerManualAdapter extends FragmentStatePagerAdapter {
    private int num_tabs;

    public PagerManualAdapter(FragmentManager fm, int num_tabs){
        super(fm);
        this.num_tabs = num_tabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DataProdukFragment();
            case 1:
                return new DataPenjualanFragment();
            case 2:
                return new DataExportManualFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return num_tabs;
    }
}
