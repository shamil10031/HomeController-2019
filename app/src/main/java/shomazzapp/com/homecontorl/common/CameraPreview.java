package shomazzapp.com.homecontorl.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import shomazzapp.com.homecontorl.common.interfaces.CameraListenner;
import shomazzapp.com.homecontorl.mvp.presnter.RegCameraPresenter;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    public static final String TAG_CALLBACK = " CamCallbacks";
    private static final String TAG = "camera";
    private byte[] mImageData;
    private Camera.Size mPreviewSize;
    private LinkedList<byte[]> mQueue = new LinkedList<byte[]>();
    private static final int MAX_BUFFER = 3000;
    private byte[] mLastFrame = null;
    private CameraListenner cameraListenner;

    public CameraPreview(Context context, Camera camera, CameraListenner cameraListenner) {
        super(context);
        this.cameraListenner = cameraListenner;
        Log.d(TAG_CALLBACK, "CameraPreview constructor called!");
        mCamera = camera;
        mPreviewSize = camera.getParameters().getPreviewSize();
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public LinkedList<byte[]> getQueue() {
        return mQueue;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG_CALLBACK, "CameraPreview surface created!");
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG_CALLBACK, "CameraPreview surface destroyed!");
        Log.d(TAG, "Queue size = " + mQueue.size());
        if (mCamera != null) {
            mCamera.release();
            resetBuff();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null) {
            return;
        }
        mCamera.stopPreview();
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(0);
        parameters.setJpegQuality(10);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        mCamera.setPreviewCallback(mPreviewCallback);
        mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
    }

    public byte[] getImageBuffer() {
        synchronized (mQueue) {
            if (mQueue.size() > 0) {
                mLastFrame = mQueue.poll();
            }
        }
        return mLastFrame;
    }

    private void resetBuff() {
        synchronized (mQueue) {
            mQueue.clear();
            mLastFrame = null;
        }
    }

    public void onPause() {
        Log.d(TAG_CALLBACK, "CameraPreview surface onPause!");
        try {
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Camera.PreviewCallback mPreviewCallback = (data, camera) -> {
        Log.d(TAG_CALLBACK, "preview callback");
        synchronized (mQueue) {
            if (mQueue.size() == MAX_BUFFER) {
                Log.d(TAG_CALLBACK, "Queue size = " + mQueue.size());
                mQueue.poll();
            }
            YuvImage im = new YuvImage(data, ImageFormat.NV21, mPreviewSize.width,
                    camera.getParameters().getPreviewSize().height, null);
            Rect r = new Rect(0, 0, mPreviewSize.width, mPreviewSize.height);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            im.compressToJpeg(r, 20, baos);
            byte[] imageBytes = baos.toByteArray();
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            image = RegCameraPresenter.rotate(image);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 20, stream);
            byte[] byteArray = stream.toByteArray();
            if (cameraListenner != null)
                cameraListenner.onFrameReady(byteArray);
            image.recycle();
            mQueue.add(byteArray);

        }
    };

    public static Camera openFrontFacingCamera() {
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam;
    }
}