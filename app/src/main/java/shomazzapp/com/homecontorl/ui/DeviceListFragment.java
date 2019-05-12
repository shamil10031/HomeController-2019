package shomazzapp.com.homecontorl.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;

import shomazzapp.com.homecontorl.R;
import shomazzapp.com.homecontorl.common.ItemDecoration;
import shomazzapp.com.homecontorl.mvp.model.Device;
import shomazzapp.com.homecontorl.mvp.presnter.DeviceListPresenter;
import shomazzapp.com.homecontorl.mvp.view.DeviceListView;
import shomazzapp.com.homecontorl.ui.adapter.DevicesAdapter;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class DeviceListFragment extends MvpAppCompatFragment implements DeviceListView {

    @InjectPresenter
    DeviceListPresenter presenter;

    private RecyclerView recycler;
    private DevicesAdapter adapter;

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
    public void changeDeviceToggle(int id) {
        adapter.changeDeviceToggle(id);
    }

    private void init(View view) {
        recycler = (RecyclerView) view.findViewById(R.id.recycler_devices);
        adapter = new DevicesAdapter(getActivity(), presenter);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecoration(getContext(), R.dimen.recycler_item_margin));

        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
            recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        else
            recycler.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setDevices(ArrayList<Device> devices) {
        adapter.replaceItems(devices);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recycler = null;
    }
}
