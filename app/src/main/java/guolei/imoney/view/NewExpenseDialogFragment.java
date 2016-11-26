package guolei.imoney.view;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import guolei.imoney.R;
import guolei.imoney.model.Expense;
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
    @BindView(R.id.newExpenseTitle)
    TextView newExpenseTitle;


    private static final String TAG = "NewExpDiaFrag";
    private Ipresenter presenter;
    private int type;
    public int caller = 1;  // 1 for datafragment, 2 for editExpense Fragment
    public Expense expense;


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
        setSpinnerAdapter();
        return view;
    }

    private void setSpinnerAdapter() {
        ArrayList<String> types = presenter.getExpenseType();
        ArrayAdapter<String> expenseTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
        expenseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseType.setAdapter(expenseTypeAdapter);
        expenseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = 1;
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(newExpenseTitle);
        if (caller == 2) {
            setContent(expense);
        }
    }

    private void setContent(Expense expense) {
        newExpenseTitle.setText("更新消费记录");
        textDescription.setText(expense.getDescription());
        textAmount.setText(expense.getAmount() + "");
        textLocation.setText(expense.getLocation());
        expenseType.setSelection(expense.getType());
    }

    @OnClick(R.id.add_expense_button)
    public void onClick() {
        newExpense();
    }

    public void newExpense() {
        Log.d(TAG, "new Expense");
        if (!validate()) {
            addExpenseButton.setEnabled(true);
            Snackbar.make(getView(), R.string.illegal_input, Snackbar.LENGTH_LONG).show();
            return;
        }
        addExpenseButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);


        String description = textDescription.getText().toString();
        float amount = Float.parseFloat(textAmount.getText().toString());
        String location = textLocation.getText().toString();
        int expenseType = type;


        if (caller == 2) {
            int id = expense.getId();
            expense = Expense.getExpense(id, expenseType, amount, location, description);
            presenter.updateExpense(expense);
            progressDialog.setMessage("正在更新...");
        } else if (caller == 1) {
            presenter.addNewExpense(type, amount, location, description);
            progressDialog.setMessage("正在添加...");
        }
        progressDialog.show();

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        home();
                    }
                }, 2000);
    }

    public boolean validate() {
        boolean validate = true;
        String description = textDescription.getText().toString();
        String amount = textAmount.getText().toString();
        String location = textLocation.getText().toString();
        if (description.isEmpty()) {
            textDescription.setError("No description");
            validate = false;
        } else {
            textDescription.setError(null);
        }
        if (amount.isEmpty()) {
            validate = false;
            textAmount.setError("No amount");
        } else {
            textAmount.setError(null);
        }
        if (location.isEmpty()) {
            validate = false;
            textLocation.setError("No location");
        } else {
            textLocation.setError(null);
        }
        return validate;
    }

    void home() {
        //hide current fragment
        //http://stackoverflow.com/questions/10905312/receive-result-from-dialogfragment

        FragmentManager manager = getFragmentManager();
        dialogFinishLister finishListener = (dialogFinishLister) manager.findFragmentByTag("data fragment");
        finishListener.onFininshDialogFragment();
        this.dismiss();
    }

    public interface dialogFinishLister {
        void onFininshDialogFragment();
    }
}
