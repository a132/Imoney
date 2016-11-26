package guolei.imoney.view;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import guolei.imoney.R;
import guolei.imoney.presenter.Ipresenter;
import guolei.imoney.presenter.presenterImp;


public class DataFragment extends Fragment implements NewExpenseDialogFragment.dialogFinishLister{
    @BindView(R.id.dataView)
    RecyclerView dataView;
    @BindView(R.id.add_a_expense)
    FloatingActionButton addAExpense;

    private Ipresenter presenter;
    private static final String TAG = "data Fragment";

    public DataFragment() {
        // Required empty public constructor
        presenter = new presenterImp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.expense_list);
        dataView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataView.setAdapter(new NormalRecyclerViewAdapter(getActivity()));
        return view;
    }

    @OnClick(R.id.add_a_expense)
    public void onClick(View view) {
        if (view == null) {
            Log.d("DataFragment", "Onclick View null");
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            NewExpenseDialogFragment fragment = new NewExpenseDialogFragment();
            fragment.show(fragmentManager,"dialog Fragment");
        }
    }

    @Override
    public void onFininshDialogFragment() {
        //http://stackoverflow.com/questions/10905312/receive-result-from-dialogfragment
        Log.d(TAG,"finish dialogfragment");
        NormalRecyclerViewAdapter adapter =(NormalRecyclerViewAdapter) dataView.getAdapter();
        adapter.refreshData();
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }
}
