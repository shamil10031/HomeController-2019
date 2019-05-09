package shomazzapp.com.homecontorl.mvp.presnter;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import shomazzapp.com.homecontorl.mvp.model.Request;
import shomazzapp.com.homecontorl.mvp.model.Response;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.common.ClientListener;
import shomazzapp.com.homecontorl.mvp.view.RegFieldsView;

@InjectViewState
public class RegFieldsPresenter extends MvpPresenter<RegFieldsView> implements ClientListener {

    private Client client;

    public void runOnUi(@NonNull Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void signUp(String name, String login, String password) {
        //TODO: check login and password for empty String
        if (client == null) client = new Client(this, Client.HOST, Client.PORT);
        client.sendRequestForResponse(Request
                .createRegistrationRequest(name, login, password));
    }

    @Override
    public void reciveResponse(Response response) {

    }
}