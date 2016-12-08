package com.example.zqh.colorselectordialog.weight;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.zqh.colorselectordialog.R;
import com.example.zqh.colorselectordialog.hsvdrawables.HueShapeDrawable;
import com.example.zqh.colorselectordialog.hsvdrawables.MyShapeDrawable;
import com.example.zqh.colorselectordialog.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 颜色选择对话框，固定颜色选择、高级设置：动态获取自定义颜色。
 * <p/>
 * <p>必需设置的参数：mSuffix（后缀）、mStartcolor（之前保存的颜色）、HsvDialogOnClickInterface（对话框点击事件接口）
 * Created by zqh on 2016/12/8.
 */
public class HSVDialog {

    private Context mContext;

    private CharSequence mPositiveButtonText;//确认按钮文本
    private CharSequence mNegativeButtonText;//取消按钮文本
    private CharSequence mNeutralButtonText;//重置按钮文本
    /**
     * 对话框标题
     */
    private String mTitle;
    /**
     * 是否显示最左边按钮（重置按钮）
     */
    private boolean isShowResetButton;
    /**
     * 本地保存数据的后缀
     */
    private String mSuffix;
    /**
     * 刚进入界面读取的已保存颜色
     * <p> 需要当前界面的字体颜色、头部标题背景颜色跟着外部变得话就要传
     */
    private int mStartcolor;
    /**
     * 对话框的自定义视图
     */
    private View dialogView;
    /**
     * 当前显示的对话框对象
     */
    private AlertDialogFix.Builder alertDialogFix;

    /**
     * 固定的颜色集合
     */
    private List<Integer> colors;

    /**
     * 对话框的点击事件接口
     */
    private HsvDialogOnClickInterface mHsvDialogOnClickInterface;

    private AlertDialog alertDialog;


    /*****
     * 界面元素
     *****/
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = -180f;
    private boolean expanded = false;
    /**
     * 高级设置开启按钮
     */
    private ImageView dialog_hue_open_iv;
    /**
     * 标题
     */
    private TextView hue_title;
    /**
     * 高级设置布局
     */
    private LinearLayout dialog_hue_color_detel_ly;
    //固定颜色的列表适配器
    private HueRecyclerAdapter hueRecyclerAdapter;

    private SeekBar hue_seekbar_h;
    private SeekBar hue_seekbar_s;
    private SeekBar hue_seekbar_v;


    public HSVDialog(Context context) {
        mContext = context;
        mTitle = context.getString(R.string.hsv_dialog_title);
        mStartcolor = ContextCompat.getColor(context, R.color.col_primary);
        isShowResetButton = true;
        mPositiveButtonText=mContext.getString(R.string.main_confirm);
        mNegativeButtonText=mContext.getString(R.string.main_cancel);
        mNeutralButtonText=mContext.getString(R.string.pref_message_counter_reset);
    }


    /**
     * 对话框点击事件接口
     */
    public interface HsvDialogOnClickInterface {
        void NegativeButtonOnClickListener(DialogInterface dialog, int which);//取消

        void PositiveButtonOnClickListener(DialogInterface dialog, int which);//确认

        void NeutralButtonOnClickListener(DialogInterface dialog, int which);//重置
    }


    /**
     * 创建对话框
     */
    public AlertDialog buildDialog() {
        alertDialogFix = new AlertDialogFix.Builder(mContext);
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_hue_ly, null, false);
        initContentView(dialogView);
        alertDialogFix.setView(dialogView);//自定义界面
        alertDialogFix.setNegativeButton(mNegativeButtonText, mStartcolor,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mHsvDialogOnClickInterface != null) {
                            mHsvDialogOnClickInterface.NegativeButtonOnClickListener(dialog, which);
                        }
                    }
                });
        alertDialogFix.setPositiveButton(mPositiveButtonText,
                mStartcolor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mHsvDialogOnClickInterface != null) {
                            mHsvDialogOnClickInterface.PositiveButtonOnClickListener(dialog, which);
                        }
                        dialog.dismiss();
                    }
                });
        if (isShowResetButton) {
            alertDialogFix.setNeutralButton(mNeutralButtonText,
                    mStartcolor, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mHsvDialogOnClickInterface != null) {
                                mHsvDialogOnClickInterface.NeutralButtonOnClickListener(dialog, which);
                            }
                        }
                    });
        }
        alertDialog = alertDialogFix.create();
        return alertDialog;
    }


    /**
     * 初始化对话框的界面
     *
     * @param contentView
     */
    private void initContentView(View contentView) {
        RecyclerView color_rcy = (RecyclerView) contentView.findViewById(R.id.dialog_hue_color_rcy);
        hue_title = (TextView) contentView.findViewById(R.id.dialog_hue_title);
        LinearLayout dialog_hue_advance_ly = (LinearLayout) contentView.findViewById(R.id.dialog_hue_advance_ly);
        dialog_hue_open_iv = (ImageView) contentView.findViewById(R.id.dialog_hue_open_iv);
        dialog_hue_color_detel_ly = (LinearLayout) contentView.findViewById(R.id.dialog_hue_color_detel_ly);
        hue_seekbar_h = (SeekBar) contentView.findViewById(R.id.hue_seekbar_h);
        hue_seekbar_s = (SeekBar) contentView.findViewById(R.id.hue_seekbar_s);
        hue_seekbar_v = (SeekBar) contentView.findViewById(R.id.hue_seekbar_v);

        initColorsView(color_rcy);
        initHueView();
        //高级设置
        dialog_hue_advance_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    RotateAnimation rotateAnimation;
                    if (expanded) { // rotate clockwise
                        rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                                INITIAL_POSITION,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    } else { // rotate counterclockwise
                        rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                                INITIAL_POSITION,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    }
                    rotateAnimation.setDuration(200);
                    rotateAnimation.setFillAfter(true);
                    dialog_hue_open_iv.startAnimation(rotateAnimation);
                    if (expanded) {
                        dialog_hue_open_iv.setRotation(INITIAL_POSITION);
                        expanded = false;
                        dialog_hue_color_detel_ly.setVisibility(View.GONE);
                    } else {
                        dialog_hue_open_iv.setRotation(ROTATED_POSITION);
                        expanded = true;
                        dialog_hue_color_detel_ly.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    /**
     * 初始化固定颜色列表
     *
     * @param color_rcy
     */
    private void initColorsView(RecyclerView color_rcy) {
        hue_title.setBackgroundColor(mStartcolor);
        hue_title.setText(mTitle);
        color_rcy.setLayoutManager(new GridLayoutManager(mContext, 4));
        color_rcy.setHasFixedSize(true);
        colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(mContext, R.color.c1));
        colors.add(ContextCompat.getColor(mContext, R.color.hue_color1));
        colors.add(ContextCompat.getColor(mContext, R.color.hue_color2));
        colors.add(ContextCompat.getColor(mContext, R.color.hue_color3));
        colors.add(ContextCompat.getColor(mContext, R.color.hue_color4));
        colors.add(ContextCompat.getColor(mContext, R.color.hue_color5));
        colors.add(ContextCompat.getColor(mContext, R.color.hue_color6));
        colors.add(ContextCompat.getColor(mContext, R.color.hue_color7));
        hueRecyclerAdapter = new HueRecyclerAdapter(mContext, colors, mStartcolor, hue_title);
        color_rcy.setAdapter(hueRecyclerAdapter);
    }

    /**
     * 初始化seekbar的UI及监听
     */
    private void initHueView() {
        hue_seekbar_h.setProgressDrawable(new HueShapeDrawable(mContext));
        hue_seekbar_s.setProgressDrawable(new MyShapeDrawable(mContext, new RectShape()));
        hue_seekbar_v.setProgressDrawable(new MyShapeDrawable(mContext, new RectShape(),
                CommonUtil.getVColor(Color.BLACK, 0.3f)));
        hue_seekbar_h.setOnSeekBarChangeListener(seekbarlistener);
        hue_seekbar_s.setOnSeekBarChangeListener(seekbarlistener);
        hue_seekbar_v.setOnSeekBarChangeListener(seekbarlistener);
        changgeHueSeekbar(mStartcolor);
    }

    /**
     * seekbar的监听事件
     */
    SeekBar.OnSeekBarChangeListener seekbarlistener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
            if (!fromUser)
                return;
            hueRecyclerAdapter.clearAllState();
            changeColor(seekBar);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    /**
     * seekbar进度条根据颜色值而显示对应位置
     * <p> 应用情况：在选择固定颜色时会变化。一开始进入当前界面会变化。
     *
     * @param color
     */
    public void changgeHueSeekbar(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hue_seekbar_h.setProgress((int) hsv[0]);
        hue_seekbar_s.setProgress((int) (hsv[1] * 100));
        hue_seekbar_v.setProgress((int) (hsv[2] * 100));
        changeColor(hue_seekbar_h);
    }

    /**
     * 颜色变化时要进行颜色的刷新
     * <p> 刷新的对象包括：标题背景、S、V
     *
     * @param mSeekBar
     */
    private void changeColor(SeekBar mSeekBar) {

        float[] hsv = {hue_seekbar_h.getProgress(), ((float) hue_seekbar_s.getProgress()) / 100f, ((float) hue_seekbar_v.getProgress()) / 100f};//seekbar_v.getProgress()-100
        int color = Color.HSVToColor(hsv);

        hue_title.setBackgroundColor(color);
        if (mSeekBar==hue_seekbar_h) {
            float[] hsv1 = {hue_seekbar_h.getProgress(), 1f, 1f};//seekbar_v.getProgress()-100
            int color1 = Color.HSVToColor(hsv1);
            ((MyShapeDrawable) hue_seekbar_s.getProgressDrawable()).setChangeColor(color1);
            ((MyShapeDrawable) hue_seekbar_v.getProgressDrawable()).setChangeColor(color1);
        }
    }


    /**
     * 多彩皮肤的固定颜色值选择列表适配器
     */
    public class HueRecyclerAdapter extends RecyclerView.Adapter<HueRecyclerAdapter.ViewHoder> {


        private List<Integer> mColors;
        private LayoutInflater layoutInflater;
        private TextView mHue_title;
        private SparseBooleanArray selectionMap;

        public HueRecyclerAdapter(Context context, List<Integer> colors, int color, TextView hue_title) {
            mColors = colors;
//            startcolor= color;
            mHue_title = hue_title;
            layoutInflater = LayoutInflater.from(context);
            selectionMap = new SparseBooleanArray();
            initColordata(color);
        }

        private void initColordata(int statrcolor) {
            for (int i = 0; i < mColors.size(); i++) {
                if (mColors.get(i) == statrcolor) {
                    selectionMap.put(i, true);
                    return;
                }
            }

        }


        @Override
        public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.hue_colorlist_item, parent, false);
            return new ViewHoder(view);
        }

        @Override
        public void onBindViewHolder(ViewHoder holder, int position) {
            Drawable colorThinDrawable = CommonUtil.getTintedDrawable(holder.color_iv.getBackground(), mColors.get(position));
            holder.color_iv.setImageDrawable(colorThinDrawable);
            if (selectionMap.get(position)) {//当前位置是选择的颜色
                holder.color_iv.setImageResource(R.drawable.ic_color_choice);
                mHue_title.setBackgroundColor(mColors.get(position));
            } else {
                holder.color_iv.setImageDrawable(null);
            }

            holder.mItemView.setTag(position);
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int seletct_pos = (int) v.getTag();
                    selectionMap.clear();
                    selectionMap.put(seletct_pos, true);
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            if (mColors != null) {
                return mColors.size();
            }
            return 0;
        }

        public int getSelectPos() {
            int pos = selectionMap.keyAt(0);
            if (selectionMap.get(pos)) {
                return pos;
            }
            return -1;

        }

        public void clearAllState() {
            selectionMap.clear();
            notifyDataSetChanged();
        }

        public class ViewHoder extends RecyclerView.ViewHolder {
            private ImageView color_iv;
            private LinearLayout mItemView;

            public ViewHoder(View itemView) {
                super(itemView);
                mItemView = (LinearLayout) itemView;
                color_iv = (ImageView) itemView.findViewById(R.id.hue_item_color);
            }
        }

    }

    /**
     * 获取当前主题色
     * <p> 注意：这个颜色只用于一开始的创建着色，之后改变着色无关。
     *
     * @return
     */
    public int getmStartcolor() {
        return mStartcolor;
    }

    /**
     * 设置当前主题色
     *
     * @param mStartcolor
     */
    public void setmStartcolor(int mStartcolor) {
        this.mStartcolor = mStartcolor;
    }

    /**
     * 获取对话框标题
     *
     * @return
     */
    public String getmTitle() {
        return mTitle;
    }

    /**
     * 设置对话框标题
     *
     * @param mTitle
     */
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * 获取当前最左边按钮是否显示，重置按钮是否需要显示
     *
     * @return
     */
    public boolean isShowResetButton() {
        return isShowResetButton;
    }

    /**
     * 设置重置按钮是否显示
     *
     * @param showResetButton
     */
    public void setShowResetButton(boolean showResetButton) {
        isShowResetButton = showResetButton;
    }

    /**
     * 获取当前对话框视图
     *
     * @return
     */
    public View getDialogView() {
        return dialogView;
    }

    /**
     * 设置保存数据的后缀
     *
     * @param mSuffix
     */
    public void setmSuffix(String mSuffix) {
        this.mSuffix = mSuffix;
    }

    public AlertDialogFix.Builder getAlertDialogFix() {
        return alertDialogFix;
    }

    /**
     * 获取当前创建的对话框
     *
     * @return
     */
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public HueRecyclerAdapter getHueRecyclerAdapter() {
        return hueRecyclerAdapter;
    }

    public TextView getHue_title() {
        return hue_title;
    }

    public void setmPositiveButtonText(CharSequence mPositiveButtonText) {
        this.mPositiveButtonText = mPositiveButtonText;
    }

    public void setmNegativeButtonText(CharSequence mNegativeButtonText) {
        this.mNegativeButtonText = mNegativeButtonText;
    }

    public void setmNeutralButtonText(CharSequence mNeutralButtonText) {
        this.mNeutralButtonText = mNeutralButtonText;
    }

    public void setHsvDialogOnClickInterface(HsvDialogOnClickInterface hsvclicklistener) {
        mHsvDialogOnClickInterface = hsvclicklistener;
    }

    public List<Integer> getColors() {
        return colors;
    }
}
