package com.lm.customstart05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CustomStartView.TouchNumberListener {

    private CustomStartView mStartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartView = (CustomStartView) findViewById(R.id.start_view);
        //设置监听
        mStartView.setTouchNumberListener(this);

    }


    @Override
    public void touchNumber(int touchNumber) {
        Toast.makeText(this, "星星数目" + touchNumber, Toast.LENGTH_SHORT).show();
    }
}
