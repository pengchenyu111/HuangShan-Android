package com.example.huangshan.tourist.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.huangshan.R;

import butterknife.ButterKnife;

public class FiveShengRockFragment extends Fragment {


    private static final String TAG = "FiveShengRockFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fivesheng_rock, container, false);
        ButterKnife.bind(this, view);


        return view;
    }
}
