package com.library.core.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.library.core.R;
import com.library.core.base.BaseApplication;
import com.library.core.module.window.dialog.CommonDialog;
import com.library.core.module.window.dialog.CommonDialogFragment;

public class DialogHelper {
    //--------------------------------------DialogFragment的使用------------------------------------
    static final String dialog = "dialogFragment";

    //每次弹框 都会创建一次DialogFragment 不适合多次重复的弹框
    public static void showDialogFragment(final String[] arrayData, FragmentManager manager) {
        CommonDialogFragment.newInstance(new CommonDialogFragment.OnDialogCallBack() {
            @Override
            public Dialog getDialog(final Context context) {
                //Dialog参数可以从外部传进来，这需要个人喜好设置
                return new CommonDialog.Builder(context)
                        .setBackGroundLevel(1f)//背景透明（0-1）
                        //.setWidthAndHeight(ScreenUtil.getScreenWidth(context) * 2 / 3, ScreenUtil.getScreenHeight(context) / 3)//dialog的宽高
                        .setWidthAndHeight(0, 0)//dialog的宽高
//                            .setXAndY(-100, 300)//设置dialog坐标点(基于Gravity之后的偏移量，默认为中心点)
                        .setXAndY(0, 0)//设置dialog坐标点(基于Gravity之后的偏移量，默认为中心点)
                        .setOutsideTouchable(true)//设置dialog外部点击是否消失
                        // .setAnimationStyle(R.style.anim_dialog)//设置dialog显示消失动画
                        .setGravity(Gravity.CENTER)//设置显示在activity的左边
                        .setView(0, new CommonDialogFragment.OnHandleViewCallBack() {
                            @Override
                            public void onHandleView(ViewHolderHelper holder) {
                                //数据的填充
                                holder.setText(0, arrayData[0])
                                        .setText(0, arrayData[1])
                                        .setOnClickListener(0, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(context, arrayData[2], Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })//布局ID
                        .create(R.style.library_core_dialog);//主题创建
            }

            @Override
            public void onDismiss(Context context) {
                //CustomToast.create(context).shortShow("关闭了Dialog");
            }
        })
                .show(manager, dialog);
    }

    //-----------------------------------------Dialog的使用-----------------------------------------
    //多次相同弹框，只跟页面内容
    public static CommonDialog showDialog(final Context context, final String[] arrayData, CommonDialog commonDialog) {
        if (commonDialog == null) {
            commonDialog = new CommonDialog.Builder(context)
                    .setBackGroundLevel(1f)//背景透明（0-1）
                    //.setWidthAndHeight(ScreenUtil.getScreenWidth(context) * 2 / 3, ScreenUtil.getScreenHeight(context) / 3)//dialog的宽高
                    .setWidthAndHeight(BaseApplication.SCREEN_WIDTH * 2 / 3, BaseApplication.SCREEN_HEIGHT / 3)//dialog的宽高
//                    .setWidthAndHeight(0, 0)//dialog的宽高
                    .setXAndY(-100, 300)//设置dialog坐标点(基于Gravity之后的偏移量，默认为中心点)
                    .setXAndY(0, 0)//设置dialog坐标点(基于Gravity之后的偏移量，默认为中心点)
                    .setOutsideTouchable(true)//设置dialog外部点击是否消失
                    //.setAnimationStyle(R.style.anim_dialog)//设置dialog显示消失动画
                    .setGravity(Gravity.CENTER)//设置显示在activity的左边
                    .setView(0, null)//布局ID
                    .create(R.style.library_core_dialog);//主题创建}

            commonDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    // CustomToast.create(context).shortShow("关闭了Dialog");
                }
            });
        }

        //数据的填充
        TextView tv_window = commonDialog.findViewById(0);
        TextView bt_window = commonDialog.findViewById(0);

        tv_window.setText(arrayData[0]);
        bt_window.setText(arrayData[1]);

        commonDialog.show();

        return commonDialog;
    }
}