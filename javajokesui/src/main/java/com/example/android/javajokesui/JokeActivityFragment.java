package com.example.android.javajokesui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class JokeActivityFragment extends Fragment {
    public static final String LOG_TAG = "JokeActivityFragment";
    static View        mRoot   = null;
    static ProgressBar spinner = null;

    public JokeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_joke, container, false);
        Intent intent = getActivity().getIntent();
        String joke = intent.getStringExtra(JokeActivity.JOKE_KEY);

        // setup spinner
        spinner = (ProgressBar) mRoot.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        Log.d(LOG_TAG, "onCreateView initial joke text = " + joke);
        SetJokeText(joke);

        return mRoot;
    }

    static public void SetJokeText(String joke) {
        Log.d(LOG_TAG, "SetJokeText joke = " + joke);

        if (mRoot != null) {
            TextView jokeTextView = (TextView) mRoot.findViewById(R.id.joke_textview);

            if (joke != null && joke.length() != 0) {
                spinner.setVisibility(View.GONE);
                jokeTextView.setText(joke);
            }
        }
    }

    public static class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP   = "javajokes.intent.action.MESSAGE_RECEIVED";
        public static final String LOG_TAG = "ResponseReceiver";
        public static final String JAVA_JOKE_KEY = "JavaJokeKey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(LOG_TAG, "onReceive action = " + action);

            if (action.equals(ACTION_RESP)) {
                String javaJoke = intent.getStringExtra(JAVA_JOKE_KEY);
                Log.d(LOG_TAG, "onReceive javaJoke = " + javaJoke);

                SetJokeText(javaJoke);
            }
        }
    }

}
