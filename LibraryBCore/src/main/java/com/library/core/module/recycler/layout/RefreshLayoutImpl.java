package com.library.core.module.recycler.layout;

import android.view.View;

//RecyclerView
public interface RefreshLayoutImpl {

    //---------------------------------------主页面的加载中布局-------------------------------------

    int getLoadingLayoutId();//获取加载中的布局ID

    void onInitLoadingLayout(View loadingView);//初始化加载中布局

    void onShowLoading();//展示加载中动画效果

    void onCancelLoading();//取消加载中动画效果

    //---------------------------------------主页面的成功布局-------------------------------------

    int getSuccessfulLayoutId();//获取成功的布局ID

    void onInitSuccessfulLayout(View successfulView);//初始化成功布局

    //--------------------------------主页面的加载失败或空数据布局布局------------------------------

    int getEmptyFailureLayoutId();//获取加载失败或空数据的布局ID

    void onInitEmptyFailureLayout(View emptyFailureView);//初始化失败或空数据布局

    //-----------------------------------主页面的页面Type的逻辑处理---------------------------------
    void setFragViewVisible(boolean loadingVisible, boolean successfulVisible, boolean emptyFailureVisible);//加载中页面，成功页面，失败或空数据页面切换

    //----------------------------------------下载刷新布局------------------------------------------

    View getPullRefreshLayout();//获取下拉刷新的布局ID

    void onInitPullRefreshView(View pullRefreshView);//初始化下拉刷新布局

    void onUpdatePullRefreshRelease(boolean half);//下拉过半后，更新下拉刷新(false)  -- 释放立即刷新(true)的切换

    void onUpdateOnRefreshingView();//初始化正在刷新布局

    void onPullRefreshDistance(int distance);//下拉刷新 下拉后距离顶部的距离

    //---------------------------------------上拉加载更多-------------------------------------------

    View getLoadMoreLayout();//获取加载更多的布局ID

    void onInitLoadMoreView(View loadMoreView);//初始化加载更多布局

    void onUpdateLoadMoreView();//更新正在加载更多布局

    void onLoadMoreDistance(int distance);//上拉加载更多 上拉后距离底部的距离

    void onUpdateLoadMoreRelease(boolean half);//上拉过半后，更新上拉加载(false)  -- 松开加载(true)的切换

    //-------------------------------下拉刷新和加载更多的页面Type的逻辑处理-------------------------

    void onPushRefreshSuccessful();//下拉刷新成功

    void onPushRefreshFailure();//下拉刷新失败

    void onLoadMoreSuccessful();//加载更多成功

    void onLoadMoreFailure();//加载更多失败

    void onLoadNoMore();//加载更多成功--没有更多数据了
}
