package com.edu.unicorn;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.viewpager.widget.ViewPager;

import com.edu.unicorn.db.SqliteHelper;
import com.edu.unicorn.entity.Bill;
import com.edu.unicorn.ui.main.BillFragment;
import com.edu.unicorn.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TableActivity extends AppCompatActivity {

    private SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        dbHelper = new SqliteHelper(this, SqliteHelper.DB_NAME, null, SqliteHelper.Version);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                View dialogView = LayoutInflater.from(TableActivity.this).inflate(R.layout.dialog_create_bill, null);

                final AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(TableActivity.this);

                final AppCompatSpinner type = dialogView.findViewById(R.id.bill_type);
                final Bill bill = new Bill();

                type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String billType = type.getSelectedItem().toString();
                        bill.setType(billType);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        bill.setType(Bill.TYPE_FOOD);
                    }
                });

                final EditText billComment = dialogView.findViewById(R.id.bill_comment);
                final TextView billDate = dialogView.findViewById(R.id.bill_date);
                final EditText billIncome = dialogView.findViewById(R.id.bill_income);
                final EditText billOutcome = dialogView.findViewById(R.id.bill_outcome);

                final Calendar now = Calendar.getInstance();
                final int year = now.get(Calendar.YEAR);
                final int month = now.get(Calendar.MONTH);
                final int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
                String hint = "";

                if (month + 1 < 10 && dayOfMonth < 10) {
                    hint = year + "-0" + (month + 1) + "-0" + dayOfMonth;
                } else if (month + 1 < 10 && dayOfMonth >= 10) {
                    hint = year + "-0" + (month + 1) + "-" + dayOfMonth;
                } else if (month + 1 >= 10 && dayOfMonth < 10) {
                    hint = year + "-" + (month + 1) + "-0" + dayOfMonth;
                } else if (month + 1 >= 10 && dayOfMonth >= 10) {
                    hint = year + "-" + (month + 1) + "-" + dayOfMonth;
                }

                billDate.setHint(hint);
                billDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String data = "";
                                if (month + 1 < 10 && dayOfMonth < 10) {
                                    data = year + "-0" + (month + 1) + "-0" + dayOfMonth;
                                } else if (month + 1 < 10 && dayOfMonth >= 10) {
                                    data = year + "-0" + (month + 1) + "-" + dayOfMonth;
                                } else if (month + 1 >= 10 && dayOfMonth < 10) {
                                    data = year + "-" + (month + 1) + "-0" + dayOfMonth;
                                } else if (month + 1 >= 10 && dayOfMonth >= 10) {
                                    data = year + "-" + (month + 1) + "-" + dayOfMonth;
                                }

                                billDate.setText(data);
                            }
                        };

                        DatePickerDialog dialog = new DatePickerDialog(TableActivity.this,
                                DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, dateSetListener,
                                year, month, dayOfMonth);
                        dialog.show();
                    }
                });

                inputDialog.setView(dialogView).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
                        df.applyPattern("yyyy-MM-dd");
                        Date date = new Date();
                        try {
                            date = df.parse(billDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (billIncome.getText().toString().equals("")) {
                            billIncome.setText("0.0");
                        }
                        if (billOutcome.getText().toString().equals("")) {
                            billOutcome.setText("0.0");
                        }

                        double income = 0.0;
                        double outcome = 0.0;
                        income = Double.parseDouble(billIncome.getText().toString());
                        outcome = Double.parseDouble(billOutcome.getText().toString());

                        bill.setComment(billComment.getText().toString());
                        bill.setDate(date);
                        bill.setIncome(income);
                        bill.setOutcome(outcome);
                        bill.setWay(Bill.WAY_CASH);

                        insertBill(bill);

                        BillFragment billFragment = (BillFragment) sectionsPagerAdapter.getItem(0);
                        billFragment.notifyDataSetChanged();

                    }
                });
                inputDialog.create().show();
            }
        });
    }

    private boolean insertBill(Bill bill) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", bill.getType());
        values.put("comment", bill.getComment());
        values.put("date", bill.dateString());
        values.put("income", bill.getIncome());
        values.put("outcome", bill.getOutcome());
        values.put("way", bill.getWay());
        db.insert(SqliteHelper.BILL_TABLE, null, values);
        db.close();
        return true;
    }
}