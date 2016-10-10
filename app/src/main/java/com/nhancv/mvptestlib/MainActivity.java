package com.nhancv.mvptestlib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ListView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.nhancv.mvptestlib.presenter.HelloWorldPresenter;
import com.nhancv.mvptestlib.view.HelloWorldView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends MvpViewStateActivity<HelloWorldView, HelloWorldPresenter> implements HelloWorldView {

    @ViewById(R.id.activity_main_tv_greeting)
    TextView activity_main_tv_greeting;
    @ViewById(R.id.activity_main_lv_user_repo)
    ListView activity_main_lv_user_repo;

    @NonNull
    @Override
    public HelloWorldPresenter createPresenter() {
        return new HelloWorldPresenter();
    }

    @Click(R.id.activity_main_btn_get_hello)
    public void activity_main_btn_get_hello_on_click() {
        presenter.greetHello();
    }

    @Click(R.id.activity_main_btn_get_goodbye)
    public void activity_main_btn_get_goodbye_on_click() {
        presenter.greetGoodbye();
    }

    @Override
    public void showHello(String greetingText) {
        MyCustomViewState vs = ((MyCustomViewState) viewState);
        vs.setHello(true);
        vs.setData(greetingText);
        activity_main_tv_greeting.setTextColor(Color.RED);
        activity_main_tv_greeting.setText(greetingText);
    }

    @Override
    public void showGoodbye(String greetingText) {
        MyCustomViewState vs = ((MyCustomViewState) viewState);
        vs.setHello(false);
        vs.setData(greetingText);
        activity_main_tv_greeting.setTextColor(Color.BLUE);
        activity_main_tv_greeting.setText(greetingText);
    }

    @Override
    public ViewState<HelloWorldView> createViewState() {
        return new MyCustomViewState();
    }

    // Will be called when no view state exists yet,
    // which is the case the first time MyCustomActivity starts
    @Override
    public void onNewViewStateInstance() {

    }

    public class MyCustomViewState implements RestorableViewState<HelloWorldView> {

        private final String KEY_STATE = "MyCustomViewState-flag";
        private final String KEY_DATA = "MyCustomViewState-data";

        public boolean isHello = true; // if false, then show B
        public String data; // Can be A or B

        @Override
        public void saveInstanceState(@NonNull Bundle out) {
            out.putBoolean(KEY_STATE, isHello);
            out.putString(KEY_DATA, data);
        }

        @Override
        public RestorableViewState<HelloWorldView> restoreInstanceState(Bundle in) {
            if (in == null) {
                return null;
            }

            isHello = in.getBoolean(KEY_STATE, true);
            data = in.getString(KEY_DATA);
            return this;

        }

        @Override
        public void apply(HelloWorldView view, boolean retained) {
            if (isHello) {
                view.showHello(data);
            } else {
                view.showGoodbye(data);
            }
        }

        /**
         * @param a true if showing a, false if showing b
         */
        public void setHello(boolean a) {
            this.isHello = a;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
