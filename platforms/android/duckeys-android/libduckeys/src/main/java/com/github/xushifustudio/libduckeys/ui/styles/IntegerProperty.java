package com.github.xushifustudio.libduckeys.ui.styles;

import com.github.xushifustudio.libduckeys.ui.box2.B2Property;
import com.github.xushifustudio.libduckeys.ui.box2.B2PropertyHandler;
import com.github.xushifustudio.libduckeys.ui.box2.B2PropertyHandlerProvider;
import com.github.xushifustudio.libduckeys.ui.box2.B2PropertyQuery;

public class IntegerProperty implements B2Property, B2PropertyHandlerProvider {

    private int mValue;

    public IntegerProperty(int num) {
        this.mValue = num;
    }

    public void setValue(int val) {
        mValue = val;
    }

    public int getValue() {
        return mValue;
    }


    @Override
    public Object value() {
        return mValue;
    }

    private class MyHandler implements B2PropertyHandler {
        @Override
        public boolean query(B2PropertyQuery q) {
            q.ok = false;
            if (q.property == null) {
                q.property = IntegerProperty.this;
                q.ok = true;
            } else if (q.property instanceof IntegerProperty) {
                IntegerProperty dst = (IntegerProperty) q.property;
                IntegerProperty src = IntegerProperty.this;
                dst.mValue = src.mValue;
                q.ok = true;
            }
            return q.ok;
        }
    }

    @Override
    public B2PropertyHandler getHandler() {
        return new MyHandler();
    }
}
