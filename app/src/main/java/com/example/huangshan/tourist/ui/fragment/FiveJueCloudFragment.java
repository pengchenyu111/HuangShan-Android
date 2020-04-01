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

public class FiveJueCloudFragment extends Fragment {

    @BindView(R.id.cloud_east) ImageView eastCloud;
    @BindView(R.id.cloud_west) ImageView westCloud;
    @BindView(R.id.cloud_south) ImageView southCloud;
    @BindView(R.id.cloud_north) ImageView northCloud;
    @BindView(R.id.cloud_tian) ImageView tianCloud;
    @BindView(R.id.cloud_red_leaf) ImageView redLeafCloud;

    private static final String TAG = "FiveJueCloudFragment";

    public static final String[] CLOUD_URL = {
            "http://101.37.173.73:8000/fivejue/cloud/huangshan_cloud_east.png",
            "http://101.37.173.73:8000/fivejue/cloud/huangshan_cloud_west.png",
            "http://101.37.173.73:8000/fivejue/cloud/huangshan_cloud_south.png",
            "http://101.37.173.73:8000/fivejue/cloud/huangshan_cloud_north.png",
            "http://101.37.173.73:8000/fivejue/cloud/huangshan_cloud_tian.png",
            "http://101.37.173.73:8000/fivejue/cloud/huangshan_cloud_red_leaf.png",
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fivejue_cloud, container, false);
        ButterKnife.bind(this, view);

        loadSongImg();

        return view;
    }

    private void loadSongImg() {
        Glide.with(getActivity()).load(CLOUD_URL[0]).into(eastCloud);
        Glide.with(getActivity()).load(CLOUD_URL[1]).into(westCloud);
        Glide.with(getActivity()).load(CLOUD_URL[2]).into(southCloud);
        Glide.with(getActivity()).load(CLOUD_URL[3]).into(northCloud);
        Glide.with(getActivity()).load(CLOUD_URL[4]).into(tianCloud);
        Glide.with(getActivity()).load(CLOUD_URL[5]).into(redLeafCloud);
    }
}
