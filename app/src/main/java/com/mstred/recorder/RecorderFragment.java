package com.mstred.recorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class RecorderFragment extends Fragment {

    private ToggleButton mRecBtn;
    private ImageView mRecImg;

    public RecorderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecBtn = (ToggleButton) rootView.findViewById(R.id.recBtn);
        mRecImg = (ImageView) rootView.findViewById(R.id.recImg);

        mRecBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mRecImg.setImageResource((isChecked) ?
                        R.color.background_material_light :
                        R.color.background_material_dark);
            }
        });
        return rootView;
    }
}