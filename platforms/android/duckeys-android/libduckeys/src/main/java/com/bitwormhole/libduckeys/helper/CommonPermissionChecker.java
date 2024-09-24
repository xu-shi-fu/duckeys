package com.bitwormhole.libduckeys.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public final class CommonPermissionChecker {

    private CommonPermissionChecker() {
    }

    public static void check(Activity act, String[] permissions) {
        List<String> want = new ArrayList<>();
        for (String perm : permissions) {
            int state = act.checkCallingOrSelfPermission(perm);
            if (state != PackageManager.PERMISSION_GRANTED) {
                want.add(perm);
            }
        }
        if (want.size() > 0) {
            String[] plist = new String[want.size()];
            plist = want.toArray(plist);
            act.requestPermissions(plist, 0);
        }
    }
}
