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

public class FiveShengFamousFragment extends Fragment {

    @BindView(R.id.famous_1) ImageView famous1;
    @BindView(R.id.famous_2) ImageView famous2;
    @BindView(R.id.famous_3) ImageView famous3;
    @BindView(R.id.famous_4) ImageView famous4;

    private static final String TAG = "FiveShengFamousFragment";

    public static final String FAMOUS_URL[] = {
            "http://101.37.173.73:8000/fivesheng/famous/huangshan_famous_1.png",
            "http://101.37.173.73:8000/fivesheng/famous/huangshan_famous_2.png",
            "http://101.37.173.73:8000/fivesheng/famous/huangshan_famous_3.png",
            "http://101.37.173.73:8000/fivesheng/famous/huangshan_famous_4.png"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fivesheng_famous, container, false);
        ButterKnife.bind(this, view);
        loadSongImg();

        return view;
    }

    private void loadSongImg() {
        Glide.with(getActivity()).load(FAMOUS_URL[0]).into(famous1);
        Glide.with(getActivity()).load(FAMOUS_URL[1]).into(famous2);
        Glide.with(getActivity()).load(FAMOUS_URL[2]).into(famous3);
        Glide.with(getActivity()).load(FAMOUS_URL[3]).into(famous4);
    }
}
