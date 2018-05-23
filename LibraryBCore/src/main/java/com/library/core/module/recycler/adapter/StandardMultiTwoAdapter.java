package com.library.core.module.recycler.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public abstract class StandardMultiTwoAdapter<A, B> extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public StandardMultiTwoAdapter(List<MultiItemEntity> data) {
        super(data);
        int[] layoutIdArray = getLayoutArray();// 获取多级条目的布局Id
        int length = layoutIdArray.length;
        if (length == 2) {
            addItemType(0, layoutIdArray[0]);
            addItemType(1, layoutIdArray[1]);
        } else {
            throw new IllegalArgumentException("Adapter not support for other item count");
        }
    }

    abstract @NonNull
    int[] getLayoutArray();//获取布局数组

    @Override
    protected void convert(BaseViewHolder holder, MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case 0:
                convertLevelOne(holder, (A) item);
                break;
            case 1:
                convertLevelTwo(holder, (B) item);
                break;
        }
    }

    abstract void convertLevelOne(BaseViewHolder holder, A a);//一级布局处理

    abstract void convertLevelTwo(BaseViewHolder holder, B b);//二级布局处理
}
