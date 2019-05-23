package shomazzapp.com.homecontorl.common;

import shomazzapp.com.homecontorl.common.interfaces.CameraListenner;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.mvp.presnter.RegCameraPresenter;

public class PhotoUploadManager implements CameraListenner {

    private Client client;
    private int photosToSend;
    private RegCameraPresenter presenter;

    public PhotoUploadManager(Client client, RegCameraPresenter presenter) {
        this.client = client;
        this.presenter = presenter;
        photosToSend = 0;
    }

    public void startPhotosUpload(int count) {
        photosToSend = count;
    }

    private synchronized void sendPhoto(byte[] picBytes, boolean closeSocket) {
        client.sendPicBytes(picBytes, closeSocket);
        photosToSend--;
    }

    @Override
    public void onFrameReady(final byte[] picBytes) {
        if (photosToSend > 0) {
            if (photosToSend == 1) {
                sendPhoto(picBytes, false);
                presenter.onPicsUploaded();
            } else {
                sendPhoto(picBytes, false);
            }
        }
    }
}
