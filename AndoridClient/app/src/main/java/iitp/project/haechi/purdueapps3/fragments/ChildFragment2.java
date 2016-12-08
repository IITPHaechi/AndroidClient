package iitp.project.haechi.purdueapps3.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iitp.project.haechi.purdueapps3.R;

/**
 * Created by dnay2 on 2016-12-07.
 */

public class ChildFragment2 extends ChildAbstract {

    private String URL = "http://172.24.2.139:9090/stream";
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState, R.layout.child_webview);
        Log.d("test", "child2");
        return view;
    }

    @Override
    public void loadVideo() {
        super.loadVideo(URL);
    }
}
