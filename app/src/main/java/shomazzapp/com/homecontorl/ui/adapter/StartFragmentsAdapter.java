package shomazzapp.com.homecontorl.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;

import shomazzapp.com.homecontorl.common.Screens;
import shomazzapp.com.homecontorl.common.interfaces.FController;

public class StartFragmentsAdapter extends FragmentPagerAdapter {

    private static final Screens[] screens = {Screens.AUTH,
            Screens.REGISTRATION_FIELDS, Screens.REGISTRATION_CAMERA};
    private FController controller;
    private Fragment[] fragments;

    public StartFragmentsAdapter(FragmentManager fm, FController controller) {
        super(fm);
        this.controller = controller;
        createFragments();
    }

    public static int getScreenPosition(Screens screen) {
        return Arrays.asList(screens).indexOf(screen);
    }

    private void createFragments() {
        fragments = new Fragment[screens.length];
        for (int i = 0; i < screens.length; i++)
            fragments[i] = controller.createFragment(screens[i]);
    }

    @Override
    public Fragment getItem(int position) {
        return position < fragments.length ?
                fragments[position] : null;
    }

    @Override
    public int getCount() {
        return screens.length;
    }
}