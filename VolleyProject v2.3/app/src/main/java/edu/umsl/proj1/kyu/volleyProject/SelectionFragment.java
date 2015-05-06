package edu.umsl.proj1.kyu.volleyProject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.facebook.Session;
import com.facebook.Response;
import com.facebook.Request;

//Facebook requires developers to use its own library to request the information.
//import com.android.volley.Request;
//import com.android.volley.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectionFragment extends Fragment {
    private static final String TAG = "SelectionFragment";
    private ProfilePictureView profilePictureView;
    private TextView userNameView;

    public SelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.selection,container, false);


        //Assign the variables to the related layout elements
        profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(false);

        // Find the user's name view
        userNameView = (TextView) view.findViewById(R.id.selection_user_name);

        // Whenever the fragment view is set up and a user session is open, get the user's data.
        // Check for an open session
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            // Get the user's data
            makeMeRequest(session);
        }
        return view;
    }


    // API call to get the user's data and populate the views
    private void makeMeRequest(final Session session) {
        Request request = Request.newMeRequest(session,new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                // Set the id for the ProfilePictureView
                                // view that in turn displays the profile picture.
                                profilePictureView.setProfileId(user.getId());
                                // Set the Textview's text to the user's name.
                                userNameView.setText(user.getName());
                            }
                        }
                        if (response.getError() != null) {

                        }
                    }
                });
        request.executeAsync();
    }

    // Respond to session changes and call the makeMeRequest() method if the session's open
    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            // Get the user's data.
            makeMeRequest(session);
        }
    }
    // Response to session state changes and call the onSessionStateChange() method
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    // To initialize the UiLifecycleHelper object and call it's onCreate() method
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    // To make a new permissions request.
    private static final int REAUTH_ACTIVITY_CODE = 100;

    // Call the corresponding UiLifecycleHelper method if the REAUTH_ACTIVITY_CODE request code is passed in:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REAUTH_ACTIVITY_CODE) {
            uiHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    // The fragment lifecycle methods call the relevant methods in the UiLifecycleHelper class
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        uiHelper.onSaveInstanceState(bundle);
    }
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
}
