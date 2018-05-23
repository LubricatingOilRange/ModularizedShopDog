package com.library.core.module.recycler.layout;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.library.core.R;
import com.library.core.util.ViewTool;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

public abstract class CustomRefreshLayoutView extends FrameLayout implements RefreshLayoutImpl {
    public static final int LOADING = 0;//主页面加载中页面
    public static final int SUCCESSFUL = 1;//主页面加载成功
    public static final int EMPTY_FAILURE = 2;//主页面加载失败或数据为空
    public static final int EMPTY_FAILURE_LOADING = 3;//主页面加载失败或数据为空时进行加载
    public static final int REFRESH_SUCCESSFUL = 7;//刷新成功
    public static final int REFRESH_FAILURE = 8;//刷新失败
    public static final int LOAD_MORE_SUCCESSFUL = 9;//加载更多成功
    public static final int LOAD_MORE_FAILURE = 10;//加载更多失败
    public static final int LOAD_NO_MORE = 11;//没有更多数据了

    protected Context mContext;
    protected View mLoadingView;//加载布局
    protected View mSuccessfulView;//加载成功布局
    protected View mEmptyFailureView;//加载失败布局

    public CustomRefreshLayoutView(Context context) {
        this(context, null);
    }

    public CustomRefreshLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    //自定义注解 实现页面切换
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({PARAMETER})
    @Inherited
    @IntDef({LOADING, SUCCESSFUL, EMPTY_FAILURE, EMPTY_FAILURE_LOADING})
    @interface FragType {
    }

    //自定义注解 实现页面刷新和加载更多的成功和失败
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({PARAMETER})
    @Inherited
    @IntDef({REFRESH_SUCCESSFUL, REFRESH_FAILURE, LOAD_MORE_SUCCESSFUL, LOAD_MORE_FAILURE, LOAD_NO_MORE})
    @interface RefreshType {
    }

    private OnRefreshCallBack mOnRefreshCallBack;

    public void setOnRefreshCallBack(OnRefreshCallBack onRefreshCallBack) {
        mOnRefreshCallBack = onRefreshCallBack;
    }

    public interface OnRefreshCallBack {

        void onRefresh();//下拉刷新

        void onLoadMore();//上拉加载更多
    }

    private void init() {
        //加载布局
        mLoadingView = View.inflate(mContext, getLoadingLayoutId(), null);
        onInitLoadingLayout(mLoadingView);//初始化加载中布局

        //获取成功布局-内容布局（定死）
        mSuccessfulView = View.inflate(mContext, getSuccessfulLayoutId(), null);
        onInitSuccessfulLayout(mSuccessfulView);//初始化成功布局

        //获取失败或空数据布局
        mEmptyFailureView = View.inflate(mContext, getEmptyFailureLayoutId(), null);
        onInitEmptyFailureLayout(mEmptyFailureView);//初始化失败或空数据布局

        //默认都是隐藏的
        ViewTool.setVisible(mLoadingView, false);
        ViewTool.setVisible(mSuccessfulView, false);
        ViewTool.setVisible(mEmptyFailureView, false);

        //添加到容器中
        addView(mLoadingView);
        addView(mSuccessfulView);
        addView(mEmptyFailureView);
    }

    /**
     * 初始化刷新布局
     * onInitSuccessfulLayout中获取RefreshLayoutView 调用
     */
    protected void onInitSwipeRefreshLayout(RefreshLayoutView swipeRefresh) {
        swipeRefresh.setHeaderViewBackgroundColor(ContextCompat.getColor(mContext, R.color.white));//设置刷新头布局的背景颜色
        swipeRefresh.setHeaderView(getPullRefreshView());// 设置下拉刷新布局
        swipeRefresh.setFooterView(getLoadMoreView());//设置加载更多布局
        swipeRefresh.setTargetScrollWithLayout(true);
        //监听下拉刷新
        swipeRefresh.setOnPullRefreshListener(new RefreshLayoutView.OnPullRefreshListener() {

            //正在刷新（释放后的刷新。请求接口数据）
            @Override
            public void onRefresh() {
                onUpdateOnRefreshingView();//更新正在刷新的布局
                if (mOnRefreshCallBack != null) {
                    mOnRefreshCallBack.onRefresh();
                }
            }

            @Override
            public void onPullDistance(int distance) {
                onPullRefreshDistance(distance);//下拉刷新 下拉后距离顶部的距离
            }

            //是否过半了，true 释放立即刷新， false 下拉刷新
            @Override
            public void onPullEnable(boolean half) {
                onUpdatePullRefreshRelease(half);//下拉过半后，更新下拉刷新(false)  -- 释放立即刷新(true)的切换
            }
        });

        //加载更多
        swipeRefresh.setOnPushLoadMoreListener(new RefreshLayoutView.OnPushLoadMoreListener() {

            @Override
            public void onLoadMore() {
                onUpdateLoadMoreView();//更新正在加载更多布局
                if (mOnRefreshCallBack != null) {
                    mOnRefreshCallBack.onLoadMore();
                }
            }

            @Override
            public void onPushEnable(boolean half) {
                onUpdateLoadMoreRelease(half);//上拉过半后，更新上拉加载(false)  -- 松开加载(true)的切换
            }

            //上拉的距离
            @Override
            public void onPushDistance(int distance) {
                onLoadMoreDistance(distance);//上拉加载更多 上拉后距离底部的距离
            }
        });
    }

    //获取下拉刷新布局
    private View getPullRefreshView() {
        View pullRefreshLayout = getPullRefreshLayout();//获取下拉刷新布局
        onInitPullRefreshView(pullRefreshLayout);//初始化下拉刷新
        return pullRefreshLayout;
    }


    private View getLoadMoreView() {
        View loadMoreLayout = getLoadMoreLayout();//获取加载更多布局
        onInitLoadMoreView(loadMoreLayout);//初始化加载更多布局
        return loadMoreLayout;
    }

    /*
     * @param type (页面切换)
     */
    private int fragType = -1;//记录上一次切换页面的Type

    public void setType(@FragType int type) {
        if (fragType == type) {
            return;
        }
        switch (type) {
            case LOADING://主页面加载中页面
                setFragViewVisible(true, false, false);//仅显示加载中页面
                onShowLoading();
                break;

            case SUCCESSFUL://主页面加载成功
                onCancelLoading();//取消加载进度
                setFragViewVisible(false, true, false);//仅显示成功页面
                break;
            case EMPTY_FAILURE://主页面加载失败或数据为空
                onCancelLoading();//取消加载进度
                setFragViewVisible(false, false, true);//仅显示成功页面
                break;

            case EMPTY_FAILURE_LOADING://主页面加载失败或数据为空时进行加载
                setFragViewVisible(true, false, true);//仅显示成功页面
                onShowLoading();
                break;
        }
        fragType = type;
    }

    //更新下拉刷新或上拉加载更多布局
    public void setRefreshType(@RefreshType int refreshType) {
        switch (refreshType) {
            case REFRESH_SUCCESSFUL:
                onPushRefreshSuccessful();//下拉刷新成功
                break;

            case REFRESH_FAILURE:
                onPushRefreshFailure();//下拉刷新失败
                break;

            case LOAD_MORE_SUCCESSFUL:
                onLoadMoreSuccessful();//加载更多成功
                break;

            case LOAD_MORE_FAILURE:
                onLoadMoreFailure();//加载更多失败
                break;
            case LOAD_NO_MORE:
                onLoadNoMore();//加载更多成功 - 没有更多数据了
                break;
        }
    }
}
