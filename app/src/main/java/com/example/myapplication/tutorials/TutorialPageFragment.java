package com.example.myapplication.tutorials;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TutorialPageFragment extends Fragment {
    private static final String ARG_LAYOUT_ID = "layout_id";

    public static TutorialPageFragment newInstance(int layoutId) {
        TutorialPageFragment fragment = new TutorialPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_ID, layoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = getArguments() != null ? getArguments().getInt(ARG_LAYOUT_ID) : 0;
        return inflater.inflate(layoutId, container, false);
    }
}
