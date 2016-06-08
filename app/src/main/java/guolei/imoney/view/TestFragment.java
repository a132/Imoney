package guolei.imoney.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import guolei.imoney.R;
import guolei.imoney.model.db.DBClass;

public class TestFragment extends Fragment implements View.OnClickListener {

    DBClass db;
    @BindView(R.id.add_test_data)
    TextView addTestData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = new DBClass(getActivity());
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);
        addTestData.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        db.AddTestData();
        Snackbar.make(v, "Add Test Data", Snackbar.LENGTH_LONG).setAction("undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.removeAll();
            }
        }).show();
    }
}
