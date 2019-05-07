package shomazzapp.com.homecontorl.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import shomazzapp.com.homecontorl.R;
import shomazzapp.com.homecontorl.common.FController;
import shomazzapp.com.homecontorl.common.Screens;
import shomazzapp.com.homecontorl.mvp.view.RegFieldsView;

public class MainActivity extends MvpAppCompatActivity implements FController {

    private static final int LAYOUT = R.layout.activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        if (savedInstanceState == null)
            addFragment(createFragment(Screens.AUTH), true);
    }

    @Override
    public void onBackPressed() {
        //TODO: remove Toast
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Toast.makeText(this, "Count before onBack = " + count, Toast.LENGTH_LONG).show();
        if (count == 1) getSupportFragmentManager().popBackStack();
        super.onBackPressed();
    }

    @Override
    public void addFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        FragmentTransaction t = getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragments_frame, fragment);
        if (addToBackStack) t.addToBackStack(null);
        t.commit();
    }

    @Override
    public Fragment createFragment(Screens screens) {
        switch (screens) {
            case AUTH:
                return AuthFragment.newInstance();
            case REGISTRATION_FIELDS:
                return RegFieldsFragment.newInstance();
            case REGISTRATION_CAMERA:
                //return
            case DEVICES_LIST:
                //return
            default:
                throw new RuntimeException("Unknown screen key !");
        }
    }
}
