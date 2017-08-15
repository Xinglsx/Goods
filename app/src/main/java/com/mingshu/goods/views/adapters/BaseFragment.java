package com.mingshu.goods.views.adapters;

import android.app.Fragment;

/**
 * Created by Lisx on 2017-07-03.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import winning.framework.BaseApplication;
import winning.framework.network.NetworkEngine;
import winning.framework.network.VolleyEngine;

public class BaseFragment extends Fragment {
    private NetworkEngine networkEngine;
    protected Activity activity;

    public BaseFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parseExtras(this.getArguments());
    }

    protected void parseExtras(Bundle extras) {
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public NetworkEngine getNetworkEngine() {
        if(this.networkEngine == null) {
            this.networkEngine = new VolleyEngine(this.activity);
        }

        return this.networkEngine;
    }

    public Handler getMainHandler() {
        BaseApplication baseApplication = (BaseApplication)this.activity.getApplicationContext();
        return baseApplication.getHandler();
    }
}
