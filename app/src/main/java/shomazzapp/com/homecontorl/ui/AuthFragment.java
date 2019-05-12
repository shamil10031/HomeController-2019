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
import shomazzapp.com.homecontorl.common.interfaces.FController;
import shomazzapp.com.homecontorl.common.interfaces.ViewPagerController;
import shomazzapp.com.homecontorl.mvp.presnter.AuthPresenter;
import shomazzapp.com.homecontorl.mvp.view.AuthView;

public class AuthFragment extends MvpAppCompatFragment implements AuthView {

    @InjectPresenter
    AuthPresenter presenter;

    private ProgressBar progressBar;
    private EditText etLogin;
    private EditText etPassword;

    private static final int LAYOUT = R.layout.fragment_auth;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FController fController = (FController) getActivity();
        presenter.setFragmentController(fController);
        presenter.setViewPagerController((ViewPagerController)
                fController.getFragmentByTag(StartFragment.class.toString()));
        presenter.setContext(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, null);
        init(view);
        clearFocuses();
        return view;
    }

    private void init(View view) {
        etLogin = (EditText) view.findViewById(R.id.et_login);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_auth);
        Button btnSignIn = (Button) view.findViewById(R.id.btn_sign_in);
        Button btnSignUp = (Button) view.findViewById(R.id.btn_sign_up);
        btnSignIn.setOnClickListener(v -> onSingIn());
        btnSignUp.setOnClickListener(v -> onSignUp());
    }

    //private

    private void onSingIn() {
        clearFocuses();
        presenter.signIn(etLogin.getText().toString(),
                etPassword.getText().toString());
    }

    private void onSignUp() {
        clearFocuses();
        presenter.onSignUp();
    }

    private void clearFocuses() {
        if (etLogin != null) {
            etLogin.clearFocus();
            hideKeyBoard(etLogin);
        }
        if (etPassword != null) {
            etPassword.clearFocus();
            hideKeyBoard(etPassword);
        }
    }

    private void hideKeyBoard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        progressBar = null;
        etPassword = null;
        etLogin = null;
    }

    @Override
    public void startAuth() {
        clearFocuses();
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void finishAuth() {
        clearFocuses();
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMsg(String msg) {
        //TODO: show error normally
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
