package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingyon.elevator.R;

/**
 * 指纹识别设置界面
 */
public class FingerSettingFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_finger_setting, container, false);
    }


    public static FingerSettingFragment newInstance() {
        Bundle args = new Bundle();
        FingerSettingFragment fragment = new FingerSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
