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

public class FiveJueSnowFragment extends Fragment {

    @BindView(R.id.snow_1) ImageView snow1;
    @BindView(R.id.snow_2) ImageView snow2;
    @BindView(R.id.snow_3) ImageView snow3;
    @BindView(R.id.snow_4) ImageView snow4;
    @BindView(R.id.snow_5) ImageView snow5;

    private static final String TAG = "FiveJueHotSpringFragmen";

    public static final String[] SNOW_URL = {
            "http://101.37.173.73:8000/fivejue/snow/winter_snow_1.png",
            "http://101.37.173.73:8000/fivejue/snow/winter_snow_2.png",
            "http://101.37.173.73:8000/fivejue/snow/winter_snow_3.png",
            "http://101.37.173.73:8000/fivejue/snow/winter_snow_4.png",
            "http://101.37.173.73:8000/fivejue/snow/winter_snow_5.png",
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fivejue_snow, container, false);
        ButterKnife.bind(this, view);
        loadSongImg();
        return view;
    }

    private void loadSongImg() {
        Glide.with(getActivity()).load(SNOW_URL[0]).into(snow1);
        Glide.with(getActivity()).load(SNOW_URL[1]).into(snow2);
        Glide.with(getActivity()).load(SNOW_URL[2]).into(snow3);
        Glide.with(getActivity()).load(SNOW_URL[3]).into(snow4);
        Glide.with(getActivity()).load(SNOW_URL[4]).into(snow5);
    }
}
