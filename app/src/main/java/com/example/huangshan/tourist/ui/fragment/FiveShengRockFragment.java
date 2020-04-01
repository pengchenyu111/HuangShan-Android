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

public class FiveShengRockFragment extends Fragment {

    @BindView(R.id.stone_carving_1) ImageView stoneCarving1;
    @BindView(R.id.stone_carving_2) ImageView stoneCarving2;
    @BindView(R.id.stone_carving_3) ImageView stoneCarving3;
    @BindView(R.id.stone_carving_4) ImageView stoneCarving4;


    private static final String TAG = "FiveShengRockFragment";

    public static final String STONE_CARVING_URL[] = {
            "http://101.37.173.73:8000/fivesheng/stonecarving/huangshan_stone_carving_1.png",
            "http://101.37.173.73:8000/fivesheng/stonecarving/huangshan_stone_carving_2.png",
            "http://101.37.173.73:8000/fivesheng/stonecarving/huangshan_stone_carving_3.png",
            "http://101.37.173.73:8000/fivesheng/stonecarving/huangshan_stone_carving_4.png"
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fivesheng_rock, container, false);
        ButterKnife.bind(this, view);
        loadSongImg();
        return view;
    }

    private void loadSongImg() {
        Glide.with(getActivity()).load(STONE_CARVING_URL[0]).into(stoneCarving1);
        Glide.with(getActivity()).load(STONE_CARVING_URL[1]).into(stoneCarving2);
        Glide.with(getActivity()).load(STONE_CARVING_URL[2]).into(stoneCarving3);
        Glide.with(getActivity()).load(STONE_CARVING_URL[3]).into(stoneCarving4);
    }
}
