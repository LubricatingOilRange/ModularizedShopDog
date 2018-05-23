package com.library.core.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

//无MVP的Fragment基类
public abstract class BaseFragment<A extends Activity> extends Fragment {

    protected A mActivity;
    private Unbinder mBind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (A) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;//获取布局Id
        if (inflater != null) {
            view = inflater.inflate(getLayoutId(), null);
            mBind = ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewCreatedInit();//在onViewCreated方法中可进行布局的初始化操作
    }

    @Override
    public void onStart() {
        super.onStart();
        onStartInit();//在onStart方法中可进行网络请求操作
    }

    protected abstract int getLayoutId();

    protected abstract void onViewCreatedInit();

    protected abstract void onStartInit();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind != null) {
            mBind.unbind();
        }
    }
}
