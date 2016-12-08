package com.example.zqh.colorselectordialog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by zqh on 2016/12/8.
 */
public class CommonUtil {


    public static final String HSV_DIALOG_COLOR_VALUE_KEY = "hsvcolor";
    public static SharedPreferences sPerfs = null;


    /**
     * 获取按钮按下的颜色（改变明暗度）
     *
     * @param color 进行调整的颜色值
     * @return 返回经过0.9f明暗度调整后的颜色值
     */
    public static int getVColor(@ColorInt int color, float vf) {
        float[] v = new float[3];
        Color.colorToHSV(color, v);
        v[2] = vf;
        return Color.HSVToColor(v);
    }

    /**
     * 获取被着色的Drawable
     *
     * @param inputDrawable
     * @param color
     * @return
     */
    public static Drawable getTintedDrawable(Drawable inputDrawable, @ColorInt int color) {
        Drawable wrapDrawable = DrawableCompat.wrap(inputDrawable);
        wrapDrawable = wrapDrawable.mutate();
        DrawableCompat.setTint(wrapDrawable, color);
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN);
        return wrapDrawable;
    }


    public static SharedPreferences getSysPrefs(Context cnt) {
        if (sPerfs == null) {
            sPerfs = PreferenceManager.getDefaultSharedPreferences(cnt);
        }
        return sPerfs;

    }


    /**
     * 获取保存的值
     *
     * @param context
     * @param key
     * @param defaultValue
     */
    public static int getSharePreenceValue(Context context, String key, int defaultValue) {
        //ContextCompat.getColor(context, R.color.col_primary) //HSV_DIALOG_COLOR_VALUE_KEY
        SharedPreferences sp = CommonUtil.getSysPrefs(context);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 保存要保存的值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setSharePreenceValue(Context context, String key, int value) {
        SharedPreferences sp = CommonUtil.getSysPrefs(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 移除个性化设置的主题名字
     * @param cnt
     * @param suffix
     */
    public static void removeCustonSkinKey(Context cnt,String key){
        SharedPreferences sp = CommonUtil.getSysPrefs(cnt);
        SharedPreferences.Editor editor=sp.edit();
        editor.remove(key);
        editor.commit();
    }

}
