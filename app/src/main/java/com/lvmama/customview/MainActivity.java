package com.lvmama.customview;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.lvmama.canvastest.CanvasActivity;
import com.lvmama.piechart.PieChartActivity;
import com.lvmama.progressbar.ProgressActivity;
import com.lvmama.refreshableview.RefreshListener;
import com.lvmama.refreshableview.RefreshableLayout;
import com.lvmama.topbar.TopBarActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RefreshableLayout mRefreshableLayout;
    private ListView mListView;

    private ArrayAdapter<String> mAdapter;


    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Button mButton;
    private Button mBtnPie;
    private Button mBtnProgress;
    private Button mBtnTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<String> dada = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dada.add(i + "项目");
        }

        mRefreshableLayout = (RefreshableLayout) findViewById(R.id.refresh);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter = new ArrayAdapter<String>(this, R.layout.item, R.id.text, dada));
        mRefreshableLayout.setRefreshListener(new RefreshListener() {
            @Override
            public void refresh() {

                new AsyncTask<Void, Void, Void>(

                ) {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < 3; i++) {
                            dada.add(0, -i + "项目");
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        mAdapter.notifyDataSetChanged();
                        mRefreshableLayout.stopRefreshing();
                    }
                }.execute();
            }
        });

      /*  mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 3; i++) {
                    dada.add(0, -i + "项目");
                }
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });*/


        init();

    }

    private void init() {
        Click click = new Click();

        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(click);
        mBtnPie = (Button) findViewById(R.id.btn_pie);
        mBtnPie.setOnClickListener(click);
        mBtnProgress = (Button) findViewById(R.id.btn_progress);
        mBtnProgress.setOnClickListener(click);
        mBtnTopBar = (Button) findViewById(R.id.btn_top_bar);
        mBtnTopBar.setOnClickListener(click);
    }

    class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn:
                    Intent intent = new Intent(MainActivity.this, CanvasActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_pie:
                    intent = new Intent(MainActivity.this, PieChartActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_progress:
                    intent = new Intent(MainActivity.this, ProgressActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_top_bar:
                    intent = new Intent(MainActivity.this, TopBarActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
