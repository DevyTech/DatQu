package com.example.database.Sistem_Scan;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerScanAdapter extends FragmentStatePagerAdapter {
    private int numb_tabs;

    public PagerScanAdapter(FragmentManager fm, int numb_tabs){
        super(fm);
        this.numb_tabs = numb_tabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DataPegawaiFragment();
            case 1:
                return new DataScanFragment();
            case 2:
                return new DataExportScanFragment();
            default:
                return null;
        }
    }

    
    @Override
    public int getCount() {
        return numb_tabs;
    }
}
