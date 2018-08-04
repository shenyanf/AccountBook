package com.shanshan.myaccountbook.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.shanshan.myaccountbook.R;
import com.shanshan.myaccountbook.database.DBTablesDefinition.AccountsDefinition;
import com.shanshan.myaccountbook.database.DBTablesDefinition.IncomeOrExpensesDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;
import com.shanshan.myaccountbook.util.MyAccountUtil;
import com.shanshan.myaccountbook.util.MyLogger;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class ChartActivity extends AppCompatActivity {
    private Logger myLogger = MyLogger.getMyLogger(ChartActivity.class.getName());
    private static Spinner spinnerStartYear = null;
    private ArrayAdapter adapterStartYear = null;
    private static Spinner spinnerStartMonth = null;
    private ArrayAdapter adapterStartMonth = null;

    private static Spinner spinnerEndYear = null;
    private ArrayAdapter adapterEndYear = null;
    private static Spinner spinnerEndMonth = null;
    private ArrayAdapter adapterEndMonth = null;
    private static List<Integer> years = MyAccountUtil.range(2010, 2021, 1);
    private static List<Integer> months = MyAccountUtil.range(1, 13, 1);
    private static List<PlaceholderFragment> fragments = new ArrayList<PlaceholderFragment>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chart);

        List<String> list = new ArrayList<>();
        list.add(Boolean.TRUE + "#" + Boolean.TRUE + "#" + AccountsDefinition.TABLE_ACCOUNT_NAME + "." + AccountsDefinition.COLUMN_ACCOUNT_NAME);
        list.add(Boolean.FALSE + "#" + Boolean.TRUE + "#" + AccountsDefinition.TABLE_ACCOUNT_NAME + "." + AccountsDefinition.COLUMN_ACCOUNT_NAME);
        list.add(Boolean.TRUE + "#" + Boolean.FALSE + "#" + IncomeOrExpensesDefinition.TABLE_INOREXP_NAME + "." + IncomeOrExpensesDefinition.COLUMN_INOREXP_NAME);
        list.add(Boolean.FALSE + "#" + Boolean.FALSE + "#" + IncomeOrExpensesDefinition.TABLE_INOREXP_NAME + "." + IncomeOrExpensesDefinition.COLUMN_INOREXP_NAME);
        final FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), list);

        ViewPager vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(adapter);

        // 日期范围
        spinnerStartYear = (Spinner) findViewById(R.id.spinner_start_year);
        adapterStartYear = new ArrayAdapter<Integer>(this, R.layout.spinner_drop_down_layout, years);
        spinnerStartYear.setAdapter(adapterStartYear);
        spinnerStartYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                       @Override
                                                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                           adapter.notifyDataSetChanged();
                                                       }

                                                       @Override
                                                       public void onNothingSelected(AdapterView<?> parent) {

                                                       }
                                                   }
        );

        spinnerStartMonth = (Spinner) findViewById(R.id.spinner_start_month);
        adapterStartMonth = new ArrayAdapter<Integer>(this, R.layout.spinner_drop_down_layout, months);
        spinnerStartMonth.setAdapter(adapterStartMonth);
        spinnerStartMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                            adapter.notifyDataSetChanged();
                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parent) {

                                                        }
                                                    }
        );


        spinnerEndYear = (Spinner) findViewById(R.id.spinner_end_year);
        adapterEndYear = new ArrayAdapter<Integer>(this, R.layout.spinner_drop_down_layout, years);
        spinnerEndYear.setAdapter(adapterEndYear);
        spinnerEndYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                     @Override
                                                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                         adapter.notifyDataSetChanged();
                                                     }

                                                     @Override
                                                     public void onNothingSelected(AdapterView<?> parent) {

                                                     }
                                                 }
        );

        spinnerEndMonth = (Spinner) findViewById(R.id.spinner_end_month);
        adapterEndMonth = new ArrayAdapter<Integer>(this, R.layout.spinner_drop_down_layout, months);
        spinnerEndMonth.setAdapter(adapterEndMonth);
        spinnerEndMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                      @Override
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                          adapter.notifyDataSetChanged();
                                                      }

                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> parent) {

                                                      }
                                                  }
        );

        //设置默认日期
        spinnerStartYear.setSelection(years.indexOf(MyAccountUtil.getCurrentYear()));
        spinnerStartMonth.setSelection(months.indexOf(MyAccountUtil.getCurrentMonth()));
        spinnerEndYear.setSelection(years.indexOf(MyAccountUtil.getCurrentYear()));
        spinnerEndMonth.setSelection(months.indexOf(MyAccountUtil.getCurrentMonth()));

        myLogger.debug("PieChartsActivity create");
    }

    class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<String> fragmentArgs;
        // 当前fragment位置
        public volatile int currentFragmentPos;

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
            currentFragmentPos = position;
            String[] args = fragmentArgs.get(position).split("#");
            if (fragments.size() > position) {
                return fragments.get(position);
            } else {
                PlaceholderFragment placeholderFragment = new PlaceholderFragment(Boolean.valueOf(args[0]), Boolean.valueOf(args[1]), args[2]);
                fragments.add(placeholderFragment);
                return placeholderFragment;
            }
        }


        public int getCurrentFragmentPos() {
            return currentFragmentPos;
        }

        @Override
        public int getCount() {
            return fragmentArgs.size();
        }
    }


    /**
     * A fragment containing a pie pieChartView.
     */
    public static class PlaceholderFragment extends Fragment {
        private PieChartView pieChartView;
        private PieChartData pieChartData;
        private ColumnChartView columnChartView;
        private ColumnChartData columnChartData;

        private boolean hasLabels = true;
        private boolean hasLabelsOutside = true;
        private boolean hasCenterCircle = true;
        private boolean hasCenterText1 = true;
        private boolean hasCenterText2 = true;
        private boolean hasLabelForSelected = true;

        private boolean isIncome = Boolean.TRUE;
        private String centerText1 = null;
        private String centerText2 = null;
        private String groupBy = null;

        public PlaceholderFragment() {

        }

        public PlaceholderFragment(boolean isIncome, boolean isAccount, String groupBy) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isIncome", isIncome);
            bundle.putBoolean("isAccount", isAccount);
            bundle.putString("groupBy", groupBy);
            super.setArguments(bundle);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle args = getArguments();

            this.isIncome = args.getBoolean("isIncome");
            this.centerText1 = isIncome ? "收入" : "支出";
            this.centerText2 = args.getBoolean("isAccount") ? "账户类别" : "收支类别";
            this.groupBy = args.getString("groupBy");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_chart, container, false);
            pieChartView = (PieChartView) rootView.findViewById(R.id.pie_chart);
            pieChartView.setOnValueTouchListener(new PieChartOnValueSelectListener() {
                @Override
                public void onValueSelected(int arcIndex, SliceValue value) {
                    Toast.makeText(getActivity(), String.valueOf(value.getLabelAsChars()), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onValueDeselected() {

                }
            });

            columnChartView = (ColumnChartView) rootView.findViewById(R.id.column_chart);
            columnChartView.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
                @Override
                public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {
                    Toast.makeText(getActivity(), String.valueOf(subcolumnValue.getLabelAsChars()), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onValueDeselected() {

                }
            });
            generateData();
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            onDestroy();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.pie_chart, menu);
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
                pieChartView.startDataAnimation();
                return true;
            }
            if (id == R.id.action_toggle_selection_mode) {
                toggleLabelForSelected();
                Toast.makeText(getActivity(),
                        "Selection mode set to " + pieChartView.isValueSelectionEnabled() + " select any point.",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }*/

        public void generateData() {
            List<SliceValue> sliceValues = new ArrayList<SliceValue>();
            List<Column> columns = new ArrayList<>();

            MyDBHelper myDBHelper = MyDBHelper.newInstance(null);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            int startMonth = months.get(spinnerStartMonth.getSelectedItemPosition());
            List<AxisValue> axisValues = new ArrayList<AxisValue>();

            String startMonthStr = "";
            if (startMonth < 10) {
                startMonthStr = "0" + startMonth;
            } else {
                startMonthStr = String.valueOf(startMonth);
            }

            String startDate = years.get(spinnerStartYear.getSelectedItemPosition()) + "-" + startMonthStr + "-01";

            int endMonth = months.get(spinnerEndMonth.getSelectedItemPosition()) + 1;

            String endMonthStr = "";
            if (endMonth < 10) {
                endMonthStr = "0" + endMonth;
            } else {
                endMonthStr = String.valueOf(endMonth);
            }
            String endDate = years.get(spinnerEndYear.getSelectedItemPosition()) + "-" + endMonthStr;

            String sql = "select records.id,accounts.name,records.date,SUM(records.amount),incomeOrExpenses.name from records,accounts,incomeOrExpenses where " +
                    "records.accountNameId=accounts.id and records.incomeOrExpense=incomeOrExpenses.id and records.date between '" + startDate + "' and '" + endDate + "' and incomeOrExpenses.inOrOut=" + (isIncome ? "0" : "1") + " group by " + groupBy;

            Cursor cursor = db.rawQuery(sql, null);
            BigDecimal bigDecimal = null;
            float amount = 0.0f;
            int i = 0;
            while (cursor.moveToNext()) {
                List<SubcolumnValue> subcolumnValues = new ArrayList<>();
                bigDecimal = new BigDecimal(cursor.getDouble(3));
                bigDecimal.setScale(2, 4);
                amount = bigDecimal.floatValue();

                SliceValue sliceValue = new SliceValue(amount, ChartUtils.nextColor());
                String label = (groupBy.contains(IncomeOrExpensesDefinition.TABLE_INOREXP_NAME) ? cursor.getString(4) : cursor.getString(1));
                sliceValue.setLabel(label + " " + amount);
                sliceValues.add(sliceValue);

                SubcolumnValue subcolumnValue = new SubcolumnValue(amount, ChartUtils.nextColor());
                subcolumnValue.setLabel(amount + "");

                axisValues.add(new AxisValue(i).setLabel(label));
                subcolumnValues.add(subcolumnValue);
                columns.add(new Column(subcolumnValues).setHasLabels(true));
                i++;
            }

            pieChartData = new PieChartData(sliceValues);

            //是否显示文本内容(默认为false)
            pieChartData.setHasLabels(hasLabels);
            //是否点击饼模块才显示文本（默认为false,为true时，setHasLabels(true)无效）
//            pieChartData.setHasLabelsOnlyForSelected(hasLabelForSelected);
            //文本内容是否显示在饼图外侧(默认为false)
            pieChartData.setHasLabelsOutside(hasLabelsOutside);
            pieChartData.setHasCenterCircle(hasCenterCircle);
            //中心圆的颜色（需setHasCenterCircle(true)，因为只有圆环才能看到中心圆）
            pieChartData.setCenterCircleColor(Color.WHITE);
            //中心圆所占饼图比例（0-1）
            pieChartData.setCenterCircleScale(0.5f);
            //饼图各模块的间隔(默认为0)
            pieChartData.setSlicesSpacing(2);

            if (hasCenterText1) {
                pieChartData.setCenterText1(this.centerText1);

                // Get roboto-italic font.
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
                pieChartData.setCenterText1Typeface(tf);

                // Get font size from dimens.xml and convert it to sp(library uses sp sliceValues).
                pieChartData.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                        (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
            }

            if (hasCenterText2) {
                pieChartData.setCenterText2(this.centerText2);

                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

                pieChartData.setCenterText2Typeface(tf);
                pieChartData.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                        (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
            }

            pieChartView.setPieChartData(pieChartData);
            pieChartView.setValueSelectionEnabled(hasLabelForSelected);

            //整个饼图所占视图比例（0-1）
            pieChartView.setCircleFillRatio(0.7f);

            columnChartData = new ColumnChartData(columns);
            Axis axisX = new Axis(axisValues).setHasLines(true).setName(centerText2).setTextColor(Color.BLACK);
            Axis axisY = new Axis().setHasLines(true).setName(centerText1 + "金额");
            columnChartData.setAxisXBottom(axisX);
            columnChartData.setAxisYLeft(axisY);
//            columnChartData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
//            columnChartData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));
            columnChartView.setColumnChartData(columnChartData);
            columnChartView.setZoomType(ZoomType.HORIZONTAL);
//            Viewport viewport = new Viewport(0, columnChartView.getMaximumViewport().height() * 1.25f, columns.size() > 5 ? 5 : columns.size(), 0);
//            columnChartView.setCurrentViewport(viewport);
//            Viewport viewportMax = new Viewport(-0.7f, columnChartView.getMaximumViewport().height() * 1.25f, columns.size(), 0);
//            columnChartView.setMaximumViewport(viewportMax);
//            columnChartView.moveTo(0, 0);
        }

        @Override
        public void onDetach() {
            super.onDetach();
        }
    }

}
