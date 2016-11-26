package guolei.imoney.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;

import com.liulishuo.share.model.IShareManager;
import com.liulishuo.share.model.ShareContentWebpage;
import com.liulishuo.share.wechat.WechatShareManager;

import java.util.ArrayList;

import guolei.imoney.R;
import guolei.imoney.util.UserManager;
import guolei.imoney.presenter.Ipresenter;
import guolei.imoney.presenter.presenterImp;
import guolei.imoney.view.AboutFragment;
import guolei.imoney.view.BudgetFragment;
import guolei.imoney.view.ChartFragment;
import guolei.imoney.view.DataFragment;
import guolei.imoney.view.SettingFragment;
import guolei.imoney.view.TypeViewFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Ipresenter presenter;
    private static String TAG = "mainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new presenterImp();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set home fragment
        DataFragment fragment = new DataFragment();
        String dataFragmentName = "data fragment";
        moveToFrgment(fragment, dataFragmentName);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SettingFragment fragment = new SettingFragment();
            moveToFrgment(fragment, "settingFragment");
        }
        if (id == R.id.newtype_settings) {
            newType();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        String fragmentName = null;

        if (id == R.id.nav_home) {
            fragment = new DataFragment();
            fragmentName = "data fragment";
        } else if (id == R.id.nav_statistics) {
            fragmentName = "chart fragment";
            fragment = new ChartFragment();
        } else if (id == R.id.nav_classify) {
            fragmentName = "type fragment";
            fragment = new TypeViewFragment();
        } else if (id == R.id.nav_test) {
            fragmentName = "budget fragment";
            fragment = new BudgetFragment();
//            fragmentName = "test fragment";
//            fragment = new TestFragment();
        } else if (id == R.id.nav_share) {
            //TODO validate if this code can make different
            shareToWechat();
        } else if (id == R.id.nav_about) {
            fragmentName = "about fragment";
            fragment = new AboutFragment();
        } else if (id == R.id.nav_loginOut) {
            loginOut();
        }
        moveToFrgment(fragment, fragmentName);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void moveToFrgment(Fragment fragment, String fragmentName) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_main, fragment, fragmentName);
            fragmentTransaction.commit();
        }
    }

    void loginOut() {
        UserManager.loginOut();
        this.finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    // 新建消费类型
    public void newType() {
        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);

        builder.setTitle(getString(R.string.new_expense_type)).setIcon(R.drawable.plus_32).
                setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString();
                ArrayList<String> list = presenter.getExpenseType();
                if (list.contains(name)) {
                    editText.setError("已经存在！");
                    return;
                }

                if (name.isEmpty() || name.equals("")) {
                    editText.setError("不能为空");
                    return;
                } else {
                    editText.setError(null);
                }
                presenter.addExpenseType(name);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(true);
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    void shareToWechat() {
        IShareManager iShareManager = new WechatShareManager(this);
        iShareManager.share(new ShareContentWebpage("iMoney", "a good app to manage your expenses", "dataUrl",
                "http://ww4.sinaimg.cn/mw690/db450502jw1f4thhymkd3j202o02o743.jpg"), WechatShareManager.WEIXIN_SHARE_TYPE_TALK);
        Log.d(TAG, "shareToWechat");
    }
}
