package guolei.imoney.view;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import guolei.imoney.R;
import guolei.imoney.presenter.NormalRecyclerViewAdapter;


public class DataFragment extends Fragment {
    @BindView(R.id.dataView)
    RecyclerView dataView;

    public DataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        ButterKnife.bind(this, view);


        dataView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataView.setAdapter(new NormalRecyclerViewAdapter(getActivity()));
        return view;
    }



}
