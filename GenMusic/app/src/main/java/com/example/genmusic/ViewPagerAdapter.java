package com.example.genmusic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.genmusic.caNhanFragment.CaNhanFragment;
import com.example.genmusic.theLoaiFragment.TheLoaiFragment;
import com.example.genmusic.trangChuFragment.TrangChuFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new TrangChuFragment();
            case 1:
                return new TheLoaiFragment();
            case 2:
                return new BangXepHangFragment();
            case 3:
                return new CaNhanFragment();
            default:
                return new TrangChuFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
