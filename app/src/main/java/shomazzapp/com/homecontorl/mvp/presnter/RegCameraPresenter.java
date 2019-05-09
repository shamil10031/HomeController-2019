package shomazzapp.com.homecontorl.mvp.presnter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import shomazzapp.com.homecontorl.common.FController;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.mvp.view.RegCameraView;
import shomazzapp.com.homecontorl.mvp.view.StartView;

@InjectViewState
public class RegCameraPresenter extends MvpPresenter<RegCameraView> {

    private FController fController;
    private Client client;

    public void setFragmentController(FController fController) {
        this.fController = fController;
    }

}
