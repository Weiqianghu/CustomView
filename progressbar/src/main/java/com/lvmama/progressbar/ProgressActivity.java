package com.lvmama.progressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;


public class ProgressActivity extends AppCompatActivity {
    private HorizonProgressBar mProgressBar1;
    private HorizonProgressBar mProgressBar2;
    private HorizonProgressBar mProgressBar3;

    private static final int MES_PROGRESS = 0x1000;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MES_PROGRESS:
                    int progress = mProgressBar1.getProgress() + 1;
                    mProgressBar1.setProgress(progress);
                    mProgressBar2.setProgress(progress);
                    mProgressBar3.setProgress(progress);
                    mHandler.sendEmptyMessageDelayed(MES_PROGRESS, 100);
                    if (progress > 100) {
                        mHandler.removeMessages(MES_PROGRESS);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        mProgressBar1 = (HorizonProgressBar) findViewById(R.id.progress1);
        mProgressBar2 = (HorizonProgressBar) findViewById(R.id.progress2);
        mProgressBar3 = (HorizonProgressBar) findViewById(R.id.progress3);
        mHandler.sendEmptyMessage(MES_PROGRESS);
    }
}
