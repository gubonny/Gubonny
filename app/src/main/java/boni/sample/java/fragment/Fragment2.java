package boni.sample.java.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import boni.sample.java.R;

/**
 * Created by gbkim on 2018-01-02.
 */

public class Fragment2 extends Fragment {

    public Fragment2() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fragment2, container, false);
    }
}
