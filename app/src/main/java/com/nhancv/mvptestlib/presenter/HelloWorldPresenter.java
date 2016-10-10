package com.nhancv.mvptestlib.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.nhancv.mvptestlib.model.GreetingGenerator;
import com.nhancv.mvptestlib.view.HelloWorldView;

/**
 * Created by nhancao on 9/26/16.
 */

public class HelloWorldPresenter extends MvpBasePresenter<HelloWorldView> {

    private GreetingGenerator greetingTask;

    public void cancelTaskIfRunning() {
        if (greetingTask != null) {
            greetingTask.cancelTask();
        }
    }

    public void greetHello() {
        cancelTaskIfRunning();
        greetingTask = new GreetingGenerator("Hello", new GreetingGenerator.GreetingTaskListener() {
            @Override
            public void onGreetingGenerated(String greetingText) {
                if (isViewAttached()) {
                    getView().showHello(greetingText);
                }
            }
        });
        greetingTask.getNum();
    }

    public void greetGoodbye() {
        cancelTaskIfRunning();
        greetingTask = new GreetingGenerator("Goodbye", new GreetingGenerator.GreetingTaskListener() {
            @Override
            public void onGreetingGenerated(String greetingText) {
                if (isViewAttached()) {
                    getView().showGoodbye(greetingText);
                }
            }
        });
        greetingTask.getNum();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            cancelTaskIfRunning();
        }
    }
}
