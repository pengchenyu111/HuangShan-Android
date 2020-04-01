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

public class FiveJueRockFragment extends Fragment {

    @BindView(R.id.rock_feilaishi) ImageView feilaishi;
    @BindView(R.id.rock_houziguanhai) ImageView houzidenghai;
    @BindView(R.id.rock_mengbishenghua) ImageView mengbishenghua;
    @BindView(R.id.rock_xiquedengmei) ImageView xiquedengmei;
    @BindView(R.id.rock_laosengruding) ImageView laosengruding;

    private static final String TAG = "FiveJueRockFragment";

    public static final String[] SONG_URL = {
            "http://101.37.173.73:8000/fivejue/rock/huangshan_rock_feilaishi.png",
            "http://101.37.173.73:8000/fivejue/rock/huangshan_rock_houziguanhai.png",
            "http://101.37.173.73:8000/fivejue/rock/huangshan_rock_mengbishenghua.png",
            "http://101.37.173.73:8000/fivejue/rock/huangshan_rock_xiquedengmei.png",
            "http://101.37.173.73:8000/fivejue/rock/huangshan_rock_laoshengcaiyao.png"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fivejue_rock, container, false);
        ButterKnife.bind(this, view);

        loadSongImg();

        return view;
    }

    private void loadSongImg() {
        Glide.with(getActivity()).load(SONG_URL[0]).into(feilaishi);
        Glide.with(getActivity()).load(SONG_URL[1]).into(houzidenghai);
        Glide.with(getActivity()).load(SONG_URL[2]).into(mengbishenghua);
        Glide.with(getActivity()).load(SONG_URL[3]).into(xiquedengmei);
        Glide.with(getActivity()).load(SONG_URL[4]).into(laosengruding);
    }
}
