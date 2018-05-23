package com.library.common.module.window;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.common.R;

public class LoadingDialog extends Dialog {
    private ImageView img;
    private TextView txt;
    private Animation mAnimation;

    public LoadingDialog(Context context) {
        this(context, "加载中...");
    }

    @SuppressLint("InflateParams")
    public LoadingDialog(Context context, String msg) {
        super(context, R.style.library_common_dialog);
        //加载布局文件
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
         View view = inflater.inflate(R.layout.dialog_library_common_loading, null);
        img =  view.findViewById(R.id.iv_library_common_loading);
        txt = view.findViewById(R.id.tv_library_common_loading);

        //给图片添加动态效果
        mAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_library_common_loading);
        txt.setText(msg);
        setContentView(view);
    }

    public void show() {
        if (!isShowing()) {
            super.show();
            img.startAnimation(mAnimation);
        }
    }

    public void cancel() {
        img.clearAnimation();
        super.cancel();
    }

    public void setMsg(String msg) {
        txt.setText(msg);
    }

    public void setMsg(int msgId) {
        txt.setText(msgId);
    }
}
