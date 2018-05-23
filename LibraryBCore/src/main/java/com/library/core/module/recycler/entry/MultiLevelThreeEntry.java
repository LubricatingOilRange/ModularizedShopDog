package com.library.core.module.recycler.entry;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MultiLevelThreeEntry implements MultiItemEntity {
    private String title;

    public MultiLevelThreeEntry(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return 2;
    }
}
