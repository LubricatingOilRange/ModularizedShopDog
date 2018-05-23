package com.library.core.module.recycler.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

// 只有一级 条目
public abstract class StandardAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public StandardAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }
}
