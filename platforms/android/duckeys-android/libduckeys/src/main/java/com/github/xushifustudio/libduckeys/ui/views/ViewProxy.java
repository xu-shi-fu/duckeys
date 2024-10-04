package com.github.xushifustudio.libduckeys.ui.views;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.github.xushifustudio.libduckeys.helper.CommonErrorHandler;

public final class ViewProxy {


    private ViewProxy() {
        // 纯工具类，直接使用静态方法
    }


    public static void setOnClickListener(View view, View.OnClickListener l) {
        view.setOnClickListener((v) -> {
            try {
                l.onClick(v);
            } catch (Exception e) {
                Context ctx = view.getContext();
                CommonErrorHandler.handle(ctx, e);
            }
        });
    }

}
