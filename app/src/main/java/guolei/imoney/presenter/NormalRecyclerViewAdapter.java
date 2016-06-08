package guolei.imoney.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import guolei.imoney.R;
import guolei.imoney.model.Expense;
import guolei.imoney.model.db.DBClass;

/**
 * Created by guolei on 2016/6/7.
 */
public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder> {


    private final LayoutInflater mLayoutInfater;
    private final Context mcontext;
    private ArrayList<Expense> expenses;
    private DBClass db;
   // private String mTitle[];

    public NormalRecyclerViewAdapter(Context context) {
        mcontext = context;
        db = new DBClass(context);
        expenses = db.queryExpense("null");
        //mTitle = context.getResources().getStringArray(R.array.titles);
        mLayoutInfater = LayoutInflater.from(context);
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInfater.inflate(R.layout.item_text, parent, false));
    }

    public void refrshData(){
        //refresh data
        expenses = db.queryExpense("null");
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        //holder.mTextView.setText(mTitle[position]);
        Expense item = expenses.get(position);
        holder.mTextView.setText(item.getDescription());
        holder.classImage.setImageResource(R.drawable.shopping_50);
    }

    @Override
    public int getItemCount() {
        refrshData();
        return expenses == null ? 0 : expenses.size();
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view)
        TextView mTextView;
        @BindView(R.id.classImage)
        ImageView classImage;

        NormalTextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "OnClick - position=" + getLayoutPosition());
                }
            });
        }
    }
}
