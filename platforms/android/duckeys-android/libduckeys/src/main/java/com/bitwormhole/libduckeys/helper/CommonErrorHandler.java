package com.bitwormhole.libduckeys.helper;

import android.content.Context;
import android.widget.Toast;

public class CommonErrorHandler {
    public static void handle(Context ctx, Throwable e) {

        if (ctx == null || e == null) {
            return;
        }

        String msg = e.getClass().getName() + ":" + e.getMessage();
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }
}
