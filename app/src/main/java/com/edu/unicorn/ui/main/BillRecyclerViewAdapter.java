package com.edu.unicorn.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.unicorn.R;
import com.edu.unicorn.db.SqliteHelper;
import com.edu.unicorn.entity.Bill;
import com.edu.unicorn.ui.main.BillFragment.OnListFragmentInteractionListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Bill} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BillRecyclerViewAdapter extends RecyclerView.Adapter<BillRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private final List<Bill> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;
    private SqliteHelper dbHelper;

    public BillRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mValues.add(new Bill());
        mListener = listener;
    }

    public void update() {
        Cursor result = dbHelper.getReadableDatabase().query(SqliteHelper.BILL_TABLE,
                new String[]{"id", "type", "comment", "date", "income", "outcome", "way"},
                null, null, null, null, null);
        this.mValues.clear();
        if (result.moveToFirst()) {
            while (!result.isAfterLast()) {
                int id = result.getInt(0);
                String type = result.getString(1);
                String comment = result.getString(2);
                String dateStr = result.getString(3);
                double income = result.getDouble(4);
                double outcome = result.getDouble(5);
                String way = result.getString(6);
                // do something useful with these

                // parse date
                SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
                df.applyPattern("yyyy-MM-dd");
                Date date = new Date();
                try {
                    date = df.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Bill bill = new Bill(type, income, outcome, date, way, comment);
                bill.setId(id);

                this.mValues.add(bill);
                result.moveToNext();
            }
        }
        Collections.reverse(mValues);
        result.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bill, parent, false);
        this.context = parent.getContext();
        this.dbHelper = new SqliteHelper(context, SqliteHelper.DB_NAME, null, SqliteHelper.Version);
        update();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mValues.isEmpty()) {
            return;
        }
        Log.d("BillRecyclerViewAdapter", "----" + position + "----");
        holder.mItem = mValues.get(position);
        String type = holder.mItem.getType();

        holder.mBillType.setText(type);
        holder.mBillComment.setText(holder.mItem.getComment());
        holder.mBillDate.setText(holder.mItem.dateString());

        double income = holder.mItem.getIncome();
        double outcome = holder.mItem.getOutcome();

        if (income > 0) {
            holder.mBillMoney.setText("+" + income + "￥");
        } else if (outcome > 0) {
            holder.mBillMoney.setText("-" + outcome + "￥");
        }

        // 根据不同的类型设置不同的背景色
        if (type.equals(Bill.TYPE_FOOD)) {
            holder.mBillType.setBackgroundResource(R.drawable.type_food_background);
        } else if (type.equals(Bill.TYPE_ENTERTAINMENT)) {
            holder.mBillType.setBackgroundResource(R.drawable.type_entert_background);
        } else if (type.equals(Bill.TYPE_MEDICAL)) {
            holder.mBillType.setBackgroundResource(R.drawable.type_medical_background);
        } else if (type.equals(Bill.TYPE_SHOPPING)) {
            holder.mBillType.setBackgroundResource(R.drawable.type_shopping_background);
        } else if (type.equals(Bill.TYPE_SPORT)) {
            holder.mBillType.setBackgroundResource(R.drawable.type_sports_background);
        } else if (type.equals(Bill.TYPE_STUDY)) {
            holder.mBillType.setBackgroundResource(R.drawable.type_study_background);
        } else if (type.equals(Bill.TYPE_TRAFFC)) {
            holder.mBillType.setBackgroundResource(R.drawable.type_traffic_background);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mBillComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBillComment(position);
            }
        });
    }

    private boolean updateBillComment(int position) {
        Bill bill = mValues.get(position);
        showInputDialog(bill);
        return true;
    }

    private boolean updateBillMoney(int position) {
        return false;
    }

    private void showInputDialog(final Bill bill) {
        /*@setView 装入一个EditView
         */

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);
        TextView title = view.findViewById(R.id.title);
        final EditText commentEdit = view.findViewById(R.id.bill_comment);
        commentEdit.setText(bill.getComment());
        title.setText("消费备注");

        final AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(context);
        inputDialog.setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                bill.setComment(commentEdit.getText().toString());
                updateBill(bill);
                notifyDataSetChanged();
            }
        });
        inputDialog.create().show();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bill_comment:
                Toast.makeText(context, "骚烤", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bill_money:
                break;
        }
    }

    private void updateBill(Bill bill) {
        ContentValues values = new ContentValues();
        values.put("comment", bill.getComment());
        dbHelper.getWritableDatabase().update(SqliteHelper.BILL_TABLE, values, "id=?", new String[]{"" + bill.getId()});
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mBillType;
        public final TextView mBillComment;
        public final TextView mBillDate;
        public final TextView mBillMoney;
        public Bill mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBillType = (TextView) view.findViewById(R.id.bill_type);
            mBillComment = (TextView) view.findViewById(R.id.bill_comment);
            mBillDate = (TextView) view.findViewById(R.id.bill_date);
            mBillMoney = (TextView) view.findViewById(R.id.bill_money);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mBillComment.getText() + "'";
        }
    }
}
