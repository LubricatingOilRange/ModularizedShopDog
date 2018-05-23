package com.library.core.module.recycler.entry;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MultiLevelOneEntry extends AbstractExpandableItem<MultiLevelTwoEntry> implements MultiItemEntity {
    private String title;
    private String subTitle;

    public MultiLevelOneEntry(String title, String subTitle) {
        this.subTitle = subTitle;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
