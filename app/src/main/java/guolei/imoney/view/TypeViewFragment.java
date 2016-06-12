package guolei.imoney.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import guolei.imoney.R;
import guolei.imoney.presenter.Ipresenter;
import guolei.imoney.presenter.NormalRecyclerViewAdapter;
import guolei.imoney.presenter.presenterImp;

/**
 * A simple {@link Fragment} subclass.
 */
public class TypeViewFragment extends Fragment {

    @BindView(R.id.type_ListView)
    RecyclerView typeListView;
    @BindView(R.id.type_picker)
    Spinner typePicker;

    private Ipresenter presenter;
    private int type = 0;
    private String TAG = "TypeViewFragment";

    public TypeViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_type_view, container, false);
        ButterKnife.bind(this, view);
        presenter = new presenterImp();
        typeListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        NormalRecyclerViewAdapter adapter = new NormalRecyclerViewAdapter(getActivity());
        adapter.type = type;
        typeListView.setAdapter(adapter);
        setupSpinner();
        return view;
    }

    private void setupSpinner() {
        ArrayList<String> types = presenter.getExpenseType();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, types);
        typePicker.setAdapter(arrayAdapter);

        typePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "on item selected");
                type = position;
                NormalRecyclerViewAdapter adapter = (NormalRecyclerViewAdapter) typeListView.getAdapter();
                adapter.type = position;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = 0;
            }
        });
    }


}
