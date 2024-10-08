package com.github.xushifustudio.libduckeys.helper;

import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.List;

public final class DialogItemSelector {

    private final List<OnSelectedListenerHolder> holders;
    private final List<Integer> ids;
    private final Context context;

    public DialogItemSelector(Context ctx) {
        ids = new ArrayList<>();
        holders = new ArrayList<>();
        context = ctx;
    }

    public interface OnSelectedListener extends Runnable {
    }


    private static class OnSelectedListenerHolder {
        OnSelectedListener listener;
        int id;
    }


    private class MyOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int index) {
            if (0 <= index && index < ids.size()) {
                int id = ids.get(index);
                this.invokeListenerForId(id);
            }
        }

        private void invokeListenerForId(int id) {
            for (OnSelectedListenerHolder h : holders) {
                if (h.id == id) {
                    h.listener.run();
                    break;
                }
            }
        }
    }


    public DialogInterface.OnClickListener getOnClickListener() {
        return new MyOnClickListener();
    }

    public String[] getItems() {
        String[] items = new String[ids.size()];
        for (int i = 0; i < items.length; i++) {
            int id = ids.get(i);
            items[i] = context.getString(id);
        }
        return items;
    }

    public DialogItemSelector addItem(int item_id) {
        ids.add(item_id);
        return this;
    }

    public DialogItemSelector addListener(int item_id, OnSelectedListener l) {

        if (l == null) {
            return this;
        }

        OnSelectedListenerHolder h = new OnSelectedListenerHolder();
        h.id = item_id;
        h.listener = l;
        holders.add(h);
        return this;
    }
}
