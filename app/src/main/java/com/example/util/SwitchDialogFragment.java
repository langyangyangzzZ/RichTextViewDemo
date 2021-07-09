package com.example.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.richtextviewdemo.R;


/**
 * @ProjectName: My Application
 * @Package: com.example.myapplication.fragmentDialog
 * @ClassName: SwitchDialogFragment
 * @Author: szj
 * @CreateDate: 5/11/21 2:40 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SwitchDialogFragment extends DialogFragment implements View.OnClickListener {
    private String title;
    private final Context mContext;
    private String rightName = "确定", leftName = "关闭";

    //RootView 宽和高 默认自适应
    private int rootViewWeight = ViewGroup.MarginLayoutParams.WRAP_CONTENT;
    private int rootViewHeight = ViewGroup.MarginLayoutParams.WRAP_CONTENT;

    //左侧/右侧按钮是否显示 默认显示 (true隐藏)
    private boolean leftVisibility, rightVisibility,topVisibility;

    //Dialog 是否点击外部消失(true 消失)
    private boolean isDisappear;

    //成功按钮点击是否消失(默认消失)
    private boolean isSuccessDismiss;

    //是否底部弹出 (true 底部弹出)
    private boolean isBottomShow;

    //是否显示动画 (true 显示动画)
    private boolean isAnimation;

    //公用布局(构造器传入)
    private final View view;

    /**
     * @param view    中间公用布局
     * @param title   标题
     * @param context 上下文
     */
    public SwitchDialogFragment(View view, String title, Context context) {
        this.view = view;
        this.title = title;
        this.mContext = context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        //Dialog 布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout, null, false);
        //设置Dialog 布局宽高
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //初始化公用布局
        initRootView(view);

        //设置自定义布局
        builder.setView(view);

        //初始化顶部标题
        initTitle(view);

        //初始化底部按钮操作
        initBtButton(view);

        AlertDialog alertDialog = builder.create();

        //点击外部是否消失
        setDialogCancelable(alertDialog, !isDisappear);

        //Dialog 是否底部显示
        setDialogBottom(alertDialog, isBottomShow);

        //设置 Dialog 动画
        setDialogAnimation(alertDialog, isAnimation);

        return alertDialog;
    }

    /**
     * 设置 Dialog Animation 动画
     *
     * @param alertDialog AlertDialog 对象
     * @param isAnimation 是否显示动画 true 显示动画
     */
    private void setDialogAnimation(AlertDialog alertDialog, boolean isAnimation) {
        if (isAnimation) {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.BottomToTopAnim;
        }
    }

    /**
     * Dialog 底部弹出
     *
     * @param alertDialog  dialog 对象
     * @param isBottomShow true 底部弹出(默认 false)
     */
    private void setDialogBottom(AlertDialog alertDialog, boolean isBottomShow) {
        if (isBottomShow) {
            Window dialogWindow = alertDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogWindow.setAttributes(lp);

            //底部弹出
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
    }

    /**
     * 点击外部消失
     *
     * @param alertDialog alertDialog对象
     * @param isDisappear true 消失
     */
    private void setDialogCancelable(AlertDialog alertDialog, boolean isDisappear) {
        alertDialog.setCancelable(isDisappear);
        alertDialog.setCanceledOnTouchOutside(isDisappear);

        //回退键不消失
        alertDialog.setOnKeyListener(((dialog, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK));
    }

    //顶部 View
    private void initTitle(View view) {
        TextView tv_data = view.findViewById(R.id.tv_data);
        tv_data.setText(title);
        tv_data.setVisibility(!topVisibility ? View.VISIBLE : View.GONE);
    }

    private void initRootView(View view1) {
        //Dialog 公共布局
        RelativeLayout root = view1.findViewById(R.id.rootView);
        root.setLayoutParams(new LinearLayout.LayoutParams(rootViewWeight, rootViewHeight));

        //添加 公共布局 到 Dialog
        root.addView(this.view);
    }

    //配置 RootView 宽高
    public void setRootViewLayoutParams(int weight, int height) {
        rootViewWeight = weight;
        rootViewHeight = height;
    }

    /**
     * 左侧按钮名字
     */
    public void setLeftName(String name) {
        leftName = name;
    }

    /**
     * 左侧按钮是否显示
     *
     * @param isVisibility true 隐藏
     */
    public void setLeftVisibility(boolean isVisibility) {
        leftVisibility = isVisibility;
    }
    /**
     * 左侧按钮是否显示
     *
     * @param isVisibility true 隐藏
     */
    public void setTopVisibility(boolean isVisibility) {
        topVisibility = isVisibility;
    }

    /**
     * 左侧按钮名字
     */
    public void setRightName(String name) {
        rightName = name;
    }

    /**
     * 右侧按钮是否显示
     *
     * @param isVisibility true 隐藏
     */
    public void setRightVisibility(boolean isVisibility) {
        rightVisibility = isVisibility;
    }

    /**
     * dialog 点击外部是否消失
     *
     * @param isDisappear true 消失
     */
    public void setDisappear(boolean isDisappear) {
        this.isDisappear = isDisappear;
    }

    /**
     * Dialog 是否底部显示
     *
     * @param isShow true底部显示
     */
    public void setBottomShow(boolean isShow) {
        isBottomShow = isShow;
    }

    public void setDialogAnimation(boolean isAnimationShow) {
        isAnimation = isAnimationShow;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //成功按钮点击消失 true 消失
    public void setIsSuccessDismiss(boolean isSuccessDismiss) {
        this.isSuccessDismiss = isSuccessDismiss;
    }

    //底部按钮配置
    private void initBtButton(View view) {
        Button bt_left = view.findViewById(R.id.bt_left);
        Button bt_right = view.findViewById(R.id.bt_right);
        LinearLayout bottomRootView = view.findViewById(R.id.bottomRootView);

        bt_left.setText(leftName);
        bt_right.setText(rightName);

        bt_left.setVisibility(!leftVisibility ? View.VISIBLE : View.GONE);
        bt_right.setVisibility(!rightVisibility ? View.VISIBLE : View.GONE);

        if (!leftVisibility && !rightVisibility) {
            bottomRootView.setVisibility(View.GONE);
        }

        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_left) {
            if (monDialogClick != null) {
                monDialogClick.onClone();
            }
            dismiss();
        } else if (id == R.id.bt_right) {
            if (monDialogClick != null) {
                monDialogClick.onSuccess();
            }

            if (!isSuccessDismiss) {
                dismiss();
            }
        }
    }

    public interface onDialogClick {
        void onSuccess();

        void onClone();
    }

    public onDialogClick monDialogClick;

    public void setOnDialogClick(onDialogClick monDialogClick) {
        this.monDialogClick = monDialogClick;
    }
}