package shomazzapp.com.homecontorl.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface RegCameraView extends MvpView {

    void showHorProgressBar();

    void hideHorProgressBar();

    void showProgressBar();

    void hideProgressBar();

    void updateProgressBar();

    @StateStrategyType(SkipStrategy.class)
    void showMsg(String msg);

    @StateStrategyType(SkipStrategy.class)
    void takePicture(boolean isLast);

    void showPic();

    void hidePic();

    void updateLoadingBar(int value);
}
