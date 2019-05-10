package shomazzapp.com.homecontorl.common.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import shomazzapp.com.homecontorl.common.Screens;

public interface FController {

    void addFragment(@NonNull Fragment fragment, boolean addToBackStack);

    Fragment createFragment(Screens screens);

    @Nullable
    Fragment getFragmentByTag(String tag);
}
