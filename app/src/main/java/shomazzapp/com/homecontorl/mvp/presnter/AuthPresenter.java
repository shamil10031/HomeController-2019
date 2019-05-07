package shomazzapp.com.homecontorl.mvp.presnter;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.io.IOException;
import java.util.Random;

import shomazzapp.com.homecontorl.common.FController;
import shomazzapp.com.homecontorl.common.Screens;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.mvp.view.AuthView;

@InjectViewState
public class AuthPresenter extends MvpPresenter<AuthView> {

    private FController fController;
    private Client client;

    public void setFragmentController(FController fController) {
        this.fController = fController;
    }

    public void signIn(String login, String password) {
        //TODO: check login and password for empty String
        getViewState().startAuth();
        //startDumbTimer(login);
        LoadPhotoTOServerTask l = new LoadPhotoTOServerTask();
        l.execute(login, password);
        //on succes
/*        fController.clearBackStack();
        fController.addFragment(fController.createFragment(Screens.DEVICES_LIST),true);*/
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
                getViewState().showError("Error #" + errorCode + ": " + login + " is gay");
            }
        }.start();
    }

    private class LoadPhotoTOServerTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getViewState().showError("Start!");
        }

        @Override
        protected Void doInBackground(String... login) {
            try {
                Client client = new Client(login[1], 8888);
                Log.d("TAG", "Host : " + login[1]);
                Log.d("TAG", "Port : " + 8888);
                client.postString(login[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getViewState().showError("Done!");
            getViewState().finishAuth();
        }
    }


}
