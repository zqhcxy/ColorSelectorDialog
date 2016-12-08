package com.example.zqh.colorselectordialog.weight;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;

import com.example.zqh.colorselectordialog.R;
import com.example.zqh.colorselectordialog.libs.RecouseSettingInf;


/**
 * Created by yzsy on 2016/4/27.
 */
public class AlertDialogFix extends android.support.v7.app.AlertDialog{

    private int  positiveColor ;
    private int negativeColor;
    private int neutralColor;


//    private static ListView lv;
    private Context context;
    public static int color;

    protected AlertDialogFix(Context _context) {
        this(_context,getHcTheme());
        context=_context;
    }

    protected AlertDialogFix(Context context, int theme) {
        super(context, theme);
        initThemeColor(this.getContext());
    }

    public AlertDialogFix(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context,cancelable,cancelListener);
    }

    public static int getHcTheme(){
        int theme;
//        if (AppToolUtil.isNightMode()){
//            theme= R.style.defaultAlertDialogStyle;
//        }else{
            theme=R.style.defaultAlertDialogLightStyle;
//        }
        return  theme;
    }

    public static void initThemeColor(Context cnt){
        int colorAttr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAttr = android.R.attr.colorAccent;
        } else {
            //Get colorAccent defined for AppCompat
            colorAttr = cnt.getResources().getIdentifier("colorAccent", "attr",cnt.getPackageName());
        }
        TypedValue outValue = new TypedValue();
        cnt.getTheme().resolveAttribute(colorAttr,outValue, true);
        color= outValue.data;
    }


    @Override
    public void create() {
        super.create();
    }

    @Override
    public void show() {

        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface di) {
                Button positive = getButton(DialogInterface.BUTTON_POSITIVE);
                Button negative =getButton(DialogInterface.BUTTON_NEGATIVE);
                Button neutral = getButton(DialogInterface.BUTTON_NEUTRAL);
//                if (AppToolUtil.isNightMode())  {
//
//                }else{
                    RecouseSettingInf recouseSettingInf=null;
                    if(context instanceof RecouseSettingInf) {
                        recouseSettingInf = (RecouseSettingInf) context;
                    }else if(context instanceof ContextThemeWrapper) {
                        try {
                            recouseSettingInf = (RecouseSettingInf) ((ContextThemeWrapper) context).getBaseContext();
                        }catch (Exception e){

                        }
                    }
                    if (recouseSettingInf != null) {
                        int checkColor = recouseSettingInf.getTineSkinColor();
                        positive.setTextColor(checkColor);

                        if (positive != null && positiveColor != 0) {
                            positive.setTextColor(positiveColor);
                        }
                        negative.setTextColor(checkColor);
                        if (negative != null && negativeColor != 0) {
                            negative.setTextColor(negativeColor);
                        }
                        neutral.setTextColor(checkColor);
                        if (neutral != null && neutralColor != 0) {
                            neutral.setTextColor(neutralColor);
                        }
                    }
                }
//            }
        });

        super.show();
    }



    public static class Builder extends android.support.v7.app.AlertDialog.Builder{

        private Context context;
        private int  positiveColor ;
        private int negativeColor;
        private int neutralColor;

        private Drawable mButtonPositiveBackground;

        private int mButtonPositiveTextColor;

        public Builder(Context context) {
            this(context,getHcTheme());
            this.context=context;
        }



        public Builder(Context context,int theme) {
            super(context,theme);
            AlertDialogFix.initThemeColor(this.getContext());
        }



        @Override
        public Builder setView(View view, int viewSpacingLeft, int viewSpacingTop,
                               int viewSpacingRight, int viewSpacingBottom) {
            return (Builder)super.setView(view,viewSpacingLeft,viewSpacingTop,viewSpacingRight,viewSpacingBottom);
        }

        @Override
        public Builder setView(View view) {
            return (Builder)super.setView(view);
        }

        @Override
        public Builder setView(int layoutResId) {
            return (Builder)super.setView(layoutResId);
        }

        @Override
        public Builder setTitle(CharSequence title) {
            return (Builder)super.setTitle(title);
        }

        @Override
        public Builder setTitle(int titleId) {
            return (Builder)super.setTitle(titleId);
        }

        @Override
        public android.support.v7.app.AlertDialog create() {

            final android.support.v7.app.AlertDialog dialog = super.create();

            dialog.setOnShowListener(new OnShowListener() {
                @Override
                public void onShow(DialogInterface di) {
                    Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    Button neutral = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                        RecouseSettingInf recouseSettingInf=null;
                        if(context instanceof RecouseSettingInf) {
                            recouseSettingInf = (RecouseSettingInf) context;
                        }else if(context instanceof ContextThemeWrapper) {
                            try {
                                recouseSettingInf = (RecouseSettingInf) ((ContextThemeWrapper) context).getBaseContext();
                            }catch (Exception e){

                            }
                        }
                        if (recouseSettingInf != null) {
                            int checkColor = recouseSettingInf.getTineSkinColor();

                            positive.setTextColor(checkColor);

                            if (positive != null && positiveColor != 0) {
                                positive.setTextColor(positiveColor);
                            }
                            negative.setTextColor(checkColor);
                            if (negative != null && negativeColor != 0) {
                                negative.setTextColor(negativeColor);
                            }

                            neutral.setTextColor(checkColor);
                            if (neutral != null && neutralColor != 0) {
                                neutral.setTextColor(neutralColor);
                            }
                        }
                    }
            });

            return dialog;
        }

        @Override
        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems,
                                           final OnMultiChoiceClickListener listener) {
            return (Builder)super.setMultiChoiceItems(items,checkedItems,listener);
        }

        @Override
        public Builder setSingleChoiceItems(int itemsId, int checkedItem, OnClickListener listener) {
            return (Builder)this.setSingleChoiceItems(context.getResources().getTextArray(itemsId), checkedItem, listener);
        }

        @Override
        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, OnClickListener listener) {

//            CustomDialog.SingleChoiceIconItemAdapter singleChoiceIconItemAdapter=new CustomDialog.SingleChoiceIconItemAdapter(getContext(),R.layout.alert_dialog_singlechoice_icon_material,R.id.ll_item,checkedItem,items,null,false);
//            return this.setSingleChoiceItems(singleChoiceIconItemAdapter,checkedItem,listener);
            return (Builder)super.setSingleChoiceItems(items, checkedItem, listener);
        }

        @Override
        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, OnClickListener listener) {
            return (Builder)super.setSingleChoiceItems(cursor, checkedItem, labelColumn, listener);
        }

        @Override
        public Builder setAdapter(ListAdapter adapter, OnClickListener listener) {
            return (Builder)super.setAdapter(adapter, listener);
        }

        @Override
        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, OnClickListener listener) {
            return (Builder)super.setSingleChoiceItems(adapter, checkedItem, listener);
        }

        @Override
        public Builder setRecycleOnMeasureEnabled(boolean enabled) {
            return (Builder)super.setRecycleOnMeasureEnabled(enabled);
        }

        @Override
        public Builder setPositiveButton(int textId, OnClickListener listener) {
            return (Builder)super.setPositiveButton(textId, listener);
        }

        @Override
        public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
            return (Builder)super.setPositiveButton(text, listener);
        }

        @Override
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            return (Builder)super.setOnKeyListener(onKeyListener);
        }

        @Override
        public Builder setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
            return (Builder)super.setOnItemSelectedListener(listener);
        }

        @Override
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            return (Builder)super.setOnDismissListener(onDismissListener);
        }

        @Override
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            return (Builder)super.setOnCancelListener(onCancelListener);
        }

        @Override
        public Builder setNeutralButton(int textId, OnClickListener listener) {
            return (Builder)super.setNeutralButton(textId, listener);
        }

        @Override
        public Builder setNeutralButton(CharSequence text, OnClickListener listener) {
            return (Builder)super.setNeutralButton(text, listener);
        }

        @Override
        public Builder setNegativeButton(int textId, OnClickListener listener) {
            return (Builder)super.setNegativeButton(textId, listener);
        }

        @Override
        public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems, OnMultiChoiceClickListener listener) {
            return (Builder)super.setMultiChoiceItems(itemsId, checkedItems, listener);
        }

        @Override
        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            return (Builder)super.setNegativeButton(text, listener);
        }

        @Override
        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, OnMultiChoiceClickListener listener) {
            return (Builder)super.setMultiChoiceItems(cursor, isCheckedColumn, labelColumn, listener);
        }

        @Override
        public Builder setMessage(int messageId) {
            return (Builder)super.setMessage(messageId);
        }

        @Override
        public Builder setMessage(CharSequence message) {
            return (Builder)super.setMessage(message);
        }

        @Override
        public Builder setItems(int itemsId, OnClickListener listener) {
            return (Builder)super.setItems(itemsId, listener);
        }

        @Override
        public Builder setItems(CharSequence[] items, OnClickListener listener) {
            return (Builder)super.setItems(items, listener);
        }

        @Override
        public Builder setInverseBackgroundForced(boolean useInverseBackground) {
            return (Builder)super.setInverseBackgroundForced(useInverseBackground);
        }

        @Override
        public Builder setIconAttribute(int attrId) {
            return (Builder)super.setIconAttribute(attrId);
        }

        @Override
        public Builder setIcon(int iconId) {
            return (Builder)super.setIcon(iconId);
        }

        @Override
        public Builder setCustomTitle(View customTitleView) {
            return (Builder)super.setCustomTitle(customTitleView);
        }

        @Override
        public Builder setIcon(Drawable icon) {
            return (Builder)super.setIcon(icon);
        }

        @Override
        public Builder setCursor(Cursor cursor, OnClickListener listener, String labelColumn) {
            return (Builder)super.setCursor(cursor, listener, labelColumn);
        }

        @Override
        public Builder setCancelable(boolean cancelable) {
            return (Builder)super.setCancelable(cancelable);
        }

        public Builder setPositiveButton(CharSequence text, int color, final OnClickListener listener) {
            positiveColor=color;
            return (Builder)super.setPositiveButton(text,listener);
        }

        public Builder setNegativeButton(CharSequence text,int color,final OnClickListener listener) {
            negativeColor=color;
            return (Builder)super.setNegativeButton(text,listener);
        }


        public Builder setNeutralButton(CharSequence text,int color,final OnClickListener listener) {
            neutralColor=color;
            return (Builder)super.setNeutralButton(text,listener);
        }


//        public Builder setSingleChoiceIconItems(CharSequence[] items,int[] iconResource, int checkedItem,OnClickListener listener) {
//            CustomDialog.SingleChoiceIconItemAdapter singleChoiceIconItemAdapter=new CustomDialog.SingleChoiceIconItemAdapter(getContext(),R.layout.alert_dialog_singlechoice_icon_material,R.id.ll_item,checkedItem,items,iconResource,true);
//            return this.setSingleChoiceItems(singleChoiceIconItemAdapter,checkedItem,listener);
//        }


        @Override
        public android.support.v7.app.AlertDialog show() {
            final android.support.v7.app.AlertDialog dialog=this.create();
            dialog.show();
            return dialog;
        }
















    }

}

