package guolei.imoney.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import guolei.imoney.application.MvpApplication;

/**
 * Created by guolei on 2016/6/7.
 */
public class UserManager {

    public static void login(){
        SharedPreferences sp = MvpApplication.getApplication().getSharedPreferences("Password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Date loginTime = new Date();
        editor.putLong("lastLoginTime", loginTime.getTime());
        editor.putBoolean("isLogin", true);
        editor.commit();
    }
    public static void loginOut(){
        SharedPreferences sp = MvpApplication.getApplication().getSharedPreferences("Password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin",false);
        editor.commit();
    }
}
