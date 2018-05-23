package com.library.core.module.recycler.decoration;

public interface SuspendItemListener {

    String getGroupName(int position);

    void onGroupClick(int position);
}
