package edu.umsl.proj.kyu.canonhw;

import android.app.Fragment;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CannonGameFragment extends Fragment {
    private CannonView cannonView; // custom view to display the game

    // called when Fragment's view needs to be created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e("Banana", "CannonGameFragment.onCreateView");
        View view = inflater.inflate(R.layout.fragment_cannon_game, container, false); // inflate cannon game layout into view

        //Now This fragment can control the function in CannonView.java
        cannonView = (CannonView) view.findViewById(R.id.cannonView);   // call cannonView fragment
        return view;
    }

    // set up volume control once Activity is created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e("Banana", "CannonGameFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        // allow volume keys to set game volume
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    // when MainActivity is paused, CannonGameFragment terminates the game
    @Override
    public void onPause() {
        Log.e("Banana", "CannonGameFragment.onPause");
        super.onPause();
        cannonView.stopGame(); // terminates the game
    }

    // when MainActivity is paused, CannonGameFragment releases resources
    @Override
    public void onDestroy() {
        Log.e("Banana", "CannonGameFragment.onDestory");
        super.onDestroy();
        cannonView.releaseResources();
    }
} // end class CannonGameFragment