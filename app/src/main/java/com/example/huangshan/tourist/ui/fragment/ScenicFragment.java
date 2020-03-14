package com.example.huangshan.tourist.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.huangshan.R;

public class ScenicFragment extends Fragment implements View.OnClickListener{

    public static ScenicFragment newInstance() {
        Bundle args = new Bundle();
        ScenicFragment fragment = new ScenicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_scenic, container ,false);
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}