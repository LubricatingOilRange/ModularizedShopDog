package com.library.core.module.recycler.decoration;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

//实现顶部悬浮
public class SuspendItemDecoration extends RecyclerView.ItemDecoration {

    private int mSideMargin = 10;   //边距 左边距

    private int mGroupHeight = 120;  //悬浮栏高度

    private int mDivideHeight = 4;      //分割线宽度

    private Paint mDividePaint;

    private TextPaint mTextPaint;
    private Paint mGroutPaint;
    /**
     * 缓存分组第一个item的position
     */
    private SparseIntArray firstInGroupCash = new SparseIntArray(100);

    private SuspendItemListener mOnGroupClickListener;

    public SuspendItemDecoration(SuspendItemListener groupListener) {

        this.mOnGroupClickListener = groupListener;

        //分割线画笔
        mDividePaint = new Paint();
        mDividePaint.setAntiAlias(true);
        mDividePaint.setColor(Color.parseColor("#CCCCCC"));//分割线颜色，默认灰色

        //设置悬浮栏背景的画笔---mGroutPaint
        mGroutPaint = new Paint();
        mGroutPaint.setAntiAlias(true);
        mGroutPaint.setColor(Color.parseColor("#48BDFF"));//group背景色，默认透明);

        //设置悬浮栏中文本的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(50);
        mTextPaint.setColor(Color.WHITE);//字体颜色，默认黑色
        mTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    /**
     * 获取分组名
     */
    private String getGroupName(int position) {
        if (mOnGroupClickListener != null) {
            return mOnGroupClickListener.getGroupName(position);
        } else {
            return null;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);


        if (state.isPreLayout()) {
            return;
        }
        //点击事件处理
        if (gestureDetector == null) {
            gestureDetector = new GestureDetector(parent.getContext(), gestureListener);
            parent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }
        //-------------------------------画分割线-----------------------
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            c.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }
        final int childCount = parent.getChildCount();//所有条目数量
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(childView);
            //默认第一个就是有个Group
            if (!isFirstInGroup(position) && i > 0) {
                if (mDivideHeight != 0) {
                    float bottom = childView.getTop();
                    //高度小于顶部悬浮栏时，跳过绘制
                    if (bottom >= mGroupHeight) {
                        c.drawRect(left, bottom - mDivideHeight, right, bottom, mDividePaint);
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (state.isPreLayout()) {
            return;
        }
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            c.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        stickyHeaderPosArray.clear();

        final int itemCount = state.getItemCount();//当前页面最多展示的条目数
        final int childCount = parent.getChildCount();//所有条目数量

        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(childView);
            //默认第一个就是有个Group
            if (isFirstInGroup(position) || i == 0) {
                //绘制悬浮
                int bottom = Math.max(mGroupHeight, (childView.getTop() + parent.getPaddingTop()));//决定当前顶部第一个悬浮Group的bottom
                if (position + 1 < itemCount) {
                    //下一组的第一个View接近头部
                    int viewBottom = childView.getBottom();
                    if (isLastLineInGroup(position) && viewBottom < bottom) {
                        bottom = viewBottom;
                    }
                }
                drawDecoration(c, position, left, right, bottom);
                stickyHeaderPosArray.put(position, bottom);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        //只有是同一组的第一个才显示悬浮栏
        if (isFirstInGroup(pos)) {
            //为悬浮view预留空间
            outRect.top = mGroupHeight;
        } else {
            //为分割线预留空间
            outRect.top = mDivideHeight;
        }
    }

    /**
     * 判断是不是组中的第一个位置
     * 根据前一个组名，判断当前是否为新的组
     * 当前为groupId为null时，则与上一个为同一组
     */
    private boolean isFirstInGroup(int position) {
        String preGroupId;
        if (position == 0) {
            preGroupId = null;
        } else {
            preGroupId = getGroupName(position - 1);
        }
        String curGroupId = getGroupName(position);
        return !TextUtils.equals(preGroupId, curGroupId);
    }

    /**
     * 得到当前分组第一个item的position
     */
    private int getFirstInGroupWithCash(int position) {
        if (firstInGroupCash.get(position) == 0) {
            int firstPosition = getFirstInGroup(position);
            firstInGroupCash.put(position, firstPosition);
            return firstPosition;
        } else {
            return firstInGroupCash.get(position);
        }
    }

    /**
     * 得到当前分组第一个item的position
     *
     * @param position position
     */
    private int getFirstInGroup(int position) {
        if (position == 0) {
            return 0;
        } else {
            if (isFirstInGroup(position)) {
                return position;
            } else {
                return getFirstInGroup(position - 1);
            }
        }
    }


    /**
     * 判断自己是否为group的最后一行
     */
    private boolean isLastLineInGroup(int position) {
        String curGroupName = getGroupName(position);
        String nextGroupName;
        //默认往下查找的数量
        int findCount = 1;
        try {
            nextGroupName = getGroupName(position + findCount);
        } catch (Exception e) {
            nextGroupName = curGroupName;
        }
        return nextGroupName != null && !TextUtils.equals(curGroupName, nextGroupName);
    }

    /**
     * group分割线的点击事件调用
     */
    private void onGroupClick(int position) {
        if (mOnGroupClickListener != null) {
            mOnGroupClickListener.onGroupClick(position);
        }
    }

    /**
     * 绘制悬浮框
     */
    private void drawDecoration(Canvas c, int position, int left, int right, int bottom) {
        int firstPositionInGroup = getFirstInGroupWithCash(position);
        String curGroupName = getGroupName(firstPositionInGroup);//当前item对应的Group
        //根据top绘制group背景
        c.drawRect(left, bottom - mGroupHeight, right, bottom, mGroutPaint);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        //文字竖直居中显示
        float baseLine = bottom - (mGroupHeight - (fm.bottom - fm.top)) / 2 - fm.bottom;
        //获取文字宽度
        mSideMargin = Math.abs(mSideMargin);
        if (curGroupName != null) {
            c.drawText(curGroupName, left + mSideMargin, baseLine, mTextPaint);
        }
    }

    /**
     * 记录每个头部和悬浮头部的坐标信息【用于点击事件】
     * 位置由子类添加
     */
    @SuppressLint("UseSparseArrays")
    private SparseArray<Integer> stickyHeaderPosArray = new SparseArray<>();
    private GestureDetector gestureDetector;
    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        //单个点击
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < stickyHeaderPosArray.size(); i++) {
                int value = stickyHeaderPosArray.valueAt(i);
                float y = e.getY();
                if (value - mGroupHeight <= y && y <= value) {
                    //如果点击到分组头
                    onGroupClick(stickyHeaderPosArray.keyAt(i));
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        //长按
        @Override
        public void onLongPress(MotionEvent e) {
        }

        //快速滑动
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };
}
