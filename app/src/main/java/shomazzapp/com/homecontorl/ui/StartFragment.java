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
import shomazzapp.com.homecontorl.common.interfaces.FController;
import shomazzapp.com.homecontorl.common.Screens;
import shomazzapp.com.homecontorl.ui.adapter.StartFragmentsAdapter;
import shomazzapp.com.homecontorl.common.interfaces.ViewPagerController;
import shomazzapp.com.homecontorl.mvp.presnter.StartPresenter;
import shomazzapp.com.homecontorl.mvp.view.StartView;

public class StartFragment extends MvpAppCompatFragment implements StartView, ViewPagerController {

    @InjectPresenter
    StartPresenter presenter;

    private ViewPager viewPager;

    private static final int LAYOUT = R.layout.fragment_start;

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setFragmentController((FController) getActivity());
    }

    @Override
    public void openFragment(Screens screen) {
        viewPager.setCurrentItem(StartFragmentsAdapter.getScreenPosition(screen), true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.start_view_pager);
        viewPager.setAdapter(new StartFragmentsAdapter(getFragmentManager(),(FController) getActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPager = null;
    }
}
