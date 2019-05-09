package shomazzapp.com.homecontorl.mvp.presnter;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import shomazzapp.com.homecontorl.common.ClientListener;
import shomazzapp.com.homecontorl.common.FController;
import shomazzapp.com.homecontorl.common.Screens;
import shomazzapp.com.homecontorl.common.ViewPagerController;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.mvp.model.Request;
import shomazzapp.com.homecontorl.mvp.model.Response;
import shomazzapp.com.homecontorl.mvp.view.AuthView;

@InjectViewState
public class AuthPresenter extends MvpPresenter<AuthView> implements ClientListener {

    private ViewPagerController pagerController;
    private FController fController;
    private Client client;

    public void setFragmentController(FController fController) {
        this.fController = fController;
    }

    public void setViewPagerController(ViewPagerController pagerController) {
        this.pagerController = pagerController;
    }

    @Override
    public void reciveResponse(Response response) {
        runOnUi(() -> {
            getViewState().finishAuth();
            switch (response.getResponceCode()) {
                case Response.SUCCESS:
                    //fController.addFragment();
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
                default:
                    getViewState().showMsg(response.getResponse());
//                  fController.clearBackStack();
//                  fController.addFragment(fController.createFragment(Screens.DEVICES_LIST), true);
                    break;
            }
        });
    }

    private void runOnUi(@NonNull Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void signIn(String login, String password) {
        //TODO: check login and password for empty String
        getViewState().startAuth();
        if (client == null) client = new Client(this, Client.HOST, Client.PORT);
        //client.sendRequestForResponse(Request.createAuthRequest(login, password));
        client.sendRequestForResponse(Request.createAviableDevicesRequest(login));
        //client.sendRequestForResponse(Request.createHomeInfoRequest());
    }


    public void onSignUp() {
        pagerController.openFragment(Screens.REGISTRATION_FIELDS);
        //fController.addFragment(fController.createFragment(Screens.REGISTRATION_FIELDS), true);
    }

}
