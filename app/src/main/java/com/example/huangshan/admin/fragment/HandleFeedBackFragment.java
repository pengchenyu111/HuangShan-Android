package com.example.huangshan.admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.huangshan.R;

/**
 * NotificationFragment中管理员处理用户反馈的地方
 */
public class HandleFeedBackFragment extends Fragment implements View.OnClickListener {


    public static HandleFeedBackFragment newInstance() {
        Bundle args = new Bundle();
        HandleFeedBackFragment fragment = new HandleFeedBackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handle_fedback,container,false);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
