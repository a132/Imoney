package guolei.imoney.view;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import guolei.imoney.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @BindView(R.id.change_password)
    TextView changePassword;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("设置");
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        return view;
    }

    @OnClick(R.id.change_password)
    public void onClick() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragmentTransaction.replace(R.id.fragment_main,fragment,"changePasswordFragment");
        fragmentTransaction.commit();
    }
}
