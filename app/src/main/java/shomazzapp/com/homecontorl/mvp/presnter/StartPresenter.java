package shomazzapp.com.homecontorl.mvp.presnter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import shomazzapp.com.homecontorl.common.interfaces.FController;
import shomazzapp.com.homecontorl.mvp.model.Client;
import shomazzapp.com.homecontorl.mvp.view.StartView;

@InjectViewState
public class StartPresenter extends MvpPresenter<StartView> {

    private FController fController;
    private Client client;

    public void setFragmentController(FController fController) {
        this.fController = fController;
    }

}
