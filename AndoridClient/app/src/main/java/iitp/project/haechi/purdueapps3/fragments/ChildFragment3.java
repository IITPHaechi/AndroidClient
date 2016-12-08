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

public class ChildFragment3 extends ChildAbstract {

    private String URL = "http://172.24.1.1:9090/stream";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState, R.layout.child_webview);
        Log.d("test", "child3");
        return view;
    }

    @Override
    public void loadVideo() {
        super.loadVideo(URL);
    }
}
