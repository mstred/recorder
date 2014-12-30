package br.org.cesar.recorder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PlayerFragment extends Fragment {

    private Activity mActv;
    private ListView mFileList;
    private SoundPool mSoundPool;
    private Map<String, Integer> mLoadedSounds = new HashMap<>();
    private int mStreamId;
    private int mSelectedItemPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSoundPool = this.createSoundPool();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActv = getActivity();
        View mRootView = inflater.inflate(R.layout.fragment_player, container, false);

        mFileList = (ListView) mRootView.findViewById(R.id.file_list);
        mFileList.setAdapter(this.createFileListAdapter());
        mFileList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mFileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long arg3) {
                PlayerFragment.this.onSelectItem(index);
            }
        });

        Button playButton = (Button) mRootView.findViewById(R.id.play_btn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerFragment.this.onPlayButtonClick();
            }
        });

        Button stopButton = (Button) mRootView.findViewById(R.id.stop_btn);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerFragment.this.onStopButtonClick();
            }
        });

        return mRootView;
    }

    protected void onSelectItem(int index) {
        mSelectedItemPosition = index;
    }

    @SuppressWarnings("deprecation")
    private SoundPool createSoundPool() {
        // Should be using SoundPool.Builder instead, but it's only for API
        // level 21 and we are using level 14 for this demo.
        return new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    private ListAdapter createFileListAdapter() {
        File filesDir = mActv.getFilesDir();
        String[] fileList = filesDir.list();
        return new ArrayAdapter<>(mActv, android.R.layout.simple_list_item_single_choice, fileList);
    }

    private void onPlayButtonClick() {
        String item = String.format("%s/%s",
                mActv.getFilesDir().getAbsolutePath(),
                mFileList.getItemAtPosition(mSelectedItemPosition));

        if (mSelectedItemPosition == -1) {
            showMsg(R.string.no_item_selected);
        } else {
            Integer soundId = mLoadedSounds.get(item);

            if (soundId == null) {
                soundId = mSoundPool.load(item, 1);
                mLoadedSounds.put(item, soundId);
            }

            mStreamId = mSoundPool.play(
                    soundId, // Sound to play
                    1.0f, // Left volume (from 0 to 1)
                    1.0f, // Right volume (from 0 to 1)
                    0, // Priority (0 is the lowest)
                    0, // Loop? (0 = no loop; -1 = loop forever
                    1.0f); // Playback rate (0.5 is the lowest; 1.0 is normal; 2.0 is the highest)

            if (mStreamId != 0) { // If successful to play the sound
                // Yay!! Commemorate!! =D
                showMsg(R.string.playing);
            } else { // If failed to play the sound
                showMsg(R.string.failed);
            }
        }
    }

    private void onStopButtonClick() {
        mSoundPool.stop(mStreamId);
    }

    private void showMsg(int resource) {
        toast(getString(resource));
    }

    private void toast(String string) {
        Toast.makeText(mActv, string, Toast.LENGTH_SHORT).show();

//        new AlertDialog.Builder(mActv)
//                .setMessage(string).setTitle(R.string.app_name)
//                .show();
    }

}
