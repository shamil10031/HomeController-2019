package shomazzapp.com.homecontorl.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import shomazzapp.com.homecontorl.R;
import shomazzapp.com.homecontorl.common.FController;
import shomazzapp.com.homecontorl.mvp.presnter.AuthPresenter;
import shomazzapp.com.homecontorl.mvp.presnter.StartPresenter;
import shomazzapp.com.homecontorl.mvp.view.AuthView;
import shomazzapp.com.homecontorl.mvp.view.StartView;

public class StartFragment extends MvpAppCompatFragment implements StartView {

    @InjectPresenter
    StartPresenter presenter;

    private static final int LAYOUT = R.layout.fragment_auth;

    public static StartFragment newInstance() {
        return new StartFragment();
    }

}
