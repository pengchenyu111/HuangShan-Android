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

public class FiveShengPaintFragment extends Fragment {

    @BindView(R.id.paint_1) ImageView paint1;
    @BindView(R.id.paint_2) ImageView paint2;
    @BindView(R.id.paint_3) ImageView paint3;

    private static final String TAG = "FiveJueCloudFragment";

    public static final String PAINT_URL[] = {
            "http://101.37.173.73:8000/fivesheng/paint/huangshan_paint_1.png",
            "http://101.37.173.73:8000/fivesheng/paint/huangshan_paint_2.png",
            "http://101.37.173.73:8000/fivesheng/paint/huangshan_paint_3.png"
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fivesheng_paint, container, false);
        ButterKnife.bind(this, view);

        loadSongImg();
        return view;
    }

    private void loadSongImg() {
        Glide.with(getActivity()).load(PAINT_URL[0]).into(paint1);
        Glide.with(getActivity()).load(PAINT_URL[1]).into(paint2);
        Glide.with(getActivity()).load(PAINT_URL[2]).into(paint3);
    }
}
