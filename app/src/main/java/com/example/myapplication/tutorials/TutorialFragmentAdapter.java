package com.example.myapplication.tutorials;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class TutorialFragmentAdapter extends FragmentStateAdapter {
    private final List<Integer> layouts;

    public TutorialFragmentAdapter(@NonNull FragmentActivity fragmentActivity, List<Integer> layouts) {
        super(fragmentActivity);
        this.layouts = layouts;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return TutorialPageFragment.newInstance(layouts.get(position));
    }

    @Override
    public int getItemCount() {
        return layouts.size();
    }
}

