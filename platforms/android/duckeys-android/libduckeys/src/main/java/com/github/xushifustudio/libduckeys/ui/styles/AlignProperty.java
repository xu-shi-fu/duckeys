package com.github.xushifustudio.libduckeys.ui.styles;

import com.github.xushifustudio.libduckeys.ui.box2.B2Align;
import com.github.xushifustudio.libduckeys.ui.box2.B2Property;
import com.github.xushifustudio.libduckeys.ui.box2.B2PropertyHandler;
import com.github.xushifustudio.libduckeys.ui.box2.B2PropertyHandlerProvider;
import com.github.xushifustudio.libduckeys.ui.box2.B2PropertyQuery;

public class AlignProperty implements B2Property, B2PropertyHandlerProvider {

    private B2Align align;

    public AlignProperty(B2Align value) {
        this.align = value;
    }

    public B2Align getAlign() {
        return align;
    }

    public void setAlign(B2Align align) {
        this.align = align;
    }

    @Override
    public Object value() {
        return this.align;
    }

    private class MyHandler implements B2PropertyHandler {
        @Override
        public boolean query(B2PropertyQuery q) {
            q.ok = false;
            if (q.property == null) {
                q.property = AlignProperty.this;
                q.ok = true;
            } else if (q.property instanceof AlignProperty) {
                AlignProperty dst = (AlignProperty) q.property;
                AlignProperty src = AlignProperty.this;
                dst.setAlign(src.getAlign());
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
