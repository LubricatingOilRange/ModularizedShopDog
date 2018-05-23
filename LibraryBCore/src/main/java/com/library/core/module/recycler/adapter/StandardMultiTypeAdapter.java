package com.library.core.module.recycler.adapter;

//  Created by ruibing.han on 2018/4/2.

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.core.module.recycler.entry.MultiTypeEntry;

import java.util.ArrayList;
import java.util.List;

//RecyclerView实现多类型条目 - 复杂布局
public abstract class StandardMultiTypeAdapter extends BaseMultiItemQuickAdapter<MultiTypeEntry, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public StandardMultiTypeAdapter(List<MultiTypeEntry> data) {
        super(data);
        int[] layoutArray = getLayoutArray();
        int length = layoutArray.length;
        if (length < 2) {
            throw new IllegalStateException("Number of layouts greater than 2");
        }
        //添加五种类型的布局
        addItemType(MultiTypeEntry.ITEM_TYPE_1, layoutArray[0]);
        addItemType(MultiTypeEntry.ITEM_TYPE_2, layoutArray[1]);

        if (length > 2) {
            addItemType(MultiTypeEntry.ITEM_TYPE_3, layoutArray[2]);
        }

        if (length > 3) {
            addItemType(MultiTypeEntry.ITEM_TYPE_4, layoutArray[3]);
        }

        if (length > 4) {
            addItemType(MultiTypeEntry.ITEM_TYPE_5, layoutArray[4]);
        }

        if (length > 5) {
            throw new IllegalStateException("The number of layouts should not exceed 5 ");
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiTypeEntry item) {
        switch (helper.getItemViewType()) {
            case MultiTypeEntry.ITEM_TYPE_1://条目类型1
//                helper.setText(R.id.tv_item_type, item.getContent());
                break;
            case MultiTypeEntry.ITEM_TYPE_2://条目类型2
//                helper.setText(R.id.tv_item_type, item.getContent());
                break;
            case MultiTypeEntry.ITEM_TYPE_3://条目类型3
//                helper.setText(R.id.tv_item_type, item.getContent());
                break;
            case MultiTypeEntry.ITEM_TYPE_4://条目类型4
//                helper.setText(R.id.tv_item_type, item.getContent());
                break;
            case MultiTypeEntry.ITEM_TYPE_5://条目类型5
                //一个条目列表中重复出现类型
                switch (item.getSpanSize()) {
                    case MultiTypeEntry.SPAN_SIZE_3:
//                        helper.setText(R.id.tv_item_type, item.getContent());
                        break;
                    case MultiTypeEntry.SPAN_SIZE_1:
//                        helper.setText(R.id.tv_item_type, item.getContent());
                        break;
                }
                break;
        }
    }

    //获取假数据 --- 使用步骤
    public List<MultiTypeEntry<String>> getMultipleItemData(RecyclerView recyclerView) {
        List<MultiTypeEntry<String>> list = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            list.add(new MultiTypeEntry<>(MultiTypeEntry.ITEM_TYPE_1, MultiTypeEntry.SPAN_SIZE_1, "ITEM_TYPE_1---" + i));
            list.add(new MultiTypeEntry<>(MultiTypeEntry.ITEM_TYPE_2, MultiTypeEntry.SPAN_SIZE_2, "ITEM_TYPE_2---" + i));
            list.add(new MultiTypeEntry<>(MultiTypeEntry.ITEM_TYPE_3, MultiTypeEntry.SPAN_SIZE_1, "ITEM_TYPE_3---" + i));
            list.add(new MultiTypeEntry<>(MultiTypeEntry.ITEM_TYPE_4, MultiTypeEntry.SPAN_SIZE_4, "ITEM_TYPE_4---" + i));
            list.add(new MultiTypeEntry<>(MultiTypeEntry.ITEM_TYPE_5, MultiTypeEntry.SPAN_SIZE_3, "ITEM_TYPE_5---" + i));
            list.add(new MultiTypeEntry<>(MultiTypeEntry.ITEM_TYPE_5, MultiTypeEntry.SPAN_SIZE_1, "ITEM_TYPE_5---" + i));
        }


        final GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        recyclerView.setLayoutManager(manager);
        setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return getData().get(position).getSpanSize();
            }
        });
        recyclerView.setAdapter(this);
        return list;
    }

    abstract int[] getLayoutArray();//获取布局
}
