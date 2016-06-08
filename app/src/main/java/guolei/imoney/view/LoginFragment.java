package guolei.imoney.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import guolei.imoney.MainActivity;
import guolei.imoney.R;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_login)
    AppCompatButton Login_button;
    @BindView(R.id.link_signup)
    TextView linkSignup;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.btn_login, R.id.link_signup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.link_signup:
                sign();
                break;
        }
    }
    public void login(){
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        Login_button.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        SharedPreferences sp = getActivity().getSharedPreferences("Password", Context.MODE_PRIVATE);
        if (email.equals(sp.getString("email", "null")) && password.equals(sp.getString("password", "null")) && !email.equals("null")) {
            SharedPreferences.Editor editor = sp.edit();
            Date loginTime = new Date();
            editor.putLong("lastLoginTime", loginTime.getTime());
            editor.putBoolean("isLogin", true);
            editor.commit();
        } else {
            return;
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                },3000);
    }

    boolean validate(){
        boolean valid = true;

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            inputPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }
    public void onLoginSuccess(){
        Login_button.setEnabled(true);
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
    public void onLoginFailed(){
        Snackbar.make(getView(),"Login Failed",Snackbar.LENGTH_LONG).show();
        Login_button.setEnabled(true);
    }
    public void sign(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SignFragment fragment = new SignFragment();
        fragmentTransaction.replace(R.id.login_content, fragment, "sign fragment");
        fragmentTransaction.commit();
    }
}
