package shomazzapp.com.homecontorl.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import shomazzapp.com.homecontorl.R;
import shomazzapp.com.homecontorl.common.FController;
import shomazzapp.com.homecontorl.mvp.presnter.AuthPresenter;
import shomazzapp.com.homecontorl.mvp.presnter.RegFieldsPresenter;
import shomazzapp.com.homecontorl.mvp.view.RegFieldsView;

public class RegFieldsFragment extends MvpAppCompatFragment implements RegFieldsView {

    @InjectPresenter
    RegFieldsPresenter presenter;

    private EditText etName;
    private EditText etLogin;
    private EditText etPassword;


    private static final int LAYOUT = R.layout.fragment_reg_fields;

    public static RegFieldsFragment newInstance() {
        return new RegFieldsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //presenter.setFragmentController((FController) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, null);
        init(view);
        return view;
    }

    private void init(View view){
        etName = (EditText) view.findViewById(R.id.et_reg_name);
        etLogin = (EditText) view.findViewById(R.id.et_reg_login);
        etPassword = (EditText) view.findViewById(R.id.et_reg_password);
        Button btnSignUp = (Button) view.findViewById(R.id.btn_reg_sign_up);
        btnSignUp.setOnClickListener(v -> onSignUp());
    }

    private void onSignUp(){
        presenter.signUp(etName.getText().toString(),
                etLogin.getText().toString(),
                etPassword.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
