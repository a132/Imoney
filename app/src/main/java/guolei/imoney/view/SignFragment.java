package guolei.imoney.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import guolei.imoney.R;

public class SignFragment extends Fragment {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_signup)
    AppCompatButton btnSignup;
    @BindView(R.id.link_login)
    TextView linkLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick({R.id.btn_signup, R.id.link_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                signUp();
                break;
            case R.id.link_login:
                backToLogin();
                break;
        }
    }

    public void signUp(){
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }
        btnSignup.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        sign(name,password,email);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onSignupSuccess();
                progressDialog.dismiss();
            }
        },3000);
    }
    public void onSignupSuccess(){
        btnSignup.setEnabled(true);
        backToLogin();
    }
    public void onSignupFailed(){
        Snackbar.make(getView(),"Sign failed",Snackbar.LENGTH_LONG).show();
        btnSignup.setEnabled(true);
    }
    public boolean validate(){
        boolean valid = true;
        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            inputName.setError("at least 3 characters");
            valid = false;
        } else {
            inputName.setError(null);
        }

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
    public void backToLogin(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment fragment = new LoginFragment();
        fragmentTransaction.replace(R.id.login_content, fragment, "sign fragment");
        fragmentTransaction.commit();
    }
    void sign(String userName, String password,String email) {
        Context context = getActivity();
        SharedPreferences sp = context.getSharedPreferences("Password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", userName);
        editor.putString("email",email);
        editor.putString("password", password);
        editor.commit();
    }

}
