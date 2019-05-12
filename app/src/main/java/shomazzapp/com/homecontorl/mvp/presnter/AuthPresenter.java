package shomazzapp.com.homecontorl.mvp.presnter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.lang.ref.WeakReference;

import shomazzapp.com.homecontorl.common.interfaces.ClientListener;
import shomazzapp.com.homecontorl.common.interfaces.FController;
import shomazzapp.com.homecontorl.common.PreferencesHelper;
import shomazzapp.com.homecontorl.common.Screens;
import shomazzapp.com.homecontorl.common.interfaces.ViewPagerController;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.mvp.model.Request;
import shomazzapp.com.homecontorl.mvp.model.Response;
import shomazzapp.com.homecontorl.mvp.view.AuthView;

@InjectViewState
public class AuthPresenter extends MvpPresenter<AuthView> implements ClientListener {

    private ViewPagerController pagerController;
    private FController fController;
    private Client client;
    private WeakReference<Context> context;
    private PreferencesHelper prefHelper;

    private String login;
    private String password;

    public AuthPresenter() {
        super();
        client = new Client(this, Client.HOST, Client.PORT);
        prefHelper = new PreferencesHelper();
    }

    public void setContext(Context context) {
        this.context = new WeakReference<>(context);
    }

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
                    fController.addFragment(fController
                            .createFragment(Screens.DEVICES_LIST), true);
                    prefHelper.putString(
                            PreferencesHelper.KEY_LOGIN, login, context.get());
                    prefHelper.putString(
                            PreferencesHelper.KEY_PASSWORD, password, context.get());
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

    public void runOnUi(@NonNull Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void signIn(String login, String password) {
        //TODO: check login and password for empty String
        /*fController.addFragment(fController
                .createFragment(Screens.DEVICES_LIST), true);*/
        getViewState().startAuth();
        this.login = login;
        this.password = password;
        client.sendRequestForResponse(Request.createAuthRequest(login, password), true);
    }

    public void onSignUp() {
        pagerController.openFragment(Screens.REGISTRATION_FIELDS);
        //fController.addFragment(fController.createFragment(Screens.REGISTRATION_FIELDS), true);
    }

}
