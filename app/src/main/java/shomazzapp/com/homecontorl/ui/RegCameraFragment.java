package shomazzapp.com.homecontorl.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import shomazzapp.com.homecontorl.R;
import shomazzapp.com.homecontorl.common.FController;
import shomazzapp.com.homecontorl.common.Screens;
import shomazzapp.com.homecontorl.common.StartFragmentsAdapter;
import shomazzapp.com.homecontorl.common.ViewPagerController;
import shomazzapp.com.homecontorl.mvp.presnter.RegCameraPresenter;
import shomazzapp.com.homecontorl.mvp.presnter.StartPresenter;
import shomazzapp.com.homecontorl.mvp.view.RegCameraView;
import shomazzapp.com.homecontorl.mvp.view.StartView;

public class RegCameraFragment extends MvpAppCompatFragment implements RegCameraView  {

    @InjectPresenter
    RegCameraPresenter presenter;


    private static final int LAYOUT = R.layout.fragment_start;

    public static RegCameraFragment newInstance() {
        return new RegCameraFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setFragmentController((FController) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
