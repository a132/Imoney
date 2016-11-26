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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Date;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import guolei.imoney.activity.MainActivity;
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
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                login(email,password);
                break;
            case R.id.link_signup:
                sign();
                break;
        }
    }
    public boolean login(String email,String password){
        //Log.d(TAG, "Login");

        if (!validate(email,password)) {
            onLoginFailed();
            return false;
        }
        Login_button.setEnabled(false);

        SharedPreferences sp = getActivity().getSharedPreferences("Password", Context.MODE_PRIVATE);

        if(!email.equals(sp.getString("email", "null"))){
            onLoginFailed();
            inputEmail.setError("账户不存在");
            return false;
        }

        if (email.equals(sp.getString("email", "null")) && password.equals(sp.getString("password", "null")) && !email.equals("null")) {
            SharedPreferences.Editor editor = sp.edit();
            Date loginTime = new Date();
            editor.putLong("lastLoginTime", loginTime.getTime());
            editor.putBoolean("isLogin", true);
            editor.commit();
            Log.d(TAG,"somthing");
        } else {
            Log.d(TAG,"Login failed");
           onLoginFailed();
            return false;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在登陆...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        onLoginSuccess();
                    }
                },2000);
        return true;
    }

    boolean validate(String email,String password){
        boolean valid = true;
        boolean hasNumber = false;
        boolean hasChar = false;

        if (email.isEmpty() || !isValidEmailAddress(email)) {
            inputEmail.setError("邮箱名不合法");
            valid = false;
        } else {
            //inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 15) {
            inputPassword.setError("6-15个字母");
            valid = false;
        }
        for (int i = 0; i< password.length();i++){
            if(Character.isDigit(password.charAt(i))){
                hasNumber = true;
            }
            if(Character.isLetter(password.charAt(i))){
                hasChar = true;
            }
        }
        if(!hasChar || !hasNumber){
            inputPassword.setError("格式错误");
            valid = false;
        }

        return valid;
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void onLoginSuccess(){
        Login_button.setEnabled(true);
        Log.d(TAG,"on Login Success");
        getActivity().finish();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
    public void onLoginFailed(){
        Snackbar.make(getView(),"登陆失败", Snackbar.LENGTH_LONG).show();
        Log.d(TAG,"on Login failed");
        inputPassword.setText("");
        YoYo.with(Techniques.Tada)
            .duration(500)
            .delay(100)
            .playOn(inputPassword);
        YoYo.with(Techniques.Tada)
                .duration(500)
                .delay(100)
                .playOn(inputEmail);
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
