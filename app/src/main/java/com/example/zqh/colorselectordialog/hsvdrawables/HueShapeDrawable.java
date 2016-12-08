package com.example.zqh.colorselectordialog.hsvdrawables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;

/**
 * HUE四种颜色进行渐变
 * Created by zqh-pc on 2016/8/30.
 */
public class HueShapeDrawable extends ShapeDrawable {
    private Paint mPain = new Paint();
    private Context mContext;
    private int boundw = 0;
    int density;

    public HueShapeDrawable(Context context) {
        mContext = context;
        density = (int) (1.0F * mContext.getResources().getDisplayMetrics().density);//progress高度
    }


    @Override
    public void draw(Canvas canvas) {
        if (boundw != getBounds().width()) {
            boundw = getBounds().width();
            mPain.setShader(new LinearGradient(0.0F, 0.0F, boundw, 0.0F, new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.RED}, null, Shader.TileMode.CLAMP));
        }
        canvas.drawRect(getBounds().left, getBounds().centerY() - density, getBounds().right, getBounds().centerY() + density, mPain);
    }

}
