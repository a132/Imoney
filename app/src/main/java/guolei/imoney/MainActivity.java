package guolei.imoney;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;

import guolei.imoney.helper.UserManager;
import guolei.imoney.presenter.Ipresenter;
import guolei.imoney.presenter.presenterImp;
import guolei.imoney.view.AboutFragment;
import guolei.imoney.view.ChartFragment;
import guolei.imoney.view.DataFragment;
import guolei.imoney.view.LoginActivity;
import guolei.imoney.view.TestFragment;
import guolei.imoney.view.TypeViewFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Ipresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new presenterImp();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set homw fragment
        DataFragment fragment = new DataFragment();
        moveToFrgment(fragment);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
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
            return true;
        }
        if(id == R.id.newtype_settings){
            newType();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment  =  null;

        if (id == R.id.nav_home) {
            fragment = new DataFragment();
        } else if (id == R.id.nav_statistics) {
            fragment = new ChartFragment();
        } else if (id == R.id.nav_classify) {
            fragment = new TypeViewFragment();
        } else if (id == R.id.nav_test) {
            fragment = new TestFragment();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
        }else if(id == R.id.nav_loginOut){
            loginOut();
        }
        moveToFrgment(fragment);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void moveToFrgment(Fragment fragment){
        if(fragment != null){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_main,fragment,"type fragment");
            fragmentTransaction.commit();
        }
    }

    void loginOut(){
        UserManager.loginOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    // 新建消费类型
    public void newType(){
        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);

        builder.setTitle("新建消费类型").setIcon(R.drawable.star_32).
                setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString();
                if (name.isEmpty()){
                    editText.setError("不能为空");
                    return;
                }
                else{
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

}
