package com.cody.component.app.viewpage;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BaseFragmenttStateAdapter extends FragmentStateAdapter {


    private List<Fragment> mFragments;
    public BaseFragmenttStateAdapter(@NonNull  FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        mFragments = new ArrayList<>();
    }
    public BaseFragmenttStateAdapter(@NonNull  FragmentActivity fragmentActivity,List<Fragment> f) {
        super(fragmentActivity);
        mFragments = new ArrayList<>();
        if(f!=null){
            mFragments.addAll(f);
        }
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}
