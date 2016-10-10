package com.nhancv.mvptestlib.model;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;

import bolts.CancellationTokenSource;
import bolts.Continuation;
import bolts.Task;

/**
 * Created by nhancao on 9/26/16.
 */

public class GreetingGenerator {
    private CancellationTokenSource cts;
    private String baseText;
    private GreetingTaskListener listener;

    public GreetingGenerator(String baseText, GreetingTaskListener listener) {
        this.baseText = baseText;
        this.listener = listener;
        this.cts = new CancellationTokenSource();
    }

    public void cancelTask() {
        cts.cancel();
    }

    public void getNum() {
        Task.callInBackground(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(2000);
                if (cts.isCancellationRequested()) {
                    throw new CancellationException();
                }
                return (int) (Math.random() * 100);
            }
        }).continueWith(new Continuation<Integer, Void>() {
            @Override
            public Void then(Task<Integer> task) {
                if (!task.isFaulted() && !task.isCancelled()) {
                    listener.onGreetingGenerated(baseText + " " + task.getResult());
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    // Callback - Listener
    public interface GreetingTaskListener {
        void onGreetingGenerated(String greetingText);
    }

}
