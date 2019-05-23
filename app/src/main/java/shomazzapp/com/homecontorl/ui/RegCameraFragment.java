package shomazzapp.com.homecontorl.ui;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.log.LoggersKt;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.selector.FocusModeSelectorsKt;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.selector.SelectorsKt;
import io.fotoapparat.view.CameraView;
import shomazzapp.com.homecontorl.R;
import shomazzapp.com.homecontorl.common.CameraPreview;
import shomazzapp.com.homecontorl.common.interfaces.ClientListener;
import shomazzapp.com.homecontorl.common.interfaces.FController;
import shomazzapp.com.homecontorl.common.interfaces.ViewPagerController;
import shomazzapp.com.homecontorl.mvp.presnter.RegCameraPresenter;
import shomazzapp.com.homecontorl.mvp.view.RegCameraView;
import shomazzapp.com.homecontorl.ui.views.CircularProgressBar;

public class RegCameraFragment extends MvpAppCompatFragment implements RegCameraView {

    @InjectPresenter
    RegCameraPresenter presenter;

    private Camera mCamera;
    private CameraPreview mPreview;
    private Fotoapparat fotoapparat;
    private CameraView cameraView;
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imViewInstruction;
    private String currentMsg;
    private CircularProgressBar circularProgressBar;

    private static final int LAYOUT = R.layout.fragment_reg_camera;

    public static RegCameraFragment newInstance() {
        return new RegCameraFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(CameraPreview.TAG_CALLBACK, "CameraFragment onCreate!");
        FController fController = (FController) getActivity();
        presenter.setFragmentController(fController);
        presenter.setViewPagerController((ViewPagerController)
                fController.getFragmentByTag(StartFragment.class.toString()));
        presenter.setContext(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(CameraPreview.TAG_CALLBACK, "CameraFragment onCreateView!");
        View view = inflater.inflate(LAYOUT, container, false);
        init(view);
        return view;
    }

    @Override
    public void takePicture(boolean isLast) {
        Log.d("TAG", "Taking picture from queue...");
        if (mPreview.getQueue() != null) {
            Log.d("TAG", "Queue size = " + mPreview.getQueue().size());
            if (mPreview.getQueue().peek() != null && mPreview.getQueue().peek().length != 0) {
                Log.d("TAG", "Picture taken!");
                presenter.onPictureTaken(mPreview.getQueue().poll(), isLast);
            }
        }
    }

    @Override
    public void showPic() {
        imViewInstruction.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePic() {
        imViewInstruction.setVisibility(View.INVISIBLE);
    }

    private void init(View view) {
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(getContext(), mCamera, presenter.getPhotoUploadManager());
        ((FrameLayout) view.findViewById(R.id.camera_view)).addView(mPreview);
        imViewInstruction = (ImageView) view.findViewById(R.id.im_view_reg_instr);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_reg_camera);
        textView = (TextView) view.findViewById(R.id.tv_reg_camera);
        circularProgressBar = (CircularProgressBar) view.findViewById(R.id.progress_circular);
        currentMsg = "Нажмите на кнопку START для регистрации лица";
        hidePic();
        hideProgressBar();
        Button btnStart = (Button) view.findViewById(R.id.btn_reg_start_camera);
        btnStart.setOnClickListener((View.OnClickListener) v -> presenter.onStart());
    }

    @Override
    public void hideHorProgressBar() {
        //        horProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateLoadingBar(int value) {
        circularProgressBar.setValueWithAnimation(value);
    }

    @Override
    public void showHorProgressBar(int maxValue) {
        circularProgressBar.setMaxValue(maxValue);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(CameraPreview.TAG_CALLBACK, "CameraFragment onResume!");
        textView.setText(currentMsg);
        hidePic();
        hideProgressBar();
        try {
            if (mCamera == null) {
                mCamera = getCameraInstance();
            }
            mPreview.setCamera(mCamera);
        } catch (Exception e) {
            Log.d("Camera", "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(CameraPreview.TAG_CALLBACK, "CameraFragment onPause!");
        mPreview.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(CameraPreview.TAG_CALLBACK, "CameraFragment onStop!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(CameraPreview.TAG_CALLBACK, "CameraFragment onDestroy!");
        fotoapparat = null;
        cameraView = null;
        textView = null;
        progressBar = null;
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMsg(String msg) {
        currentMsg = msg;
        textView.setText(msg);
    }

    @Override
    public void updateProgressBar() {

    }

    private void initFotoapparat() {
        fotoapparat = Fotoapparat
                .with(getContext())
                .into(cameraView)           // view which will draw the camera preview
                .previewScaleType(ScaleType.CenterInside)  // we want the preview to fill the view
                .photoResolution(ResolutionSelectorsKt
                        .highestResolution())   // we want to have the biggest photo possible
                .lensPosition(LensPositionSelectorsKt.front())       // we want back camera
                .focusMode(SelectorsKt.firstAvailable(
                        // (optional) use the first focus mode which is supported by device
                        FocusModeSelectorsKt.continuousFocusPicture(),
                        FocusModeSelectorsKt.autoFocus(),
                        // in case if continuous focus is not available on device, auto focus will be used
                        FocusModeSelectorsKt.fixed()
                        // if even auto focus is not available - fixed focus mode will be used
                ))
                .logger(LoggersKt.loggers(
                        // (optional) we want to log camera events in 2 places at once
                        LoggersKt.logcat(),           // ... in logcat
                        LoggersKt.fileLogger(getContext())    // ... and to file
                ))
                .build();
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = CameraPreview.openFrontFacingCamera();
            c.setDisplayOrientation(90);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
}