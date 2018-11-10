package com.tjohnn.baking.util;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public final class SimpleIdlingResource implements IdlingResource {

    private static SimpleIdlingResource simpleIdlingResource;

    @Nullable
    private volatile ResourceCallback mCallback;

    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }

    public static SimpleIdlingResource getInstance(){
        if(simpleIdlingResource == null){
            simpleIdlingResource = new SimpleIdlingResource();
        }
        return simpleIdlingResource;
    }


}
