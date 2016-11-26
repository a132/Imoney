package guolei.imoney.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Date;

import guolei.imoney.activity.MainActivity;
import guolei.imoney.R;
import guolei.imoney.view.LoginFragment;

public class LoginActivity extends AppCompatActivity {
//http://sourcey.com/beautiful-android-login-and-signup-screens-with-material-design/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyTheme);
        Log.d("Login Activity","on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp =  this.getSharedPreferences("Password", Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin",false);
        long loginTime = sp.getLong("lastLoginTime",0);
        Date date = new Date();
        long threeDays = 3*24*60*60*1000;

        if(!isLogin) {
            setLoginFragment();
            return;
        }
        else{
            //上次登录是在三天前
            if(loginTime!=0 && loginTime + threeDays < date.getTime()){
                setLoginFragment();
                return;
            }
            if(loginTime !=0){
                enterMainInterface();
            }
        }
    }

    //进入登陆界面
    void setLoginFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment fragment = new LoginFragment();
        fragmentTransaction.replace(R.id.login_content,fragment,"sign fragment");
        fragmentTransaction.commit();
    }
    //进入应用主界面
    void enterMainInterface(){
        this.finish();
        startActivity(new Intent(this,MainActivity.class));
    }

}
