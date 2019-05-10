package shomazzapp.com.homecontorl.mvp.presnter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.lang.ref.WeakReference;

import shomazzapp.com.homecontorl.common.interfaces.ClientListener;
import shomazzapp.com.homecontorl.common.interfaces.FController;
import shomazzapp.com.homecontorl.common.PreferencesHelper;
import shomazzapp.com.homecontorl.common.Screens;
import shomazzapp.com.homecontorl.common.interfaces.ViewPagerController;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.mvp.model.Response;
import shomazzapp.com.homecontorl.mvp.view.RegCameraView;

@InjectViewState
public class RegCameraPresenter extends MvpPresenter<RegCameraView> implements ClientListener {

    private ViewPagerController pagerController;
    private FController fController;
    private Client client;
    private WeakReference<Activity> context;
    private PreferencesHelper prefHelper;

    public RegCameraPresenter() {
        super();
        client = new Client(this, Client.HOST, Client.PORT);
        prefHelper = new PreferencesHelper();
    }

    public void setFragmentController(FController fController) {
        this.fController = fController;
    }

    public void runOnUi(@NonNull Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void setContext(Activity context) {
        this.context = new WeakReference<>(context);
    }

    public void setViewPagerController(ViewPagerController pagerController) {
        this.pagerController = pagerController;
    }

    @Override
    public void reciveResponse(Response response) {
        runOnUi(() -> {
            getViewState().finishLoading();
            switch (response.getResponceCode()) {
                case Response.SUCCESS:
                    fController.addFragment(fController
                            .createFragment(Screens.DEVICES_LIST), true);
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
                    getViewState().showMsg("Can't register new User. Try later");
                    break;
                default:
                    getViewState().showMsg(response.getMessage());
//                  fController.clearBackStack();
//                  fController.addFragment(fController.createFragment(Screens.DEVICES_LIST), true);
                    break;
            }
        });
    }

    public void requestPermissionIfNeed(Activity context){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.CAMERA}, 0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

}
