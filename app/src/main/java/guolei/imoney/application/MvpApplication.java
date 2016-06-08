package guolei.imoney.application;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by guolei on 2016/5/28.
 */
public class MvpApplication extends Application {

    private static MvpApplication mApplication;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher =  LeakCanary.install(this);
        MvpApplication.mApplication = this;
    }
    public static Application getApplication(){
        return mApplication;
    }
    public static RefWatcher getRefWatcher(Context context){
        MvpApplication application = (MvpApplication)context.getApplicationContext();
        return application.refWatcher;
    }
}
