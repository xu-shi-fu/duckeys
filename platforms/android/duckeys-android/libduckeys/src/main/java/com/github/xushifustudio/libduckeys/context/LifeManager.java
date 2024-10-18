package com.github.xushifustudio.libduckeys.context;


import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class LifeManager {

    private final List<Life> mItems;
    private List<Life> mOnCreateBuffer;
    private MyOnCreateCaller mOnCreateCaller;
    private Life mMainLife;

    public LifeManager() {
        mItems = new ArrayList<>();
        mOnCreateBuffer = new ArrayList<>();
        mOnCreateCaller = null;
    }

    public void add(Life l) {
        if (l == null) {
            return;
        }
        if (this.mItems.contains(l)) {
            return; // 避免重复
        }
        MyOnCreateCaller caller = mOnCreateCaller;
        List<Life> buffer = mOnCreateBuffer;
        if (caller != null) {
            caller.invoke(l);
        }
        if (buffer != null) {
            buffer.add(l);
        }
        mItems.add(l);
    }


    private void invokeItem(Life item, AbstractLife.OnLifeListener listener) {
        try {
            listener.OnLife(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void forEachItem(boolean reverse, AbstractLife.OnLifeListener listener) {
        final List<Life> all = new ArrayList<>(mItems);
        if (!reverse) {
            // forward
            for (Life item : all) {
                invokeItem(item, listener);
            }
        } else {
            // backward
            for (int i = all.size(); i > 0; ) {
                i--;
                Life item = all.get(i);
                invokeItem(item, listener);
            }
        }
    }

    private static class MyOnCreateCaller {

        final Bundle savedInstanceState;

        MyOnCreateCaller(Bundle b) {
            this.savedInstanceState = b;
        }

        public void invoke(Life l) {
            l.onCreate(this.savedInstanceState);
        }
    }


    private class MyMainLife implements Life {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            MyOnCreateCaller caller = new MyOnCreateCaller(savedInstanceState);
            List<Life> buffer = mOnCreateBuffer;
            if (buffer != null) {
                for (Life l : buffer) {
                    caller.invoke(l);
                }
            }
            mOnCreateBuffer = null;
            mOnCreateCaller = caller;
        }

        @Override
        public void onStart() {
            mOnCreateCaller = null;
            mOnCreateBuffer = null;
            forEachItem(false, Life::onStart);
        }

        @Override
        public void onRestart() {
            mOnCreateCaller = null;
            mOnCreateBuffer = null;
            forEachItem(false, Life::onRestart);
        }

        @Override
        public void onPause() {
            forEachItem(true, Life::onPause);
        }

        @Override
        public void onResume() {
            forEachItem(false, Life::onResume);
        }

        @Override
        public void onStop() {
            forEachItem(true, Life::onStop);
        }

        @Override
        public void onDestroy() {
            forEachItem(true, Life::onDestroy);
        }
    }

    public Life getMain() {
        Life ml = mMainLife;
        if (ml == null) {
            ml = new MyMainLife();
            mMainLife = ml;
        }
        return ml;
    }
}
