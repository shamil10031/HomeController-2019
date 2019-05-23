package shomazzapp.com.homecontorl.common;

import shomazzapp.com.homecontorl.common.interfaces.CameraListenner;
import shomazzapp.com.homecontorl.mvp.model.Client;

public class PhotoUploadManager implements CameraListenner {

    private Client client;
    private int photosToSend;

    public PhotoUploadManager(Client client) {
        this.client = client;
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
            sendPhoto(picBytes, photosToSend == 1);
        }
    }
}
