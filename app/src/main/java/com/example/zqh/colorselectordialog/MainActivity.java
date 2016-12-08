package com.example.zqh.colorselectordialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zqh.colorselectordialog.libs.RecouseSettingInf;

/**
 * 颜色选择对话框。
 *
 * @author zqh 2016-12-08
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,RecouseSettingInf {

    private Button hsv_btn;
    private Button colorselector_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
    }

    private void findView() {
        hsv_btn = (Button) findViewById(R.id.hsv_btn);
        colorselector_btn = (Button) findViewById(R.id.colorselector_btn);

        hsv_btn.setOnClickListener(this);
        colorselector_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hsv_btn:
                statrtInten(HsvDialogActivity.class);
                break;
            case R.id.colorselector_btn:

                break;
        }
    }

    private void statrtInten(Class aClass) {
        Intent intent = new Intent(MainActivity.this, aClass);
        startActivity(intent);
    }

    @Override
    public int getTineSkinColor() {
        return 0;
    }
}
