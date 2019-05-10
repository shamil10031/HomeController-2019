package shomazzapp.com.homecontorl.common.interfaces;

import android.support.annotation.NonNull;

import shomazzapp.com.homecontorl.mvp.model.Response;

public interface ClientListener {

    void runOnUi(@NonNull Runnable runnable);

    void reciveResponse(Response response);

}
