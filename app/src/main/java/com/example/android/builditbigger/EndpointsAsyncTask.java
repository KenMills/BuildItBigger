package com.example.android.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.example.android.javajokesui.JokeActivityFragment;
import com.example.kenm.builditbigger.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by kenm on 10/7/2015.
 */
class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    public static final String LOG_TAG = "EndpointsAsyncTask";
    private static MyApi myApiService = null;
    private Context context;
    String mLastJoke = "";
    private EndpointAsyncTaskListener mListener = null;
    private Exception mError = null;

    public static interface EndpointAsyncTaskListener {
        public void onComplete(String javaJokeString, Exception e);
    }

    public EndpointsAsyncTask setListener(EndpointAsyncTaskListener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
//Android Studio emulator
//                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
//Genymotion emulator
                    .setRootUrl("http://10.0.3.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0].first;
        String name = params[0].second;

        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            mError = e;
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        mLastJoke = result;
//        Toast.makeText(context, result, Toast.LENGTH_LONG).show();

        sendResponse(result);

        if (this.mListener != null)
            this.mListener.onComplete(result, mError);
    }

    public String getLastJoke() {
        return mLastJoke;
    }

    private void sendResponse(String joke) {
        Log.d(LOG_TAG, "sendResponse joke = " +joke);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(JokeActivityFragment.ResponseReceiver.ACTION_RESP);
        broadcastIntent.putExtra(JokeActivityFragment.ResponseReceiver.JAVA_JOKE_KEY, joke);

        context.sendBroadcast(broadcastIntent);
    }

}