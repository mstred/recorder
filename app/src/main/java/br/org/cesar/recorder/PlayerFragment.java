package br.org.cesar.recorder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
	
	private View mRootView;
	private ListView mFilesListView;
	private SoundPool mSoundPool;
	private Map<String, Integer> mLoadedSounds = new HashMap<String, Integer>();
	private int mStreamId;
	private int mSelectedItemPosition = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mSoundPool = this.createSoundPool();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.mRootView = inflater.inflate(R.layout.fragment_player, container, false);
		
		this.mFilesListView = (ListView) this.mRootView.findViewById(R.id.PlayerFragment_FileList);
		mFilesListView.setAdapter(this.createFileListAdapter());
		mFilesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				PlayerFragment.this.onSelectItem(index);
			}
		});
		
		Button playButton = (Button) this.mRootView.findViewById(R.id.PlayerFragment_PlayButton);
		playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PlayerFragment.this.onPlayButtonClick();
			}
		});
		
		Button stopButton = (Button) this.mRootView.findViewById(R.id.PlayerFragment_StopButton);
		stopButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PlayerFragment.this.onStopButtonClick();
			}
		});
		
		return this.mRootView;
	}
	
	protected void onSelectItem(int index) {
		this.mSelectedItemPosition = index;
	}

	@SuppressWarnings("deprecation")
	private SoundPool createSoundPool() {
		// OBS: Should be using SoundPool.Builder instead, but it's only for API
		// level 21 and we are using level 14 for this demo. 
		return new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
	}

	private ListAdapter createFileListAdapter() {
		File filesDir = this.getActivity().getFilesDir();
		String[] filesList = filesDir.list();
		
		return new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_single_choice, filesList);
	}
	
	private void onPlayButtonClick() {
		String item = 
				this.getActivity().getFilesDir().getAbsolutePath() +
				"/" +
				this.mFilesListView.getItemAtPosition(this.mSelectedItemPosition);
		
		if (this.mSelectedItemPosition == -1) {
			this.showDialog(R.string.PlayerFragment_NoItemSelected);
		} else {
			Integer soundId = this.mLoadedSounds.get(item);
			
			if (soundId == null) {
				soundId = this.mSoundPool.load(item, 1);
				this.mLoadedSounds.put(item, soundId);
			}
			
			this.mStreamId = this.mSoundPool.play(
					soundId, // Sound to play
					1.0f, // Left volume (from 0 to 1)
					1.0f, // Right volume (from 0 to 1)
					0, // Priority (0 is the lowest)
					0, // Loop? (0 = no loop; -1 = loop forever
					1.0f); // Playback rate (0.5 is the lowest; 1.0 is normal; 2.0 is the highest)
			
			if (this.mStreamId != 0) { // If successful to play the sound
				// Yay!! Commemorate!! =D
				this.showDialog("Playing the sound.");
			} else { // If failed to play the sound
				this.showDialog(R.string.PlayerFragment_FailedToPlayTheSound);
			}
		}
	}

	private void onStopButtonClick() {
		this.mSoundPool.stop(this.mStreamId);
	}

	private void showDialog(int resource) {
		this.showDialog(getString(resource));
	}

	private void showDialog(String string) {
		// TODO Should display a dialog instead of a Toast.
		Toast.makeText(this.getActivity(), string, Toast.LENGTH_SHORT).show();
	}

}
