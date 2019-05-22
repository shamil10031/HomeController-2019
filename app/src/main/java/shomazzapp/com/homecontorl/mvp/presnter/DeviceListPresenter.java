package shomazzapp.com.homecontorl.mvp.presnter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.lang.ref.WeakReference;

import shomazzapp.com.homecontorl.common.PreferencesHelper;
import shomazzapp.com.homecontorl.common.interfaces.ClientListener;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.mvp.model.Device;
import shomazzapp.com.homecontorl.mvp.model.Request;
import shomazzapp.com.homecontorl.mvp.model.Response;
import shomazzapp.com.homecontorl.mvp.view.DeviceListView;

@InjectViewState
public class DeviceListPresenter extends MvpPresenter<DeviceListView> implements ClientListener {

    private Client client;
    private PreferencesHelper prefHelper;
    private WeakReference<Context> context;
    private int toggleDeviceId;
    private int requestCode;

    private static final String testJson = "[" +
            "{id:123, item_name: \"Утюг\", item_description: \"Хороший утюг, очень горячий\", item_photo: \"https://img.mvideo.ru/Pdb/20039195b.jpg\", is_item_on: \"true\"}, " +
            "{id:123, item_name: \"Утюг\", item_description: \"Хороший утюг, очень горячий\", item_photo: \"https://img.mvideo.ru/Pdb/20039195b.jpg\", is_item_on: \"false\"}, " +
            "{id:123, item_name: \"Утюг\", item_description: \"Хороший утюг, очень горячий\", item_photo: \"https://img.mvideo.ru/Pdb/20039195b.jpg\", is_item_on: \"false\"}, " +
            "{id:123, item_name: \"Утюг\", item_description: \"Хороший утюг, очень горячий\", item_photo: \"https://img.mvideo.ru/Pdb/20039195b.jpg\", is_item_on: \"true\"} " +
            "]";


    public DeviceListPresenter() {
        super();
        prefHelper = new PreferencesHelper();
        client = new Client(this, Client.HOST, Client.PORT);
    }

    public void setContext(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    public void runOnUi(@NonNull Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public Context getContext(){
        return context.get();
    }

    @Override
    public void reciveResponse(Response response) {
        runOnUi(() -> {
            //getViewState().setDevices(Device.getDevicesFromJson(testJson));
            //getViewState().finishAuth();
            switch (response.getResponceCode()) {
                case Response.SUCCESS:
                    if (requestCode == Request.AVALIABLE_DEVICES) {
                        getViewState().setDevices(Device.getDevicesFromJson(response.getMessage()));
                        requestHomeInfo();
                    }
                    else if (requestCode == Request.TOGLE_DEVICE)
                        getViewState().changeDeviceToggle(toggleDeviceId);
                    else if (requestCode == Request.HOME_INFO)
                        getViewState().showMsg("Temperature: " + response.getMessage().split(" ")[0]
                        + ", Humidity: " + response.getMessage().split(" ")[1]);
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
                    getViewState().showMsg("Error!");
                    break;
                default:
                    getViewState().showMsg(response.getMessage());
                    break;
            }
        });
    }

    public void requestHomeInfo(){
        requestCode = Request.HOME_INFO;
        client.sendRequestForResponse(Request.createHomeInfoRequest(),true);
    }

    public void toggleDevice(int id) {
        //getViewState().showProgressBar();
        requestCode = Request.TOGLE_DEVICE;
        toggleDeviceId = id;
        client.sendRequestForResponse(Request.createToggleDevice(id), true);
    }

    public void requestDiveceList() {
        //reciveResponse(new Response(200, testJson));
        //getViewState().showProgressBar();
        requestCode = Request.AVALIABLE_DEVICES;
        client.sendRequestForResponse(Request.
                        createAviableDevicesRequest(
                                //"test"),
                                prefHelper.getString(PreferencesHelper.KEY_LOGIN, context.get())),
                true);
    }

}
