package com.shanshan.myaccountbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shanshan.myaccountbook.database.DBTablesDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pie_chart);

        List<String> list = new ArrayList<>();
        list.add(Boolean.TRUE + "#" + Boolean.TRUE + "#" + DBTablesDefinition.Accounts.TABLE_ACCOUNT_NAME + "." + DBTablesDefinition.Accounts.COLUMN_ACCOUNT_NAME);
        list.add(Boolean.FALSE + "#" + Boolean.TRUE + "#" + DBTablesDefinition.Accounts.TABLE_ACCOUNT_NAME + "." + DBTablesDefinition.Accounts.COLUMN_ACCOUNT_NAME);
        list.add(Boolean.TRUE + "#" + Boolean.FALSE + "#" + DBTablesDefinition.IncomeOrExpenses.TABLE_INOREXP_NAME + "." + DBTablesDefinition.IncomeOrExpenses.COLUMN_INOREXP_NAME);
        list.add(Boolean.FALSE + "#" + Boolean.FALSE + "#" + DBTablesDefinition.IncomeOrExpenses.TABLE_INOREXP_NAME + "." + DBTablesDefinition.IncomeOrExpenses.COLUMN_INOREXP_NAME);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), list);

        ViewPager vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(adapter);
    }


    public class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<String> fragmentArgs;

        public FragmentAdapter(android.support.v4.app.FragmentManager fm, List<String> fragmentArgs) {
            super(fm);
            this.fragmentArgs = fragmentArgs;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            String[] args = fragmentArgs.get(position).split("#");
            return new PlaceholderFragment(Boolean.valueOf(args[0]), Boolean.valueOf(args[1]), args[2]);
        }

        @Override
        public int getCount() {
            return fragmentArgs.size();
        }
    }

    /**
     * A fragment containing a pie chart.
     */
    public class PlaceholderFragment extends Fragment {
        private List<SliceValue> values = new ArrayList<SliceValue>();
        private PieChartView chart;
        private PieChartData data;

        private boolean hasLabels = true;
        private boolean hasLabelsOutside = false;
        private boolean hasCenterCircle = true;
        private boolean hasCenterText1 = true;
        private boolean hasCenterText2 = true;
        private boolean isExploded = false;
        private boolean hasLabelForSelected = true;

        private boolean isIncome = Boolean.TRUE;
        private String centerText1 = null;
        private String centerText2 = null;
        private String groupBy = null;

        public PlaceholderFragment() {
        }

        public PlaceholderFragment(boolean isIncome, boolean isAccount, String groupBy) {
            this.isIncome = isIncome;
            this.centerText1 = isIncome ? "收入" : "支出";
            this.centerText2 = isAccount ? "账户类别" : "收支类别";
            this.groupBy = groupBy;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false);
            chart = (PieChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ChartOnValueSelectListener());

            toggleLabelForSelected();
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            System.out.println("piechart destroy view..");
            onDestroy();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            System.out.println("piechart destroy..");
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            getMenuInflater().inflate(R.menu.pie_chart, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }


        /*@Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_reset) {
                reset();
                generateData();
                return true;
            }
            if (id == R.id.action_explode) {
                explodeChart();
                return true;
            }
            if (id == R.id.action_center_circle) {
                hasCenterCircle = !hasCenterCircle;
                if (!hasCenterCircle) {
                    hasCenterText1 = false;
                    hasCenterText2 = false;
                }

                generateData();
                return true;
            }
            if (id == R.id.action_center_text1) {
                hasCenterText1 = !hasCenterText1;

                if (hasCenterText1) {
                    hasCenterCircle = true;
                }

                hasCenterText2 = false;

                generateData();
                return true;
            }
            if (id == R.id.action_center_text2) {
                hasCenterText2 = !hasCenterText2;

                if (hasCenterText2) {
                    hasCenterText1 = true;// text 2 need text 1 to by also drawn.
                    hasCenterCircle = true;
                }

                generateData();
                return true;
            }
            if (id == R.id.action_toggle_labels) {
                toggleLabels();
                return true;
            }
            if (id == R.id.action_toggle_labels_outside) {
                toggleLabelsOutside();
                return true;
            }
            if (id == R.id.action_animate) {
                chart.startDataAnimation();
                return true;
            }
            if (id == R.id.action_toggle_selection_mode) {
                toggleLabelForSelected();
                Toast.makeText(getActivity(),
                        "Selection mode set to " + chart.isValueSelectionEnabled() + " select any point.",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }*/

        private void reset() {
            chart.setCircleFillRatio(1.0f);
            hasLabels = false;
            hasLabelsOutside = false;
            hasCenterCircle = false;
            hasCenterText1 = false;
            hasCenterText2 = false;
            isExploded = false;
            hasLabelForSelected = false;
        }

        private void generateData() {
            MyDBHelper myDBHelper = MyDBHelper.newInstance(null);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            String sql = "select records.id,accounts.name,records.date,SUM(records.amount),incomeOrExpenses.name from records,accounts,incomeOrExpenses where records.accountNameId=accounts.id and records.incomeOrExpense=incomeOrExpenses.id and incomeOrExpenses.flag=" + (isIncome ? "0" : "1") + " group by " + groupBy;

            Cursor cursor = db.rawQuery(sql, null);
            System.out.println("Cursor count is " + cursor.getCount());
            BigDecimal bigDecimal = null;
            float amount = 0.0f;
            while (cursor.moveToNext()) {
                System.out.println(cursor.getInt(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getDouble(3) + " " + cursor.getString(4));

                bigDecimal = new BigDecimal(cursor.getDouble(3));
                bigDecimal.setScale(2, 4);
                amount = bigDecimal.floatValue();

                SliceValue sliceValue = new SliceValue(amount, ChartUtils.nextColor());

                String label = (groupBy.contains(DBTablesDefinition.IncomeOrExpenses.TABLE_INOREXP_NAME) ? cursor.getString(4) : cursor.getString(1));
                sliceValue.setLabel(label + " " + amount);
                values.add(sliceValue);
            }

            data = new PieChartData(values);
            data.setHasLabels(hasLabels);
            data.setHasLabelsOnlyForSelected(hasLabelForSelected);
            data.setHasLabelsOutside(hasLabelsOutside);
            data.setHasCenterCircle(hasCenterCircle);


            if (isExploded) {
                data.setSlicesSpacing(24);
            }

            if (hasCenterText1) {
                data.setCenterText1(this.centerText1);

                // Get roboto-italic font.
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
                data.setCenterText1Typeface(tf);

                // Get font size from dimens.xml and convert it to sp(library uses sp values).
                data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                        (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
            }

            if (hasCenterText2) {
                data.setCenterText2(this.centerText2);

                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

                data.setCenterText2Typeface(tf);
                data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                        (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
            }

            chart.setPieChartData(data);
        }

        private void explodeChart() {
            isExploded = !isExploded;
            generateData();

        }

        private void toggleLabelsOutside() {
            // has labels have to be true:P
            hasLabelsOutside = !hasLabelsOutside;
            if (hasLabelsOutside) {
                hasLabels = true;
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);
            }

            if (hasLabelsOutside) {
                chart.setCircleFillRatio(0.7f);
            } else {
                chart.setCircleFillRatio(1.0f);
            }

            generateData();

        }

        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);

                if (hasLabelsOutside) {
                    chart.setCircleFillRatio(0.7f);
                } else {
                    chart.setCircleFillRatio(1.0f);
                }
            }

            generateData();
        }

        private void toggleLabelForSelected() {
            hasLabelForSelected = !hasLabelForSelected;

            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelForSelected) {
                hasLabels = false;
                hasLabelsOutside = false;

                if (hasLabelsOutside) {
                    chart.setCircleFillRatio(0.7f);
                } else {
                    chart.setCircleFillRatio(1.0f);
                }
            }

            generateData();
        }

        @Override
        public void onDetach() {
            super.onDetach();
        }

        class ChartOnValueSelectListener implements PieChartOnValueSelectListener {
            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {
                Toast.makeText(getActivity(), String.valueOf(value.getLabelAsChars()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }
        }
    }
}
