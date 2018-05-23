package com.library.core.module.recycler.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.chad.library.adapter.base.animation.BaseAnimation;

public class MyAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 1, 3.0f, 1),
                ObjectAnimator.ofFloat(view, "scaleX", 1, 3.0f, 1)
        };
    }
}
