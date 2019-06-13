package com.edu.unicorn.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.unicorn.R;
import com.edu.unicorn.db.SqliteHelper;
import com.edu.unicorn.entity.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static StatisticsFragment fragment;

    private OnFragmentInteractionListener mListener;

    private AppCompatSpinner yearSpinner;
    private AppCompatSpinner monthSpinner;
    private AppCompatSpinner daySpinner;
    private TextView yearIncome;
    private TextView monthIncome;
    private TextView dayIncome;
    private TextView yearOutcome;
    private TextView monthOutcome;
    private TextView dayOutcome;

    private SqliteHelper dbHelper;

    public StatisticsFragment() {
        // Required empty public constructor
        dbHelper = new SqliteHelper(getContext(), SqliteHelper.DB_NAME, null, SqliteHelper.Version);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(int code) {
        if (fragment == null) {
            fragment = new StatisticsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_PARAM1, code);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        yearSpinner = view.findViewById(R.id.year);
        monthSpinner = view.findViewById(R.id.month);
        daySpinner = view.findViewById(R.id.day);

        yearIncome = view.findViewById(R.id.year_income);
        monthIncome = view.findViewById(R.id.month_income);
        dayIncome = view.findViewById(R.id.day_income);

        yearOutcome = view.findViewById(R.id.year_outcome);
        monthOutcome = view.findViewById(R.id.month_outcome);
        dayOutcome = view.findViewById(R.id.day_outcome);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String year = yearSpinner.getSelectedItem().toString();
                List<Bill> bills = queryBillByYear(year);
                double incomes = 0;
                double outcomes = 0;

                for (Bill bill :
                        bills) {
                    incomes += bill.getIncome();
                    outcomes += bill.getOutcome();
                }
                yearIncome.setText("全年总收入 " + incomes + " 元");
                yearOutcome.setText("全年总支出 " + outcomes + " 元");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String month = yearSpinner.getSelectedItem().toString() + "-" + monthSpinner.getSelectedItem().toString();
                List<Bill> bills = queryBillByYear(month);
                double incomes = 0;
                double outcomes = 0;

                for (Bill bill :
                        bills) {
                    incomes += bill.getIncome();
                    outcomes += bill.getOutcome();
                }

                monthIncome.setText("本月总收入 " + incomes + " 元");
                monthOutcome.setText("本月总支出 " + outcomes + " 元");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String month = yearSpinner.getSelectedItem().toString() + "-"
                        + monthSpinner.getSelectedItem().toString() + "-"
                        + daySpinner.getSelectedItem().toString();
                List<Bill> bills = queryBillByYear(month);
                double incomes = 0;
                double outcomes = 0;

                for (Bill bill :
                        bills) {
                    incomes += bill.getIncome();
                    outcomes += bill.getOutcome();
                }

                dayIncome.setText("当日总收入 " + incomes + " 元");
                dayOutcome.setText("当日总支出 " + outcomes + " 元");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private List<Bill> queryBillByYear(String year) {
//        dbHelper.getReadableDatabase().query()
        return new ArrayList<>();
    }

    private List<Bill> queryBillByMonth(String month) {
        return new ArrayList<>();
    }

    private List<Bill> queryBillByDate(String date) {
        return new ArrayList<>();
    }
}
