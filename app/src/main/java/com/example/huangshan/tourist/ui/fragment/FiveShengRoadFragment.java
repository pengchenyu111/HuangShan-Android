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

public class FiveShengRoadFragment extends Fragment {

    @BindView(R.id.old_road_1) ImageView oldRoad1;
    @BindView(R.id.old_road_2) ImageView oldRoad2;
    @BindView(R.id.old_road_3) ImageView oldRoad3;
    @BindView(R.id.old_road_4) ImageView oldRoad4;

    private static final String TAG = "FiveShengRoadFragment";

    public static final String OLD_ROAD_URL[] = {
            "http://101.37.173.73:8000/fivesheng/oldroad/huangshan_old_road_1.png",
            "http://101.37.173.73:8000/fivesheng/oldroad/huangshan_old_road_2.png",
            "http://101.37.173.73:8000/fivesheng/oldroad/huangshan_old_road_3.png",
            "http://101.37.173.73:8000/fivesheng/oldroad/huangshan_old_road_4.png"
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fivesheng_road, container, false);
        ButterKnife.bind(this, view);
        loadSongImg();

        return view;
    }

    private void loadSongImg() {
        Glide.with(getActivity()).load(OLD_ROAD_URL[0]).into(oldRoad1);
        Glide.with(getActivity()).load(OLD_ROAD_URL[1]).into(oldRoad2);
        Glide.with(getActivity()).load(OLD_ROAD_URL[2]).into(oldRoad3);
        Glide.with(getActivity()).load(OLD_ROAD_URL[3]).into(oldRoad4);
    }
}
