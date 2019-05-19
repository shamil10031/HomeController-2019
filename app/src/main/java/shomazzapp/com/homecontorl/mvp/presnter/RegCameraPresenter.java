package shomazzapp.com.homecontorl.mvp.presnter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.io.IOException;
import java.lang.ref.WeakReference;

import shomazzapp.com.homecontorl.common.PreferencesHelper;
import shomazzapp.com.homecontorl.common.Screens;
import shomazzapp.com.homecontorl.common.interfaces.ClientListener;
import shomazzapp.com.homecontorl.common.interfaces.FController;
import shomazzapp.com.homecontorl.common.interfaces.ViewPagerController;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.mvp.model.Request;
import shomazzapp.com.homecontorl.mvp.model.Response;
import shomazzapp.com.homecontorl.mvp.view.RegCameraView;

@InjectViewState
public class RegCameraPresenter extends MvpPresenter<RegCameraView> implements ClientListener {

    private ViewPagerController pagerController;
    private FController fController;
    private Client client;
    private WeakReference<Activity> context;
    private PreferencesHelper prefHelper;
    public int photosCount;
    private int photosSended;
    private int UdpPort;

    private static final int delay = 100;
    private long lastTimeFrameShoted = 0;

    public RegCameraPresenter() {
        super();
        client = new Client(this, Client.HOST, Client.PORT);
        prefHelper = new PreferencesHelper();
    }

    public void onStart() {
        getViewState().showProgressBar();
        getViewState().hideHorProgressBar();
        photosSended = 0;
        runOnUi(() -> getViewState().showMsg("Connecting..."));
        client.sendRequestForResponse(Request.createAddFhotosRequest(
                prefHelper.getString(PreferencesHelper.KEY_LOGIN, context.get())),
                false);
    }

    public static Bitmap rotate(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //public void onPictureTaken(Bitmap bitmap, boolean isLast) {
    public void onPictureTaken(byte[] bitmap, boolean isLast) {
//        Log.d("TAG", "Checking for delay. " + System.currentTimeMillis() + " - " + lastTimeFrameShoted);
        try {
            Thread.sleep(50);
        } catch (Exception e){
            e.printStackTrace();
        }
        if (System.currentTimeMillis() - lastTimeFrameShoted >= delay) {
            Log.d("TAG", "Prepared! Sending...");
            if (!isLast)
                try {
                    //client.sendBitmap(rotate(bitmap), false).join();
                    client.sendPicBytes(bitmap, false).join();
                    photosSended++;
                    lastTimeFrameShoted = System.currentTimeMillis();
                    if (photosSended + 1 == photosCount) {
                        getViewState().takePicture(true);
                    }
                    else {
                        getViewState().takePicture(false);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            else {
                try {
                    //client.sendBitmap(rotate(bitmap), false).join();
                    client.sendPicBytes(bitmap, false).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getViewState().hidePic();
                getViewState().hideHorProgressBar();
                getViewState().showProgressBar();
                getViewState().showMsg("Face registered, now relax :) It's almost done..");
                Thread thread = new Thread(() -> {
                    try {
                        client.connectSocketIfNeed();
                        reciveResponse(client.getResponse(client.getSocket()));
                    } catch (IOException e) {
                        reciveResponse(new Response(
                                Response.CONNECTION_ERROR, null));
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        } else {
            if (photosSended + 1 == photosCount)
                getViewState().takePicture(true);
            else
                getViewState().takePicture(false);
        }
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
        Log.d("TAG", "Response :" + response.toString());
        runOnUi(() -> {
            switch (response.getResponceCode()) {
                case Response.SUCCESS:
                    getViewState().hideProgressBar();
                    getViewState().hideHorProgressBar();
                    fController.addFragment(fController
                            .createFragment(Screens.DEVICES_LIST), true);
                    return;
                case Response.TIMEOUT_WAITING:
                    getViewState().hideProgressBar();
                    getViewState().hideHorProgressBar();
                    getViewState().showMsg("Connection timeout. Try later");
                    break;
                case Response.CONNECTION_ERROR:
                    getViewState().hideProgressBar();
                    getViewState().hideHorProgressBar();
                    getViewState().showMsg("Connection error. Try later");
                    break;
                case Response.NO_ROUTE_TO_HOST:
                    getViewState().hideProgressBar();
                    getViewState().hideHorProgressBar();
                    getViewState().showMsg("Server not founded. " +
                            "You have to be connected to same host with server");
                    break;
                case Response.ERROR:
                    getViewState().hideProgressBar();
                    getViewState().hideHorProgressBar();
                    getViewState().showMsg("Can't register new User. Try later");
                    break;
                case Response.BITMAP_SENDED:
                    getViewState().updateLoadingBar(Integer.parseInt(response.getMessage()));
                    break;
                case 201:
                    getViewState().showMsg("Face scanning... Follow instructions :)");
                    //getViewState().showPic();
                    getViewState().hideProgressBar();
                    getViewState().showHorProgressBar();
                    photosCount = Integer.parseInt(response.getMessage().split(" ")[0]);
//                    client.setUdpPort(Integer.parseInt(response.getMessage().split(" ")[1]));
                    sendPictures();
                    break;
            }
        });
    }

    private void sendPictures() {
        getViewState().takePicture(false);
    }

    public void requestPermissionIfNeed(Activity context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.CAMERA}, 0);
            }
        }
    }

}
