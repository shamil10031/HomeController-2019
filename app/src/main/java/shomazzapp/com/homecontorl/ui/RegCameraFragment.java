package shomazzapp.com.homecontorl.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

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
import shomazzapp.com.homecontorl.common.interfaces.FController;
import shomazzapp.com.homecontorl.common.interfaces.ViewPagerController;
import shomazzapp.com.homecontorl.mvp.presnter.RegCameraPresenter;
import shomazzapp.com.homecontorl.mvp.view.RegCameraView;

public class RegCameraFragment extends MvpAppCompatFragment implements RegCameraView {

    @InjectPresenter
    RegCameraPresenter presenter;

    private Fotoapparat fotoapparat;
    private CameraView cameraView;

    private static final int LAYOUT = R.layout.fragment_reg_camera;

    public static RegCameraFragment newInstance() {
        return new RegCameraFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.requestPermissionIfNeed(getActivity());
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
        View view = inflater.inflate(LAYOUT, container, false);
        init(view);
        return view;
    }

    @Override
    public void takePicture() {
        Log.d("TAG", "Taking picture...");
        if (fotoapparat != null) {
            PhotoResult photoResult = fotoapparat.takePicture();
            Log.d("TAG", "Picture taken! Preparing...");
            photoResult.toBitmap().whenDone(bitmapPhoto ->
                    presenter.onPictureTaken(bitmapPhoto.bitmap));
        } else {
            Log.e("TAG", "Fotoapparat is null");
        }
    }

    private void init(View view) {
        cameraView = (CameraView) view.findViewById(R.id.camera_view);
        fotoapparat = Fotoapparat
                .with(getContext())
                .into(cameraView)           // view which will draw the camera preview
                .previewScaleType(ScaleType.CenterInside)  // we want the preview to fill the view
                .photoResolution(ResolutionSelectorsKt.lowestResolution())   // we want to have the biggest photo possible
                .lensPosition(LensPositionSelectorsKt.front())       // we want back camera
                .focusMode(SelectorsKt.firstAvailable(  // (optional) use the first focus mode which is supported by device
                        FocusModeSelectorsKt.continuousFocusPicture(),
                        FocusModeSelectorsKt.autoFocus(),        // in case if continuous focus is not available on device, auto focus will be used
                        FocusModeSelectorsKt.fixed()             // if even auto focus is not available - fixed focus mode will be used
                ))
                .logger(LoggersKt.loggers(            // (optional) we want to log camera events in 2 places at once
                        LoggersKt.logcat(),           // ... in logcat
                        LoggersKt.fileLogger(getContext())    // ... and to file
                ))
                .build();

        Button btnStart = (Button) view.findViewById(R.id.btn_reg_start_camera);
        btnStart.setOnClickListener((View.OnClickListener) v -> presenter.onStart());
    }

    @Override
    public void onResume() {
        super.onResume();
        fotoapparat.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        fotoapparat.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fotoapparat = null;
        cameraView = null;
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void finishLoading() {

    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
