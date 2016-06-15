package guolei.imoney.view;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("修改密码");
        return view;
    }

}
