package com.lvmama.piechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity {
    private PieChart mPieChart;

    private List<PieData> mPieDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        initData();
        initView();

    }

    private void initView() {
        mPieChart = (PieChart) findViewById(R.id.pie_chart);
        mPieChart.stetData(mPieDatas);
    }

    private void initData() {
        if (mPieDatas == null) {
            mPieDatas = new ArrayList<>();
        }

        for (int i = 0; i < 6; i++) {
            PieData piedata = new PieData();
            piedata.setValue(i * 10);

            mPieDatas.add(piedata);
        }
    }
}
