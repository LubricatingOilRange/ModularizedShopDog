package com.library.core.module.window.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.library.core.helper.ViewHolderHelper;

public class CommonDialogFragment extends DialogFragment {

    /**
     * 回调获得需要显示的dialog
     */
    private OnDialogCallBack mOnDialogCallBack;
    private Dialog mDialog;

    public interface OnDialogCallBack {
        Dialog getDialog(Context context);

        void onDismiss(Context context);
    }

    /**
     * 处理布局的回掉接口
     */
    public interface OnHandleViewCallBack {
        void onHandleView(ViewHolderHelper holder);
    }


    public static CommonDialogFragment newInstance(OnDialogCallBack call) {
        CommonDialogFragment instance = new CommonDialogFragment();
        instance.mOnDialogCallBack = call;
        return instance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (null == mOnDialogCallBack) {
            return super.onCreateDialog(savedInstanceState);
        } else {
            mDialog = mOnDialogCallBack.getDialog(getActivity());
            return mDialog;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDialogCallBack != null) {
            mOnDialogCallBack.onDismiss(getActivity());
        }
    }

    public Dialog getCommonDialog() {
        return mDialog;
    }
}
