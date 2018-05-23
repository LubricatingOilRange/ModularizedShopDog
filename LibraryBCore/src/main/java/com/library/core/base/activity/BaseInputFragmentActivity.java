package com.library.core.base.activity;

//  Created by ruibing.han on 2018/5/15.

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.library.core.R;
import com.library.core.base.fragment.BaseFragment;

//含有EditText
public abstract class BaseInputFragmentActivity extends BaseInputActivity {
    private boolean isSkipBackStack = true;//当前是否执行跳跃回退 返回

    //返回跳跃 （返回到指定的Fragment）
    public void setSkipBackStack(boolean skipBackStack) {
        isSkipBackStack = skipBackStack;
    }

    //-------------------------------------replace 实现Fragment的显示-------------------------------

    /**
     * 通过Replace将Fragment添加容器中 模拟栈 实现回退效果
     *
     * @param replaceFragTag （切换到哪个fragment的tag）
     * @param isAddBackStack (是否添加到回退栈)
     * @param isShowAnim     (是否展示动画效果)
     * @param isBackLeft     (是否往左进栈)
     */
    public void replaceFragment(@NonNull String replaceFragTag, boolean isAddBackStack, boolean isShowAnim, boolean isBackLeft) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // 添加Fragment切换的动画效果
        if (isShowAnim) {
            if (isBackLeft) {
                //如果往左进栈 --- 左进右出
                transaction.setCustomAnimations(getAnimInLeft(), getAnimOutRight(), getAnimInRight(), getAnimOutLeft());
            } else {
                //如果往右进栈 --- 右进左出
                transaction.setCustomAnimations(getAnimInRight(), getAnimOutLeft(), getAnimInLeft(), getAnimOutRight());
            }
        }

        BaseFragment replaceFragment = getNextFragment(replaceFragTag);
        transaction.replace(getContainerViewId(), replaceFragment, replaceFragment.getClass().getSimpleName());
        if (isAddBackStack) {
            transaction.addToBackStack(replaceFragment.getClass().getSimpleName());//添加到回退栈
        }

        transaction.commitAllowingStateLoss();
    }


    //-----------------------------------------popBackStack--------------------------------------
    private void popBackStack(String tag, int flags) {
        getSupportFragmentManager().popBackStack(tag, flags);
    }

    //普通的一层一层 回退
    protected void onFragmentBackPressed() {
        onFragmentBackPressed(null, 0);
    }

    /**
     * 如果tag为null，flags为0时，弹出回退栈中最上层的那个fragment（普通的一层一层回退）
     * 如果tag为null ，flags为1时，跳过回退栈中所有fragment(包括本身)，--返回后成一个空白页面
     * 如果tag不为null，tag所对应的fragment，flags为0时，弹出该fragment（不包括本身）以上的Fragment
     * 如果tag不为null，tag所对应的fragment，flags为1时，弹出该fragment（包括本身）以上的fragment。
     */
    protected void onFragmentBackPressed(String tag, int flags) {
        FragmentManager manager = getSupportFragmentManager();

        if (TextUtils.isEmpty(tag) && flags == 0) {//普通的一层一层回退
            if (manager.getBackStackEntryCount() < 2) {
                finish();
            } else {
                popBackStack(tag, flags);
            }
        } else if (TextUtils.isEmpty(tag) && flags == 1) {//清栈了（释放栈中的Fragment）
            finish();
            ;//一般用来退出当前页面
        } else if (!TextUtils.isEmpty(tag) && flags == 0) {//返回到指定的页面,弹出当前fragment(不包括本身)栈上面的fragment
            if (isSkipBackStack) {
                popBackStack(tag, flags);
                isSkipBackStack = false;
            } else {
                onFragmentBackPressed();//不进行跳跃返回  逐步返回
            }
        } else if (!TextUtils.isEmpty(tag) && flags == 1) {//返回到指定的页面,弹出当前fragment(包括本身)栈上面的fragment
            if (manager.getBackStackEntryCount() > 0) {
                String name = manager.getBackStackEntryAt(0).getName();

                if (name.equals(tag)) {
                    //如果当前返回到指定的fragment 是第一个Fragment  因为是flag == 1 就直接finish
                    finish();
                } else {
                    if (isSkipBackStack) {
                        popBackStack(tag, flags);
                        isSkipBackStack = false;
                    } else {
                        onFragmentBackPressed();//不进行跳跃返回  逐步返回
                    }
                }
            }
        }
    }


    //--------------------------------------add 和 hide 实现Fragment的显示--------------------------
    //添加的第一个页面
    public void addFirstFragment(@NonNull String nextFragTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(getContainerViewId(), getNextFragment(nextFragTag), nextFragTag)
                .commitAllowingStateLoss();
    }

    /**
     * 通过add,hide 控制fragment
     *
     * @param curFragTag  (当前 fragment)
     * @param nextFragTag ( 切换到哪个fragment)
     * @param isShowAnim  (是否展示动画效果)
     * @param isShowLeft  (是否往左展示页面)
     */
    public void addOtherFragment(@NonNull String curFragTag, @NonNull String nextFragTag, boolean isShowAnim, boolean isShowLeft) {
        //不是第一次添加
        FragmentManager manager = getSupportFragmentManager();
        Fragment curFragment = manager.findFragmentByTag(curFragTag);//获取当前的fragment
        if (curFragment == null) {
            throw new NullPointerException("Current Fragment is null !");
        }

        FragmentTransaction transaction = manager.beginTransaction();//开启事务管理

        if (isShowAnim) {
            if (isShowLeft) {
                //如果往左切换 --- 左进右出
                transaction.setCustomAnimations(getAnimInLeft(), getAnimOutRight(), 0, 0);
            } else {
                //如果往右切换 --- 右进左出
                transaction.setCustomAnimations(getAnimInRight(), getAnimOutLeft(), 0, 0);
            }
        }

        Fragment nextFragment = manager.findFragmentByTag(nextFragTag);//获取当前的fragment

        if (nextFragment == null) {
            nextFragment = getNextFragment(nextFragTag);//获取下一个页面的Fragment
            transaction
                    .add(getContainerViewId(), nextFragment, nextFragTag)
                    .hide(curFragment);
        } else {
            transaction
                    .show(nextFragment)
                    .hide(curFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    protected abstract BaseFragment getNextFragment(String nextFragTag);

    protected abstract int getContainerViewId();

    protected int getAnimInLeft() {
        return R.anim.library_core_in_left;//左边进入动画
    }

    protected int getAnimOutLeft() {
        return R.anim.library_core_out_left;//左边出去动画
    }

    protected int getAnimInRight() {
        return R.anim.library_core_in_right;//右边进入动画
    }

    protected int getAnimOutRight() {
        return R.anim.library_core_out_right;//右边出去动画
    }
}
