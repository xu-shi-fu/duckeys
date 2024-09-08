package com.bitwormhole.libduckeys.context;


import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class LifeManager {

    private final List<Life> mItems;
    private Life mMainLife;

    public LifeManager() {
        mItems = new ArrayList<>();
    }

    public void add(Life l) {
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


    private class MyMainLife implements Life {


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            forEachItem(false, (l) -> {
                l.onCreate(savedInstanceState);
            });
        }

        @Override
        public void onStart() {
            forEachItem(false, Life::onStart);
        }

        @Override
        public void onRestart() {
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

    public final Life getMain() {
        Life ml = mMainLife;
        if (ml == null) {
            ml = new MyMainLife();
            mMainLife = ml;
        }
        return ml;
    }

}
