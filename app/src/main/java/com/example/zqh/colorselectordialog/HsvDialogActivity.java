package com.example.zqh.colorselectordialog;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.zqh.colorselectordialog.libs.RecouseSettingInf;
import com.example.zqh.colorselectordialog.utils.CommonUtil;
import com.example.zqh.colorselectordialog.weight.HSVDialog;

/**
 * 自定义HSV对话框
 *
 * @author zqh 2016-12-08
 */
public class HsvDialogActivity extends AppCompatActivity implements RecouseSettingInf {

    private TextView hsv_color_tv;
    private HSVDialog hsvDialog;

    private HSVDialog.HueRecyclerAdapter hueRecyclerAdapter;
    private int mStartcolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsv_dialog);

        hsv_color_tv = (TextView) findViewById(R.id.hsv_color_tv);

        hsv_color_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
        updataUi();
    }

    /**
     * 创建对话框
     */
    private void createDialog() {
        if (hsvDialog == null) {
            hsvDialog = new HSVDialog(this);
        }

        mStartcolor = CommonUtil.getSharePreenceValue(HsvDialogActivity.this,
                CommonUtil.HSV_DIALOG_COLOR_VALUE_KEY,
                ContextCompat.getColor(HsvDialogActivity.this, R.color.col_primary));
        hsvDialog.setmStartcolor(mStartcolor);
//        hsvDialog.setmSuffix(mPreference.getmSuffix());//需要单独保存可以添加后缀作为区分。
        hsvDialog.setHsvDialogOnClickInterface(hsvDialogOnClickInterface);
        AlertDialog alertDialog = hsvDialog.buildDialog();
        hueRecyclerAdapter = hsvDialog.getHueRecyclerAdapter();
        alertDialog.show();
    }


    /**
     * 对话框的点击事件
     */
    private HSVDialog.HsvDialogOnClickInterface hsvDialogOnClickInterface = new HSVDialog.HsvDialogOnClickInterface() {
        @Override
        public void NegativeButtonOnClickListener(DialogInterface dialog, int which) {

        }

        @Override
        public void PositiveButtonOnClickListener(DialogInterface dialog, int which) {
            CommonUtil.setSharePreenceValue(getApplicationContext(),
                    CommonUtil.HSV_DIALOG_COLOR_VALUE_KEY,
                    getCommitColor());
            updataUi();
        }

        @Override
        public void NeutralButtonOnClickListener(DialogInterface dialog, int which) {
            CommonUtil.removeCustonSkinKey(HsvDialogActivity.this, CommonUtil.HSV_DIALOG_COLOR_VALUE_KEY);
        }
    };

    /**
     * 获取最终确定的颜色
     *
     * @return
     */
    private int getCommitColor() {
        int color;
        int checkpos = hueRecyclerAdapter.getSelectPos();
        if (checkpos == -1) {
            ColorDrawable drawable = (ColorDrawable) hsvDialog.getHue_title().getBackground();
            color = drawable.getColor();
        } else {
            color = hsvDialog.getColors().get(hueRecyclerAdapter.getSelectPos());
        }
        return color;
    }

    private void updataUi(){
        Drawable drawable=CommonUtil.getTintedDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_palette),
                getTineSkinColor());
        hsv_color_tv.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
    }


    @Override
    public int getTineSkinColor() {
        return CommonUtil.getSharePreenceValue(HsvDialogActivity.this,
                CommonUtil.HSV_DIALOG_COLOR_VALUE_KEY,
                ContextCompat.getColor(HsvDialogActivity.this, R.color.col_primary));
    }
}
