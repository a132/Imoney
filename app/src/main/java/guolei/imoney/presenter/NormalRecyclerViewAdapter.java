package guolei.imoney.presenter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import guolei.imoney.R;
import guolei.imoney.helper.StringHelper;
import guolei.imoney.model.Expense;
import guolei.imoney.view.NewExpenseDialogFragment;

/**
 * Created by guolei on 2016/6/7.
 */
public class NormalRecyclerViewAdapter extends RecyclerSwipeAdapter<NormalRecyclerViewAdapter.NormalTextViewHolder> {

    private String TAG = "NormalRecyclerViewAdapter";
    private final LayoutInflater mLayoutInfater;
    private final Context mcontext;
    private ArrayList<Expense> expenses;
    private Ipresenter presenter;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public int type = -1;

    //protected SwipeItemRecyclerMangerImpl mItemManager = new SwipeItemRecyclerMangerImpl(this);


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public NormalRecyclerViewAdapter(Context context) {
        mcontext = context;
        presenter = new presenterImp();
        getData();
        //mTitle = context.getResources().getStringArray(R.array.titles);
        mLayoutInfater = LayoutInflater.from(context);
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInfater.inflate(R.layout.item_text, parent, false));
    }

    public void refreshData() {
        Log.d(TAG, "refresh data");
        //重新载入数据   可改进： insert into arraylist
        expenses = presenter.getExpense(null);
    }

    public void getData() {
        if (type == -1) {
            expenses = presenter.getExpense(null);
        } else {
            Log.d(TAG, "getExpense By type");
            expenses = presenter.getExpenseByType(type);
        }
    }
    @Override
    public void onBindViewHolder(final NormalTextViewHolder holder, final int position) {
        final Expense item = expenses.get(position);
        String date = format.format(item.getDate());
        String blankArea = StringHelper.getBlankString(46 - item.getDescription().length() * 2);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemManger.removeShownLayouts(holder.swipe);
                presenter.deleteExpense(item.getId());
                notifyItemRemoved(position);
                expenses.remove(position);
                notifyItemRangeChanged(position,expenses.size());
                mItemManger.closeAllItems();
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemManger.closeAllItems();
                FragmentManager fragmentManager = ((Activity)mcontext).getFragmentManager();
                NewExpenseDialogFragment fragment = new NewExpenseDialogFragment();
                fragment.caller = 2;
                fragment.expense = item;
                fragment.show(fragmentManager,"editExpense Fragment");
            }
        });

        holder.swipe.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.swipe.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(holder.buildImage);
            }
        });
        holder.swipe.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mcontext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mTextView.setText(item.getDescription() + blankArea + date);
        holder.cellAmountLocation.setText("金额：" + item.getAmount() + "      地点: " + item.getLocation());
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
                holder.classImage.setImageResource(R.drawable.degreesfille_50);
                break;
            case 4:
                holder.classImage.setImageResource(R.drawable.donasarkarfilled_50);
                break;
            default:
                //user added
                holder.classImage.setImageResource(R.drawable.shopping_50);
                break;
        }

        mItemManger.bindView(holder.itemView,position);
    }

    @Override
    public int getItemCount() {
        return expenses == null ? 0 : expenses.size();
    }


    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view)
        TextView mTextView;
        @BindView(R.id.classImage)
        ImageView classImage;
        @BindView(R.id.cell_amount_location)
        TextView cellAmountLocation;
        @BindView(R.id.swipe)
        SwipeLayout swipe;
        @BindView(R.id.build_image)
        ImageView buildImage;
        @BindView(R.id.edit_Button)
        Button editButton;
        @BindView(R.id.delete_button)
        Button deleteButton;


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
