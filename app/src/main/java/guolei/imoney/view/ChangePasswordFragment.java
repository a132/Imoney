package guolei.imoney.view;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import guolei.imoney.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {


    @BindView(R.id.old_password)
    EditText oldPassword;
    @BindView(R.id.npassword_1)
    EditText npassword1;
    @BindView(R.id.npassword_2)
    EditText npassword2;
    @BindView(R.id.btn_changePassword)
    AppCompatButton btnChangePassword;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("修改密码");
        return view;
    }

    @OnClick(R.id.btn_changePassword)
    public void onClick() {
        changePassword();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在修改...");
        progressDialog.show();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToHomeFrgment();
                progressDialog.dismiss();
            }
        },2000);
    }
    public void changePassword(){
        String old_Password = oldPassword.getText().toString();
        String new_password = npassword1.getText().toString();
        String new_password2 = npassword2.getText().toString();

        SharedPreferences sp = getActivity().getSharedPreferences("Password", Context.MODE_PRIVATE);
        String spPassword = sp.getString("password", "null");
        if(old_Password.equals(spPassword) && !spPassword.equals("null")){
            Log.d("change password","old password ok");
        }else{
            oldPassword.setText("");
            oldPassword.setError("错误的密码");
            return;
        }
        if (new_password.equals(new_password2)){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("password", new_password);
            editor.commit();
        }else{
            npassword2.setText("");
            npassword2.setError("两次密码不同");
            return;
        }
    }

    public void moveToHomeFrgment(){
        Fragment fragment =new  DataFragment();
        String fragmentName = "data fragment";
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main,fragment,fragmentName);
        fragmentTransaction.commit();
    }

}
