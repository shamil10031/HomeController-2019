package shomazzapp.com.homecontorl.mvp.presnter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.lang.ref.WeakReference;

import shomazzapp.com.homecontorl.common.ClientListener;
import shomazzapp.com.homecontorl.common.PreferencesHelper;
import shomazzapp.com.homecontorl.common.Screens;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.mvp.model.Request;
import shomazzapp.com.homecontorl.mvp.model.Response;
import shomazzapp.com.homecontorl.mvp.view.DeviceListView;

@InjectViewState
public class DeviceListPresenter extends MvpPresenter<DeviceListView> implements ClientListener {

    private Client client;
    private PreferencesHelper prefHelper;
    private WeakReference<Context> context;

    public DeviceListPresenter() {
        super();
        prefHelper = new PreferencesHelper();
        client = new Client(this, Client.HOST, Client.PORT);
    }

    public void setContext(Context context){
        this.context = new WeakReference<>(context);
    }

    @Override
    public void runOnUi(@NonNull Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    @Override
    public void reciveResponse(Response response) {
        runOnUi(() -> {
            //getViewState().finishAuth();
            switch (response.getResponceCode()) {
                case Response.SUCCESS:
                    getViewState().setText(response.getMessage());
                    return;
                case Response.TIMEOUT_WAITING:
                    getViewState().showMsg("Connection timeout");
                    break;
                case Response.CONNECTION_ERROR:
                    getViewState().showMsg("Connection error");
                    break;
                case Response.NO_ROUTE_TO_HOST:
                    getViewState().showMsg("Server not founded!");
                    break;
                case Response.ERROR:
                    getViewState().showMsg("Incorrect login/password");
                    break;
                default:
                    getViewState().showMsg(response.getMessage());
//                  fController.clearBackStack();
//                  fController.addFragment(fController.createFragment(Screens.DEVICES_LIST), true);
                    break;
            }
        });
    }

    public void requestDiveceList(){
        //getViewState().startLoading();
        client.sendRequestForResponse(Request.
                createAviableDevicesRequest(prefHelper.getString(
                        PreferencesHelper.KEY_LOGIN, context.get())));
    }

}
