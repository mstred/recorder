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
    private int mStreamId;
    private int mSelectedItemPosition = -1;

    private Button mPlayBtn, mStopBtn;

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

        mPlayBtn = (Button) mRootView.findViewById(R.id.play_btn);
        mPlayBtn.setEnabled(false);
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerFragment.this.onPlayButtonClick();
            }
        });

        mStopBtn = (Button) mRootView.findViewById(R.id.stop_btn);
        mStopBtn.setEnabled(false);
        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerFragment.this.onStopButtonClick();
            }
        });

        return mRootView;
    }

    protected void onSelectItem(int index) {
        boolean enabled = index != -1;
        mSelectedItemPosition = index;
        mPlayBtn.setEnabled(enabled);
        mStopBtn.setEnabled(enabled);
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

        mSoundPool.load(item, 1);

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

                mStreamId = soundPool.play(
                        sampleId, // Sound to play
                        1.0f, // Left volume (from 0 to 1)
                        1.0f, // Right volume (from 0 to 1)
                        0, // Priority (0 is the lowest)
                        0, // Loop? (0 = no loop; -1 = loop forever)
                        1.0f); // Playback rate (0.5 is the lowest; 1.0 is normal; 2.0 is the highest)

                showMsg(status == 0 ? R.string.playing : R.string.failed);
            }
        });
    }

    private void onStopButtonClick() {
        mSoundPool.stop(mStreamId);
    }

    private void showMsg(int resource) {
        toast(getString(resource));
    }

    private void toast(String string) {
        Toast.makeText(mActv, string, Toast.LENGTH_SHORT).show();
    }
}
