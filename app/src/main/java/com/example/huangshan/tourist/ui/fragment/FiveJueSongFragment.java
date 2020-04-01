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

public class FiveJueSongFragment extends Fragment {

    @BindView(R.id.song_yingke) ImageView yingkesong;
    @BindView(R.id.song_songke) ImageView songkesong;
    @BindView(R.id.song_tuanjie) ImageView tuanjiesong;
    @BindView(R.id.song_lianli) ImageView lianlisong;
    @BindView(R.id.song_tanhai) ImageView tanhaisong;

    private static final String TAG = "FiveJueSongFragment";

    public static final String[] SONG_URL = {
            "http://101.37.173.73:8000/fivejue/song/huangshan_fivejue_yingkesong.png",
            "http://101.37.173.73:8000/fivejue/song/huangshan_fivejue_songkesong.png",
            "http://101.37.173.73:8000/fivejue/song/huangshan_fivejue_tuanjiesong.png",
            "http://101.37.173.73:8000/fivejue/song/huangshan_fivejue_lianlisong.png",
            "http://101.37.173.73:8000/fivejue/song/huangshan_fivejue_tanhaisong.png"
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fivejue_song, container, false);
        ButterKnife.bind(this, view);

        loadSongImg();
        return view;
    }

    private void loadSongImg() {
        Glide.with(getActivity()).load(SONG_URL[0]).into(yingkesong);
        Glide.with(getActivity()).load(SONG_URL[1]).into(songkesong);
        Glide.with(getActivity()).load(SONG_URL[2]).into(tuanjiesong);
        Glide.with(getActivity()).load(SONG_URL[3]).into(lianlisong);
        Glide.with(getActivity()).load(SONG_URL[4]).into(tanhaisong);
    }
}
