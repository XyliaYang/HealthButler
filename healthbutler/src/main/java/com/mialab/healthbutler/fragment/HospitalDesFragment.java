package com.mialab.healthbutler.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mialab.healthbutler.R;

import butterknife.ButterKnife;

/**
 * Created by Wesly186 on 2016/6/9.
 */
public class HospitalDesFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hos_desc, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
