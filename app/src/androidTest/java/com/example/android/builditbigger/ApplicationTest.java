package com.example.android.builditbigger;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.util.concurrent.CountDownLatch;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public static final String LOG_TAG = "ApplicationTest";
    String mJavaJoke = null;
    Exception mError = null;
    CountDownLatch signal = null;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        signal = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        signal.countDown();
    }

    public void testJavaJokeTask() throws InterruptedException {

        EndpointsAsyncTask task = new EndpointsAsyncTask();
        task.setListener(new EndpointsAsyncTask.EndpointAsyncTaskListener() {
            @Override
            public void onComplete(String jsonString, Exception e) {
                mJavaJoke = jsonString;
                mError = e;
                signal.countDown();
            }
        }).execute(new Pair<Context, String>(getContext(), "JavaJokes"));

        signal.await();    // wait for async task to complete

        Log.d(LOG_TAG, "testJavaJokeTask response mJavaJoke = " + mJavaJoke);
        Log.d(LOG_TAG, "testJavaJokeTask response mError = " + mError);

        assertNull(mError);
        assertFalse(TextUtils.isEmpty(mJavaJoke));
    }
}
