package com.example.huangshan.tourist.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.huangshan.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FiveJueHotSpringFragment extends Fragment {

    @BindView(R.id.hot_spring_1) ImageView hotSpring1;
    @BindView(R.id.hot_spring_2) ImageView hotSpring2;
    @BindView(R.id.hot_spring_3) ImageView hotSpring3;
    @BindView(R.id.hot_spring_4) ImageView hotSpring4;
    @BindView(R.id.hot_spring_5) ImageView hotSpring5;

    private static final String TAG = "FiveJueHotSpringFragmen";

    public static final String[] HOT_SPRING_URL = {
            "http://101.37.173.73:8000/fivejue/hotspring/huangshan_hot_spring_1.png",
            "http://101.37.173.73:8000/fivejue/hotspring/huangshan_hot_spring_2.png",
            "http://101.37.173.73:8000/fivejue/hotspring/huangshan_hot_spring_3.png",
            "http://101.37.173.73:8000/fivejue/hotspring/huangshan_hot_spring_4.png",
            "http://101.37.173.73:8000/fivejue/hotspring/huangshan_hot_spring_5.png",
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fivejue_hot_spring, container, false);
        ButterKnife.bind(this, view);

        loadSongImg();

        return view;
    }

    private void loadSongImg() {
        Glide.with(getActivity()).load(HOT_SPRING_URL[0]).into(hotSpring1);
        Glide.with(getActivity()).load(HOT_SPRING_URL[1]).into(hotSpring2);
        Glide.with(getActivity()).load(HOT_SPRING_URL[2]).into(hotSpring3);
        Glide.with(getActivity()).load(HOT_SPRING_URL[3]).into(hotSpring4);
        Glide.with(getActivity()).load(HOT_SPRING_URL[4]).into(hotSpring5);
    }
}
