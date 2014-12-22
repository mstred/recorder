package br.org.cesar.recorder;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;

public class RecorderFragment extends Fragment {

    private ImageView mRecImg;

    private MediaRecorder mRec;

    private Context mCtx;

    public RecorderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setUpRecorder();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ToggleButton mRecBtn = (ToggleButton) rootView.findViewById(R.id.recBtn);
        mRecImg = (ImageView) rootView.findViewById(R.id.recImg);
        mCtx = rootView.getContext();

        mRecBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isRecording) {
                if (isRecording) {
                    mRecImg.setImageResource(R.color.red_400);
                    rec();
                } else {
                    mRecImg.setImageResource(R.color.background_material_dark);
                    stop();
                }
            }
        });
        return rootView;
    }

    private void setUpRecorder() {
        mRec = new MediaRecorder();
        mRec.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    private void rec() {
        try {
            String filename = String.format("%s.3gp", System.currentTimeMillis());
            File recFile = new File(mCtx.getFilesDir(), filename);
            recFile.createNewFile();
            mRec.setOutputFile(recFile.getAbsolutePath());
            mRec.prepare();
            mRec.start();
        } catch (IOException e) {
            Log.e(MainActivity.TAG, e.getMessage(), e);
        }
    }

    private void stop() {
        mRec.stop();
        mRec.release();
    }
}