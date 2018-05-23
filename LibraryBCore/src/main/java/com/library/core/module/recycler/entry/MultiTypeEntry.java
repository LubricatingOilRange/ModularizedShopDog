package com.library.core.module.recycler.entry;

//  Created by ruibing.han on 2018/4/2.

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MultiTypeEntry<T> implements MultiItemEntity {
    //条目的数据类型
    public static final int ITEM_TYPE_1 = 1;
    public static final int ITEM_TYPE_2 = 2;
    public static final int ITEM_TYPE_3 = 3;
    public static final int ITEM_TYPE_4 = 4;
    public static final int ITEM_TYPE_5 = 5;

    //占的百分比
    public static final int SPAN_SIZE_1 = 1;
    public static final int SPAN_SIZE_2 = 2;
    public static final int SPAN_SIZE_3 = 3;
    public static final int SPAN_SIZE_4 = 4;

    private int itemType;
    private int spanSize;

    private T data;

    public MultiTypeEntry(int itemType, int spanSize, T data) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.data = data;
    }

    public MultiTypeEntry(int itemType, int spanSize) {
        this(itemType, spanSize, null);
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
