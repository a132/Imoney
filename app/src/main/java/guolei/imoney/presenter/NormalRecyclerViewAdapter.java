package guolei.imoney.presenter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import guolei.imoney.R;
import guolei.imoney.helper.StringHelper;
import guolei.imoney.model.Expense;

/**
 * Created by guolei on 2016/6/7.
 */
public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder> {

    private final LayoutInflater mLayoutInfater;
    private final Context mcontext;
    private ArrayList<Expense> expenses;
    private Ipresenter presenter;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public NormalRecyclerViewAdapter(Context context) {
        mcontext = context;
        presenter = new presenterImp();
        expenses = presenter.getExpense(null);
        //mTitle = context.getResources().getStringArray(R.array.titles);
        mLayoutInfater = LayoutInflater.from(context);
    }


    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInfater.inflate(R.layout.item_text, parent, false));
    }

    public void refrshData() {
        //refresh data
        expenses = presenter.getExpense(null);
    }



    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        Expense item = expenses.get(position);
        String date =format.format(item.getDate());
        String blankArea = StringHelper.getBlankString(50-item.getDescription().length());

        holder.mTextView.setText(item.getDescription() +blankArea+ date);
        holder.cellAmountLocation.setText("金额：" + item.getAmount() + "      地点: " +item.getLocation());
        switch (item.getType()) {
            case 0:
                holder.classImage.setImageResource(R.drawable.food_50);
                break;
            case 1:
                holder.classImage.setImageResource(R.drawable.clother_50);
                break;
            case 2:
                holder.classImage.setImageResource(R.drawable.train_50);
                break;
            case 3:
                holder.classImage.setImageResource(R.drawable.shopping_50);
                break;
            default:
                //user added
                holder.classImage.setImageResource(R.drawable.dog_50);
                break;
        }
    }

    @Override
    public int getItemCount() {
        refrshData();
        return expenses == null ? 0 : expenses.size();
    }

    public void ShowMessage(View v, int position) {
        Snackbar.make(v, expenses.get(position).toString(), Snackbar.LENGTH_LONG).show();
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view)
        TextView mTextView;
        @BindView(R.id.classImage)
        ImageView classImage;
        @BindView(R.id.cell_amount_location)
        TextView cellAmountLocation;

        NormalTextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "OnClick - position=" + getLayoutPosition());
                    String message = mTextView.getText().toString();
                    Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
