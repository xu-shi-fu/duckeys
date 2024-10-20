package com.github.xushifustudio.libduckeys.ui.elements.b2;


import android.graphics.Paint;
import android.util.Log;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchContext;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.ICanvas;
import com.github.xushifustudio.libduckeys.ui.styles.B2StyleReader;

public class B2Button extends B2TextView {

    public B2Button() {
        this.interactive = true;
    }

    @Override
    protected void onTouchBefore(B2OnTouchThis self) {
        super.onTouchBefore(self);
        int action = self.context.action;
        if (action == B2OnTouchContext.ACTION_DOWN || action == B2OnTouchContext.ACTION_POINTER_DOWN) {
            String str = this.getText();
            String msg = "B2Button.onTouchBefore, action:" + action + " at:" + str;
            Log.i(DuckLogger.TAG, msg);
        }
    }

    @Override
    protected void onTouchChildren(B2OnTouchThis self) {
        super.onTouchChildren(self);
    }

    @Override
    protected void onTouchAfter(B2OnTouchThis self) {
        super.onTouchAfter(self);
    }
}
