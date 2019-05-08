package shomazzapp.com.homecontorl.mvp.presnter;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Random;

import shomazzapp.com.homecontorl.common.FController;
import shomazzapp.com.homecontorl.common.Response;
import shomazzapp.com.homecontorl.common.Screens;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.mvp.model.ClientListener;
import shomazzapp.com.homecontorl.mvp.view.AuthView;

@InjectViewState
public class AuthPresenter extends MvpPresenter<AuthView> implements ClientListener {

    private FController fController;
    private Client client;

    public void setFragmentController(FController fController) {
        this.fController = fController;
    }

    @Override
    public void reciveResponse(Response response) {
        runOnUi(() -> {
            getViewState().finishAuth();
            if (response.getResponceCode() != Response.OK) {
                getViewState().showMsg("Failed to get response :(");
            } else {
                getViewState().showMsg(response.getResponse());
//            fController.clearBackStack();
//            fController.addFragment(fController.createFragment(Screens.DEVICES_LIST), true);
            }
        });
    }

    private void runOnUi(@NonNull Runnable runnable){
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void signIn(String login, String password) {
        //TODO: check login and password for empty String
        getViewState().startAuth();
        if (client == null) client = new Client(this, Client.HOST, Client.PORT);
        client.postMsgForResponse(login + "~" + password);
        //startDumbTimer(login);
    }

    public void onSignUp() {
        fController.addFragment(fController.createFragment(Screens.REGISTRATION_FIELDS), true);
    }

    private void startDumbTimer(String login) {
        new CountDownTimer(2000, new Random().nextInt(400) + 100) {
            int errorCode = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                this.errorCode++;
            }

            @Override
            public void onFinish() {
                getViewState().finishAuth();
                getViewState().showMsg("Error #" + errorCode + ": " + login + " is gay");
            }
        }.start();
    }

}
