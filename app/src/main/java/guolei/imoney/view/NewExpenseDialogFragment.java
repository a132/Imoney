package guolei.imoney.view;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import guolei.imoney.R;
import guolei.imoney.presenter.Ipresenter;
import guolei.imoney.presenter.presenterImp;

/**
 * Created by guolei on 2016/6/8.
 */
public class NewExpenseDialogFragment extends DialogFragment {
    @BindView(R.id.add_expense_button)
    Button addExpenseButton;
    @BindView(R.id.text_description)
    EditText textDescription;
    @BindView(R.id.text_amount)
    EditText textAmount;
    @BindView(R.id.text_location)
    EditText textLocation;
    @BindView(R.id.expense_type)
    Spinner expenseType;


    private static final String TAG = "DialogFragment";
    private Ipresenter presenter;
    private int type;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set fullscreen
        setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container);
        ButterKnife.bind(this, view);
        presenter = new presenterImp();
        ArrayList<String> types = presenter.getExpenseType();

        ArrayAdapter<String> expenseTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
        expenseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseType.setAdapter(expenseTypeAdapter);
        expenseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Snackbar.make(getView(),parent.getItemAtPosition(position).toString() ,Snackbar.LENGTH_LONG).show();
                type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = 1;
            }
        });
        return view;
    }

    @OnClick(R.id.add_expense_button)
    public void onClick() {
        newExpense();
    }

    public void newExpense(){
        Log.d(TAG,"new Expense");
        if(!validate()){
            addExpenseButton.setEnabled(true);
            Snackbar.make(getView(),"Try Again",Snackbar.LENGTH_LONG).show();
            return;
        }
        addExpenseButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在添加...");
        progressDialog.show();

        String description = textDescription.getText().toString();
        float amount =Float.parseFloat(textAmount.getText().toString());
        String location = textLocation.getText().toString();
        int expenseType = type;
        presenter.addNewExpense(type,amount,location,description);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        home();
                    }
                },2000);

    }
    public boolean validate(){
        boolean validate = true;
        String description = textDescription.getText().toString();
        String amount = textAmount.getText().toString();
        String location = textLocation.getText().toString();
        if (description.isEmpty()){
            textDescription.setError("No description");
            validate = false;
        }else {
            textDescription.setError(null);
        }
        if (amount.isEmpty()){
            validate = false;
            textAmount.setError("No amount");
        }else {
            textAmount.setError(null);
        }
        if(location.isEmpty()){
            validate = false;
            textLocation.setError("No location");
        }else {
            textLocation.setError(null);
        }
        return validate;
    }

    void home(){
        //hide current fragment
        //http://stackoverflow.com/questions/10905312/receive-result-from-dialogfragment

        FragmentManager manager = getFragmentManager();
        dialogFinishLister activity = (dialogFinishLister)manager.findFragmentByTag("data fragment");
        activity.onFininshDialogFragment();
        this.dismiss();
    }
    public interface dialogFinishLister{
        void onFininshDialogFragment();
    }

}
