package com.example.zqh.colorselectordialog.hsvdrawables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.content.ContextCompat;

import com.example.zqh.colorselectordialog.R;


/**
 * 颜色的明暗度渐变
 * Created by zqh-pc on 2016/8/30.
 */
public class MyShapeDrawable extends ShapeDrawable {
    private Context mContext;
    private int changeColor;
    private int mStartColor;
    private int density;
    private int boundw = 0;//是否已经保存过的宽度，减少不必要的重复操作
    private Paint mPain = new Paint();


    public MyShapeDrawable(Context context, Shape s) {
        super(s);
        mContext = context;
        initData(-1);
    }

    /**
     * @param context
     * @param s
     * @param startColor 初始渐变颜色
     */
    public MyShapeDrawable(Context context, Shape s, int startColor) {
        super(s);
        mContext = context;
        initData(startColor);
    }

    private void initData(int startColor) {
        if (startColor == -1) {
            mStartColor = ContextCompat.getColor(mContext, R.color.c9);
        }else{
            mStartColor=startColor;
        }
        changeColor = ContextCompat.getColor(mContext, R.color.cr_hue_7);
        density = (int) (1.0F * mContext.getResources().getDisplayMetrics().density);
//        mStrokePaint.setStyle(Paint.Style.STROKE);//画边框
    }

    /**
     * 渐变的颜色
     *
     * @param color
     */
    public void setChangeColor(int color) {
        changeColor = color;
        invalidateSelf();
    }

    /**
     * 开始渐变的颜色
     *
     * @param color
     */
    public void setStartColorr(int color) {
        mStartColor = color;
    }

    @Override
    public void draw(Canvas canvas) {
        int x1 = getBounds().left, y1 = getBounds().centerY() - density, x2 = getBounds().right, y2 = getBounds().centerY() + density;
        if (boundw != getBounds().width()) {
            Shader shader = new LinearGradient(x1, y1, x2, y2, mStartColor, changeColor, Shader.TileMode.CLAMP);
            mPain.setShader(shader);
        }
        canvas.drawRect(new RectF(x1, y1, x2, y2), mPain);
    }

}
