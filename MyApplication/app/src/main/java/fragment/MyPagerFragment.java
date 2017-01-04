package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import base.BasePager;

/**
 * Created by public1 on 2017/1/4.
 */

public class MyPagerFragment extends Fragment {

    private final BasePager basePager;

    public MyPagerFragment(BasePager basePager) {
        this.basePager = basePager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (basePager != null) {
            return basePager.rootView;
        }
        return null;

    }
}
