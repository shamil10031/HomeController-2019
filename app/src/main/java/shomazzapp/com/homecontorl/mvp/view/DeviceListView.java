package shomazzapp.com.homecontorl.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.ArrayList;

import shomazzapp.com.homecontorl.mvp.model.Device;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface DeviceListView extends MvpView {

    void showMsg(String msg);

    void setDevices(ArrayList <Device> devices);

}
