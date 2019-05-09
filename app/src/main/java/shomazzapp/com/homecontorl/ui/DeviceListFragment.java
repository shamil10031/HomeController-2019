package shomazzapp.com.homecontorl.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import shomazzapp.com.homecontorl.R;
import shomazzapp.com.homecontorl.mvp.presnter.DeviceListPresenter;
import shomazzapp.com.homecontorl.mvp.view.DeviceListView;

public class DeviceListFragment extends MvpAppCompatFragment implements DeviceListView {

    @InjectPresenter
    DeviceListPresenter presenter;

    private TextView textView;

    private static final int LAYOUT = R.layout.fragment_device_list;

    public static DeviceListFragment newInstance() {
        return new DeviceListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setContext(getActivity());
        presenter.requestDiveceList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        init(view);
        return view;
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void init(View view) {
        textView = (TextView) view.findViewById(R.id.tv_device_list);
    }

    @Override
    public void setText(String str) {
        textView.setText(str);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        textView = null;
    }
}
